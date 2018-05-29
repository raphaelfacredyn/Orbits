package com.raphael.orbits.screens;

import com.raphael.orbits.Screen;
import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.dataClasses.Player;
import processing.core.PGraphics;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameSetup extends Screen {
    private static final int padding = 50;
    public ArrayList<Player> players = new ArrayList<>();
    private Color tmpColor;
    private String tmpString;
    private Player tmpPlayer;
    private PGraphics canvas;
    private ActionListener onDone;

    public GameSetup(PGraphics canvas, ActionListener onDone) {
        this.canvas = canvas;
        this.onDone = onDone;
    }

    public void draw() {
        canvas.noStroke();

        tmpColor = Color.themeColors[0];
        canvas.background(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);

        tmpColor = Color.themeColors[1];
        canvas.fill(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);

        canvas.rect(padding, padding, canvas.width - 2 * padding, canvas.height - 2 * padding, 10);

        canvas.textSize(64);
        tmpString = "Player Select";
        tmpColor = Color.themeColors[4];
        canvas.fill(tmpColor.r, tmpColor.g, tmpColor.b, tmpColor.a);
        canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 3);

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

                canvas.text(formatPlayer(tmpPlayer, i + 1), x * columnWidth + canvas.width / 2 - width / 2, y * rowHeight + padding * 5);
            }
        }

    }

    private String formatPlayer(Player p, int num) {
        return "Player " + num + ": " + p.toString();
    }

    @Override
    public void keyTyped(char key) {
        if (key == '!') {
            onDone.actionPerformed(null);
        } else {
            tmpPlayer = new Player(("" + key).toLowerCase().charAt(0));
            if (players.size() < Color.playerColorPresets.length && !players.contains(tmpPlayer)) {
                tmpPlayer.color = Color.playerColorPresets[players.size() % Color.playerColorPresets.length];
                players.add(tmpPlayer);
            }
        }
    }
}
