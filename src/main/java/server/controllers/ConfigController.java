package server.controllers;

import org.springframework.web.bind.annotation.*;
import server.model.Config;

@RestController()
public class ConfigController {
    private static Config config = new Config();

    public static Config getConfiguration() {
        return config;
    }

    @CrossOrigin
    @GetMapping("/config")
    public Config getConfig() {
        return config;
    }

    /*
    {
        "height": 1000,
        "width": 1000,
        "antiAliasing": 3,
        "maxReflexion": 10,
        "finalFilter": "NONE"
    }
     */

    @CrossOrigin
    @PostMapping("/config")
    public Config setConfig(@RequestBody Config config) {
        this.config = config;
        return this.config;
    }
}
