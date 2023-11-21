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
    private static LngLatHandling lngLatHandler = new LngLatHandler();
    private static final LngLat startPoint = new LngLat(-3.186874, 55.944494);

    public static List<Move> findPath(LngLat goalPoint, NamedRegion[] noFlyZones, NamedRegion centralArea, Order order){

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(currNode -> currNode.getGScore() +
                currNode.getHScore()));
        Map<LngLat, Node> nodeMap = new HashMap<>();
        Set<LngLat> closedSet = new HashSet<>();

        boolean inCentral = true;

        Node startNode = new Node(startPoint, null, 0.0, heuristic(startPoint, goalPoint), 999);
        nodeMap.put(startPoint, startNode);
        openSet.add(startNode);
//        closedSet.add(startNode.getCurrLngLat());

        while(!openSet.isEmpty()){

            Node currNode = openSet.poll();
            closedSet.add(currNode.getCurrLngLat());

            if (lngLatHandler.isCloseTo(currNode.getCurrLngLat(), goalPoint)){
                return reconstructPath(currNode, goalPoint, startPoint, order);
            }
            if (!lngLatHandler.isInRegion(currNode.getCurrLngLat(), centralArea)){
                inCentral = false;
            }

            List<Node> nextPositions = generateNextPositions(currNode.getCurrLngLat(), noFlyZones, inCentral, centralArea);
            for(Node nextPosition: nextPositions){

                if (closedSet.contains(nextPosition.getCurrLngLat())){
                    continue;
                }

                double tentativeGScore = currNode.getGScore() + SystemConstants.DRONE_MOVE_DISTANCE;

                LngLat pos = nextPosition.getCurrLngLat();
                nextPosition.setParentNode(currNode);
                nextPosition.setGScore(tentativeGScore);
                nextPosition.setHScore(heuristic(nextPosition.getCurrLngLat(), goalPoint));

                if(nodeMap.containsKey(pos)){
                    Node node = nodeMap.get(pos);
                    if(node.getGScore() > tentativeGScore){
                        nodeMap.put(pos, nextPosition);
                        openSet.remove(node);
                        openSet.add(nextPosition);
                    }
                }
                else{
                    nodeMap.put(pos, nextPosition);
                    openSet.add(nextPosition);
                }
            }
        }
        return null;
    }

    public static List<Move> reconstructPath(Node currNode, LngLat End, LngLat Start, Order order){
        List<Move> path = new ArrayList<>();
        List<Move> fullPath = new ArrayList<>();
        while(currNode.getParentNode() != null){
            Move move = new Move(order.getOrderNo(), currNode.getParentNode().getCurrLngLat().lng(),
                    currNode.getParentNode().getCurrLngLat().lat(), currNode.getCurrLngLat().lng(),
                    currNode.getCurrLngLat().lat(), currNode.getAngle());
            path.add(move);
            currNode = currNode.getParentNode();
        }

        fullPath.addAll(path);
        Collections.reverse(fullPath);
//        fullPath.add(new Move(order.getOrderNo(), End.lng(), End.lat(), End.lng(), End.lat(), 999));

        for(Move move: path){
            Move revMove = new Move(move.getOrderNo(), move.getToLongitude(), move.getToLatitude(),
                    move.getFromLongitude(), move.getFromLatitude(), (move.getAngle() + 180) % 360);
            fullPath.add(revMove);
        }
//        fullPath.add(new Move(order.getOrderNo(), Start.lng(), Start.lat(), Start.lng(), Start.lat(), 999));
        return fullPath;
    }

    private static List<Node> generateNextPositions(LngLat curr, NamedRegion[] noFlyZones, boolean flag, NamedRegion centralArea){
        List<Node> nextPositions = new ArrayList<>();
        double[] legalMoves = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        for(double legalMove: legalMoves){
            boolean isValid = true;
            LngLat nextPosition = lngLatHandler.nextPosition(curr, legalMove);
            for(NamedRegion noFlyZone: noFlyZones){
                if(lngLatHandler.isInRegion(nextPosition, noFlyZone)){
                    isValid = false;
                }
                if(!flag){
                    if(lngLatHandler.isInRegion(nextPosition, centralArea)){
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

    private static double heuristic(LngLat pos, LngLat end){
        return lngLatHandler.distanceTo(pos, end);
    }
}
