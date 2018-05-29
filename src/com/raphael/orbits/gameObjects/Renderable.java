package com.raphael.orbits.gameObjects;

import com.raphael.orbits.dataClasses.Color;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;
import processing.core.PGraphics;

import static processing.core.PConstants.CLOSE;

public abstract class Renderable extends Body {
    public static int SCALE = 50;
    public static double SCALE_CONVERSION_FACTOR = 50 / SCALE;

    private static void renderFixture(PGraphics canvas, Circle circle, Color color, Color outlineColor, float outlineWeight) {
        double diameter = circle.getRadius() * 2;
        Vector2 center = circle.getCenter();

        canvas.stroke(outlineColor.r, outlineColor.g, outlineColor.b, outlineColor.a);
        canvas.strokeWeight(outlineWeight);

        canvas.fill(color.r, color.g, color.b, color.a);
        canvas.ellipse((float) center.x * SCALE, (float) center.y * SCALE, (float) diameter * SCALE, (float) diameter * SCALE);
    }

    private static void renderFixture(PGraphics canvas, Polygon polygon, Color color, Color outlineColor, float outlineWeight) {
        Vector2[] vertices = polygon.getVertices();

        canvas.stroke(outlineColor.r, outlineColor.g, outlineColor.b, outlineColor.a);
        canvas.strokeWeight(outlineWeight);

        canvas.fill(color.r, color.g, color.b, color.a);
        canvas.beginShape();
        for (Vector2 vertex : vertices)
            canvas.vertex((float) vertex.x * SCALE, (float) vertex.y * SCALE);
        canvas.endShape(CLOSE);
    }

    abstract Color getColor();

    abstract Color getOutlineColor();

    abstract float getOutlineWeight();

    Body getBody() {
        return this;
    }

    public void render(PGraphics canvas) {
        canvas.pushMatrix();

        canvas.translate((float) getTransform().getTranslationX() * SCALE, (float) getTransform().getTranslationY() * SCALE);
        canvas.rotate((float) getTransform().getRotation());

        for (BodyFixture fixture : getFixtures()) {
            if (fixture.getShape() instanceof Circle) {
                renderFixture(canvas, (Circle) fixture.getShape(), getColor(), getOutlineColor(), getOutlineWeight());
            } else if (fixture.getShape() instanceof Polygon) {
                renderFixture(canvas, (Polygon) fixture.getShape(), getColor(), getOutlineColor(), getOutlineWeight());
            }
        }

        canvas.popMatrix();
    }
}
