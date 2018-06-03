package com.raphael.orbits.screens;

import com.raphael.orbits.OrbitsGame;
import com.raphael.orbits.Screen;
import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.player.Player;
import processing.core.PGraphics;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoundOrGameEnd extends Screen {
    private static final int padding = 50;
    PGraphics canvas;
    ArrayList<Player> players;
    ActionListener onDone;
    int pointsToWin;
    private Color tmpColor;
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

        tmpColor = Color.themeColors[0];
        canvas.background(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);

        tmpColor = Color.themeColors[1];
        canvas.fill(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);

        canvas.rect(padding, padding, canvas.width - 2 * padding, canvas.height - 2 * padding, 10);

        canvas.textSize(64);
        tmpString = (pointsToWin == -1 ? "Game" : "Round") + " End";
        tmpColor = Color.themeColors[4];
        canvas.fill(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);
        canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 3);

        if (pointsToWin != -1) {
            canvas.textSize(32);
            tmpString = "Points To Win: " + pointsToWin;
            tmpColor = Color.themeColors[4];
            canvas.fill(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);
            canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 4);

            tmpString = "Point Lead To Win: " + OrbitsGame.pointLeadToWin;
            tmpColor = Color.themeColors[4];
            canvas.fill(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);
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
                tmpColor = tmpPlayer.color;
                canvas.fill(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);

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
