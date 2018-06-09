package com.raphael.orbits.overlays;

import java.util.ArrayList;

public class OverlayManager {
    public ArrayList<Overlay> overlays = new ArrayList<>();

    public void tick(int currTime) {
        overlays.removeIf(overlay -> {
            overlay.tick(currTime);
            return overlay.isDone();
        });
    }
}
