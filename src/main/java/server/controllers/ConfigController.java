package server.controllers;

import org.springframework.web.bind.annotation.*;
import server.model.Config;

@RestController()
public class ConfigController {
    private Config config = new Config();

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

    @PostMapping("/config")
    public Config setConfig(@RequestBody Config config) {
        this.config = config;
        return this.config;
    }
}
