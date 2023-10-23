package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
import uk.ac.ed.inf.model.LngLatHandler;

import java.util.*;

public class AStarPathFindingAlgorithim {
    public static List<LngLat> astar(LngLat Start, LngLat End, NamedRegion[] noFlyZones, NamedRegion centralArea){
        PriorityQueue<OpenSetVal> openSet = new PriorityQueue<>(Comparator.comparing(OpenSetVal::getFScore));
        Set<OpenSetVal> closedList = new HashSet<>();
        HashMap<LngLat, Double> gScore = new HashMap<>();
        HashMap<LngLat, Double> hScore = new HashMap<>();
        HashMap<LngLat, LngLat> cameFrom = new HashMap<>();
        LngLatHandling lngLatHandling = new LngLatHandler();
//        boolean arrived = false;
        gScore.put(Start, 0.0);
        hScore.put(Start, heuristic(Start, End));
        openSet.add(new OpenSetVal(Start, gScore.get(Start) + hScore.get(Start)));
        while(!openSet.isEmpty()){
            LngLat current = openSet.poll().getLngLat();
            if(lngLatHandling.isCloseTo(current, End)){
                return reconstructPath(Start, End, cameFrom);
            }
            closedList.add(openSet.poll());
            openSet.remove(openSet.poll());
            List<LngLat> nextPositions = generateNextPositions(current, new LngLatHandler());
            nextPositions = filterMoves(nextPositions, noFlyZones, new LngLatHandler());
            for(LngLat nextPosition: nextPositions){
                if(closedList.contains(nextPosition)){
                    continue;
                }
                double gScoreNext = gScore.get(current) + heuristic(current, nextPosition);
                gScore.put(nextPosition,gScoreNext);
                hScore.put(nextPosition, heuristic(nextPosition, End));
                OpenSetVal newVal = new OpenSetVal(nextPosition, gScore.get(nextPosition) + hScore.get(nextPosition));
                if(!openSet.contains(newVal)){
                    cameFrom.put(nextPosition, current);
                    openSet.add(newVal);
                }
            }
        }
        return null;
    }

    public static List<LngLat> filterMoves(List<LngLat> nextPositions, NamedRegion[] noFlyZones, LngLatHandling lngLatHandling){
        List<LngLat> NextPositionsCopy = new ArrayList<>(nextPositions);
        for(LngLat nextPosition: nextPositions){
            for(NamedRegion noFlyZone: noFlyZones){
                if(lngLatHandling.isInRegion(nextPosition, noFlyZone)){
                    NextPositionsCopy.remove(nextPosition);
                }
            }
        }
        return NextPositionsCopy;
    }
    public static List<LngLat> generateNextPositions(LngLat curr, LngLatHandling lngLatHandling){
        List<LngLat> nextPositions = new ArrayList<>();
        double[] legalMoves = { 0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        for(double legalMove: legalMoves){
            nextPositions.add(lngLatHandling.nextPosition(curr, legalMove));
        }
        return nextPositions;
    }
    public static double heuristic(LngLat pos, LngLat end){
        LngLatHandling lngLatHandler = new LngLatHandler();
        return lngLatHandler.distanceTo(pos, end);
    }
    public static List<LngLat> reconstructPath(LngLat Start, LngLat End, Map<LngLat, LngLat> cameFrom) {
        List<LngLat> path = new ArrayList<>();
        path.add(Start);
        while (cameFrom.containsKey(Start)) {
            Start = cameFrom.get(Start);
            path.add(Start);
        }
        return path;
    }
}
