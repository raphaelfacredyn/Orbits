package com.raphael.orbits.gameObjects;

import com.raphael.orbits.dataClasses.Color;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;
import static com.raphael.orbits.dataClasses.Color.themeColors;

public class Walls extends Renderable {
    public Walls(double width, double height) {
        getBody().addFixture(Geometry.createRectangle(width, 0.5 * SCALE_CONVERSION_FACTOR)).getShape().translate(width / 2, 0);
        getBody().addFixture(Geometry.createRectangle(width, 0.5 * SCALE_CONVERSION_FACTOR)).getShape().translate(width / 2, height);
        getBody().addFixture(Geometry.createRectangle(0.5 * SCALE_CONVERSION_FACTOR, height)).getShape().translate(0, height / 2);
        getBody().addFixture(Geometry.createRectangle(0.5 * SCALE_CONVERSION_FACTOR, height)).getShape().translate(width, height / 2);
        getBody().getFixtures().forEach((BodyFixture b) -> {
            b.setRestitution(1);
            b.setFriction(0);
        });
        setAngularDamping(0);
        getBody().setMass(MassType.INFINITE);
    }

    @Override
    Color getColor() {
        return themeColors[2];
    }

    @Override
    Color getOutlineColor() {
        return TRANSPARENT;
    }

    @Override
    float getOutlineWeight() {
        return 0;
    }
}
