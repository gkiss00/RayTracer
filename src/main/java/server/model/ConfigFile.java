package server.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ConfigFile {
    public Config config = new Config();
    public List<Camera> cameras = new ArrayList<>();
    public List<Object> objects = new ArrayList<>();

    public ConfigFile() {}

    public String toJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{}";
        }
    }
}
