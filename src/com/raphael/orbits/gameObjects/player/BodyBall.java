package com.raphael.orbits.gameObjects.player;

import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.dataClasses.PlayerGameEvent;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;

public class BodyBall extends PlayerBall {
    public ArrayList<PlayerGameEvent> completedEvents = new ArrayList<>();
    public boolean shot = false;

    public BodyBall(Vector2 pos) {
        super(pos);
    }

    public BodyBall(double x, double y) {
        super(x, y);
    }

    public BodyBall(Vector2 pos, double r) {
        super(pos, r);
    }

    public BodyBall(double x, double y, double r) {
        super(x, y, r);
    }

    @Override
    public Color getColor() {
        return color;
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
