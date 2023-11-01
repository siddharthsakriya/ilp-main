package uk.ac.ed.inf.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import uk.ac.ed.inf.model.Move;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathToJson {
    /**
     * Serialises the given moveList to the given outputFile
     * @param moveList
     * @param outputFile
     */
    public static void serialiseMove(List<Move> moveList, File outputFile){
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            List<Move> existingMoves = new ArrayList<>();

            if(outputFile.exists()){
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                CollectionType collectionType = typeFactory.constructCollectionType(ArrayList.class, Move.class);
                existingMoves = objectMapper.readValue(outputFile, collectionType);
            }

            existingMoves.addAll(moveList);

            ObjectWriter writer = objectMapper.writerFor(new TypeReference<List<Move>>() {});
            writer.writeValue(outputFile, existingMoves);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
