package com.raphael.orbits.gameObjects.player;

import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.Ball;
import com.raphael.orbits.gameObjects.Orbit;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.geometry.Vector2;

import static com.raphael.orbits.Utils.getAngleBetween;
import static com.raphael.orbits.gameObjects.player.Player.*;

public abstract class PlayerBall extends Ball {
    public static final double PLAYER_BALL_SIZE = Ball.DEFAULT_BALL_SIZE;

    public Color color;

    public PlayerBall(Vector2 pos) {
        super(pos);
    }

    public PlayerBall(double x, double y) {
        super(x, y);
    }

    public PlayerBall(Vector2 pos, double r) {
        super(pos, r);
    }

    public PlayerBall(double x, double y, double r) {
        super(x, y, r);
    }

    public Player getPlayer() {
        return ((Player) getUserData());
    }

    private World getWorld() {
        return getPlayer().world;
    }

    public boolean isOrbiting() {
        return !getJoints().isEmpty();
    }

    public void toggleOrbiting(Orbit orbit) {
        if (isOrbiting()) {
            stopOrbiting();
        } else {
            startOrbiting(orbit);
        }
    }

    public void startOrbiting(Orbit orbit) {
        if (!isOrbiting()) {
            DistanceJoint orbitJoint = new DistanceJoint(this, orbit, getWorldCenter(), orbit.getWorldCenter());
            getWorld().addJoint(orbitJoint);
        }
    }

    public void stopOrbiting() {
        if (isOrbiting())
            getWorld().removeJoint(getJoints().remove(0));
    }

    public Orbit getOrbit() {
        return (Orbit) getJoints().get(0).getBody2();
    }

    public double distanceFromVector(Vector2 v) {
        if (isOrbiting()) {
            Orbit orbit = getOrbit();
            double radius = getWorldCenter().distance(orbit.getWorldCenter());
            double angleDiff = Math.abs(getAngleBetween(orbit.getWorldCenter(), getWorldCenter()) - getAngleBetween(orbit.getWorldCenter(), v));
            return radius * angleDiff; // radius * 2 * PI * angleDiff / (2 * PI) then cancel 2 * PI
        }
        return getWorldCenter().distance(v);
    }

    public void rectifyVelocity() {
        if (isOrbiting())
            setLinearVelocity(getLinearVelocity().getNormalized().multiply(ORBIT_SPEED));
        else
            setLinearVelocity(getLinearVelocity().getNormalized().multiply(getPlayer().superState ? SUPER_SPEED : SPEED));
    }

    public void prepareForRemoval() {
        stopOrbiting();
    }
}
