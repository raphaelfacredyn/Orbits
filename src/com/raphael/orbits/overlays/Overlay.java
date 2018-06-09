package com.raphael.orbits.overlays;

import processing.core.PGraphics;

public abstract class Overlay {
    public PGraphics canvas;
    public int startTime = -1;
    public int currTime = -1;
    public int elapsedTime = -1;

    public Overlay(PGraphics canvas) {
        this.canvas = canvas;
    }

    public void tick(int currTime) {
        if (startTime == -1)
            startTime = currTime;
        this.currTime = currTime;
        this.elapsedTime = currTime - startTime;
    }

    public abstract boolean isDone();
}
