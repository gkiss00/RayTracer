package rayTracer.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.BaseObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<Obj> getObjects(File file) {
        List<Obj> objects = new ArrayList<>();
        try {
            objects = objectMapper.readValue(file, new TypeReference<List<Obj>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
