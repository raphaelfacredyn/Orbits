package com.raphael.orbits.dataClasses;

import com.raphael.orbits.gameObjects.Ball;
import com.raphael.orbits.gameObjects.player.BodyBall;
import com.raphael.orbits.gameObjects.player.HeadBall;
import com.raphael.orbits.screens.Game;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import java.util.*;

import static com.raphael.orbits.gameObjects.Renderable.SCALE_CONVERSION_FACTOR;

public class Player {
    public static final double SPEED = 5 * SCALE_CONVERSION_FACTOR;
    public static final double SUPER_SPEED = 7 * SCALE_CONVERSION_FACTOR;
    public static final double BALL_SEPARATION = Ball.DEFAULT_BALL_SIZE * 4;
    public char key;
    public Color color;
    // Position at time of collision, Direction after collision, Last ball to pass
    public HashMap<Vector2, Vector2> headToHeadCollisions = new HashMap<>();
    public ArrayList<BodyBall> balls = new ArrayList<>();
    public World world;
    HeadBall head;
    int score = 0;

    int numOfBallsToAdd = 0;

    public Player(char key) {
        this(key, Color.randomPlayerColor());
    }

    public Player(char key, Color color) {
        this.key = key;
        this.color = color;
    }

    public void initializePlayer(World w, Vector2 pos, Vector2 dir) {
        world = w;

        head = new HeadBall(pos);
        head.color = color;
        head.setAutoSleepingEnabled(false);
        head.setLinearVelocity(dir.multiply(SPEED));
        head.setUserData(this);
        world.addBody(head);
    }

    public void update(Game game) {
        if (head != null) { // Meaning not dead
            rectifyVelocityIfNeeded(head);
            for (Body b : balls) {
                rectifyVelocityIfNeeded(b);
            }

            // Handle head to head collisions for BodyBalls
            if (!balls.isEmpty()) { // If there actually are BodyBalls
                Iterator<Map.Entry<Vector2, Vector2>> it = headToHeadCollisions.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Vector2, Vector2> e = it.next();
                    if (e.getValue() == null)
                        e.setValue(head.getLinearVelocity().copy());

                    Map.Entry<Integer, Ball> lastCompleted = getLastCompletedIndex(e.getKey());
                    BodyBall earliestNonComplete = balls.get(lastCompleted.getKey() + 1);

                    if (lastCompleted.getValue().getWorldCenter().distance(e.getKey()) >= BALL_SEPARATION) {
                        Transform t = new Transform();
                        t.translate(e.getKey());
                        earliestNonComplete.setTransform(t);

                        earliestNonComplete.setLinearVelocity(e.getValue().copy());
                        earliestNonComplete.completedHeadToHeadCollisions.add(e.getKey());

                        if (lastCompleted.getKey() + 1 == balls.size() - 1) { // If the one we just collided was the last one
                            it.remove();
                            for (BodyBall b : balls) {
                                b.completedHeadToHeadCollisions.remove(e.getKey());
                            }
                        }
                    }
                }
            } else {
                headToHeadCollisions.clear(); // Get rid of those because we don't need them b/c we don't have any balls
            }

            // Don't add a ball if it would be in the wall
            int successfulAdds = 0;
            for (int i = 0; i < numOfBallsToAdd; i++) {
                Ball lastBall = balls.size() == 0 ? head : balls.get(balls.size() - 1);
                //TODO: If orbiting then this is not the right position
                Vector2 pos = lastBall.getWorldCenter().copy().add(lastBall.getLinearVelocity().getNormalized().getNegative().multiply(BALL_SEPARATION));
                BodyBall ball = new BodyBall(pos);

                boolean notPossible = false;
                for (BodyFixture wall : game.walls.getFixtures())
                    notPossible |= ball.createAABB().overlaps(wall.getShape().createAABB());

                if (notPossible)
                    break;
                else {
                    ball.setLinearVelocity(lastBall.getLinearVelocity().copy());
                    ball.color = color;
                    ball.setUserData(this);
                    balls.add(ball);
                    world.addBody(ball);
                    successfulAdds++;
                }
            }
            numOfBallsToAdd -= successfulAdds;
        }
    }

    private AbstractMap.SimpleEntry<Integer, Ball> getLastCompletedIndex(Vector2 key) {
        for (int i = balls.size() - 1; i >= 0; i--) {
            BodyBall b = balls.get(i);
            if (b.completedHeadToHeadCollisions.contains(key))
                return new AbstractMap.SimpleEntry<>(i, b);
        }
        return new AbstractMap.SimpleEntry<>(-1, head); // -1 Means head
    }

    private void rectifyVelocityIfNeeded(Body b) {
        if (b.getJoints().isEmpty())
            b.setLinearVelocity(b.getLinearVelocity().getNormalized().multiply(Player.SPEED));
    }

    public void addBalls(int num) {
        numOfBallsToAdd += num;
    }

    public void die(Game game) {
        game.toRemove.add(head);
        game.toRemove.addAll(balls);

        head = null;
        balls.clear();

        for (Player p : game.players)
            if (p != this)
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
}
