package uk.ac.ed.inf.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.ed.inf.model.Delivery;
import uk.ac.ed.inf.model.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            ObjectWriter writer = objectMapper.writerFor(new TypeReference<List<Move>>() {});
            writer.writeValue(outputFile, moveList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serialiseOrder(List<Delivery> deliveries, File outputFile){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ObjectWriter writer = objectMapper.writerFor(new TypeReference<List<Delivery>>() {});
            writer.writeValue(outputFile, deliveries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processMoves(List<Move> movesList, File file){
        JSONObject featureCollections = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONArray features = new JSONArray();
        JSONObject feature = new JSONObject();
        JSONObject geometry = new JSONObject();

        featureCollections.put("type", "FeatureCollection");
        featureCollections.put("features", features);

        features.put(feature);
        feature.put("type", "Feature");
        feature.put("properties", properties);
        feature.put("geometry", geometry);

        geometry.put("type", "LineString");
        geometry.put("coordinates", populateCoordinates(movesList));

        writeToGeoJsonFile(file, featureCollections.toString());

    }

    private static JSONArray createCoordinate(double longitude, double latitude) {
        JSONArray coordinate = new JSONArray();
        coordinate.put(longitude);
        coordinate.put(latitude);
        return coordinate;
    }

    public static JSONArray populateCoordinates(List<Move> movesList){
        JSONArray coordinates = new JSONArray();
        for(Move move: movesList){
            coordinates.put(createCoordinate(move.getFromLongitude(), move.getFromLatitude()));
        }
        return coordinates;
    }

    public static void writeToGeoJsonFile(File file, String geoJsonString){
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(geoJsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
