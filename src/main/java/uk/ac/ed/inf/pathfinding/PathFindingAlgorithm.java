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
        Node startNode = new Node(Start, null, 0.0, heuristic(Start, End), 999);

        nodeMap.put(Start, startNode);
        openSet.add(startNode);
        closedSet.add(startNode.getCurrLngLat());

        while(!openSet.isEmpty()){
            Node currNode = openSet.poll();
            closedSet.add(currNode.getCurrLngLat());

            if (lngLatHandling.isCloseTo(currNode.getCurrLngLat(), End)){
                return reconstructPath(currNode, End, order);
            }

            if (!lngLatHandling.isInRegion(currNode.getCurrLngLat(), centralArea)){
                flag = false;
            }

            List<Node> nextPositions = generateNextPositions(currNode.getCurrLngLat(), lngLatHandling, noFlyZones, flag, centralArea);

            for(Node nextPosition: nextPositions){
                if (closedSet.contains(nextPosition.getCurrLngLat())){
                    continue;
                }

                double tentativeGScore = currNode.getGScore() + SystemConstants.DRONE_MOVE_DISTANCE;

                LngLat pos = nextPosition.getCurrLngLat();
                nextPosition.setParentNode(currNode);
                nextPosition.setgScore(tentativeGScore);
                nextPosition.sethScore(heuristic(nextPosition.getCurrLngLat(), End));

                if(nodeMap.containsKey(pos)){
                    Node node = nodeMap.get(nextPosition);
                    if(node.getGScore() > tentativeGScore){
                        nodeMap.put(pos, nextPosition);
                        openSet.remove(node);
                        openSet.add(nextPosition);
                    }
                }

                else{
                    nodeMap.put(pos, nextPosition);
                    openSet.add(nextPosition);
                    closedSet.add(pos);
                }

            }
        }
        return null;
    }

    public static List<Move> reconstructPath(Node currNode, LngLat End, Order order){
        List<Move> path = new ArrayList<>();
        List<Move> fullPath = new ArrayList<>();
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
            move.setAngle(currNode.getAngle());
            path.add(move);
            currNode = currNode.getParentLngLat();
        }
        fullPath.addAll(path);
        Collections.reverse(fullPath);
        //TODO: might have to change
        fullPath.add(new Move(order.getOrderNo(), End.lng(), End.lat(), End.lng(), End.lat(), 999));
        fullPath.addAll(path);
        //TODO: might have to change
        fullPath.add(new Move(order.getOrderNo(), End.lng(), End.lat(), End.lng(), End.lat(), 999));
        return fullPath;
    }

    public static List<Node> generateNextPositions(LngLat curr, LngLatHandling lngLatHandling, NamedRegion[] noFlyZones, boolean flag, NamedRegion centralArea){
        List<Node> nextPositions = new ArrayList<>();
        double[] legalMoves = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        for(double legalMove: legalMoves){
            boolean isValid = true;
            LngLat nextPosition = lngLatHandling.nextPosition(curr, legalMove);
            for(NamedRegion noFlyZone: noFlyZones){
                if(lngLatHandling.isInRegion(nextPosition, noFlyZone)){
                    isValid = false;
                }
                if(!flag){
                    if(lngLatHandling.isInRegion(nextPosition, centralArea)){
                        isValid = false;
                    }
                }
            }
            if (isValid){
                nextPositions.add(new Node(nextPosition, null, 0.0, 0.0, legalMove));
            }
        }
        return nextPositions;
    }

    public static double heuristic(LngLat pos, LngLat end){
        LngLatHandling lngLatHandler = new LngLatHandler();
        return lngLatHandler.distanceTo(pos, end);
    }
}
