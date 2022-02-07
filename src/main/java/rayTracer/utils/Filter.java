package rayTracer.utils;

import rayTracer.enums.FilterTypeEnum;

public class Filter {
    public static void applyFilter(Color color, FilterTypeEnum filter) {
        switch (filter) {
            case NONE -> {
                return;
            }
            case SEPIA -> {
                applySepia(color);
            }
            case AVERAGE_GREYSCALE -> {
                applyAverageGreyScale(color);
            }
            case WEIGHTED_GREYSCALE -> {
                applyWeightedGreyscale(color);
            }
            case INVERSE -> {
                applyInverse(color);
            }
        }
    }

    private static void applySepia(Color color) {
        double red = 0.393 * color.getRed() + 0.769 * color.getGreen() + 0.189 * color.getBlue();
        double green = 0.349 * color.getRed() + 0.686 * color.getGreen() + 0.168 * color.getBlue();
        double blue = 0.272 * color.getRed() + 0.534 * color.getGreen() + 0.131 * color.getBlue();
        color.setRed(red);
        color.setGreen(green);
        color.setBlue(blue);
    }

    private static void applyAverageGreyScale(Color color) {
        double average = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
        color.setRed(average);
        color.setGreen(average);
        color.setBlue(average);
    }

    private static void applyWeightedGreyscale(Color color) {
        double average = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
        color.setRed(average);
        color.setGreen(average);
        color.setBlue(average);
    }

    private static void applyInverse(Color color) {
        color.setRed(1.0 - color.getRed());
        color.setGreen(1.0 - color.getGreen());
        color.setBlue(1.0 - color.getBlue());
    }
}
