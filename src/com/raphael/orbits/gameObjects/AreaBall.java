package com.raphael.orbits.gameObjects;


import com.raphael.orbits.dataClasses.Color;
import org.dyn4j.geometry.Vector2;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;
import static com.raphael.orbits.dataClasses.Color.themeColors;

public class AreaBall extends Ball {
    public static final double DEFAULT_AREA_BALL_SIZE = 1.5 * DEFAULT_BALL_SIZE;
    public static final double PICKUP_RADIUS = SCALE_CONVERSION_FACTOR * 3;

    public AreaBall(Vector2 pos) {
        super(pos, DEFAULT_AREA_BALL_SIZE);
    }

    public AreaBall(double x, double y) {
        super(x, y, DEFAULT_AREA_BALL_SIZE);
    }

    public AreaBall(Vector2 pos, double r) {
        super(pos, r);
    }

    public AreaBall(double x, double y, double r) {
        super(x, y, r);
    }

    @Override
    public Color getColor() {
        return TRANSPARENT;
    }

    @Override
    public Color getOutlineColor() {
        return themeColors[4];
    }

    @Override
    public float getOutlineWeight() {
        return 6;
    }
}
