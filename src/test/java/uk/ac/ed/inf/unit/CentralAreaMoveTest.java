package uk.ac.ed.inf.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import uk.ac.ed.inf.App;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.model.Move;



import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Unit test for simple App.
 */
public class
CentralAreaMoveTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CentralAreaMoveTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CentralAreaMoveTest.class);
    }

    /**
     * checking if we fly in a no fly zone, by getting every point from the flightpath json file and checking if it is in the no fly zone
     */
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

        App.main(new String[]{"2023-09-" + dayString, "https://ilp-rest.azurewebsites.net"});

        Move[] paths;

        try {
            // getting the flightpath data from the json file
            String json = new String(Files.readAllBytes(Paths.get("resultfiles/flightpath-2023-09-"+dayString+".json")));

            ObjectMapper mapper = new ObjectMapper();

            // returning the selected data (we get the first value as the api returns an array of length 1)
            paths = mapper.readValue(json, Move[].class);

        } catch (Exception e) {
            throw new IllegalArgumentException("The api request was not successful. Error code: " + e.getMessage());
        }

        LngLatHandler handler = new LngLatHandler();
        ILPRestClient ILPRestClient = new ILPRestClient("https://ilp-rest.azurewebsites.net/");
        NamedRegion centralArea = ILPRestClient.getCentralArea();

        boolean inCentralArea = false;
        boolean pickup = true;
        String previousOrderNo = "";

        // for each point in the flightpath
        for (Move path : paths) {

            // reset inCentralArea if we are on a new order, or we have arrived at the destination (angle = 999)
            if (!previousOrderNo.equals(path.getOrderNo())) {
                inCentralArea = false;
                pickup = true;
                previousOrderNo = path.getOrderNo();
            }

            if (path.getAngle() == 999) {
                pickup = !pickup;
            }

            if (pickup == false) {

                if (!inCentralArea && handler.isInCentralArea(new LngLat(path.getFromLongitude(), path.getFromLatitude()), centralArea)) {
                    inCentralArea = true;
                }

                if (inCentralArea) {
                    assertTrue(handler.isInCentralArea(new LngLat(path.getToLongitude(), path.getToLatitude()), centralArea));
                }
            }
        }
    }

}



