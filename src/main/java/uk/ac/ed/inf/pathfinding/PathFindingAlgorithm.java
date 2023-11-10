package uk.ac.ed.inf.pathfinding;

import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.model.Node;

import java.util.*;

public class PathFindingAlgorithm {
    public static List<Move> findPath(LngLat Start, LngLat End, NamedRegion[] noFlyZones, NamedRegion centralArea, Order order){

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(o -> o.getGScore() + o.getHScore())
        );

        Map<LngLat, Node> nodeMap = new HashMap<>();
        boolean flag = true;
        Set<LngLat> closedSet = new HashSet<>();
        LngLatHandling lngLatHandling = new LngLatHandler();
        Node startNode = new Node(Start, null, 0.0, heuristic(Start, End));

        nodeMap.put(Start, startNode);
        openSet.add(startNode);
        closedSet.add(startNode.getCurrLngLat());

        while(!openSet.isEmpty()){
            Node currNode = openSet.poll();
            closedSet.add(currNode.getCurrLngLat());

            if (lngLatHandling.isCloseTo(currNode.getCurrLngLat(), End)){
                return reconstructPath(currNode, End, order);
            }

//            boolean enteredCentral = false;
//
//            if (!enteredCentral && currNodelngLatHandling.isInRegion(currNode.getCurrLngLat(), centralArea) && !lngLatHandling.isInRegion(currNode.getParentLngLat().getCurrLngLat(), centralArea)) {
//                enteredCentral = true;
//            }
            if (!lngLatHandling.isInRegion(currNode.getCurrLngLat(), centralArea)){
                flag = false;
            }
            List<LngLat> nextPositions1 = generateNextPositions(currNode.getCurrLngLat(), lngLatHandling);
            List<LngLat> nextPositions = filterMoves(nextPositions1, noFlyZones, lngLatHandling, flag, centralArea, currNode.getCurrLngLat());

            for(LngLat nextPosition: nextPositions){
                if (closedSet.contains(nextPosition)){
                    continue;
                }

                double tentativeGScore = currNode.getGScore() + SystemConstants.DRONE_MOVE_DISTANCE;
                Node nextNode = new Node(nextPosition, currNode, tentativeGScore, heuristic(nextPosition, End));

                if(nodeMap.containsKey(nextPosition)){
                    Node node = nodeMap.get(nextPosition);
                    if( node.getGScore() > tentativeGScore){
                        nodeMap.put(nextPosition, nextNode);
                        openSet.remove(node);
                        openSet.add(nextNode);

                    }
                }
                else{
                    nodeMap.put(nextPosition, nextNode);
                    openSet.add(nextNode);
                    closedSet.add(nextNode.getCurrLngLat());
                }
            }
        }
        return null;
    }

    public static List<LngLat> filterMoves(List<LngLat> nextPositions, NamedRegion[] noFlyZones, LngLatHandling lngLatHandling, boolean flag, NamedRegion centralArea, LngLat curr){
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

    public static List<Move> reconstructPath(Node currNode, LngLat End, Order order){
        List<Move> path = new ArrayList<>();
        while(currNode != null){
            Move move = new Move();
            move.setOrderNo(order.getOrderNo());
            if (currNode.getParentLngLat() == null){
                move.setToLongitude(currNode.getCurrLngLat().lng());
                move.setToLatitude(currNode.getCurrLngLat().lat());
                break;
            }
            move.setFromLongitude(currNode.getParentLngLat().getCurrLngLat().lng());
            move.setFromLatitude(currNode.getParentLngLat().getCurrLngLat().lat());
            move.setToLongitude(currNode.getCurrLngLat().lng());
            move.setToLatitude(currNode.getCurrLngLat().lat());
            path.add(move);
            currNode = currNode.getParentLngLat();
        }
        Collections.reverse(path);
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
//
//    public static boolean doesLineIntersect(LngLat start, LngLat end, LngLat[] polygon){
//        for (int i = 0; i < polygon.length; i++){
//            LngLat p2 = polygon[i];
//            LngLat q2 = polygon[(i + 1) % polygon.length];
//            if (doIntersect(start, end, p2, q2)){
//                return true;
//            }
//
//        }
//        return false;
//    }
//
//    public static boolean onSegment(LngLat p, LngLat q, LngLat r) {
//        // Check if point q lies on line segment pr
//        return (q.lng() <= Math.max(p.lng(), r.lng()) && q.lng() >= Math.min(p.lng(), r.lng()) &&
//                q.lat() <= Math.max(p.lat(), r.lat()) && q.lat() >= Math.min(p.lat(), r.lat()));
//    }
//
//    public static int orientation(LngLat p, LngLat q, LngLat r) {
//        // Calculate orientation of triplet (p, q, r)
//        // Returns:
//        // 0 if p, q, r are collinear
//        // 1 if p, q, r are clockwise
//        // 2 if p, q, r are counterclockwise
//        double val = (q.lat() - p.lat()) * (r.lng() - q.lng()) - (q.lng() - p.lng()) * (r.lat() - q.lat());
//        if (val == 0) return 0;
//        return (val > 0) ? 1 : 2;
//    }
//
//    public static boolean doIntersect(LngLat p1, LngLat q1, LngLat p2, LngLat q2) {
//        int o1 = orientation(p1, q1, p2);
//        int o2 = orientation(p1, q1, q2);
//        int o3 = orientation(p2, q2, p1);
//        int o4 = orientation(p2, q2, q1);
//
//        if (o1 != o2 && o3 != o4) {
//            return true;
//        }
//
//        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
//        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
//        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
//        if (o4 == 0 && onSegment(p2, q1, q2)) return true;
//
//        return false;
//    }
}
