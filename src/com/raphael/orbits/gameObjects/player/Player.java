package com.raphael.orbits.gameObjects.player;

import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.dataClasses.PlayerGameEvent;
import com.raphael.orbits.gameObjects.Ball;
import com.raphael.orbits.gameObjects.Orbit;
import com.raphael.orbits.screens.Game;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import java.util.*;

import static com.raphael.orbits.Utils.getAngleBetween;
import static com.raphael.orbits.Utils.inWalls;
import static com.raphael.orbits.gameObjects.Renderable.SCALE_CONVERSION_FACTOR;

public class Player {
    public static final double SPEED = 5 * SCALE_CONVERSION_FACTOR;
    public static final double SUPER_SPEED = 1.3 * SPEED * SCALE_CONVERSION_FACTOR;
    public static final double ORBIT_SPEED = SPEED;
    public static final double BULLET_SPEED = 2 * SPEED;

    public static final double BALL_SEPARATION = Ball.DEFAULT_BALL_SIZE * 4;

    private static final long SUPER_STATE_DURATION = 500;

    public char key;
    public Color color;

    public World world;
    public Game game;
    public ArrayList<BodyBall> balls = new ArrayList<>();
    public ArrayList<BodyBall> bullets = new ArrayList<>();
    public ArrayList<PlayerGameEvent> events = new ArrayList<>();
    public int score = 0;
    public boolean superState = false;
    HeadBall head;
    private int numOfBallsToAdd = 0;

    public Player(char key) {
        this(key, Color.randomPlayerColor());
    }

    public Player(char key, Color color) {
        this.key = key;
        this.color = color;
    }

    public Player(char key, Color color, int score) {
        this.key = key;
        this.color = color;
        this.score = score;
    }

    public void initializePlayer(World world, Game game, Vector2 pos, Vector2 dir) {
        this.world = world;
        this.game = game;

        head = new HeadBall(pos);
        setupBall(head);
        head.setLinearVelocity(dir.multiply(SPEED));
        world.addBody(head);
    }

