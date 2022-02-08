package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import server.model.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@SpringBootApplication()
@RestController()
public class RayTracerServer {
    private Config config = new Config();
    public static void main(String[]args) {
        SpringApplication.run(RayTracerServer.class, args);
    }

    @CrossOrigin
    @GetMapping(
            value = "/image",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    @ResponseBody
    public byte[] getImage() {
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
    @GetMapping(
            value = "/images"
    )
    @ResponseBody
    public byte[][] getImages() {
        byte[][] res = new byte[3][];
        try {
            for (int i = 0; i < 3; ++i) {
                BufferedImage bImage = ImageIO.read(new File("Image.png"));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", bos );
                byte [] data = bos.toByteArray();
                res[i] = data;
            }
            return res;
        } catch(Exception e) {

        }
        return res;
    }
}
