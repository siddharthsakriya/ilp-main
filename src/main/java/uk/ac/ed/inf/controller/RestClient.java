package uk.ac.ed.inf.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.*;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class RestClient {
    public Restaurant[] getRestaurants() {
        String uri = "https://ilp-rest.azurewebsites.net/restaurants";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Restaurant[]> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, Restaurant[].class);
        Restaurant[] restaurants = responseEntity.getBody();
        return restaurants;
    }
    public Order[] getOrdersByDate(String date) {
        String uri = "https://ilp-rest.azurewebsites.net/orders" + date;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order[]> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, Order[].class);
        Order[] orders = responseEntity.getBody();
        return orders;
    }

    public NamedRegion[] getNoFlyZones(){
        String uri = "https://ilp-rest.azurewebsites.net/noFlyZones";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NamedRegion[]> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, NamedRegion[].class);
        NamedRegion[] noFlyZones = responseEntity.getBody();
        return noFlyZones;
    }

    public NamedRegion getCentralArea(){
        String uri = "https://ilp-rest.azurewebsites.net/centralArea";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NamedRegion> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, NamedRegion.class);
        NamedRegion centralArea = responseEntity.getBody();
        return centralArea;
    }
}
