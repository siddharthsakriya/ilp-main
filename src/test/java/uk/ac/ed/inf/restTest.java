package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.controller.RestClient;
import uk.ac.ed.inf.ilp.data.Order;
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

}
