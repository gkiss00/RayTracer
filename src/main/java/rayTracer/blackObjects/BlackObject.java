package rayTracer.blackObjects;

import rayTracer.math.Line3D;
import rayTracer.utils.Intersection;
import rayTracer.utils.Transform;

import java.util.List;

public abstract class BlackObject {
    protected final static double EPSILON = 0.0001;
    protected Transform transform;

    public BlackObject() {
        try {
            this.transform = new Transform();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateMatrices(
            double alpha,
            double beta,
            double gama,
            double scalingX,
            double scalingY,
            double scalingZ,
            double translationX,
            double translationY,
            double translationZ
    ) {
        try {
            transform.updateMatrices(alpha, beta, gama, scalingX, scalingY, scalingZ, translationX, translationY, translationZ);
        } catch (Exception e) {}
    }

    abstract public void hit(Line3D ray, List<Intersection> intersections) throws Exception;
}
