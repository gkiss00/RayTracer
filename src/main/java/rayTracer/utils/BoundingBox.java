package rayTracer.utils;

import lombok.Data;
import rayTracer.math.Line3D;

@Data
public class BoundingBox {
    private final static double EPSILON = 0.0001;
    private final double front;
    private final double back;
    private final double right;
    private final double left;
    private final double top;
    private final double bottom;

    public BoundingBox(
            double front,
            double back,
            double right,
            double left,
            double top,
            double bottom
    ) {
        this.front = front;
        this.back = back;
        this.right = right;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
    }

    public boolean hit(Line3D localRay) {
        double x0, y0, z0, a, b, c;
        x0 = localRay.getPX();
        y0 = localRay.getPY();
        z0 = localRay.getPZ();
        a = localRay.getVX();
        b = localRay.getVY();
        c = localRay.getVZ();

        double t;
        double x, y, z;
        if (c > EPSILON || c < -EPSILON) {
            // UP
            t = (top - z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = top;

                if (y >= left && y <= right && x >= front && x <= back) {
                    return true;
                }
            }
            // DOWN
            t = (bottom -z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = bottom;

                if (y >= left && y <= right && x >= front && x <= back) {
                    return true;
                }
            }
        }
        if (b > EPSILON || b < -EPSILON) {
            // LEFT
            t = (left - y0) / b;
            if(t > 0) {
                x = x0 + a * t;
                y = left;
                z = z0 + c * t;

                if (z >= bottom && z <= top && x >= front && x <= back) {
                    return true;
                }
            }
            // RIGHT
            t = (right -y0) / b;
            if(t > 0) {
                x = x0 + a * t;
                y = right;
                z = z0 + c * t;

                if (z >= bottom && z <= top && x >= front && x <= back) {
                    return true;
                }
            }
        }
        if (a > EPSILON || a < -EPSILON) {
            // BACK
            t = (back - x0) / a;
            if(t > 0) {
                x = back;
                y = y0 + b * t;
                z = z0 + c * t;

                if (z >= bottom && z <= top && y >= left && y <= right) {
                    return true;
                }
            }
            // FRONT
            t = (front - x0) / a;
            if(t > 0) {
                x = front;
                y = y0 + b * t;
                z = z0 + c * t;

                if (z >= bottom && z <= top && y >= left && y <= right) {
                    return true;
                }
            }
        }
        return false;
    }
}
