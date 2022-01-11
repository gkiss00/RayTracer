package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import objects.BaseObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<BaseObject> getObjects(File file) {
        List<BaseObject> objects = new ArrayList<>();
        try {
            objects = objectMapper.readValue(file, new TypeReference<List<BaseObject>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
