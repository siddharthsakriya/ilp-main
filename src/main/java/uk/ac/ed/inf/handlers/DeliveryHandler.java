package uk.ac.ed.inf.handlers;

import uk.ac.ed.inf.converters.PathToGeoJson;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.pathfinding.AStarPathFindingAlgorithim;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.converters.OrderToJson;
import uk.ac.ed.inf.converters.PathToJson;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.File;
import java.util.*;

public class DeliveryHandler {
    public static HashMap<String, File> setupPath(String date){
        String filepath = "resultfiles";
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
        Order[] orders = ILPRestClient.getOrdersByDate(date);
        Restaurant[] restaurants = ILPRestClient.getRestaurants();
        NamedRegion[] noFlyZones = ILPRestClient.getNoFlyZones();
        NamedRegion centralArea = ILPRestClient.getCentralArea();
        OrderHandler orderHandler = new OrderHandler();
        HashMap<String, File> fileMap = setupPath(date);

        for (Order order: orders){
            order = orderHandler.validateOrder(order, restaurants);
            Restaurant orderRestaurant = orderHandler.getOrderRestraunt(order, restaurants);
            if (order.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED && order.getOrderValidationCode() == OrderValidationCode.NO_ERROR){
                List<Move> pickupPath = AStarPathFindingAlgorithim.astar(new LngLat(-3.186874, 55.944494), orderRestaurant.location(), noFlyZones, centralArea, order);
                PathToJson.serialiseMove(pickupPath, fileMap.get("flightpathFileName"));
                PathToGeoJson.geofyJson(fileMap.get("droneGeoJsonFileName"), pickupPath);
                Collections.reverse(pickupPath);
                PathToJson.serialiseMove(pickupPath, fileMap.get("flightpathFileName"));
                PathToGeoJson.geofyJson(fileMap.get("droneGeoJsonFileName"), pickupPath);
                order.setOrderStatus(OrderStatus.DELIVERED);
                OrderToJson.serialiseOrder(order, fileMap.get("deliveryFileName"));
            }
            else {
                OrderToJson.serialiseOrder(order, fileMap.get("deliveryFileName"));
            }
        }
    }

}