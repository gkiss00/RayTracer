package rayTracer.lights;

import lombok.Data;
import rayTracer.math.Point3D;
import rayTracer.utils.Color;

@Data
public class Light {
    private Point3D point;
    private Color color;

    public Light(Point3D point) {
        this.point = new Point3D(point);
        this.color = new Color(1, 1, 1);
    }

    public Light(Point3D point, Color color) {
        this.point = new Point3D(point);
        this.color = new Color(color);
    }
}
