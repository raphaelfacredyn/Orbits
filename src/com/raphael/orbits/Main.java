package com.raphael.orbits;

import processing.core.PApplet;

public class Main {

    public static void main(String args[]) {
//        new Thread(new BackgroundMusicPlayer()).start(); // Not in OrbitsGame because each game doesn't need its own song
        PApplet.main(OrbitsGame.class);
    }
}
