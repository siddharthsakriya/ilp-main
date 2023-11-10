package uk.ac.ed.inf.unit;

import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.pathfinding.PathFindingAlgorithm;

import java.time.LocalDate;
import java.util.List;

public class PathTest {
    public static void main(String[] args) {
        List<Move> path = setup();
        List<Move> path1 = setup2();
        List<Move> path2 = setup3();
        List<Move> path3 = setup4();

        for (Move move : path1){
            System.out.println(" ");
            System.out.println("[\n" + move.getFromLongitude() + ", " + move.getFromLatitude() + "\n],");
        }
        System.out.println(path.size());

    }

    public static List<Move> setup(){
        LngLat destination = new LngLat( -3.202541470527649,55.943284737579376);
        LngLat start = new LngLat(-3.186874, 55.944494);
        ILPRestClient ILPRestClient = new ILPRestClient();
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                5100,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));
        return PathFindingAlgorithm.findPath(start, destination, ILPRestClient.getNoFlyZones(), ILPRestClient.getCentralArea(), order);
    }

    public static List<Move> setup2(){
        LngLat destination = new LngLat( -3.202541470527649,55.943284737579376);
        LngLat start = new LngLat(-3.186874, 55.944494);
        ILPRestClient ILPRestClient = new ILPRestClient();
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                5100,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));
        return PathFindingAlgorithm.findPath(start, destination, ILPRestClient.getNoFlyZones(), ILPRestClient.getCentralArea(), order);
    }

    public static List<Move> setup3(){
        LngLat destination = new LngLat( -3.1838572025299072,55.94449876875712);
        LngLat start = new LngLat(-3.186874, 55.944494);
        ILPRestClient ILPRestClient = new ILPRestClient();
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                5100,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));
        return PathFindingAlgorithm.findPath(start, destination, ILPRestClient.getNoFlyZones(), ILPRestClient.getCentralArea(), order);
    }

    public static List<Move> setup4(){
        LngLat destination = new LngLat( -3.1940174102783203,55.94390696616939);
        LngLat start = new LngLat(-3.186874, 55.944494);
        ILPRestClient ILPRestClient = new ILPRestClient();
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                5100,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000),
                        new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));
        return PathFindingAlgorithm.findPath(start, destination, ILPRestClient.getNoFlyZones(), ILPRestClient.getCentralArea(), order);
    }

}
