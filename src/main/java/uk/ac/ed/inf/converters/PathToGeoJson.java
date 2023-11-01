package uk.ac.ed.inf.converters;

import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.ed.inf.model.Move;

import java.io.*;
import java.util.List;

public class PathToGeoJson {
    public static void geofyJson(File file, List<Move> path) {

        if (fileExists(file)) {
            // If the file exists, append new coordinates
            for(Move move: path){
                appendCoordinatesToFile(file, move.getFromLongitude(), move.getFromLatitude());
            }
        } else {
            // If the file does not exist, create it and add the first set of coordinates
            createFileWithCoordinates(file, path);
        }
    }

    // Helper method to check if a file exists
    private static boolean fileExists(File file) {
        return file.exists() && !file.isDirectory();
    }

    // Helper method to append coordinates to a file
    private static void appendCoordinatesToFile(File file, double longitude, double latitude) {
        try {
            // Load existing GeoJSON data from the file
            String geoJSON = readGeoJSONFromFile(file);

            // Parse the GeoJSON data
            JSONObject featureCollection = new JSONObject(geoJSON);

            // Retrieve the feature
            JSONArray features = featureCollection.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);

            // Append new coordinates to the existing feature
            appendCoordinatesToFeature(feature, longitude, latitude);

            // Write the updated GeoJSON data back to the file
            writeGeoJSONToFile(file, featureCollection.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to create a new file with coordinates
    private static void createFileWithCoordinates(File file, List<Move> path) {
        JSONObject featureCollection = new JSONObject();
        featureCollection.put("type", "FeatureCollection");
        JSONArray features = new JSONArray();

        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");
        feature.put("properties", new JSONObject());

        JSONObject geometry = new JSONObject();
        geometry.put("type", "LineString");
        JSONArray coordinates = new JSONArray();
        for(Move move: path){
            coordinates.put(createCoordinate(move.getFromLongitude(), move.getFromLatitude()));
        }
        geometry.put("coordinates", coordinates);
        feature.put("geometry", geometry);
        features.put(feature);
        featureCollection.put("features", features);

        // Write the GeoJSON data to the new file
        writeGeoJSONToFile(file, featureCollection.toString());
    }

    // Helper method to read GeoJSON data from a file
    private static String readGeoJSONFromFile(File fileName) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Helper method to append new coordinates to an existing feature's LineString geometry
    private static void appendCoordinatesToFeature(JSONObject feature, double longitude, double latitude) {
        JSONObject geometry = feature.getJSONObject("geometry");
        JSONArray coordinates = geometry.getJSONArray("coordinates");
        coordinates.put(createCoordinate(longitude, latitude));
    }

    // Helper method to create a coordinate array [longitude, latitude]
    private static JSONArray createCoordinate(double longitude, double latitude) {
        JSONArray coordinate = new JSONArray();
        coordinate.put(longitude);
        coordinate.put(latitude);
        return coordinate;
    }

    // Helper method to write the GeoJSON data to a file
    private static void writeGeoJSONToFile(File file, String geoJSON) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(geoJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
