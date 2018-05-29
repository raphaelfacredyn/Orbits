package com.raphael.orbits.gameObjects.player;

import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.Ball;
import org.dyn4j.geometry.Vector2;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;

public class HeadBall extends Ball {
    public Color color;

    public HeadBall(Vector2 pos) {
        super(pos);
    }

    public HeadBall(double x, double y) {
        super(x, y);
    }

    public HeadBall(Vector2 pos, double r) {
        super(pos, r);
    }

    public HeadBall(double x, double y, double r) {
        super(x, y, r);
    }

    @Override
    public Color getColor() {
        return TRANSPARENT;
    }

    @Override
    public Color getOutlineColor() {
        return color;
    }

    @Override
    public float getOutlineWeight() {
        return 4;
    }
}
