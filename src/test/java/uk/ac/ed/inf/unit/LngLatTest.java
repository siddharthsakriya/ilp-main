package uk.ac.ed.inf.unit;

import junit.framework.TestCase;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.awt.*;

import static junit.framework.Assert.assertTrue;

public class LngLatTest extends TestCase {
    public static LngLat[] sampleIntCoordinates1;
    public static LngLat[] sampleIntCoordinates2;

//    55.94502253545242,-3.187770722273225
//            55.94502253545242,-3.186825458130709
//            55.94545184977702,-3.186825458130709

    protected void setUp() throws Exception {
        super.setUp();
        LngLat p1 = new LngLat(55.94545184977702, -3.187770722273225);
        LngLat p2 = new LngLat(55.94502253545242, -3.1875371540984756);
        LngLat p3 = new LngLat(55.94513486546662, -3.186825458130709);
        LngLat p4 = new LngLat(55.94551339982776, -3.187105739940409);

        LngLat p5 = new LngLat(55.94545184977702, -3.187770722273225);
        LngLat p6 = new LngLat(55.94502253545242, -3.187770722273225);
        LngLat p7 = new LngLat(55.94502253545242, -3.186825458130709);
        LngLat p8 = new LngLat(55.94545184977702, -3.186825458130709);
        sampleIntCoordinates1 = new LngLat[]{p1,p2,p3,p4};
        sampleIntCoordinates2 = new LngLat[]{p5,p6,p7,p8};
    }

    public void testIsDistanceWoring(){
        LngLat startPosition = new LngLat(0,0);
        LngLat endPosition = new LngLat(3,4);
        LngLatHandling lngLatHandler = new LngLatHandler();
        double result = lngLatHandler.distanceTo(startPosition, endPosition);
        assertEquals(5.0, result);
    }
    public void testIsDistanceWoring1(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat endPosition = new LngLat(4.848484,5.92992);
        LngLatHandling lngLatHandler = new LngLatHandler();
        double result = lngLatHandler.distanceTo(startPosition, endPosition);
        assertEquals(5.379331689456113, result);
    }

    public void testIsCloseToVeryClose() {
        LngLatHandling lngLatHandling = new LngLatHandler();

        LngLat startPosition = new LngLat(55.94502253545242, -3.1875371540984756);
        LngLat otherPosition =  new LngLat(55.94503253545242, -3.1875371540984756);

        assertTrue(lngLatHandling.isCloseTo(startPosition, otherPosition));
    }

    public void testIsCloseToSamePoint() {
        LngLatHandling lngLatHandling = new LngLatHandler();

        LngLat startPosition = new LngLat(55.94502253545242, -3.1875371540984756);
        LngLat otherPosition =  new LngLat(55.94502253545242, -3.1875371540984756);

        assertTrue(lngLatHandling.isCloseTo(startPosition, otherPosition));
    }

    public void testNextPosition() {
        LngLatHandling lngLatHandling = new LngLatHandler();

        LngLat startPosition = new LngLat(55.94502253545242, -3.1875371540984756);
        LngLat result = lngLatHandling.nextPosition(startPosition, 90);
        // Define the error range

        // Assert that the latitude and longitude are within the specified error range
        assertEquals(55.94502253545242, result.lng());
        assertEquals(-3.1873871540984756, result.lat());
    }

    public void testNextPositionInvalidAngle() {
        LngLatHandling lngLatHandling = new LngLatHandler();

        LngLat startPosition = new LngLat(55.94502253545242, -3.1875371540984756);
        LngLat result = lngLatHandling.nextPosition(startPosition, 89);

        assertEquals(55.94502253545242, result.lng());
        assertEquals(-3.1875371540984756, result.lat());
    }

    public void testIsInRegion() {
        LngLatHandling lngLatHandler = new LngLatHandler();
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates2);
        LngLat position = new LngLat(55.94525184977702, -3.187);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsOnEdge1() {
        LngLatHandling lngLatHandler = new LngLatHandler();
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates2);
        LngLat position = new LngLat(55.94502253545242, -3.18725458130709);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsOnVertex1() {
        LngLatHandling lngLatHandler = new LngLatHandler();
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates2);
        LngLat position = new LngLat(55.94502253545242, -3.187770722273225);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }
}