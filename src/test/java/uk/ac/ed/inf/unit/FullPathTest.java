package uk.ac.ed.inf.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Assert;
import uk.ac.ed.inf.App;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.handlers.OrderHandler;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.model.Delivery;
import uk.ac.ed.inf.model.Move;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Unit test for simple App.
 */
public class FullPathTest extends TestCase {

    public FullPathTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(FullPathTest.class);
    }

    public void testEnteringLeavingCentralArea() {

        // getting a random day between 2020-09-01 and 2023-09-30
        Random random = new Random();
        String dayString;

        int day = random.nextInt(1, 31);

        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = String.valueOf(day);
        }

        App.main(new String[]{"2023-11-" + dayString, "https://ilp-rest.azurewebsites.net"});

        Move[] paths;

        try {
            // getting the flightpath data from the json file
            String json = new String(Files.readAllBytes(Paths.get("resultfiles/flightpath-2023-11-"+dayString+".json")));

            ObjectMapper mapper = new ObjectMapper();

            // returning the selected data (we get the first value as the api returns an array of length 1)
            paths = mapper.readValue(json, Move[].class);

        } catch (Exception e) {
            throw new IllegalArgumentException("The api request was not successful. Error code: " + e.getMessage());
        }
        Delivery[] deliveries;
        try {
            // getting the flightpath data from the json file
            String json = new String(Files.readAllBytes(Paths.get("resultfiles/deliveries-2023-11-"+dayString+".json")));

            ObjectMapper mapper = new ObjectMapper();
            // returning the selected data (we get the first value as the api returns an array of length 1)
            deliveries = mapper.readValue(json, Delivery[].class);

        } catch (Exception e) {
            throw new IllegalArgumentException("The api request was not successful. Error code: " + e.getMessage());
        }

        LngLatHandler handler = new LngLatHandler();
        ILPRestClient ILPRestClient = new ILPRestClient("https://ilp-rest.azurewebsites.net/");
        boolean pickup = true;

        Restaurant[] restaurants = ILPRestClient.getRestaurants();
        Order[] orders = ILPRestClient.getOrdersByDate("2023-11-"+dayString);
        Boolean validPath = false;
        Boolean validPath2 = false;
        OrderHandler orderHandler = new OrderHandler();
        for(Move path: paths){
            if (path.getAngle() == 999){
                List<Order> orders1 = Arrays.asList(orders);
                Order order = orders1.stream().filter(o -> o.getOrderNo().equals(path.getOrderNo())).findFirst().orElse(null);
                Restaurant restaurant1 = orderHandler.getOrderRestaurant(order, restaurants);

                if(pickup){
                    if(handler.isCloseTo(new LngLat(path.getFromLongitude(), path.getFromLatitude()), new LngLat(restaurant1.location().lng(), restaurant1.location().lat()))){
                        if(handler.isCloseTo(new LngLat(path.getToLongitude(), path.getToLatitude()), new LngLat(restaurant1.location().lng(), restaurant1.location().lat()))){
                            validPath2 = true;
                        }
                    }
                }
                else if(!pickup){
                    if(handler.isCloseTo(new LngLat(path.getFromLongitude(), path.getFromLatitude()), new LngLat(-3.186874, 55.944494))){
                        if(handler.isCloseTo(new LngLat(path.getToLongitude(), path.getToLatitude()), new LngLat(-3.186874, 55.944494))){
                            validPath2 = true;
                        }
                    }
                }
                else {
                    validPath2 = false;
                    break;
                }
            }

            if(path.getAngle() == 999){
                pickup = !pickup;
            }

        }
        // for each point in the flightpath
        for (Move path : paths) {

            if(path.getAngle() != 999){
                LngLat lnglatfrom = new LngLat(path.getFromLongitude(), path.getFromLatitude());
                LngLat lnglatto = new LngLat(path.getToLongitude(), path.getToLatitude());
                LngLat gen = handler.nextPosition(lnglatfrom, path.getAngle());
                if (gen.equals(lnglatto)) {
                    validPath = true;
                }
                else {
                    validPath = false;
                    break;
                }

            }
        }
        Assert.assertTrue(validPath);
        Assert.assertTrue(validPath2);
    }

}



