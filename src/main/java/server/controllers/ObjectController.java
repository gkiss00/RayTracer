package server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import server.model.Object;

import java.util.ArrayList;
import java.util.List;

@RestController()
public class ObjectController {
    private static List<Object> objects = new ArrayList<>();

    public static List<Object> getObjects() {
        return objects;
    }

    @CrossOrigin
    @GetMapping(value = "/objects", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getAll() {
        return objects;
    }

    @GetMapping(value = "/object/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getOne(@PathVariable() Integer id) {
        if(id < 0 || id >= objects.size())
            return null;
        return objects.get(id);
    }

    /*
    {
        "type": "SPHERE",
        "values": [30],
        "coordinates": {
            "x": 0,
            "y": 0,
            "z": 0
        },
        "scaling": {
            "x": 1,
            "y": 1,
            "z": 1
        },
        "rotation": {
            "x": 10,
            "y": 20,
            "z": 30
        }
    }
     */

    @PostMapping(value = "/object/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object addOne(@RequestBody Object object) {
        objects.add(object);
        return object;
    }

    @PostMapping(value = "/object/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateOne(@PathVariable Integer id, @RequestBody Object object) {
        if(id < 0 || id >= objects.size())
            return null;
        Object o = objects.get(id);
        o.update(object);
        return o;
    }
}
