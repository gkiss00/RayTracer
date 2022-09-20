package rayTracer.utils;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VideoMaker {
    public static void createMp4File(String imagesPath, String videoFilename) {
        SeekableByteChannel out = null;
        try {
            out = NIOUtils.writableFileChannel(videoFilename);

            // for Android use: AndroidSequenceEncoder
            AWTSequenceEncoder encoder = new AWTSequenceEncoder(out, Rational.R(250, 0)); // fps den

            Path directoryPath = Paths.get(new File(imagesPath).toURI());

            if (Files.isDirectory(directoryPath)) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*." + "png");

                List<File> filesList = new ArrayList<>();
                for (Path path : stream) {
                    filesList.add(path.toFile());
                }
                File[] files = new File[filesList.size()];
                filesList.toArray(files);

                sortImages(files);

                for (File img : files) {
                    System.err.println("Encoding image " + img.getName());
                    // Generate the image, for Android use Bitmap
                    BufferedImage image = ImageIO.read(img);
                    // Encode the image
                    encoder.encodeImage(image);
                }
            }
            // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            encoder.finish();
        } catch(Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
        }finally {
            NIOUtils.closeQuietly(out);
        }
    }

    private static void sortImages(File[] images) {
        int size = images.length;
        for (int i = 0; i < size - 1; ++i) {
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; ++j) {
                if (
                        comparePaths(images[j].getAbsolutePath(), images[j + 1].getAbsolutePath()) > 0
                ) {
                    File tmp = images[j];
                    images[j] = images[j + 1];
                    images[j + 1] = tmp;
                    swapped = true;
                }
            }
            if(!swapped)
                break;
        }
    }

    private static int comparePaths(String path1, String path2) {
        if (path1.length() < path2.length())
            return -1;
        if(path1.length() > path2.length())
            return 1;
        return path1.compareTo(path2);
    }
}
