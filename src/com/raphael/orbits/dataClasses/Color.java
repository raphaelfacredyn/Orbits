package com.raphael.orbits.dataClasses;

public class Color {
    public static final Color TRANSPARENT = new Color(0, 0);
    public static final Color WHITE = new Color(255);
    public static Color[] themeColors = new Color[]{
            new Color(238, 238, 255), // White
            new Color(217, 217, 217), // Light Gray
            new Color(102, 58, 182), // Purple
            new Color(33, 149, 242), // Blue
            new Color(95, 124, 138) // Dark Gray
    };
    public static Color[] playerColorPresets = new Color[]{
            new Color(155, 39, 175),
            new Color(57, 92, 169),
            new Color(254, 86, 34),
            new Color(75, 174, 79),
            new Color(254, 192, 7),
            new Color(120, 84, 71),
            new Color(138, 194, 73),
            new Color(0, 149, 135),
            new Color(254, 234, 59),
            new Color(232, 30, 98),
            themeColors[3]
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
