package server.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import server.model.ConfigFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

@RestController
public class ConfigFileController {
    private ConfigFile configFile = new ConfigFile();

    // Return a JSON file of the config without json extension
    @GetMapping(
            value = "/configFile",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    byte[] getConfigFile() throws Exception{
        String fileName = "configFile.json";
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(configFile.toJSON());
        fileWriter.close();
        InputStream in = new FileInputStream(file);
        System.out.println(in);
        return IOUtils.toByteArray(in);
    }

    // Return a JSON file of the config with json extension
    @GetMapping(
            value = "/configFile1"
    )
    public @ResponseBody
    ResponseEntity<byte[]> getConfigFile1() throws Exception{
        updateConfigFile();
        String jsonFile = configFile.toJSON();
        byte[] byteFile = jsonFile.getBytes();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=configFile.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(byteFile.length)
                .body(byteFile);
    }

    private void updateConfigFile() {
        configFile.config = ConfigController.getConfiguration();
        configFile.cameras.clear();
        configFile.cameras.addAll(CameraController.getCameras());
        configFile.objects.clear();
        configFile.objects.addAll(ObjectController.getObjects());
    }
}
