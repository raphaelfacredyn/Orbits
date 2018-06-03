package com.raphael.orbits;

import com.raphael.orbits.dataClasses.PlayerGameEvent;
import com.raphael.orbits.gameObjects.*;
import com.raphael.orbits.gameObjects.player.BodyBall;
import com.raphael.orbits.gameObjects.player.HeadBall;
import com.raphael.orbits.gameObjects.player.Player;
import com.raphael.orbits.screens.Game;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.contact.ContactListener;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.dynamics.contact.PersistedContactPoint;
import org.dyn4j.dynamics.contact.SolvedContactPoint;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;


public class CollisionHandler implements ContactListener {
    Game game;

    public CollisionHandler(Game game) {
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
    public boolean persist(PersistedContactPoint contactPoint) {
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

        // FreeBalls don't collide with each other
        if (contactPoint.getBody1() instanceof FreeBall && contactPoint.getBody2() instanceof FreeBall)
            return false;

        // AreaBalls don't collide with each other
        if (contactPoint.getBody1() instanceof AreaBall && contactPoint.getBody2() instanceof AreaBall)
            return false;

        // AreaBalls don't collide with FreeBalls
        if ((contactPoint.getBody1() instanceof AreaBall && contactPoint.getBody2() instanceof FreeBall) || (contactPoint.getBody1() instanceof FreeBall && contactPoint.getBody2() instanceof AreaBall))
            return false;

        // BodyBalls don't collide with each other
        if (contactPoint.getBody1() instanceof BodyBall && contactPoint.getBody2() instanceof BodyBall)
            return false;

        // Add ball to player if HeadBall hits FreeBall
        if (handleHeadBallFreeBallCollision(contactPoint))
            return false;

        // Add all nearby balls to player if HeadBall hits AreaBall
        if (handleHeadBallAreaBallCollision(contactPoint))
            return false;

        // BodyBalls don't collide with FreeBalls
        if ((contactPoint.getBody1() instanceof BodyBall && contactPoint.getBody2() instanceof FreeBall) || (contactPoint.getBody1() instanceof FreeBall && contactPoint.getBody2() instanceof BodyBall))
            return false;

        // BodyBalls don't collide with AreaBalls
        if ((contactPoint.getBody1() instanceof BodyBall && contactPoint.getBody2() instanceof AreaBall) || (contactPoint.getBody1() instanceof AreaBall && contactPoint.getBody2() instanceof BodyBall))
            return false;

        // Kill player if it hits BodyBall of other player
        if (handleHeadBallBodyBallCollision(contactPoint))
            return false;

        // Make Body Balls collide upon reaching position of HeadBall HeadBall Collision
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

            if (ball instanceof BodyBall && ((BodyBall) ball).shot) {
                FreeBall newBall = new FreeBall(ball.getWorldCenter());
                newBall.setLinearVelocity(ball.getLinearVelocity().copy().multiply(0.4));
                ((BodyBall) ball).getPlayer().removeBullet((BodyBall) ball);
                ball = newBall;
                game.toAdd.add(ball);
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

            Player player = headBall.getPlayer();
            if (!player.superState) {
                game.toRemove.add(freeBall);
                player.addBalls(1);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean handleHeadBallAreaBallCollision(ContactPoint contactPoint) {
        if ((contactPoint.getBody1() instanceof HeadBall && contactPoint.getBody2() instanceof AreaBall) || (contactPoint.getBody1() instanceof AreaBall && contactPoint.getBody2() instanceof HeadBall)) {
            HeadBall headBall;
            AreaBall areaBall;
            if (contactPoint.getBody1() instanceof HeadBall) {
                headBall = (HeadBall) contactPoint.getBody1();
                areaBall = (AreaBall) contactPoint.getBody2();
            } else {
                headBall = (HeadBall) contactPoint.getBody2();
                areaBall = (AreaBall) contactPoint.getBody1();
            }

            Player player = headBall.getPlayer();
            if (!player.superState) {
                game.toRemove.add(areaBall);
                for (Body b : player.world.getBodies()) {
                    if (b instanceof Ball && !(b instanceof HeadBall)) {
                        if (b.getWorldCenter().distance(areaBall.getWorldCenter()) < AreaBall.PICKUP_RADIUS && !player.balls.contains(b) && !game.toRemove.contains(b)) {
                            game.toRemove.add(b);
                            if (b instanceof AreaBall) {
                                ContactPoint newContactPoint = new ContactPoint(null, headBall, null, areaBall, null, null, null, 0, false);
                                handleHeadBallAreaBallCollision(newContactPoint);
                            } else {
                                player.addBalls(1);
                                if (b instanceof BodyBall) {
                                    if (((BodyBall) b).shot)
                                        ((BodyBall) b).getPlayer().removeBullet((BodyBall) b);
                                    else {
                                        game.toRemove.remove(b); // Don't remove that ball because it is part of the body and then there will be a gap so remove from the back
                                        ((BodyBall) b).getPlayer().removeLastBall();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean handleHeadBallBodyBallCollision(ContactPoint contactPoint) {
        if ((contactPoint.getBody1() instanceof HeadBall && contactPoint.getBody2() instanceof BodyBall) || (contactPoint.getBody1() instanceof BodyBall && contactPoint.getBody2() instanceof HeadBall)) {
            Player winner;
            Player dead;
            BodyBall ball;
            if (contactPoint.getBody1() instanceof HeadBall) {
                dead = ((HeadBall) contactPoint.getBody1()).getPlayer();
                ball = (BodyBall) contactPoint.getBody2();
                winner = ball.getPlayer();
            } else {
                dead = ((HeadBall) contactPoint.getBody2()).getPlayer();
                ball = (BodyBall) contactPoint.getBody1();
                winner = ball.getPlayer();
            }

            if (!dead.superState && dead != winner) {
                winner.addBalls(dead.balls.size());
                dead.die(game);
                if (ball.shot)
                    winner.removeBullet(ball);
            }

            return true;
        } else {
            return false;
        }
    }

    private void handleHeadBallHeadBallCollision(ContactPoint contactPoint) {
        if (contactPoint.getBody1() instanceof HeadBall && contactPoint.getBody2() instanceof HeadBall) {
            Player p1 = ((HeadBall) contactPoint.getBody1()).getPlayer();
            Player p2 = ((HeadBall) contactPoint.getBody2()).getPlayer();

            if (!p1.superState && !p2.superState) {
                p1.events.add(new PlayerGameEvent(contactPoint.getBody1().getWorldCenter().copy()));
                p2.events.add(new PlayerGameEvent(contactPoint.getBody2().getWorldCenter().copy()));
            }
        }
    }
}