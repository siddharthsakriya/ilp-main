package uk.ac.ed.inf.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.ac.ed.inf.ilp.data.Order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderToJson {
    /**
     * Serialises an order to a json file
     * @param order
     * @param outputFile
     */
    public static void serialiseOrder(Order order, File outputFile){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Order> existingOrders = new ArrayList<>();;
            if(outputFile.exists()){
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                CollectionType collectionType = typeFactory.constructCollectionType(ArrayList.class, Order.class);
                existingOrders = objectMapper.readValue(outputFile, collectionType);
            }
            existingOrders.add(order);
            ObjectWriter writer = objectMapper.writerFor(new TypeReference<List<Order>>() {});
            writer.writeValue(outputFile, existingOrders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
