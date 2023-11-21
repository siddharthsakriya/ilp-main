package uk.ac.ed.inf.unit;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import uk.ac.ed.inf.handlers.DeliveryHandler;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class RestRetrivalTests {
    @Test
    public void testGetRest(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net/");
        Restaurant[] restaurants = restController.getRestaurants();
        Assert.assertTrue(restaurants.length > 0);
    }

    @Test
    public void testGetOrderFromDate(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net/");
        Order[] orders27th = restController.getOrdersByDate("2023-10-27");
        Order[] orders28th = restController.getOrdersByDate("2023-10-28");
        Order[] orders29th = restController.getOrdersByDate("2023-10-29");
        Order[] orders30th = restController.getOrdersByDate("2023-10-30");

        Assert.assertTrue(orders27th.length > 0);
        Assert.assertTrue(orders28th.length > 0);
        Assert.assertTrue(orders29th.length > 0);
        Assert.assertTrue(orders30th.length > 0);
    }

    @Test
    public void testGetNoFlyZones(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net/");
        NamedRegion[] noFlyZones = restController.getNoFlyZones();
        for (NamedRegion noFlyZone : noFlyZones){
            LngLat[] vertices = noFlyZone.vertices();
            for (LngLat v: vertices){
                System.out.println("["+ v.lng() + " , " + v.lat() + "],");
            }
            System.out.println("-----");
        }
        Assert.assertTrue(restController.getNoFlyZones().length > 0);
    }

    @Test
    public void testGetCentralArea(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net/");
        Assert.assertTrue(restController.getCentralArea() != null);
    }

    @Test
    public void generateJson() {
        String date = "2023-09-01";
        DeliveryHandler.deliverOrders(date, "https://ilp-rest.azurewebsites.net/");

    }

    @Test
    public void testErrorOrders(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net/");
        Order[] orders = restController.getOrdersByDate("jdjdj-10-27");
    }

    @Test
    public void testErrorOrders2(){
        ILPRestClient restController = new ILPRestClient("dsdjudju://ilp-jfnfnfi.azurewebsites.net/");
        Order[] orders = restController.getOrdersByDate("2023-10-27");
    }

    @Test
    public void testErrorNoFlyZones(){
        ILPRestClient restController = new ILPRestClient("https://jjsjsn-rest.azurewebsites.net/");
        NamedRegion[] noFlyZones = restController.getNoFlyZones();
    }

    @Test
    public void testErrorRestaurant(){
        ILPRestClient restController = new ILPRestClient("https://jjsjsn-rest.azurewebsites.net/");
        Restaurant[] restaurants = restController.getRestaurants();
    }

    @Test
    public void  testErrorCentralArea(){
        ILPRestClient restController = new ILPRestClient("https://jjsjsn-rest.azurewebsites.net/");
        NamedRegion centralArea = restController.getCentralArea();
    }

}
