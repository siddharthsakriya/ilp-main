package uk.ac.ed.inf.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.*;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class ILPRestClient {
    private static String BASE_URL;
    private static final RestTemplate restTemplate = new RestTemplate();

    public ILPRestClient(String url) {
        this.BASE_URL = url;
    }

    /**
     * Get all restaurants
     * @return an array of restaurants
     */
    public Restaurant[] getRestaurants() {
        try {
            String url = BASE_URL + "/restaurants";
            ResponseEntity<Restaurant[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Restaurant[].class);
            return responseEntity.getBody();
        }
        catch (ResourceAccessException e){
            System.out.println("Error, the URL is incorrect");
            System.exit(1);
            return null;
        }
    }

    /**
     * Get all orders on a specific date
     * @param date
     * @return an array of orders for a specific date
     */
    public Order[] getOrdersByDate(String date) {
        try{

            String url = BASE_URL + "/orders/" +  date;
            ResponseEntity<Order[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Order[].class);
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e){
            System.out.println("Error, the date is incorrect");
            System.exit(1);
            return null;
        }
        catch (ResourceAccessException e){
            System.out.println("Error, the URL is incorrect");
            System.exit(1);
            return null;
        }
    }

    /**
     * Get all no-fly zones
     * @return an array of no-fly zones
     */
    public NamedRegion[] getNoFlyZones(){
        try {
            String url = BASE_URL + "/noFlyZones";
            ResponseEntity<NamedRegion[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, NamedRegion[].class);
            return responseEntity.getBody();
        }
        catch (ResourceAccessException e){
            System.out.println("Error, the URL is incorrect");
            System.exit(1);
            return null;
        }
    }

    /**
     * Get the central area
     * @return the central area
     */
    public NamedRegion getCentralArea(){
        try{
            String url = BASE_URL + "/centralArea";
            ResponseEntity<NamedRegion> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, NamedRegion.class);
            return responseEntity.getBody();
        }
        catch (ResourceAccessException e){
            System.out.println("Error, the URL is incorrect");
            System.exit(1);
            return null;
        }
    }
}