    public void update() {
        if (!isDead()) {

            head.rectifyVelocity();
            for (BodyBall b : balls)
                b.rectifyVelocity();

            // Handle events
            if (!balls.isEmpty()) {
                Iterator<PlayerGameEvent> it = events.iterator();
                while (it.hasNext()) {
                    PlayerGameEvent e = it.next();
                    if (e.direction == null)
                        e.direction = head.getLinearVelocity().copy();

                    Map.Entry<Integer, PlayerBall> lastCompleted = getLastBallToHaveCompletedEvent(e);
                    try {
                        BodyBall earliestNonComplete = balls.get(lastCompleted.getKey() + 1);

                        if (lastCompleted.getValue().distanceFromVector(e.position) >= BALL_SEPARATION) {
                            Transform t = new Transform();
                            t.translate(e.position);
                            earliestNonComplete.setTransform(t);

                            if (e.toggleOrbit)
                                earliestNonComplete.toggleOrbiting(e.orbit);

                            earliestNonComplete.setLinearVelocity(e.direction.copy());

                            earliestNonComplete.completedEvents.add(e);

                            if (lastCompleted.getKey() + 1 == balls.size() - 1) { // If the event has occurred to all balls
                                it.remove();
                                for (BodyBall b : balls) {
                                    b.completedEvents.remove(e);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        // Doesn't happen very often
                        // Caused by the shooting which deletes the ball so it isn't available anymore
                    }
                }
            } else {
                events.clear(); // Get rid of those because we don't need them b/c we don't have any balls
            }

            // Don't add a ball if it would be in the wall
            for (int i = 0; i < numOfBallsToAdd; i++) {
                PlayerBall lastBall = balls.size() == 0 ? head : balls.get(balls.size() - 1);

                BodyBall ball;
                // Find where to place new ball
                if (lastBall.isOrbiting()) {
                    Orbit orbit = lastBall.getOrbit();
                    Vector2 orbitCenter = orbit.getWorldCenter().copy();
                    Vector2 lastBallPos = lastBall.getWorldCenter().copy();
                    double orbitToLastBallAngle = getAngleBetween(orbitCenter, lastBallPos);
                    Vector2 lastBallPreviousPosition = lastBallPos.copy().subtract(lastBall.getLinearVelocity().copy().multiply(0.1));
                    double orbitToLastBallPreviousPosAngle = getAngleBetween(orbitCenter, lastBallPreviousPosition);
                    boolean clockwise = orbitToLastBallAngle < orbitToLastBallPreviousPosAngle;
                    double distanceFromOrbit = lastBallPos.distance(orbitCenter);
                    double angleToRotate = Math.acos((2 * Math.pow(distanceFromOrbit, 2) - Math.pow(BALL_SEPARATION, 2)) / (2 * distanceFromOrbit * distanceFromOrbit));
                    double newBallAngle = orbitToLastBallAngle + (clockwise ? 1 : -1) * angleToRotate;
                    ball = new BodyBall(new Vector2(Math.cos(newBallAngle), -Math.sin(newBallAngle)).multiply(distanceFromOrbit).add(orbitCenter));
                } else {
                    ball = new BodyBall(lastBall.getWorldCenter().copy().add(lastBall.getLinearVelocity().getNormalized().getNegative().multiply(BALL_SEPARATION)));
                }

                if (inWalls(ball.createAABB(), game.walls))
                    break;
                else {
                    ball.setLinearVelocity(lastBall.getLinearVelocity().copy());
                    setupBall(ball);
                    if (lastBall.isOrbiting())
                        ball.startOrbiting(lastBall.getOrbit());
                    balls.add(ball);
                    world.addBody(ball);
                    numOfBallsToAdd--;
                }
            }
        }
    }

    private void setupBall(PlayerBall ball) {
        ball.color = color;
        ball.setUserData(this);
    }

    public boolean isDead() {
        return head == null;
    }

    private AbstractMap.SimpleEntry<Integer, PlayerBall> getLastBallToHaveCompletedEvent(PlayerGameEvent e) {
        for (int i = balls.size() - 1; i >= 0; i--) {
            BodyBall b = balls.get(i);
            if (b.completedEvents.contains(e))
                return new AbstractMap.SimpleEntry<>(i, b);
        }
        return new AbstractMap.SimpleEntry<>(-1, head); // -1 Means head
    }

    public void addBalls(int num) {
        numOfBallsToAdd += num;
    }

    public void die(Game game) {
        game.toRemove.add(head);
        game.toRemove.addAll(balls);
        game.toRemove.addAll(bullets);

        head.stopOrbiting();

        for (BodyBall b : balls)
            b.stopOrbiting();

        head = null;
        balls.clear();
        bullets.clear();

        for (Player p : game.players)
            if (p != this && !p.isDead())
                p.score++;
    }

    @Override
    public String toString() {
        return "'" + ("" + key).toUpperCase() + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return key == player.key;
    }

    public void trigger() {
        Orbit orbit = orbitUnder();
        if (orbit == null) {
            if (balls.size() > 0)
                shoot();
            startSuperState();
        } else {
            head.toggleOrbiting(orbit);
            events.add(new PlayerGameEvent(head.getWorldCenter(), true, orbit));
        }
    }

    private void startSuperState() {
        superState = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                superState = false;
            }
        }, SUPER_STATE_DURATION);
    }

    private void shoot() {
        BodyBall bullet = balls.remove(balls.size() - 1);
        bullets.add(bullet);
        bullet.shot = true;
        Transform position = head.getTransform().copy();
        position.translate(head.getLinearVelocity().getNormalized().setMagnitude(BALL_SEPARATION));
        bullet.setTransform(position);
        bullet.setLinearVelocity(head.getLinearVelocity().getNormalized().multiply(BULLET_SPEED));
        bullet.stopOrbiting();
        if (inWalls(bullet.createAABB(), game.walls))
            game.toRemove.add(bullet);
    }

    private Orbit orbitUnder() {
        if (!isDead())
            for (Body b : world.getBodies())
                if (b instanceof Orbit && b.getFixture(0).getShape().getRadius() >= b.getWorldCenter().distance(head.getWorldCenter()))
                    return (Orbit) b;
        return null;
    }

    public Player clone() {
        return new Player(key, color, score);
    }

    public void removeBall(ArrayList<BodyBall> set, BodyBall ball) {
        ball.prepareForRemoval();
        set.remove(ball);
        game.toRemove.add(ball);
    }

    public void removeBall(BodyBall ball) {
        removeBall(balls, ball);
    }

    public void removeBullet(BodyBall bullet) {
        removeBall(bullets, bullet);
    }

    public void removeLastBall() {
        removeBall(balls.get(balls.size() - 1));
    }
}
