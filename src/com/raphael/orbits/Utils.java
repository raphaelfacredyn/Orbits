package com.raphael.orbits;

import com.raphael.orbits.gameObjects.Renderable;
import com.raphael.orbits.gameObjects.Walls;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;

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

    public static double worldToPixel(double world){
        return world * Renderable.SCALE;
    }

    public static double pixelToWorld(double pixel){
        return pixel / Renderable.SCALE;
    }
}
