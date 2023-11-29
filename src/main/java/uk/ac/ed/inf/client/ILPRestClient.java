package uk.ac.ed.inf.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.net.URL;

public class ILPRestClient {
    private static String BASE_URL;

    public ILPRestClient(String url) {
        this.BASE_URL = url;
    }

    /**
     * Get all restaurants
     * @return an array of restaurants
     */
    public Restaurant[] getRestaurants() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Restaurant[] restaurants = mapper.readValue(new URL(BASE_URL + "/restaurants"), Restaurant[].class);
            System.out.println("Restaurants Retrieved Successfully");
            return restaurants;
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("URL is invalid");
            System.exit(1);
        }
        Restaurant[] restaurants = {};
        return restaurants;
    }

    /**
     * Get all orders on a specific date
     * @param date the date to get orders for
     * @return an array of orders for a specific date
     */
    public Order[] getOrdersByDate(String date) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            Order[] orders = mapper.readValue(new URL(BASE_URL + "/orders/" + date), Order[].class);
            System.out.println("Orders Retrieved Successfully");
            return orders;
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("URL is invalid");
            System.exit(1);
        }
        Order[] orders = {};
        return orders;
    }

    /**
     * Get all no-fly zones
     * @return an array of no-fly zones
     */
    public NamedRegion[] getNoFlyZones(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            NamedRegion[] noFlyZones = mapper.readValue(new URL(BASE_URL + "/noflyzones"), NamedRegion[].class);
            System.out.println("No Fly Zones Retrieved Successfully");
            return noFlyZones;
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("URL is invalid");
            System.exit(1);
        }
        NamedRegion[] noFlyZones = {};
        return noFlyZones;
    }

    /**
     * Get the central area
     * @return the central area
     */
    public NamedRegion getCentralArea(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            NamedRegion centralArea = mapper.readValue(new URL(BASE_URL + "/centralarea"), NamedRegion.class);
            System.out.println("Central Area Retrieved Successfully");
            return centralArea;
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("URL is invalid");
            System.exit(1);
        }
        return null;
    }

    /**
     * Get the is alive status
     * @return the is alive status
     */
    public boolean getIsAlive(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String isAlive = mapper.readValue(new URL(BASE_URL + "/isalive"), String.class);
            System.out.println("Is Alive Retrieved Successfully");
            return Boolean.parseBoolean(isAlive);
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("URL is invalid");
            System.exit(1);
        }
        return false;
    }

}
