package com.raphael.orbits.overlays.concrete;

import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.AreaBall;
import com.raphael.orbits.overlays.Overlay;
import org.dyn4j.geometry.Vector2;
import processing.core.PGraphics;

import static com.raphael.orbits.Utils.fill;
import static com.raphael.orbits.Utils.worldToPixel;

public class AreaBallCollectEffect extends Overlay {
    public static final long DURATION = 750;
    Vector2 origin;
    int radius = 0;
    double multiplier;
    Color color;

    public AreaBallCollectEffect(PGraphics canvas, Vector2 center) {
        super(canvas);
        origin = worldToPixel(center);
    }

    @Override
    public void tick(int currTime) {
        super.tick(currTime);
        multiplier = Math.sin((double) elapsedTime / DURATION * Math.PI);
        radius = (int) (worldToPixel(AreaBall.PICKUP_RADIUS) * multiplier);
        color = AreaBall.COLOR.clone();
        color.a = (int) (100 * multiplier); // Don't go full opacity
        canvas.noStroke();
        fill(canvas, color);
        canvas.ellipse((float) origin.x, (float) origin.y, 2 * radius, 2 * radius);
    }

    @Override
    public boolean isDone() {
        return elapsedTime > DURATION;
    }
}
