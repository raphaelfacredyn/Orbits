package com.raphael.orbits.gameObjects;

import com.raphael.orbits.dataClasses.Color;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;
import static com.raphael.orbits.dataClasses.Color.themeColors;

public class Orbit extends Renderable {
    public Orbit(double x, double y, double r) {
        addFixture(Geometry.createCircle(r)).setSensor(true);   // Orbits don't collide with anything
        translate(x, y);
        setMass(MassType.INFINITE);
    }

    @Override
    Color getColor() {
        return themeColors[1];
    }

    @Override
    Color getOutlineColor() {
        return TRANSPARENT;
    }

    @Override
    float getOutlineWeight() {
        return 0;
    }

    public boolean overlaps(Orbit other) {
        return getFixture(0).getShape().getRadius() + other.getFixture(0).getShape().getRadius() > getWorldCenter().distance(other.getWorldCenter());
    }
}
