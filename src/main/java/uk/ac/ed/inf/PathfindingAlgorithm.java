package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
import uk.ac.ed.inf.model.LngLatHandler;

import java.util.ArrayList;
import java.util.List;

public class PathfindingAlgorithm {
    public static List<LngLat> pathfinder(LngLat start, LngLat end, NamedRegion[] noFlyZones, NamedRegion centralArea){
        LngLatHandling lngLatHandling = new LngLatHandler();
        LngLat curr = start;
        boolean flag = false;
        List<LngLat> moves = new ArrayList<>();
        moves.add(start);
        while (true){
            List<LngLat> nextPositions = generateNextPositions(curr, lngLatHandling);
            List<LngLat> nextPositionsFiltered = filterMoves(nextPositions, noFlyZones, lngLatHandling, centralArea, start, flag);
            LngLat nextMove = pickOptimalMove(nextPositionsFiltered, lngLatHandling, curr, end);
            if(lngLatHandling.isCloseTo(nextMove, end)){
                break;
            }
            if(lngLatHandling.isInRegion(nextMove, centralArea) != lngLatHandling.isInRegion(start, centralArea)){
                flag = true;
            }
            moves.add(nextMove);
            curr = nextMove;
        }
        moves.add(end);
        return moves;
    }
    public static List<LngLat> generateNextPositions(LngLat curr, LngLatHandling lngLatHandling){
        List<LngLat> nextPositions = new ArrayList<>();
        double[] legalMoves = { 0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        for(double legalMove: legalMoves){
            nextPositions.add(lngLatHandling.nextPosition(curr, legalMove));
        }
        return nextPositions;
    }
    public static List<LngLat> filterMoves(List<LngLat> nextPositions, NamedRegion[] noFlyZones, LngLatHandling lngLatHandling, NamedRegion centralArea, LngLat start, boolean flag){
        List<LngLat> NextPositionsCopy = new ArrayList<>(nextPositions);
        for(LngLat nextPosition: nextPositions){
            for(NamedRegion noFlyZone: noFlyZones){
                if(lngLatHandling.isInRegion(nextPosition, noFlyZone)){
                    NextPositionsCopy.remove(nextPosition);
                }
                if(flag){
                    if(lngLatHandling.isInRegion(nextPosition, centralArea) == lngLatHandling.isInRegion(start, centralArea)){
                        NextPositionsCopy.remove(nextPosition);
                    }
                }
            }
        }
        return NextPositionsCopy;
    }

    public static LngLat pickOptimalMove(List<LngLat> nextPositionsFiltered, LngLatHandling lngLatHandling, LngLat curr, LngLat end){
        double minDist = Double.MAX_VALUE;
        LngLat nextMove = null;
        for (LngLat nextPosition: nextPositionsFiltered){
            if(lngLatHandling.distanceTo(nextPosition, end) < minDist && curr != nextPosition){
                minDist = lngLatHandling.distanceTo(nextPosition, end);
                nextMove = nextPosition;
            }
        }
        return nextMove;
    }
}

