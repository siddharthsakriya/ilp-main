package uk.ac.ed.inf.unit;

import junit.framework.Assert;

import org.junit.Test;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.ilp.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RestRetrivalTests {

    @Test
    public void testGetRest(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net");
        Restaurant[] restaurants = restController.getRestaurants();
        Assert.assertTrue(restaurants.length > 0);
    }

    @Test
    public void testGetOrderFromDate(){
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            String dayString;
            int day = random.nextInt(1, 31);
            if (day < 10) {
                dayString = "0" + day;
            } else {
                dayString = String.valueOf(day);
            }
            //TODO: change to october please or november or december
            dates.add("2023-09-" + dayString);
        }
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net");

        Order[] orders1 = restController.getOrdersByDate(dates.get(0));
        Order[] orders2 = restController.getOrdersByDate(dates.get(1));
        Order[] orders3 = restController.getOrdersByDate(dates.get(2));
        Order[] orders4 = restController.getOrdersByDate(dates.get(3));

        Assert.assertTrue(orders1.length > 0);
        Assert.assertTrue(orders2.length > 0);
        Assert.assertTrue(orders3.length > 0);
        Assert.assertTrue(orders4.length > 0);
    }

    @Test
    public void testGetNoFlyZones(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net");
        NamedRegion[] noFlyZones = restController.getNoFlyZones();
        Assert.assertTrue(restController.getNoFlyZones().length > 0);
    }

    @Test
    public void testGetCentralArea(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net");
        Assert.assertTrue(restController.getCentralArea() != null);
    }

//TODO Uncomment these before submitting because if you run ur system will exit !!!!
//
//    @Test
//    public void testErrorOrders(){
//        ILPRestClient restController = new ILPRestClient("https://ilp-ddhjfdhjfdj.azurewebsites.net");
//        Order[] orders = restController.getOrdersByDate("jdjdj-10-27");
//    }
//
//    @Test
//    public void testErrorOrders2(){
//        ILPRestClient restController = new ILPRestClient("https://ilp-djnjdjn.azurewebsites.net");
//        Order[] orders = restController.getOrdersByDate("2023-11-12");
//    }
//
//    @Test
//    public void testErrorNoFlyZones(){
//        ILPRestClient restController = new ILPRestClient("https://ilp-redhdhbdst.azurewebsites.net");
//        NamedRegion[] noFlyZones = restController.getNoFlyZones();
//    }
//
//    @Test
//    public void testErrorRestaurant(){
//        ILPRestClient restController = new ILPRestClient("https://ilp-uduhdsudusuh.azurewebsites.net");
//        Restaurant[] restaurants = restController.getRestaurants();
//    }
//
//    @Test
//    public void  testErrorCentralArea(){
//        ILPRestClient restController = new ILPRestClient("https://ilp-revjjst.azurewebsites.net/");
//        NamedRegion centralArea = restController.getCentralArea();
//    }


}
