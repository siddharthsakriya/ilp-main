package uk.ac.ed.inf.handlers;

import uk.ac.ed.inf.model.Delivery;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.pathfinding.PathFindingAlgorithm;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class DeliveryHandler {

    /**
     * Creates the resultfiles directory and the files that are to be written to
     * @param date
     * @return HashMap<String, File> fileMap, with keys flightpathFileName, deliveryFileName, droneGeoJsonFileName
     */
    public static HashMap<String, File> setupPath(String date){
        String filepath = "resultfiles";
        String flightpathFileName = "flightpath-" + date + ".json";
        String deliveryFileName = "deliveries-" + date + ".json";
        String droneGeoJsonFileName = "drone-" + date + ".geojson";

        File directory = new File(filepath);
        directory.mkdirs();
        HashMap<String, File> fileMap = new HashMap<>();

        fileMap.put("flightpathFileName", createResultFile(flightpathFileName, directory));
        fileMap.put("deliveryFileName", createResultFile(deliveryFileName, directory));
        fileMap.put("droneGeoJsonFileName", createResultFile(droneGeoJsonFileName, directory));

        return fileMap;
    }

    /**
     * Creates a file with the given fileName in the given directory, helper function for setupPath
     * @param fileName
     * @param directory
     * @return File resultFile
     */
    private static File createResultFile(String fileName, File directory){
        File resultFile = new File(directory, fileName);
        if (resultFile.exists()) {
            resultFile.delete();
        }
        return resultFile;
    }

    /**
     * Iterates through the orders and delivers them if they are valid
     * @param date
     */
    public static void deliverOrders(String date) {
        Order[] orders = ILPRestClient.getOrdersByDate(date);
        Restaurant[] restaurants = ILPRestClient.getRestaurants();
        NamedRegion[] noFlyZones = ILPRestClient.getNoFlyZones();
        NamedRegion centralArea = ILPRestClient.getCentralArea();
        OrderHandler orderHandler = new OrderHandler();
        HashMap<String, File> fileMap = setupPath(date);
        LngLat startPoint = new LngLat(-3.186874, 55.944494);

        for (Order order: orders){
            order = orderHandler.validateOrder(order, restaurants);
            Restaurant orderRestaurant = orderHandler.getOrderRestaurant(order, restaurants);
            if (isOrderReady(order)){
                List<Move> path = PathFindingAlgorithm.findPath(startPoint,
                        orderRestaurant.location(), noFlyZones, centralArea, order);
//                PathToJson.serialiseMove(path, fileMap.get("flightpathFileName"));
//
//                Collections.reverse(path);

                PathToJson.serialiseMove(path, fileMap.get("flightpathFileName"));

                order.setOrderStatus(OrderStatus.DELIVERED);
                OrderToJson.serialiseOrder(new Delivery(order.getOrderNo(), order.getOrderStatus(), order.getOrderValidationCode(), order.getPriceTotalInPence()), fileMap.get("deliveryFileName"));
            }
            else {
                OrderToJson.serialiseOrder(new Delivery(order.getOrderNo(), order.getOrderStatus(), order.getOrderValidationCode(), order.getPriceTotalInPence()), fileMap.get("deliveryFileName"));
            }
        }
    }

    /**
     * Checks if an order is ready to be delivered, helper function for deliverOrders
     * @param order
     * @return
     */
    private static boolean isOrderReady(Order order){
        return order.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED
                && order.getOrderValidationCode() == OrderValidationCode.NO_ERROR;
    }

}