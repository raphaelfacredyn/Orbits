package com.raphael.orbits;

import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.Renderable;
import com.raphael.orbits.gameObjects.Walls;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;
import processing.core.PGraphics;

public class Utils {
    public static boolean inWalls(AABB ball, Walls walls) {
        boolean in = false;
        for (BodyFixture wall : walls.getFixtures())
            in |= ball.overlaps(wall.getShape().createAABB());
        return in;
    }

    public static double getAngleBetween(Vector2 a, Vector2 b) {
        double xDiff = b.x - a.x;
        double yDiff = -(b.y - a.y); // Y is negatived because it in computers it goes the opposite way of on coordinate grids
        return Math.atan2(yDiff, xDiff);
    }

    public static double worldToPixel(double world) {
        return world * Renderable.SCALE;
    }

    public static double pixelToWorld(double pixel) {
        return pixel / Renderable.SCALE;
    }

    public static Vector2 worldToPixel(Vector2 v) {
        return new Vector2(worldToPixel(v.x), worldToPixel(v.y));
    }

    public static Vector2 pixelToWorld(Vector2 v) {
        return new Vector2(pixelToWorld(v.x), pixelToWorld(v.y));
    }

    public static void background(PGraphics canvas, Color color) {
        canvas.background(color.r, color.g, color.b, color.a);
    }

    public static void fill(PGraphics canvas, Color color) {
        canvas.fill(color.r, color.g, color.b, color.a);
    }

    public static void stroke(PGraphics canvas, Color color) {
        canvas.stroke(color.r, color.g, color.b, color.a);
    }

    public static Vector2 getIntermediate(Vector2 start, Vector2 end, double multiplier) {
        return new Vector2(
                getIntermediate(start.x, end.x, multiplier),
                getIntermediate(start.y, end.y, multiplier)
        );
    }

    public static Color getIntermediate(Color start, Color end, double multiplier) {
        return new Color(
                (int) Math.sqrt(getIntermediate(Math.pow(start.r, 2), Math.pow(end.r, 2), multiplier)),
                (int) Math.sqrt(getIntermediate(Math.pow(start.g, 2), Math.pow(end.g, 2), multiplier)),
                (int) Math.sqrt(getIntermediate(Math.pow(start.b, 2), Math.pow(end.b, 2), multiplier)),
                (int) Math.sqrt(getIntermediate(Math.pow(start.a, 2), Math.pow(end.a, 2), multiplier))
        );
    }

    public static double getIntermediate(double start, double end, double multiplier) {
        return start + (end - start) * multiplier;
    }
}
