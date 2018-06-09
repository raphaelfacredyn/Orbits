package com.raphael.orbits.screens;

import com.raphael.orbits.Screen;
import com.raphael.orbits.dataClasses.Color;
import com.raphael.orbits.gameObjects.player.Player;
import processing.core.PGraphics;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import static com.raphael.orbits.Utils.background;
import static com.raphael.orbits.Utils.fill;

public class GameSetup extends Screen {
    private static final int padding = 50;
    public ArrayList<Player> players = new ArrayList<>();
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

        background(canvas, Color.themeColors[0]);

        fill(canvas, Color.themeColors[1]);

        canvas.rect(padding, padding, canvas.width - 2 * padding, canvas.height - 2 * padding, 10);

        canvas.textSize(64);
        fill(canvas, Color.themeColors[4]);
        tmpString = "Player Select";
        canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 3);

        canvas.textSize(32);
        tmpString = "When done press '!'";
        canvas.text(tmpString, canvas.width / 2 - canvas.textWidth(tmpString) / 2, padding * 4);

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

                canvas.text(formatPlayer(tmpPlayer, i + 1), x * columnWidth + canvas.width / 2 - width / 2 + columnWidth / 8, y * rowHeight + padding * 5);
            }
        }

    }

    private String formatPlayer(Player p, int num) {
        return "Player " + num + ": " + p.toString();
    }

    @Override
    public void keyTyped(char key) {
        if (key == '!') {
            if (players.size() > 1)
                onDone.actionPerformed(null);
        } else {
            tmpPlayer = new Player(Character.toLowerCase(key));
            if (players.contains(tmpPlayer)) {
                players.remove(tmpPlayer);
            } else if (players.size() < Color.playerColorPresets.length) {
                boolean done = false;
                Color color = null;
                while (!done) {
                    color = Color.playerColorPresets[(int) (Math.random() * Color.playerColorPresets.length)];
                    boolean copy = false;
                    for (Player p : players)
                        copy |= p.color.equals(color);
                    done = !copy;
                }
                tmpPlayer.color = color;
                players.add(tmpPlayer);
            }

        }
    }
}
