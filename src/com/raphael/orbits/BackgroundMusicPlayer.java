package com.raphael.orbits;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class BackgroundMusicPlayer implements Runnable {
    URL[] songs = new URL[]{
            getSong("10µF.wav"),
            getSong("FinalMoments.wav"),
            getSong("LightLatt.wav"),
            getSong("longing.wav"),
            getSong("montawk.wav"),
    };

    private static URL getSong(String song) {
        return BackgroundMusicPlayer.class.getClassLoader().getResource("songs/" + song);
    }

    @Override
    public void run() {
        playRandomSongs();
    }

    private void playRandomSongs() {
        try {
            URL song = getRandomSong();
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

    private URL getRandomSong() {
        return songs[(int) (Math.random() * (songs.length))];
    }
}
