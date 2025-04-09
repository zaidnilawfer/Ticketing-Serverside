package com.example.serverside_coursework.controller;

import com.example.serverside_coursework.config.AppConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = {"http://localhost:4200"}) // Approach 1: Controller-level CORS

public class ConfigController {
    private final AppConfigProperties configProperties;
    private final Environment env;

    @Autowired
    public ConfigController(AppConfigProperties configProperties, Environment env) {
        this.configProperties = configProperties;
        this.env = env;
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getConfig(@RequestParam String key) {
        return ResponseEntity.ok(env.getProperty(key));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateConfig(@RequestParam String key, @RequestParam String value) {
        // This is a runtime update of system properties
        System.setProperty(key, value);
        return ResponseEntity.ok("Configuration updated");
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentConfig() {
        return ResponseEntity.ok(Map.of(
                "poolCapacity", configProperties.getPoolCapacity(),
                "threadPoolSize", configProperties.getThreadPoolSize(),
                "batchSize", configProperties.getBatchSize()
        ));
    }
}