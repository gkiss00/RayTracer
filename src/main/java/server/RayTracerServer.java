package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rayTracer.RayTracer;
import server.controllers.CameraController;
import server.controllers.ConfigController;
import server.controllers.LightController;
import server.controllers.ObjectController;
import server.utils.Converter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;


@SpringBootApplication()
@RestController()
public class RayTracerServer {
    public static void main(String[]args) {
        SpringApplication.run(RayTracerServer.class, args);
    }

    /*
        Create the image can take several minutes
     */
    @CrossOrigin
    @GetMapping(
            value = "/run",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] run() {
        RayTracer.run(
                ConfigController.getConfiguration(),
                Converter.getObjects(ObjectController.getObjects()),
                Converter.getCameras(CameraController.getCameras()),
                Converter.getLights(LightController.getLights())
        );
        try {
            BufferedImage bImage = ImageIO.read(new File("Image.png"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos );
            byte [] data = bos.toByteArray();
            return data;
        } catch(Exception e) {

        }
        byte [] data = new byte[0];
        return data;
    }

    @CrossOrigin
    @PostMapping(
            value="/test"
    )
    public ResponseEntity test(@RequestParam("file")MultipartFile[] file) {
        for (int i = 0; i < file.length; ++i) {
            String fileName = file[i].getOriginalFilename();
            System.out.println(fileName + " " + file[i].getSize());
            try {
                file[i].transferTo(new File("C:\\Users\\gkgkg\\Desktop\\server\\" + i + fileName));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.ok("File uploaded");
    }
}
