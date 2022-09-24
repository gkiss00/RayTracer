package rayTracer.config;

import rayTracer.objects.Obj;
import rayTracer.objects.blackObjects.BlackObject;
import rayTracer.enums.FilterTypeEnum;
import rayTracer.lights.Light;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.visual.Camera;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public final double EPSILON = 0.00001;
    public int REFLECTION_MAX = 5;
    public FilterTypeEnum filter = FilterTypeEnum.NONE;
    public final Color ambientLight = new Color(1, 1, 1, 1);
    public int height = 900;
    public int width = 900;
    public int ANTI_ALIASING = 5;
    public double MAX_DIST = 60;
    public double SHADOW_DEEPNESS = 0.3;
    public boolean MIST = true;
    public double MAX_MIST_DIST = 3010;
    public Camera cam;
    public List<Obj> objects = new ArrayList<>();
    public List<Obj> backObjects = new ArrayList<>();
    public List<Light> lights = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for(Obj obj: objects) {
            res.append(obj.toString());
            res.append("\n");
        }
        return res.toString();
    }
}
