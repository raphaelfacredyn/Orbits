package com.raphael.orbits.gameObjects;

import com.raphael.orbits.dataClasses.Color;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;

public abstract class Ball extends Renderable {
    public static final double DEFAULT_BALL_RADIUS = 0.2 * SCALE_CONVERSION_FACTOR;

    public Ball(Vector2 pos) {
        this(pos.x, pos.y);
    }

    public Ball(double x, double y) {
        this(x, y, DEFAULT_BALL_RADIUS);
    }


    public Ball(Vector2 pos, double r) {
        this(pos.x, pos.y, r);
    }

    public Ball(double x, double y, double r) {
        addFixture(Geometry.createCircle(r), 1, 0, 1);
        setAngularDamping(0);
        setAutoSleepingEnabled(false);
        translate(x, y);
        setMass(MassType.NORMAL);
    }

    @Override
    public Color getColor() {
        return Color.themeColors[3];
    }

    @Override
    public Color getOutlineColor() {
        return TRANSPARENT;
    }

    @Override
    public float getOutlineWeight() {
        return 0;
    }
}
