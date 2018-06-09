package com.raphael.orbits.dataClasses;

public class Color {
    public static final Color TRANSPARENT = new Color(0, 0);
    public static final Color WHITE = new Color(255);
    public static final Color BLACK = new Color(0);
    public static Color[] themeColors = new Color[]{
            new Color(238, 238, 255), // White
            new Color(217, 217, 217), // Light Gray
            new Color(102, 58, 182), // Purple
            new Color(33, 149, 242), // Blue
            new Color(95, 124, 138) // Dark Gray
    };
    public static Color[] playerColorPresets = new Color[]{
            new Color(13, 81, 5),
            new Color(250, 241, 53),
            new Color(152, 228, 79),
            new Color(222, 112, 37),
            new Color(98, 154, 254),
            new Color(230, 3, 3),
            new Color(214, 122, 185),
//            new Color(101, 56, 48), // Too similar to the last color
            new Color(33, 59, 216),
            new Color(5, 0, 62),
            new Color(16, 235, 154),
            new Color(200, 16, 235),
            new Color(119, 0, 0),
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

    public Color clone() {
        return new Color(r, g, b, a);
    }
}
