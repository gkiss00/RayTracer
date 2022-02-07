package server.model;

import rayTracer.enums.FilterTypeEnum;

public class Config {
    private int height;
    private int width;
    private int antiAliasing;
    private int maxReflexion;
    private FilterTypeEnum finalFilter;

    public Config() {
        this.height = 600;
        this.width = 600;
        this.antiAliasing = 1;
        this.maxReflexion = 3;
        this.finalFilter = FilterTypeEnum.NONE;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getAntiAliasing() {
        return antiAliasing;
    }

    public int getMaxReflexion() {
        return maxReflexion;
    }

    public FilterTypeEnum getFinalFilter() {
        return finalFilter;
    }

    public void setAntiAliasing(int antiAliasing) {
        this.antiAliasing = antiAliasing;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setMaxReflexion(int maxReflexion) {
        this.maxReflexion = maxReflexion;
    }

    public void setFinalFilter(FilterTypeEnum finalFilter) {
        this.finalFilter = finalFilter;
    }
}
