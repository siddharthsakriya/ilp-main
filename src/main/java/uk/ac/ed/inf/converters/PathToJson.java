package uk.ac.ed.inf.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.model.Move;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PathToJson {
    public static void convertToMoveAndSerialise(List<LngLat> moves, Order order, Map<String, File> fileMap){
        List<Move> moveList = new ArrayList<>();

        for (int i = 0; i < moves.size() - 2; i++){
            Move move = new Move();
            move.setOrderNo(order.getOrderNo());
            move.setFromLatitude(moves.get(i).lat());
            move.setFromLongitude(moves.get(i).lng());
            move.setToLatitude(moves.get(i + 1).lat());
            move.setToLongitude(moves.get(i + 1).lng());
            //TODO: add code here to serialise object and add it to the json file
            moveList.add(move);
        }
        serialiseMove(moveList, fileMap.get("flightpathFileName"));
    }
    private static void serialiseMove(List<Move> moveList, File outputFile){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Move> existingMoves;
            if(outputFile.exists()){
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                CollectionType collectionType = typeFactory.constructCollectionType(ArrayList.class, Move.class);
                existingMoves = objectMapper.readValue(outputFile, collectionType);
            }
            else{
                existingMoves = new ArrayList<>();
            }
            existingMoves.addAll(moveList);
            ObjectWriter writer = objectMapper.writerFor(new TypeReference<List<Move>>() {});
            writer.writeValue(outputFile, existingMoves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
