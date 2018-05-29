package com.raphael.orbits;

import com.raphael.orbits.screens.Game;
import com.raphael.orbits.screens.GameSetup;
import processing.core.PApplet;
import processing.core.PGraphics;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OrbitsGame extends PApplet {
    GameSetup gameSetup;
    Game game;
    PGraphics canvas;
    Screen currScreen;
    Boolean fullscreen;
    float factor;

    public OrbitsGame() {
        fullscreen = 0 == JOptionPane.showConfirmDialog(frame, "Fullscreen", "Should the game be fullscreen?", JOptionPane.YES_NO_OPTION);
    }

    public void settings() {
        if (fullscreen)
            fullScreen(P2D);
        else
            size(displayWidth, displayHeight - 50, P2D);
    }

    public void setup() {
        surface.setResizable(true);

        canvas = createGraphics(1600, 900, P2D);

        textFont(createFont("fonts/Montserrat-Regular.ttf", 1)); // Set default font

        gameSetup = new GameSetup(canvas, (ActionEvent e) -> {
            game = new Game(OrbitsGame.this, canvas, gameSetup.players);
            currScreen = game;
        }); // Start game setup
        currScreen = gameSetup;
    }

    public void draw() {
        background(0);
        factor = Math.min((float) width / canvas.width, (float) height / canvas.height);
        canvas.beginDraw();
        currScreen.draw();
        canvas.endDraw();
        scale(factor);
        image(canvas, 0, 0);
    }

    @Override
    public void keyTyped() {
        super.keyTyped();
        currScreen.keyTyped(key);
    }
}
