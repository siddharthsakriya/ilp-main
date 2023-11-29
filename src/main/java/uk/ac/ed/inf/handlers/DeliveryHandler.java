package uk.ac.ed.inf.handlers;

import uk.ac.ed.inf.model.Delivery;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.pathfinding.PathFindingAlgorithm;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.converters.JsonWriter;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeliveryHandler {

    /**
     * Sets up the necessary files and directories for a given date and returns a HashMap of file objects.
     *
     * @param date The date for which the files and directory are being set up.
     * @return A HashMap with file objects as values, where the keys are as follows:
     *         - "flightpathFileName": The file containing flight path data for the specified date.
     *         - "deliveryFileName": The file containing delivery information for the specified date.
     *         - "droneGeoJsonFileName": The file containing drone geospatial data for the specified date.
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
    public static void deliverOrders(String date, String url) {
        ILPRestClient ilpRestClient = new ILPRestClient(url);
        Order[] orders = ilpRestClient.getOrdersByDate(date);
        Restaurant[] restaurants = ilpRestClient.getRestaurants();
        NamedRegion[] noFlyZones = ilpRestClient.getNoFlyZones();
        NamedRegion centralArea = ilpRestClient.getCentralArea();

        OrderHandler orderHandler = new OrderHandler();
        HashMap<String, File> fileMap = setupPath(date);
        List<Delivery> deliveriesToWrite = new ArrayList<>();
        List<Move> movesToWrite = new ArrayList<>();


        if (restaurants.length == 0){
            System.err.println("No restaurants found");
            System.exit(1);
        }
        for (Order order: orders){
            order = orderHandler.validateOrder(order, restaurants);
            Restaurant orderRestaurant = orderHandler.getOrderRestaurant(order, restaurants);
            if (isOrderReady(order)){
                List<Move> path = PathFindingAlgorithm.findPath(orderRestaurant.location(), noFlyZones, centralArea,
                        order);
                if (path == null){
                    deliveriesToWrite.add(new Delivery(order.getOrderNo(), order.getOrderStatus().toString(),
                            order.getOrderValidationCode().toString(), order.getPriceTotalInPence()));
                    continue;
                }
                movesToWrite.addAll(path);
                order.setOrderStatus(OrderStatus.DELIVERED);
                deliveriesToWrite.add(new Delivery(order.getOrderNo(), order.getOrderStatus().toString(),
                        order.getOrderValidationCode().toString(), order.getPriceTotalInPence()));
            }
            else {
                deliveriesToWrite.add(new Delivery(order.getOrderNo(), order.getOrderStatus().toString(),
                        order.getOrderValidationCode().toString(), order.getPriceTotalInPence()));
            }
        }
        writeToFiles(movesToWrite, deliveriesToWrite, fileMap);
    }

    /**
     * Checks if an order is ready to be delivered, helper function for deliverOrders
     * @param order order to check
     * @return
     */
    private static boolean isOrderReady(Order order){
        return order.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED
                && order.getOrderValidationCode() == OrderValidationCode.NO_ERROR;
    }

    /**
     * Writes the moves and deliveries to the files, helper function for deliverOrders
     * @param movesToWrite 
     * @param deliveriesToWrite
     * @param fileMap
     */
    private static void writeToFiles(List<Move> movesToWrite, List<Delivery> deliveriesToWrite, HashMap<String, File> fileMap){
        JsonWriter.writeMoveJson(movesToWrite, fileMap.get("flightpathFileName"));
        JsonWriter.writeMoveGeoJson(movesToWrite, fileMap.get("droneGeoJsonFileName"));
        JsonWriter.writeOrderJson(deliveriesToWrite, fileMap.get("deliveryFileName"));
    }

}