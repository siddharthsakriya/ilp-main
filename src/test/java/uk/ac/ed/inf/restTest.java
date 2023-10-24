package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.controller.DeliveryHandler;
import uk.ac.ed.inf.controller.RestClient;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class restTest {
    @Test
    public void testGetRest(){
        RestClient restController = new RestClient();
        Restaurant[] restaurants = restController.getRestaurants();
        for(Restaurant restaurant: restaurants){
            System.out.println(restaurant.name());
        }
    }
    @Test
    public void genJson(){
        String date = "2023-12-24";
        DeliveryHandler.deliverOrders(date);
    }

    @Test
    public void filesOverwritten(){
        String date = "2023-12-24";
        DeliveryHandler.setupPath(date);
    }
}
