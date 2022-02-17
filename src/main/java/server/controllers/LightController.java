package server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import server.model.Light;
import server.model.LightDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LightController {
    private static final List<Light> lights = new ArrayList<>();

    public static List<Light> getLights() {
        return lights;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                   GET                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @GetMapping(value = "/lights", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Light> getAll() {
        return lights;
    }

    @CrossOrigin
    @GetMapping("/light/{id}")
    public Light getOne(@PathVariable Integer id) {
        Light light = null;
        for (int i = 0; i < lights.size(); ++i) {
            if (lights.get(i).getId() == id) {
                light = lights.get(i);
                break;
            }
        }
        return light;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                  POST                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @PostMapping("/light/add")
    public Light addLight(
            @RequestBody LightDTO lightDTO
    ) {
        Light light = new Light(lightDTO);
        lights.add(light);
        return light;
    }


    @CrossOrigin
    @PostMapping("/light/{id}/update")
    public Light updateLight(
            @PathVariable Integer id,
            @RequestBody LightDTO lightDTO
    ) {
        Light light = null;
        for (int i = 0; i < lights.size(); ++i) {
            if (lights.get(i).getId() == id) {
                light = lights.get(i);
                light.update(lightDTO);
                break;
            }
        }
        return light;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                  DELETE               *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @DeleteMapping("/light/{id}")
    public Light deleteLight(
            @PathVariable Integer id
    ) {
        for (int i = 0; i < lights.size(); ++i) {
            if (lights.get(i).getId() == id) {
                return lights.remove(i);
            }
        }
        return null;
    }
}
