package com.raphael.orbits.dataClasses;

public class Color {
    public static final Color TRANSPARENT = new Color(0, 0);
    public static final Color WHITE = new Color(255);
    public static Color[] playerColorPresets = new Color[]{
            new Color(226, 18, 28),
            new Color(254, 95, 0),
            new Color(232, 144, 5),
            new Color(37, 194, 39),
            new Color(35, 100, 170),
            new Color(127, 124, 175), // Purple
//            new Color(12, 98, 145), // Blue
//            new Color(46, 64, 87) // Dark Gray
    };
    public static Color[] themeColors = new Color[]{
            new Color(238, 238, 255), // White
            new Color(214, 214, 214), // Light Gray
            new Color(127, 124, 175), // Purple
            new Color(12, 98, 145), // Blue
            new Color(46, 64, 87) // Dark Gray
    };
    public int r;
    public int g;
    public int b;
    public int a;

    public Color(int c) {
        this(c, c, c);
    }

    public Color(int c, int a) {
        this(c, c, c, a);
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static Color randomPlayerColor() {
        return playerColorPresets[(int) (Math.random() * playerColorPresets.length)];
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return r == color.r &&
                g == color.g &&
                b == color.b &&
                a == color.a;
    }
}
