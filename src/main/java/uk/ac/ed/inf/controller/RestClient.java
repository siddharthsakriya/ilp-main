package uk.ac.ed.inf.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

@Service
public class RestClient {
    @ResponseBody
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
}
