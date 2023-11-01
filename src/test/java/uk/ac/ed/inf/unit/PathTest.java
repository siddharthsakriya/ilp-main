package uk.ac.ed.inf.unit;

import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.pathfinding.AStarPathFindingAlgorithim;

import java.time.LocalDate;
import java.util.List;

public class PathTest {
    public static void main(String[] args) {
        List<Move> path = Astar();
        //List<LngLat> path2 = Astar();
        System.out.println(path.size());
        int total = 0;
        for (Move move : path){
            System.out.println(" ");
            System.out.println("[\n" + move.getFromLongitude() + ", " + move.getFromLatitude() + "\n],");
            total += 1;
        }
        System.out.println(total);

    }

    public static List<Move> Astar(){
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
        return AStarPathFindingAlgorithim.astar(start, destination, ILPRestClient.getNoFlyZones(), ILPRestClient.getCentralArea(), order);
    }

    public static void printNoFlyZones(){
        ILPRestClient ILPRestClient = new ILPRestClient();
        for (LngLat lngLat : ILPRestClient.getCentralArea().vertices()){
            System.out.println(" ");
            System.out.println("[\n" + lngLat.lng() + ", " + lngLat.lat() + "\n],");
        }
    }


}
