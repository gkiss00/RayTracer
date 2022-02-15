package server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import server.model.Camera;
import server.model.Object;
import server.model.ObjectDTO;

import java.util.ArrayList;
import java.util.List;

@RestController()
public class ObjectController {
    private static List<Object> objects = new ArrayList<>();

    public static List<Object> getObjects() {
        return objects;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                   GET                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @GetMapping(value = "/objects", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getAll() {
        return objects;
    }

    @CrossOrigin
    @GetMapping(value = "/object/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getOne(@PathVariable() Integer id) {
        Object obj = null;
        for (int i = 0; i < objects.size(); ++i) {
            if (objects.get(i).getId() == id) {
                obj = objects.get(i);
                break;
            }
        }
        return obj;
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

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                  POST                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @PostMapping(value = "/object/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object addOne(@RequestBody ObjectDTO objectDTO) {
        Object object = new Object(objectDTO);
        objects.add(object);
        return object;
    }

    @CrossOrigin
    @PostMapping(value = "/object/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateOne(@PathVariable Integer id, @RequestBody ObjectDTO objectDTO) {
        Object object = null;
        for (int i = 0; i < objects.size(); ++i) {
            if (objects.get(i).getId() == id) {
                object = objects.get(i);
                object.update(objectDTO);
                break;
            }
        }
        return object;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                  DELETE               *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @DeleteMapping("/object/{id}")
    public Object deleteObject(
            @PathVariable Integer id
    ) {
        for (int i = 0; i < objects.size(); ++i) {
            if (objects.get(i).getId() == id) {
                return objects.remove(i);
            }
        }
        return null;
    }
}
