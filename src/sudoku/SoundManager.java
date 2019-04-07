/**
 * @file SoundManager.java
 * @author Melanie Roy-Plommer
 * @version 1.0
 *
 * @section DESCRIPTION
 * < >
 *
 * @section LICENSE Copyright 2018 - 2019 Permission to use, copy, modify,
 * and/or distribute this software for any purpose with or without fee is hereby
 * granted, provided that the above copyright notice and this permission notice
 * appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 *
 * @section Academic Integrity I certify that this work is solely my own and
 * complies with NBCC Academic Integrity Policy (policy 1111)
 */
package sudoku;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioInputStream;
import java.net.URL;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author melan
 */
public class SoundManager {

    private Mixer mixer;
    private Clip backgroundClip;
    private Clip effectsClip1;
    private Clip effectsClip2;

    private Map<String, AudioInputStream> audioStreams = new HashMap<>();

    public SoundManager() {
        // Get default mixer.
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixInfos[1]);

        // Set up clip lines (these are our output lines that we hand sounds to)
        DataLine.Info lineInfo = new DataLine.Info(Clip.class, null);
        try {
            backgroundClip = (Clip) mixer.getLine(lineInfo);
            effectsClip1 = (Clip) mixer.getLine(lineInfo);
            effectsClip2 = (Clip) mixer.getLine(lineInfo);
        } catch (LineUnavailableException lue) {
            lue.printStackTrace(System.out);
        }
    }

    /**
     *
     * @param soundID
     * @param soundPath
     */
    public void addSound(String soundID, String soundPath) {
        try {
            // Get AudioInputStream from soundPath
            URL soundURL = SoundManager.class.getResource(soundPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            audioStream.mark(1000000000);
            audioStreams.put(soundID, audioStream);
            //System.out.println(audioStreams.size());
        } catch (UnsupportedAudioFileException | IOException uafe) {
            uafe.printStackTrace(System.out);
        }
    }

    /**
     *
     * @param soundID
     * @param timesToLoop
     */
    public void playSound(String soundID, int timesToLoop) {
        try {
            if ("background".equals(soundID)) {
                backgroundClip.close();
                audioStreams.get(soundID).reset();
                backgroundClip.open(audioStreams.get(soundID));
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                if (effectsClip1.isActive()) {
                    effectsClip2.close();
                    audioStreams.get(soundID).reset();
                    effectsClip2.open(audioStreams.get(soundID));
                    effectsClip2.start();
                } else {
                    effectsClip1.close();
                    audioStreams.get(soundID).reset();
                    effectsClip1.open(audioStreams.get(soundID));
                    effectsClip1.start();
                }
            }
        } catch (LineUnavailableException | IOException lue) {
            lue.printStackTrace(System.out);
        }
    }

    /**
     *
     */
    public void stopBackgroundSound() {
        backgroundClip.stop();
        backgroundClip.setFramePosition(0);
    }

    /**
     *
     */
    public void stopSoundEffects() {
        effectsClip1.stop();
        effectsClip1.setFramePosition(0);
        effectsClip2.stop();
        effectsClip2.setFramePosition(0);
    }

    /**
     *
     */
    public void pauseBackgroundSound() {
        backgroundClip.stop();
    }

    /**
     *
     */
    public void stopAll() {
        stopBackgroundSound();
        stopSoundEffects();
    }

    /**
     *
     */
    public void unpauseBackgroundSound() {
        backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     *
     * @param soundPath
     */
    public void changeBackgroundSound(String soundPath) {
        try {
            backgroundClip.stop();
            audioStreams.remove("background");
            addSound("background", soundPath);
            audioStreams.get("background").reset();
            playSound("background", 1);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        }

    }

}
