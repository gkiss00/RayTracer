package server.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@RestController()
public class ImageController {
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
