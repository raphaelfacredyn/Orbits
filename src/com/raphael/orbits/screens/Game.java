package com.raphael.orbits.screens;

import com.raphael.orbits.CollisionHandler;
import com.raphael.orbits.Screen;
import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.*;
import com.raphael.orbits.gameObjects.player.Player;
import com.raphael.orbits.overlays.OverlayManager;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.raphael.orbits.Utils.background;
import static com.raphael.orbits.Utils.pixelToWorld;
import static com.raphael.orbits.gameObjects.Renderable.SCALE_CONVERSION_FACTOR;
import static com.raphael.orbits.gameObjects.Walls.WALL_WIDTH;
import static processing.core.PApplet.*;
import static processing.core.PConstants.PI;

public class Game extends Screen {
    private static final double PLAYER_SPAWN_CIRCLE_RADIUS = 3 * SCALE_CONVERSION_FACTOR;
    private static final double FREE_BALL_CONCENTRATION = 0.00002;
    private static final double AREA_BALL_CONCENTRATION = FREE_BALL_CONCENTRATION / 10;
    private static final long TIME_AFTER_WIN = 3000;

    private static double WIDTH, HEIGHT;
    public Walls walls;
    public ArrayList<Player> players;
    public ArrayList<Body> toAdd = new ArrayList<>();
    public ArrayList<Body> toRemove = new ArrayList<>();
    public PGraphics canvas;
    public OverlayManager overlayManager;
    World world;
    PApplet applet;
    int prevTime = -1, currTime, elapsedTime;
    ActionListener onDone;
    boolean gameOver = false;

    public Game(PApplet applet, PGraphics canvas, ArrayList<Player> players, ActionListener onDone) {
        WIDTH = pixelToWorld(canvas.width);
        HEIGHT = pixelToWorld(canvas.height);

        this.canvas = canvas;
        this.applet = applet;

        this.onDone = onDone;

        overlayManager = new OverlayManager();

        world = new World();
        world.setGravity(new Vector2(0, 0));

        world.addListener(new CollisionHandler(this));

        this.players = players;

        walls = new Walls(WIDTH, HEIGHT);
        world.addBody(walls);

        randomMap();

        positionPlayers();
    }

    private void positionPlayers() {
        // Start players in a circle
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            float angle = map(i, 0, players.size(), 0, 2 * PI);

            Vector2 dir = new Vector2(cos(angle), sin(angle));
            Vector2 pos = dir.copy().multiply(PLAYER_SPAWN_CIRCLE_RADIUS).add(WIDTH / 2, HEIGHT / 2);

            p.initializePlayer(world, this, pos, dir);
        }
    }

    private void randomMap() {
        int maxAttempts = 50;
        ArrayList<Orbit> orbits = new ArrayList<>();
        int numOrbits = (int) (7 + Math.random() * 5);
        Orbit o;
        boolean overlaps;
        int attempts;
        double r;
        for (int i = 0; i < numOrbits; i++) {
            attempts = 0;
            do {
                overlaps = false;
                r = (1 + Math.random()) * 2 * SCALE_CONVERSION_FACTOR;
                o = new Orbit(Math.random() * (WIDTH - (2 * r + 2 * WALL_WIDTH)) + r + WALL_WIDTH, Math.random() * (HEIGHT - (2 * r + 2 * WALL_WIDTH)) + r + WALL_WIDTH, r);

                for (Orbit other : orbits)
                    overlaps |= o.overlaps(other);

                attempts++;
            } while (overlaps && attempts < maxAttempts);
            if (!overlaps)
                orbits.add(o);
        }
        for (Orbit orbit : orbits)
            world.addBody(orbit);
    }

    @Override
    public void draw() {
        background(canvas, Color.themeColors[0]);

        for (Body b : toAdd)
            world.addBody(b);
        toAdd.clear();

        for (Body b : toRemove)
            world.removeBody(b);
        toRemove.clear();

        if (!gameOver) {
            int numLivingPlayers = 0;
            for (Player p : players)
                numLivingPlayers += p.isDead() ? 0 : 1;
            if (numLivingPlayers <= 1) {
                gameOver = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        onDone.actionPerformed(null);
                    }
                }, TIME_AFTER_WIN);
            }
        }

        if (prevTime == -1)
            prevTime = applet.millis();
        else {
            elapsedTime = currTime - prevTime;
            prevTime = currTime;
            currTime = applet.millis();
        }
        world.update(elapsedTime);
        updateFreeBalls();
        updateAreaBalls();
        for (Player p : players)
            p.update();
        drawBodies();
        overlayManager.tick(currTime);
    }

    private void updateAreaBalls() {
        int areaBallCount = 0;
        for (Body b : world.getBodies()) {
            if (b instanceof AreaBall) {
                areaBallCount++;
            }
        }

        for (int i = 0; i < (int) (canvas.width * canvas.height * AREA_BALL_CONCENTRATION) - areaBallCount; i++) {
            randomAreaBall();
        }
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

    private void randomAreaBall() {
        world.addBody(new AreaBall(new Vector2((WIDTH - Player.BALL_SEPARATION * 2) * Math.random() + Player.BALL_SEPARATION, (HEIGHT - Player.BALL_SEPARATION * 2) * Math.random() + Player.BALL_SEPARATION)));
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
        for (Player p : players)
            if (p.key == Character.toLowerCase(key))
                p.trigger();
    }
}
