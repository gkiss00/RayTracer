package rayTracer.utils;

import java.util.Random;

public class Color {
    private double red;
    private double green;
    private double blue;
    private double alpha;

    public Color() {
        Random rand = new Random();
        this.red = rand.nextFloat();
        this.green = rand.nextFloat();
        this.blue = rand.nextFloat();
        this.alpha = 1;
    }

    public Color(String rgb) { // #ff0B45
        java.awt.Color tmp = java.awt.Color.decode(rgb);
        this.red = (double)tmp.getRed() / 255;
        this.green = (double)tmp.getGreen() / 255;
        this.blue = (double)tmp.getBlue() / 255;
        this.alpha = 1;
    }


    public Color(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1;
    }

    public Color(double red, double green, double blue, double alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(Color color) {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
        this.alpha = color.alpha;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void add(Color color) {
        this.red += color.red;
        this.green += color.green;
        this.blue += color.blue;
    }

    public void add(double red, double green, double blue) {
        this.red += red;
        this.green += green;
        this.blue += blue;
    }

    public void black() {
        red = 0;
        green = 0;
        blue = 0;
    }

    public void divide(double d) {
        this.red /= d;
        this.green /= d;
        this.blue /= d;
    }

    public void unit() {
        double max = Math.max(red, Math.max(green, blue));
        if(max > 1.0) {
            red /= max;
            green /= max;
            blue /= max;
        }
    }

    public Color reduceOf(double d) {
        return new Color(
                red * (1.0 - d),
                green * (1.0 - d),
                blue * (1.0 - d),
                alpha
        );
    }

    public int toInt() {
        int intRed = Math.min((int) (red * 255.0), 255);
        intRed = Math.max(intRed, 0);
        int intGreen = Math.min((int) (green * 255.0), 255);
        intGreen = Math.max(intGreen, 0);
        int intBlue = Math.min((int) (blue * 255.0), 255);
        intBlue = Math.max(intBlue, 0);
        return (int)(intRed * Math.pow(16, 4) + intGreen * Math.pow(16, 2) + intBlue);
    }

    public static Color alphaBlending(Color color1, Color color2) {
        if(color1.getAlpha() == 0 && color2.getAlpha() == 0)
            return new Color(color1);
        double red, green, blue, alpha;

        alpha = color1.alpha + (color2.alpha * (1.0 - color1.alpha));
        red = ((color1.red * color1.alpha) + (color2.red * color2.alpha * (1 - color1.alpha))) / alpha;
        green = ((color1.green * color1.alpha) + (color2.green * color2.alpha * (1 - color1.alpha))) / alpha;
        blue = ((color1.blue * color1.alpha) + (color2.blue * color2.alpha * (1 - color1.alpha))) / alpha;

        return new Color(red, green, blue, alpha);
    }

    public static Color colorReflection(Color color1, Color color2, double factor) {
        double red, green, blue, alpha;
        red = color1.red + (color2.red - color1.red) * factor;
        green = color1.green + (color2.green - color1.green) * factor;
        blue = color1.blue + (color2.blue - color1.blue) * factor;
        alpha = color1.alpha;
        return new Color(red, green, blue, alpha);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Color == false)
            return false;
        Color c = (Color)o;
        return ((red == c.red) && (green == c.green) && (blue == c.blue));
    }

    @Override
    public String toString() {
        return String.format("Color: { r: %f, g: %f, b: %f, alpha: %f}", red, green, blue, alpha);
    }
}
