package com.raphael.orbits.dataClasses;

import com.raphael.orbits.gameObjects.Orbit;
import org.dyn4j.geometry.Vector2;

import java.util.Objects;

public class PlayerGameEvent {
    public Vector2 position;
    public Vector2 direction;
    public boolean toggleOrbit;
    public Orbit orbit;

    public PlayerGameEvent(Vector2 position) {
        this(position, null, false, null);
    }

    public PlayerGameEvent(Vector2 position, boolean toggleOrbit, Orbit orbit) {
        this(position, null, toggleOrbit, orbit);
    }

    public PlayerGameEvent(Vector2 position, Vector2 direction, boolean toggleOrbit, Orbit orbit) {
        this.position = position;
        this.direction = direction;
        this.toggleOrbit = toggleOrbit;
        this.orbit = orbit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerGameEvent that = (PlayerGameEvent) o;
        return toggleOrbit == that.toggleOrbit &&
                Objects.equals(position, that.position) &&
                Objects.equals(direction, that.direction);
    }
}
