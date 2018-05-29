package com.raphael.orbits.gameObjects;


import com.raphael.orbits.dataClasses.Color;
import org.dyn4j.geometry.Vector2;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;
import static com.raphael.orbits.dataClasses.Color.themeColors;

public class FreeBall extends Ball {
    public FreeBall(Vector2 pos) {
        super(pos);
    }

    public FreeBall(double x, double y) {
        super(x, y);
    }

    public FreeBall(Vector2 pos, double r) {
        super(pos, r);
    }

    public FreeBall(double x, double y, double r) {
        super(x, y, r);
    }

    @Override
    public Color getColor() {
        return themeColors[4];
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
