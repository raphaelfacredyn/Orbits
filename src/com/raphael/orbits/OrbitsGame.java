package com.raphael.orbits;

import com.raphael.orbits.gameObjects.player.Player;
import com.raphael.orbits.screens.Game;
import com.raphael.orbits.screens.GameSetup;
import com.raphael.orbits.screens.RoundOrGameEnd;
import processing.core.PApplet;
import processing.core.PGraphics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OrbitsGame extends PApplet {
    public static int pointLeadToWin = 2;
    PGraphics canvas;
    Screen currScreen;
    Boolean fullscreen;
    float factor;
    ArrayList<Player> players;
    private int pointsToWin = 0;
    private ActionListener onRoundOrGameEndDone = (ActionEvent e) -> {
        boolean winner = isWinner();
        for (int i = 0; i < players.size(); i++) {
            players.set(i, players.get(i).clone());
            if (winner)
                players.get(i).score = 0;
        }
        newGame();
    };
    private ActionListener onGameDone = (ActionEvent e) -> {
        System.gc();
        currScreen = new RoundOrGameEnd(canvas, players, onRoundOrGameEndDone, isWinner() ? -1 : pointsToWin);
    };

    public OrbitsGame() {
        fullscreen = 0 == JOptionPane.showConfirmDialog(frame, "Fullscreen", "Should the game be fullscreen?", JOptionPane.YES_NO_OPTION);
    }

    private boolean isWinner() {
        int highest = -1;
        int numWithHighest = 0;
        for (Player p : players) {
            if (p.score > highest) {
                highest = p.score;
                numWithHighest = 0;
            }
            if (p.score == highest) {
                numWithHighest++;
            }
        }

        int secondHighest = -1;
        for (Player p : players)
            if (p.score > secondHighest && p.score < highest)
                secondHighest = p.score;

        return numWithHighest == 1 && highest == pointsToWin && highest - secondHighest >= 2;
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

        currScreen = new GameSetup(canvas, (ActionEvent e) -> {
            players = ((GameSetup) currScreen).players;
            pointsToWin = (players.size() - 1) * 5;
            newGame();
        }); // Start game setup
    }

    private void newGame() {
        currScreen = new Game(OrbitsGame.this, canvas, players, onGameDone);
    }

    public void draw() {
        background(0);
        factor = Math.min((float) width / canvas.width, (float) height / canvas.height);
        canvas.beginDraw();
        currScreen.draw();
        canvas.endDraw();
        scale(factor);
        image(canvas, (width - canvas.width * factor) / 2, (height - canvas.height * factor) / 2);
    }

    @Override
    public void keyTyped() {
        super.keyTyped();
        currScreen.keyTyped(key);
    }
}
