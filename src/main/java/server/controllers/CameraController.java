package server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import server.model.Camera;
import server.model.CameraDTO;

import java.util.ArrayList;
import java.util.List;

@RestController()
public class CameraController {
    private static List<Camera> cameras = new ArrayList<>();

    public static List<Camera> getCameras() {
        return cameras;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                   GET                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @GetMapping(value = "/cameras", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody() List<Camera> getAll() {
        return cameras;
    }

    @CrossOrigin
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

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                  POST                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @PostMapping("/camera/add")
    public Camera addCamera(
            @RequestBody CameraDTO cameraDTO
    ) {
        Camera camera = new Camera(cameraDTO);
        cameras.add(camera);
        return camera;
    }


    @CrossOrigin
    @PostMapping("/camera/{id}/update")
    public Camera updateCamera(
            @PathVariable Integer id,
            @RequestBody CameraDTO camera
    ) {
        Camera cam = null;
        for (int i = 0; i < cameras.size(); ++i) {
            if (cameras.get(i).getId() == id) {
                cam = cameras.get(i);
                cam.update(camera);
                break;
            }
        }
        return cam;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                  DELETE               *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    @CrossOrigin
    @DeleteMapping("/camera/{id}")
    public Camera deleteCamera(
            @PathVariable Integer id
    ) {
        for (int i = 0; i < cameras.size(); ++i) {
            if (cameras.get(i).getId() == id) {
                return cameras.remove(i);
            }
        }
        return null;
    }
}
