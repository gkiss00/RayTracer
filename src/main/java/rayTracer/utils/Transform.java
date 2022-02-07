package rayTracer.utils;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Matrix;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;

public class Transform {
    private Matrix toLocalMatrix;
    private Matrix toRealMatrix;
    private Point3D realOrigin;
    private Point3D localOrigin;

    public Transform() throws Exception{
        this.toLocalMatrix = new Matrix(4, 4);
        this.toRealMatrix = new Matrix(4, 4);
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
    ) throws Exception{
        Matrix translationMatrix = new Matrix(4, 4);
        translationMatrix.unit();
        translationMatrix.set(3, 0, translationX);
        translationMatrix.set(3, 1, translationY);
        translationMatrix.set(3, 2, translationZ);

        Matrix scalingMatrix = new Matrix(4, 4);
        scalingMatrix.unit();
        scalingMatrix.set(0, 0, scalingX);
        scalingMatrix.set(1, 1, scalingY);
        scalingMatrix.set(2, 2, scalingZ);

        Matrix rotateXMatrix = new Matrix(4, 4);
        rotateXMatrix.unit();
        rotateXMatrix.set(1, 1, Math.cos(Math.toRadians(alpha)));
        rotateXMatrix.set(2, 1, -Math.sin(Math.toRadians(alpha)));
        rotateXMatrix.set(1, 2, Math.sin(Math.toRadians(alpha)));
        rotateXMatrix.set(2, 2, Math.cos(Math.toRadians(alpha)));

        Matrix rotateYMatrix = new Matrix(4, 4);
        rotateYMatrix.unit();
        rotateYMatrix.set(0, 0, Math.cos(Math.toRadians(beta)));
        rotateYMatrix.set(2, 0, Math.sin(Math.toRadians(beta)));
        rotateYMatrix.set(0, 2, -Math.sin(Math.toRadians(beta)));
        rotateYMatrix.set(2, 2, Math.cos(Math.toRadians(beta)));

        Matrix rotateZMatrix = new Matrix(4, 4);
        rotateZMatrix.unit();
        rotateZMatrix.set(0, 0, Math.cos(Math.toRadians(gama)));
        rotateZMatrix.set(1, 0, -Math.sin(Math.toRadians(gama)));
        rotateZMatrix.set(0, 1, Math.sin(Math.toRadians(gama)));
        rotateZMatrix.set(1, 1, Math.cos(Math.toRadians(gama)));

        toRealMatrix = Matrix.multMany(translationMatrix, scalingMatrix, rotateZMatrix, rotateYMatrix, rotateXMatrix);
        toLocalMatrix = new Matrix(toRealMatrix);
        toLocalMatrix.inverse();

        this.realOrigin = apply(new Point3D(0, 0, 0), MatrixTransformEnum.TO_REAL);
        this.localOrigin = apply(new Point3D(0, 0, 0), MatrixTransformEnum.TO_LOCAL);
    }

    public Point3D apply(Point3D point, MatrixTransformEnum type) throws Exception{
        Matrix pointMatrix = new Matrix(1, 4);
        pointMatrix.set(0, 0, point.getX());
        pointMatrix.set(0, 1, point.getY());
        pointMatrix.set(0, 2, point.getZ());
        pointMatrix.set(0, 3, 1);

        Matrix pointResultMatrix;
        if(type == MatrixTransformEnum.TO_LOCAL) {
            pointResultMatrix = Matrix.mult(toLocalMatrix, pointMatrix);
//            pointResultMatrix = Matrix.mult(toLocalMatrix, point.getX(), point.getY(), point.getZ(), 1);
        } else {
            pointResultMatrix = Matrix.mult(toRealMatrix, pointMatrix);
//            pointResultMatrix = Matrix.mult(toRealMatrix, point.getX(), point.getY(), point.getZ(), 1);
        }

        return new Point3D(
                pointResultMatrix.get(0, 0),
                pointResultMatrix.get(0, 1),
                pointResultMatrix.get(0, 2)
        );
    }

    public Vector3D apply(Vector3D vector, MatrixTransformEnum type) throws Exception{
        Matrix vectorMatrix = new Matrix(1, 4);
        vectorMatrix.set(0, 0, vector.getX());
        vectorMatrix.set(0, 1, vector.getY());
        vectorMatrix.set(0, 2, vector.getZ());
        vectorMatrix.set(0, 3, 1);

        Matrix vectorResultMatrix;
        if(type == MatrixTransformEnum.TO_LOCAL) {
            vectorResultMatrix = Matrix.mult(toLocalMatrix, vectorMatrix);
//            vectorResultMatrix = Matrix.mult(toLocalMatrix, vector.getX(), vector.getY(), vector.getZ(), 1);
            return new Vector3D(
                    vectorResultMatrix.get(0, 0) - localOrigin.getX(),
                    vectorResultMatrix.get(0, 1) - localOrigin.getY(),
                    vectorResultMatrix.get(0, 2) - localOrigin.getZ()
            );
        } else {
            vectorResultMatrix = Matrix.mult(toRealMatrix, vectorMatrix);
//            vectorResultMatrix = Matrix.mult(toRealMatrix, vector.getX(), vector.getY(), vector.getZ(), 1);
            return new Vector3D(
                    vectorResultMatrix.get(0, 0) - realOrigin.getX(),
                    vectorResultMatrix.get(0, 1) - realOrigin.getY(),
                    vectorResultMatrix.get(0, 2) - realOrigin.getZ()
            );
        }
    }

    public Line3D apply(Line3D line, MatrixTransformEnum type) throws Exception{
        return new Line3D(apply(line.getPoint(), type), apply((line.getVector()), type));
    }
}
