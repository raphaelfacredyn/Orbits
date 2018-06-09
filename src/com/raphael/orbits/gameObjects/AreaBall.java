package com.raphael.orbits.gameObjects;


import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.player.BodyBall;
import com.raphael.orbits.gameObjects.player.HeadBall;
import com.raphael.orbits.gameObjects.player.Player;
import com.raphael.orbits.overlays.concrete.AreaBallCollectEffect;
import com.raphael.orbits.screens.Game;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import static com.raphael.orbits.dataClasses.Color.TRANSPARENT;
import static com.raphael.orbits.dataClasses.Color.themeColors;

public class AreaBall extends Ball {
    public static final Color COLOR = themeColors[4];
    ;
    public static final double DEFAULT_AREA_BALL_RADIUS = 1.5 * DEFAULT_BALL_RADIUS;
    public static final double PICKUP_RADIUS = SCALE_CONVERSION_FACTOR * 3;
    public boolean triggered = false;

    public AreaBall(Vector2 pos) {
        super(pos, DEFAULT_AREA_BALL_RADIUS);
    }

    public AreaBall(double x, double y) {
        super(x, y, DEFAULT_AREA_BALL_RADIUS);
    }

    public AreaBall(Vector2 pos, double r) {
        super(pos, r);
    }

    public AreaBall(double x, double y, double r) {
        super(x, y, r);
    }

    @Override
    public Color getColor() {
        return TRANSPARENT;
    }

    @Override
    public Color getOutlineColor() {
        return COLOR;
    }

    @Override
    public float getOutlineWeight() {
        return 6;
    }

    public void trigger(Game game, Player player) {
        triggered = true;

        game.toRemove.add(this);
        game.overlayManager.overlays.add(new AreaBallCollectEffect(game.canvas, getWorldCenter().copy()));
        for (Body b : player.world.getBodies()) {
            if (b instanceof Ball && !(b instanceof HeadBall)) {
                if (b.getWorldCenter().distance(getWorldCenter()) < AreaBall.PICKUP_RADIUS && !player.balls.contains(b)) {
                    if (b instanceof AreaBall) {
                        if (!((AreaBall) b).triggered)
                            ((AreaBall) b).trigger(game, player);
                    } else {
                        player.addBalls(b.getWorldCenter());
                        if (b instanceof BodyBall) {
                            if (((BodyBall) b).shot)
                                ((BodyBall) b).getPlayer().removeBullet((BodyBall) b);
                            else
                                ((BodyBall) b).getPlayer().removeLastBodyBall();
                        } else {
                            game.toRemove.add(b);
                        }
                    }
                }
            }
        }
    }
}
