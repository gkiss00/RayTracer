package rayTracer.utils;

import rayTracer.math.Point3D;
import rayTracer.math.Triangle;
import rayTracer.objects.baseObjects.composedObjects.triangleMade.Polygon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static rayTracer.enums.PolygonTypeEnum.BLENDER_OBJECT;

public class ObjectFileReader {
    private static final String COMMENT_KEY = "#";
    private static final String VERTEX_KEY = "v";
    private static final String TEXTURE_KEY = "vt";
    private static final String NORMAL_KEY = "vn";
    private static final String FACE_KEY = "f";

    public static Polygon read(String path) {
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            Polygon polygon = readFile(scanner);
            scanner.close();
            return polygon;
        } catch (FileNotFoundException exception) {
            System.out.println(exception);
            System.exit(1);
            return null;
        }
    }

    private static Polygon readFile(Scanner scanner) {
        List<Point3D> vertices = new ArrayList<>();
        List<Triangle> faces = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] elements = line.split("\\s+");
            manageElements(elements, vertices, faces);
        }
        return new Polygon(BLENDER_OBJECT, null, faces);
    }

    private static void manageElements(String[] elements, List<Point3D> vertices, List<Triangle> faces) {
        if (elements.length == 0) { return; }

        String key = elements[0];

        if (key.equals(COMMENT_KEY)) {
            // this is a comment => do nothing
        } else if (key.equals(VERTEX_KEY)) {
            Point3D vertex = getVertex(elements);
            vertices.add(vertex);
        } else if (key.equals(TEXTURE_KEY)) {
            // do nothing for now
        } else if (key.equals(NORMAL_KEY)) {
            // do nothing for now
        } else if (key.equals(FACE_KEY)) {
            List<Triangle> face = getFace(elements, vertices);
            faces.addAll(face);
        } else {
            // do nothing
        }
    }

    // in obj file coming from blender, the vertices are in m. We multiply by 100 to get it by pixel-cm
    private static Point3D getVertex(String[] elements) {
        double x = Double.parseDouble(elements[1]) * 100;
        double y = Double.parseDouble(elements[2]) * 100;
        double z = Double.parseDouble(elements[3]) * 100;
        return new Point3D(x, z, y);

    }

    // f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
    // a face is defined by a minimum of 3 vertices
    private static List<Triangle> getFace(String[] elements, List<Point3D> vertices) {
        int nbVertices = elements.length - 1;
        // store the vertices indexes
        Integer[] vertexIndexes = new Integer[elements.length - 1];
        for (int i = 1; i < elements.length; ++i) {
            String[] keys = elements[i].split("/+");
            vertexIndexes[i - 1] = Integer.parseInt(keys[0]) - 1;
        }
        // create the triangle from the vertices N triangles where N = nb vertices - 2
        List<Triangle> triangles = new ArrayList<>();
        for(int i = 0; i < nbVertices - 2; ++i) {
            triangles.add(new Triangle(vertices.get(vertexIndexes[0]), vertices.get(vertexIndexes[i + 1]), vertices.get(vertexIndexes[i + 2])));
        }
        return triangles;
    }
}
