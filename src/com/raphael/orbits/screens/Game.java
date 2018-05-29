package com.raphael.orbits.screens;

import com.raphael.orbits.CollisionFilter;
import com.raphael.orbits.Screen;
import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.dataClasses.Player;
import com.raphael.orbits.gameObjects.FreeBall;
import com.raphael.orbits.gameObjects.Orbit;
import com.raphael.orbits.gameObjects.Renderable;
import com.raphael.orbits.gameObjects.Walls;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

import static com.raphael.orbits.gameObjects.Renderable.SCALE_CONVERSION_FACTOR;
import static processing.core.PApplet.*;
import static processing.core.PConstants.PI;

public class Game extends Screen {
    private static final double PLAYER_SPAWN_CIRCLE_RADIUS = 3 * SCALE_CONVERSION_FACTOR;
    private static final double FREE_BALL_CONCENTRATION = 0.00002;
    private static double WIDTH, HEIGHT;
    public Walls walls;
    public ArrayList<Player> players;
    public ArrayList<Body> toAdd = new ArrayList<>();
    public ArrayList<Body> toRemove = new ArrayList<>();
    World world;
    PGraphics canvas;
    PApplet applet;
    int prevTime = -1, currTime, elapsedTime;
    Color tmpColor;

    public Game(PApplet applet, PGraphics canvas, ArrayList<Player> players) {
        WIDTH = canvas.width / Renderable.SCALE;
        HEIGHT = canvas.height / Renderable.SCALE;

        this.canvas = canvas;
        this.applet = applet;

        world = new World();
        world.setGravity(new Vector2(0, 0));

        world.addListener(new CollisionFilter(this));

        this.players = new ArrayList<>(players);

        walls = new Walls(WIDTH, HEIGHT);
        world.addBody(walls);

        for (int i = 0; i < 3; i++) {
            world.addBody(new Orbit(3 * SCALE_CONVERSION_FACTOR + i * 9 * SCALE_CONVERSION_FACTOR, 4 * SCALE_CONVERSION_FACTOR, 1 * SCALE_CONVERSION_FACTOR));
            world.addBody(new Orbit(8 * SCALE_CONVERSION_FACTOR + i * 9 * SCALE_CONVERSION_FACTOR, 4 * SCALE_CONVERSION_FACTOR, 2 * SCALE_CONVERSION_FACTOR));
            world.addBody(new Orbit(4 * SCALE_CONVERSION_FACTOR + i * 9 * SCALE_CONVERSION_FACTOR, 10 * SCALE_CONVERSION_FACTOR, 3 * SCALE_CONVERSION_FACTOR));
        }

        // Start players in a circle
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            float angle = map(i, 0, players.size(), 0, 2 * PI);

            Vector2 dir = new Vector2(cos(angle), sin(angle));
            Vector2 pos = dir.copy().multiply(PLAYER_SPAWN_CIRCLE_RADIUS).add(WIDTH / 2, HEIGHT / 2);

            p.initializePlayer(world, pos, dir);
        }
    }

    public void draw() {
        tmpColor = Color.themeColors[0];
        canvas.background(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);

        for (Body b : toAdd)
            world.addBody(b);
        toAdd.clear();

        for (Body b : toRemove)
            world.removeBody(b);
        toRemove.clear();

        if (prevTime == -1)
            prevTime = applet.millis();
        else {
            currTime = applet.millis();
            elapsedTime = currTime - prevTime;
            prevTime = applet.millis();
        }
        world.update(elapsedTime);
        updateFreeBalls();
        for (Player p : players) {
            p.update(this);
        }
        drawBodies();
    }

    private void updateFreeBalls() {
        int freeBallCount = 0;
        for (Body b : world.getBodies()) {
            if (b instanceof FreeBall) {
                freeBallCount++;
            }
        }

        for (int i = 0; i < (int) (canvas.width * canvas.height * FREE_BALL_CONCENTRATION) - freeBallCount; i++) {
            randomFreeBall();
        }
    }

    private void randomFreeBall() {
        world.addBody(new FreeBall(new Vector2((WIDTH - Player.BALL_SEPARATION * 2) * Math.random() + Player.BALL_SEPARATION, (HEIGHT - Player.BALL_SEPARATION * 2) * Math.random() + Player.BALL_SEPARATION)));
    }

    private void drawBodies() {
        for (Body b : world.getBodies()) {
            if (b instanceof Renderable) {
                ((Renderable) b).render(canvas);
            }
        }
    }

    @Override
    public void keyTyped(char key) {

    }
}
