package rayTracer.config;

import rayTracer.blackObjects.BlackObject;
import rayTracer.enums.FilterTypeEnum;
import rayTracer.lights.Light;
import rayTracer.objects.BaseObject;
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
    public int ANTI_ALIASING = 3;
    public double MAX_DIST = 60;
    public double SHADOW_DEEPNESS = 0.3;

    public Camera cam;
    public List<BaseObject> objects = new ArrayList<>();
    public List<BlackObject> backObjects = new ArrayList<>();
    public List<Light> lights = new ArrayList<>();
}
