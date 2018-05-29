package com.raphael.orbits;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundMusicPlayer implements Runnable {
    File[] songs = new File("data/songs").listFiles();

    @Override
    public void run() {
        playRandomSongs();
    }

    private void playRandomSongs() {
        try {
            File song = getRandomSong();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(song);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playRandomSongs();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            playRandomSongs();
        }
    }

    private File getRandomSong() {
        return songs[(int) (Math.random() * (songs.length))];
    }
}
