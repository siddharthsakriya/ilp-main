package uk.ac.ed.inf.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.*;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class ILPRestClient {
    private static final String BASE_URL = "https://ilp-rest.azurewebsites.net";
    private static final RestTemplate restTemplate = new RestTemplate();


    /**
     * Get all restaurants
     * @return an array of restaurants
     */
    public static Restaurant[] getRestaurants() {
        String url = BASE_URL + "/restaurants";
        ResponseEntity<Restaurant[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Restaurant[].class);
        return responseEntity.getBody();
    }

    /**
     * Get all orders on a specific date
     * @param date
     * @return an array of orders for a specific date
     */
    public static Order[] getOrdersByDate(String date) {
        String url = BASE_URL + "/orders/" +  date;
        ResponseEntity<Order[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Order[].class);
        return responseEntity.getBody();
    }

    /**
     * Get all no-fly zones
     * @return an array of no-fly zones
     */
    public static NamedRegion[] getNoFlyZones(){
        String url = BASE_URL + "/noFlyZones";
        ResponseEntity<NamedRegion[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, NamedRegion[].class);
        return responseEntity.getBody();
    }

    /**
     * Get the central area
     * @return the central area
     */
    public static NamedRegion getCentralArea(){
        String url = BASE_URL + "/centralArea";
        ResponseEntity<NamedRegion> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, NamedRegion.class);
        return responseEntity.getBody();
    }
}
