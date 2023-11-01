package uk.ac.ed.inf.unit;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.handlers.DeliveryHandler;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class restTest {
    @Test
    public void testGetRest(){
        ILPRestClient restController = new ILPRestClient();
        Restaurant[] restaurants = restController.getRestaurants();
        for(Restaurant restaurant: restaurants){
            System.out.println(restaurant.name());
        }
    }

    @Test
    public void genJson(){
        String date = "2023-10-28";
        DeliveryHandler.deliverOrders(date);
    }

    @Test
    public void filesOverwritten(){
        String date = "2023-10-27";
        ILPRestClient ILPRestClient = new ILPRestClient();
        Order[] orders = ILPRestClient.getOrdersByDate(date);
    }

}
