package uk.ac.ed.inf.pathfinding;


import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.model.Node;

import java.util.*;


public class AStarPathFindingAlgorithim {
    public static List<LngLat> astar(LngLat Start, LngLat End, NamedRegion[] noFlyZones, NamedRegion centralArea){
        PriorityQueue<Node> openSet = new PriorityQueue<>(new Comparator<Node>(){
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.getgScore() + o1.gethScore(), o2.getgScore() + o2.gethScore());
            }
        });

        boolean flag = true;
        Set<LngLat> closedSet = new HashSet<>();
        LngLatHandling lngLatHandling = new LngLatHandler();
        Node startNode = new Node(Start, null, 0.0, heuristic(Start, End));

        openSet.add(startNode);
        closedSet.add(startNode.getCurrLngLat());
        while(!openSet.isEmpty()){
            Node currNode = openSet.poll();
            if (lngLatHandling.isCloseTo(currNode.getCurrLngLat(), End)){
                return reconstructPath(currNode, End);
            }
            if (!lngLatHandling.isInRegion(currNode.getCurrLngLat(), centralArea)){
                flag = false;
            }
            List<LngLat> nextPositions1 = generateNextPositions(currNode.getCurrLngLat(), lngLatHandling);
            List<LngLat> nextPositions = filterMoves(nextPositions1, noFlyZones, lngLatHandling, flag, centralArea);
            for(LngLat nextPosition: nextPositions){
                //remove curr node g score for quick implementation
                Node nextNode = new Node(nextPosition, currNode, lngLatHandling.distanceTo(currNode.getCurrLngLat(), nextPosition), heuristic(nextPosition, End));
                if(closedSet.contains(nextPosition)){
                    continue;
                }
                if(!openSet.contains(nextNode)){
                    openSet.add(nextNode);
                    closedSet.add(nextPosition);
                }
            }
        }
        return null;
    }



    public static List<LngLat> filterMoves(List<LngLat> nextPositions, NamedRegion[] noFlyZones, LngLatHandling lngLatHandling, boolean flag, NamedRegion centralArea){
        List<LngLat> NextPositionsCopy = new ArrayList<>(nextPositions);
        for(LngLat nextPosition: nextPositions){
            for(NamedRegion noFlyZone: noFlyZones){
                if(lngLatHandling.isInRegion(nextPosition, noFlyZone)){
                    NextPositionsCopy.remove(nextPosition);
                }
                if(!flag){
                   if(lngLatHandling.isInRegion(nextPosition, centralArea)){
                       NextPositionsCopy.remove(nextPosition);
                   }
                }

            }
        }
        return NextPositionsCopy;
    }

    public static List<LngLat> reconstructPath(Node currNode, LngLat End){
        List<LngLat> path = new ArrayList<>();
        while(currNode != null){
            path.add(currNode.getCurrLngLat());
            currNode = currNode.getParentLngLat();
        }
        Collections.reverse(path);
        path.add(End);
        return path;
    }

    public static List<LngLat> generateNextPositions(LngLat curr, LngLatHandling lngLatHandling){
        List<LngLat> nextPositions = new ArrayList<>();
        double[] legalMoves = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        for(double legalMove: legalMoves){
            nextPositions.add(lngLatHandling.nextPosition(curr, legalMove));
        }
        return nextPositions;
    }

    public static double heuristic(LngLat pos, LngLat end){
        LngLatHandling lngLatHandler = new LngLatHandler();
        return lngLatHandler.distanceTo(pos, end);
    }
}
