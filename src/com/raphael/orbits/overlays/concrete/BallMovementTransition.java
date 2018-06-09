package com.raphael.orbits.overlays.concrete;


import com.raphael.orbits.gameObjects.player.BodyBall;
import com.raphael.orbits.gameObjects.player.PlayerBall;
import com.raphael.orbits.overlays.Overlay;
import org.dyn4j.geometry.Vector2;
import processing.core.PGraphics;

import static com.raphael.orbits.Utils.*;

public class BallMovementTransition extends Overlay {
    public static final long DURATION = 750;
    Vector2 originPos;
    BodyBall end;
    double multiplier;
    Vector2 endPos;
    Vector2 intermediatePos;
    double radius = worldToPixel(PlayerBall.DEFAULT_BALL_RADIUS);

    public BallMovementTransition(PGraphics canvas, Vector2 origin, BodyBall end) {
        super(canvas);
        originPos = origin;
        this.end = end;
        end.hidden = true;
    }

    private boolean validEndBall() {
        return !end.removed && !end.shot;
    }

    @Override
    public void tick(int currTime) {
        super.tick(currTime);
        if (validEndBall()) {
            multiplier = (double) elapsedTime / DURATION;
            canvas.noStroke();
            fill(canvas, end.color);
            endPos = end.getWorldCenter();
            intermediatePos = worldToPixel(getIntermediate(originPos, endPos, multiplier));
            canvas.ellipse((float) intermediatePos.x, (float) intermediatePos.y, (float) (2 * radius), (float) (2 * radius));
        }
    }

    @Override
    public boolean isDone() {
        if (elapsedTime > DURATION || !validEndBall()) {
            end.hidden = false;
            return true;
        }
        return false;
    }
}
