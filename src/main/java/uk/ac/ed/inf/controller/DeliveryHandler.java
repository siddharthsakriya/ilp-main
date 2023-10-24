package uk.ac.ed.inf.controller;

import uk.ac.ed.inf.PathfindingAlgorithm;
import uk.ac.ed.inf.converters.OrderToJson;
import uk.ac.ed.inf.converters.PathToJson;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.model.OrderValidator;

import java.io.File;
import java.util.*;

public class DeliveryHandler {
    public static HashMap<String, File> setupPath(String date){
        String filepath = "src/main/java/uk/ac/ed/inf/resultfiles";
        String flightpathFileName = "flightpath-" + date + ".json";
        String deliveryFileName = "deliveries-" + date + ".json";
        String droneGeoJsonFileName = "drone-" + date + ".geojson";

        File directory = new File(filepath);
        directory.mkdirs();
        HashMap<String, File> fileMap = new HashMap<>();

        File flightpathFile = new File(directory, flightpathFileName);
        if (flightpathFile.exists()) {
            flightpathFile.delete();
        }
        fileMap.put("flightpathFileName", flightpathFile);

        File deliveryFile = new File(directory, deliveryFileName);
        if (deliveryFile.exists()) {
            deliveryFile.delete();
        }
        fileMap.put("deliveryFileName", deliveryFile);

        File droneGeoJsonFile = new File(directory, droneGeoJsonFileName);
        if (droneGeoJsonFile.exists()) {
            droneGeoJsonFile.delete();
        }
        fileMap.put("droneGeoJsonFileName", droneGeoJsonFile);
        return fileMap;
    }

    public static void deliverOrders(String date){
        Order[] orders = RestClient.getOrdersByDate(date);
        Restaurant[] restaurants = RestClient.getRestaurants();
        NamedRegion[] noFlyZones = RestClient.getNoFlyZones();
        NamedRegion centralArea = RestClient.getCentralArea();
        OrderValidator orderValidator = new OrderValidator();
        HashMap<String, File> fileMap = setupPath(date);

        for (Order order: orders){
            order = orderValidator.validateOrder(order, restaurants);
            Restaurant orderRestaurant = orderValidator.getOrderRestraunt(order, restaurants);
            if (order.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED && order.getOrderValidationCode() == OrderValidationCode.NO_ERROR){
                List<LngLat> path = PathfindingAlgorithm.pathfinder(new LngLat(-3.187670, 55.944752), orderRestaurant.location(), noFlyZones, centralArea);
                PathToJson.convertToMoveAndSerialise(path, order, fileMap);
                Collections.reverse(path);
                PathToJson.convertToMoveAndSerialise(path, order, fileMap);
                order.setOrderStatus(OrderStatus.DELIVERED);
                OrderToJson.serialiseOrder(order, fileMap.get("deliveryFileName"));
            }
            else {
                OrderToJson.serialiseOrder(order, fileMap.get("deliveryFileName"));
            }
        }
    }

}
