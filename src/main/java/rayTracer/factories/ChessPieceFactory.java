package rayTracer.factories;

import rayTracer.enums.ChessPieceEnum;
import rayTracer.enums.CutTypeEnum;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.Hyperboloid;
import rayTracer.objects.baseObjects.QuadraticSurface;
import rayTracer.objects.baseObjects.composedObjects.objectMade.Assembly;
import rayTracer.objects.baseObjects.simpleObjects.Sphere;
import rayTracer.objects.baseObjects.simpleObjects.Torus;
import rayTracer.utils.Color;

public class ChessPieceFactory {
    public static Assembly createPiece(ChessPieceEnum type, double size) {
        switch (type) {
            case PAWN -> {
                return createPawn(size);
            }
            case ROOK -> {

            }
            case KNIGHT -> {

            }
            case BISHOP -> {

            }
            case QUEEN -> {

            }
            case KING -> {

            }
        }
        return null;
    }

    private static Assembly createPawn(double size) {
        Assembly assembly = new Assembly();

        // 24.5

        double headRatio = 5.0 / 24.5;
        double body1Ratio1 = 3.5 / 24.5;
        double body1Ratio2 = 2 / 24.5;
        double body2Ratio1 = 1.0 / 24.5;
        double body2Ratio2 = 0.075 / 24.5;
        double body2Ratio3 = 5 / 24.5;
        double footRatio1 = 5 / 24.5;
        double footRatio2 = 5 / 24.5;

        // HEAD
        Sphere sp1 = new Sphere(5);
        sp1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 3);
        assembly.add(sp1);

        // BODY
        Torus torus1 = new Torus(3.5, 2);
        torus1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        assembly.add(torus1);

        //QuadraticSurface s = new QuadraticSurface(1, 1, -0.075, 0, 0, 0, 0, 0, 0, -5);
        Hyperboloid s = new Hyperboloid(0.25, 0.25, 1, 60);
        s.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -3);
        s.isLimited = true;
        s.upperLimit = 3;
        s.lowerLimit = -13.5;
        assembly.add(s);

        // FOOT
        Torus torus2 = new Torus(3.5, 2);
        torus2.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -15);
        assembly.add(torus2);

        Torus torus3 = new Torus(4, 2.2);
        torus3.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -16.5);
        torus3.addCut(CutTypeEnum.BOTTOM);
        assembly.add(torus3);

        return assembly;
    }
}
