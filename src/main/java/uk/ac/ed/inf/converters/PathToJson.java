package uk.ac.ed.inf.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.model.Move;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PathToJson {
    private static void convertToMoveAndSerialise(List<LngLat> moves, Order order, String date){
        File outputFile = setupPath(date);
        for (int i = 0; i < moves.size() - 2; i++){
            Move move = new Move();
            move.setOrderNo(order.getOrderNo());
            move.setFromLatitude(moves.get(i).lat());
            move.setFromLatitude(moves.get(i).lng());
            move.setToLatitude(moves.get(i + 1).lat());
            move.setToLongitude(moves.get(i + 1).lng());
            //TODO: add code here to serialise object and add it to the json file
            serialiseMove(move, outputFile);
        }
    }
    private static File setupPath(String date){
        String filepath = "src/main/java/uk/ac/ed/inf/resultfiles";
        String fileName = "flightpath-"+date+".json";
        File directory = new File(filepath);
        directory.mkdirs();
        File outputFile = new File(directory, fileName);
        System.out.println("JSON file created at: " + outputFile.getAbsolutePath());
        return outputFile;
    }
    private static void serialiseMove(Move move, File outputFile){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(outputFile, move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
