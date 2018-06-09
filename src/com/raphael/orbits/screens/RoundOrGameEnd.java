package com.raphael.orbits.screens;

import com.raphael.orbits.OrbitsGame;
import com.raphael.orbits.Screen;
import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.player.Player;
import processing.core.PGraphics;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import static com.raphael.orbits.Utils.background;
import static com.raphael.orbits.Utils.fill;

public class RoundOrGameEnd extends Screen {
    private static final int padding = 50;
    PGraphics canvas;
    ArrayList<Player> players;
    ActionListener onDone;
    int pointsToWin;
    private String tmpString;
    private Player tmpPlayer;

    public RoundOrGameEnd(PGraphics canvas, ArrayList<Player> players, ActionListener onDone, int pointsToWin) {
        this.canvas = canvas;
        this.onDone = onDone;
        this.players = players;
        this.pointsToWin = pointsToWin;
    }

    @Override
    public void draw() {
        canvas.noStroke();

        background(canvas, Color.themeColors[0]);

        fill(canvas, Color.themeColors[1]);

        canvas.rect(padding, padding, canvas.width - 2 * padding, canvas.height - 2 * padding, 10);

        canvas.textSize(64);
        fill(canvas, Color.themeColors[4]);
        tmpString = (pointsToWin == -1 ? "Game" : "Round") + " End";
        canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 3);

        if (pointsToWin != -1) {
            canvas.textSize(32);
            tmpString = "Points To Win: " + pointsToWin;
            canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 4);
            tmpString = "Point Lead To Win: " + OrbitsGame.pointLeadToWin;
            canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 4.75f);
        }

        int rowHeight = padding * 2;
        int rows = (canvas.height - padding * 6) / (padding * 2);
        int columnWidth = padding * 5;
        int x, y, width;
        if (!players.isEmpty() && rows > 0) {
            canvas.textSize(32);
            x = (players.size() - 1) / rows;
            width = columnWidth * (x + 1);
            for (int i = 0; i < players.size(); i++) {
                tmpPlayer = players.get(i);
                fill(canvas, tmpPlayer.color);

                x = i / rows;
                y = i % rows;

                canvas.text(formatPlayer(tmpPlayer, i + 1), x * columnWidth + canvas.width / 2 - width / 2 + columnWidth / 8, y * rowHeight + padding * 6);
            }
        }
    }

    private String formatPlayer(Player p, int num) {
        return "Player " + num + " " + p.toString() + ": " + p.score;
    }

    @Override
    public void keyTyped(char key) {
        if (key == '!') {
            onDone.actionPerformed(null);
        }
    }
}
