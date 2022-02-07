package app.controllers;

import rayTracer.enums.FilterTypeEnum;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.awt.*;

public class MainViewController {
    private final int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final SimpleIntegerProperty width = new SimpleIntegerProperty();
    private final SimpleIntegerProperty height = new SimpleIntegerProperty();
    private final SimpleIntegerProperty antiAliasing = new SimpleIntegerProperty();
    private final SimpleIntegerProperty reflectiveIndex = new SimpleIntegerProperty();
    private final ObjectProperty<FilterTypeEnum> finalFilter = new SimpleObjectProperty<>();

    public MainViewController() {
        width.setValue(600);
        height.setValue(600);
        antiAliasing.setValue(1);
        reflectiveIndex.setValue(3);
        finalFilter.setValue(FilterTypeEnum.NONE);
    }

    public SimpleIntegerProperty widthProperty() {
        return width;
    }

    public SimpleIntegerProperty heightProperty() {
        return height;
    }

    public SimpleIntegerProperty antiAliasingProperty() {
        return antiAliasing;
    }

    public SimpleIntegerProperty reflectiveIndexProperty() {
        return reflectiveIndex;
    }

    public ObjectProperty<FilterTypeEnum> finalFilterProperty() {
        return finalFilter;
    }

    public void process() {
        System.out.println(width.getValue());
        System.out.println(height.getValue());
        System.out.println(antiAliasing.getValue());
        System.out.println(reflectiveIndex.getValue());
        System.out.println(finalFilter.getValue());
        System.out.println(screenHeight);
        System.out.println(screenWidth);
    }
}
