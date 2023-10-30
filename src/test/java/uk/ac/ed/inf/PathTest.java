package uk.ac.ed.inf;

import uk.ac.ed.inf.clients.ILPRestClient;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.pathfinding.AStarPathFindingAlgorithim;

import java.util.List;

public class PathTest {
    public static void main(String[] args) {
        List<LngLat> path = Astar();
        //List<LngLat> path2 = Astar();
        System.out.println(path.size());
        for (LngLat lngLat : path){
            System.out.println(" ");
            System.out.println("[\n" + lngLat.lng() + ", " + lngLat.lat() + "\n],");
        }

    }

    public static List<LngLat> Astar(){
        LngLat destination = new LngLat( -3.202541470527649,55.943284737579376);
        LngLat start = new LngLat(-3.186874, 55.944494);
        ILPRestClient ILPRestClient = new ILPRestClient();
        return AStarPathFindingAlgorithim.astar(start, destination, ILPRestClient.getNoFlyZones(), ILPRestClient.getCentralArea());
    }

    public static void printNoFlyZones(){
        ILPRestClient ILPRestClient = new ILPRestClient();
        for (LngLat lngLat : ILPRestClient.getCentralArea().vertices()){
            System.out.println(" ");
            System.out.println("[\n" + lngLat.lng() + ", " + lngLat.lat() + "\n],");
        }
    }


}
