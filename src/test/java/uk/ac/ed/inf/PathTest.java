package uk.ac.ed.inf;

import uk.ac.ed.inf.controller.RestClient;
import uk.ac.ed.inf.ilp.data.LngLat;

import java.util.List;

public class PathTest {
    public static void main(String[] args) {
        List<LngLat> path = PathTest();
//        List<LngLat> path2 = Astar();
        System.out.println(path.size());
        for (LngLat lngLat : path){
            System.out.println(" ");
            System.out.println("[\n" + lngLat.lng() + ", " + lngLat.lat() + "\n],");
        }
    }
    public static List<LngLat> PathTest(){
        LngLat start = new LngLat(-3.202541470527649,55.943284737579376);
        LngLat destination = new LngLat(-3.187670, 55.944752);
        RestClient restClient = new RestClient();
        return PathfindingAlgorithm.pathfinder(start, destination, restClient.getNoFlyZones(), restClient.getCentralArea());
    }

    public static List<LngLat> Astar(){
        LngLat start = new LngLat(-3.1940174102783203,55.94390696616939);
        LngLat destination = new LngLat(-3.187670, 55.944752);
        RestClient restClient = new RestClient();
        return AStarPathFindingAlgorithim.astar(start, destination, restClient.getNoFlyZones(), restClient.getCentralArea());
    }

    public static void printNoFlyZones(){
        RestClient restClient = new RestClient();
        for (LngLat lngLat : restClient.getCentralArea().vertices()){
            System.out.println(" ");
            System.out.println("[\n" + lngLat.lng() + ", " + lngLat.lat() + "\n],");
        }
    }


}
