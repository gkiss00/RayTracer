package server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import server.model.Camera;

import java.util.ArrayList;
import java.util.List;

@RestController()
public class CameraController {
    private List<Camera> cameras = new ArrayList<>();

    @GetMapping(value = "/cameras", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody() List<Camera> getAll() {
        return cameras;
    }

    @GetMapping("/camera/{id}")
    public Camera getOne(@PathVariable Integer id) {
        if(id < 0 || id >= cameras.size())
            return null;
        return cameras.get(id);
    }

        /*
        {
            "pointOfVue": {
                "x" : 0,
                "y" : 0,
                "z" : 0
            },
            "direction": {
                "x" : 1,
                "y" : 0,
                "z" : 0
            },
             "up": {
                "x" : 0,
                "y" : 0,
                "z" : 1
            },
            "angle" : 60
        }
     */

    @PostMapping("/camera/add")
    public Camera addCamera(
            @RequestBody Camera camera
    ) {
        cameras.add(camera);
        return camera;
    }


    @PostMapping("/camera/{id}/update")
    public Camera updateCamera(
            @PathVariable Integer id,
            @RequestBody Camera camera
    ) {
        if(id < 0 || id > cameras.size())
            return null;
        Camera cam = cameras.get(id);
        cam.update(camera);
        return cam;
    }
}
