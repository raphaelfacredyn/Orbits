package com.raphael.orbits;

import com.raphael.orbits.dataClasses.Player;
import com.raphael.orbits.gameObjects.Ball;
import com.raphael.orbits.gameObjects.FreeBall;
import com.raphael.orbits.gameObjects.Orbit;
import com.raphael.orbits.gameObjects.Walls;
import com.raphael.orbits.gameObjects.player.BodyBall;
import com.raphael.orbits.gameObjects.player.HeadBall;
import com.raphael.orbits.screens.Game;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.contact.ContactListener;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.dynamics.contact.PersistedContactPoint;
import org.dyn4j.dynamics.contact.SolvedContactPoint;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;


public class CollisionFilter implements ContactListener {
    Game game;

    public CollisionFilter(Game game) {
        this.game = game;
    }

    @Override
    public void sensed(ContactPoint contactPoint) {

    }

    @Override
    public boolean begin(ContactPoint contactPoint) {
        return true;
    }

    @Override
    public void end(ContactPoint contactPoint) {

    }

    @Override
    public boolean persist(PersistedContactPoint persistedContactPoint) {
        return true;
    }

    @Override
    public boolean preSolve(ContactPoint contactPoint) {
        // Because balls sometimes bounce off of walls incorrectly so handle it manually
        if (handleWallBallCollision(contactPoint))
            return false;

        // Balls don't collide with Orbits
        if ((contactPoint.getBody1() instanceof Ball && contactPoint.getBody2() instanceof Orbit) || (contactPoint.getBody1() instanceof Orbit && contactPoint.getBody2() instanceof Ball))
            return false;

        // BodyBalls don't collide with each other
        if (contactPoint.getBody1() instanceof BodyBall && contactPoint.getBody2() instanceof BodyBall)
            return false;

        // Add ball to player if HeadBall hits FreeBall
        if (handleHeadBallFreeBallCollision(contactPoint))
            return false;

        // BodyBalls don't collide with FreeBalls
        if ((contactPoint.getBody1() instanceof BodyBall && contactPoint.getBody2() instanceof FreeBall) || (contactPoint.getBody1() instanceof FreeBall && contactPoint.getBody2() instanceof BodyBall))
            return false;

        // Add ball to player if HeadBall hits FreeBall
        if (handleHeadBallBodyBallCollision(contactPoint))
            return false;

        // Make Body Balls collide upon reaching position
        handleHeadBallHeadBallCollision(contactPoint);

        return true;
    }

    @Override
    public void postSolve(SolvedContactPoint solvedContactPoint) {
    }

    private boolean handleWallBallCollision(ContactPoint contactPoint) {
        if ((contactPoint.getBody1() instanceof Ball && contactPoint.getBody2() instanceof Walls) || (contactPoint.getBody1() instanceof Walls && contactPoint.getBody2() instanceof Ball)) {
            Body ball;
            BodyFixture wall;
            if (contactPoint.getBody1() instanceof Ball) {
                ball = contactPoint.getBody1();
                wall = contactPoint.getFixture2();
            } else {
                ball = contactPoint.getBody2();
                wall = contactPoint.getFixture1();
            }
            Vector2[] vertices = ((Polygon) wall.getShape()).getVertices();
            double biggestX = Math.max(Math.abs(vertices[0].x - vertices[1].x), Math.abs(vertices[0].x - vertices[2].x));
            double biggestY = Math.max(Math.abs(vertices[0].y - vertices[1].y), Math.abs(vertices[0].y - vertices[2].y));
            Vector2 velocity = ball.getLinearVelocity();
            if (biggestY > biggestX) {
                velocity.x = -velocity.x;
                ball.setLinearVelocity(velocity);
            } else {
                velocity.y = -velocity.y;
                ball.setLinearVelocity(velocity);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean handleHeadBallFreeBallCollision(ContactPoint contactPoint) {
        if ((contactPoint.getBody1() instanceof HeadBall && contactPoint.getBody2() instanceof FreeBall) || (contactPoint.getBody1() instanceof FreeBall && contactPoint.getBody2() instanceof HeadBall)) {
            HeadBall headBall;
            FreeBall freeBall;
            if (contactPoint.getBody1() instanceof HeadBall) {
                headBall = (HeadBall) contactPoint.getBody1();
                freeBall = (FreeBall) contactPoint.getBody2();
            } else {
                headBall = (HeadBall) contactPoint.getBody2();
                freeBall = (FreeBall) contactPoint.getBody1();
            }

            Player player = (Player) headBall.getUserData();
            game.toRemove.add(freeBall);
            player.addBalls(1);

            return true;
        } else {
            return false;
        }
    }

    private boolean handleHeadBallBodyBallCollision(ContactPoint contactPoint) {
        if ((contactPoint.getBody1() instanceof HeadBall && contactPoint.getBody2() instanceof BodyBall) || (contactPoint.getBody1() instanceof BodyBall && contactPoint.getBody2() instanceof HeadBall)) {
            Player winner;
            Player dead;
            if (contactPoint.getBody1() instanceof HeadBall) {
                dead = (Player) contactPoint.getBody1().getUserData();
                winner = (Player) contactPoint.getBody2().getUserData();
            } else {
                dead = (Player) contactPoint.getBody2().getUserData();
                winner = (Player) contactPoint.getBody1().getUserData();
            }

            if (dead != winner) {
                winner.addBalls(dead.balls.size());
                dead.die(game);
            }

            return true;
        } else {
            return false;
        }
    }

    private void handleHeadBallHeadBallCollision(ContactPoint contactPoint) {
        if (contactPoint.getBody1() instanceof HeadBall && contactPoint.getBody2() instanceof HeadBall) {
            ((Player) contactPoint.getBody1().getUserData()).headToHeadCollisions.put(contactPoint.getBody1().getWorldCenter().copy(), null);
            ((Player) contactPoint.getBody2().getUserData()).headToHeadCollisions.put(contactPoint.getBody2().getWorldCenter().copy(), null);
        }

    }
}