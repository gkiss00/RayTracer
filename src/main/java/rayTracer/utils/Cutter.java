package rayTracer.utils;

import rayTracer.enums.CutTypeEnum;
import rayTracer.math.Point3D;

import java.util.List;

public class Cutter {
    public static boolean cut(Point3D point, List<CutTypeEnum> cuts) {
        for (int i = 0; i < cuts.size(); ++i) {
            switch (cuts.get(i)) {
                case TOP:
                    if(cutTop(point))
                        return true;
                    break;
                case BOTTOM:
                    if(cutBottom(point))
                        return true;
                    break;
                case LEFT:
                    if(cutLeft(point))
                        return true;
                    break;
                case RIGHT:
                    if(cutRight(point))
                        return true;
                    break;
                case FRONT:
                    if(cutFront(point))
                        return true;
                    break;
                case BACK:
                    if(cutBack(point))
                        return true;
                    break;
            }
        }
        return false;
    }

    private static boolean cutTop(Point3D point) {
        return point.getZ() >= 0;
    }

    private static boolean cutBottom(Point3D point) {
        return point.getZ() <= 0;
    }

    private static boolean cutLeft(Point3D point) {
        return point.getY() <= 0;
    }

    private static boolean cutRight(Point3D point) {
        return point.getY() >= 0;
    }

    private static boolean cutFront(Point3D point) {
        return point.getX() <= 0;
    }

    private static boolean cutBack(Point3D point) {
        return point.getX() >= 0;
    }
}
