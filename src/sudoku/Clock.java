/**
* @file Clock.java
* @author Melanie Roy-Plommer
* @version 1.0
*
* @section DESCRIPTION
* <  >
*
* @section LICENSE
* Copyright 2018 - 2019
* Permission to use, copy, modify, and/or distribute this software for
* any purpose with or without fee is hereby granted, provided that the
* above copyright notice and this permission notice appear in all copies.
*
* THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
* WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
* ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
* WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
* ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
* OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*
* @section Academic Integrity
* I certify that this work is solely my own and complies with
* NBCC Academic Integrity Policy (policy 1111)
*/
package sudoku;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import java.awt.Color;

/**
 * @section Academic Integrity I certify that this work is solely my own and
 * complies with NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class Clock extends ImageEntity {

    private ArrayList<String> imagePaths = new ArrayList<String>() {
        {
            add("resources/clock/zeroWhite.png");
            add("resources/clock/oneWhite.png");
            add("resources/clock/twoWhite.png");
            add("resources/clock/threeWhite.png");
            add("resources/clock/fourWhite.png");
            add("resources/clock/fiveWhite.png");
            add("resources/clock/sixWhite.png");
            add("resources/clock/sevenWhite.png");
            add("resources/clock/eightWhite.png");
            add("resources/clock/nineWhite.png");
            add("resources/clock/colonWhite.png");
        }
    };

    // Time Variables
    private int elapsedTime = 0;
    private int currentTimeInSeconds = 0;
    private int previousTimeInSeconds = 0;
    private int[] timeDigits = new int[6];
    private Image[] timeImages = new Image[6];

    // Control flags
    private boolean running = false;

    public Clock(Dimension size, Point position, int orientation) {
        super(size, position, orientation);

        //showBoundingBox();
        addImage("resources/blank.png");

        // load number images from files
        imagePaths.forEach((path) -> {
            addImage(path);
        });

        // zero out the time digits
        for (int i = 0; i < timeDigits.length; i++) {
            timeDigits[i] = 1;
        }

        // set the time images
        updateTimeImages();
    }

    public void update() {
        super.update();

        if (running) {
            currentTimeInSeconds = (int) (System.currentTimeMillis() / 1000);
            if (currentTimeInSeconds - previousTimeInSeconds >= 1) {
                elapsedTime += currentTimeInSeconds - previousTimeInSeconds;
                previousTimeInSeconds = currentTimeInSeconds;
                updateTimeDigits();
                updateTimeImages();
            }
        }
    }

    private void updateTimeDigits() {
        int trimmedElapsedTime = elapsedTime;
        // 10s of hours
        timeDigits[0] = trimmedElapsedTime / (10 * 3600);
        trimmedElapsedTime %= (10 * 3600);

        // singles of hours
        timeDigits[1] = trimmedElapsedTime / 3600;
        trimmedElapsedTime %= 3600;

        // 10s of minutes
        timeDigits[2] = trimmedElapsedTime / (10 * 60);
        trimmedElapsedTime %= (10 * 60);

        // singles of minutes
        timeDigits[3] = trimmedElapsedTime / 60;
        trimmedElapsedTime %= 60;

        // 10s of seconds
        timeDigits[4] = trimmedElapsedTime / 10;
        trimmedElapsedTime %= 10;

        // singles of seconds
        timeDigits[5] = trimmedElapsedTime;
    }

    private void updateTimeImages() {
        for (int i = 0; i < timeDigits.length; i++) {
            updateTimeImage(i, timeDigits[i]);
        }
    }

    private void updateTimeImage(int imageIndex, int digit) {
        timeImages[imageIndex] = images.get(digit + 1);
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect((int) (x - 0.5 * w), (int) (y - 0.5 * h), (int) w, (int) h);
        g.setColor(new Color(0, 0, 0));
        g.drawRect((int) (x - 0.5 * w), (int) (y - 0.5 * h), (int) w, (int) h);
        g.drawImage(timeImages[0], (int) (x - (int) (w / 2) + 20), (int) (y - 20), 40, 40, null); // h1
        g.drawImage(timeImages[1], (int) (x - (int) (w / 2) + 40), (int) (y - 20), 40, 40, null); // h2
        g.drawImage(images.get(11), (int) (x - (int) (w / 2) + 70), (int) (y - 20), 20, 40, null); // :
        g.drawImage(timeImages[2], (int) (x - (int) (w / 2) + 80), (int) (y - 20), 40, 40, null); // m1
        g.drawImage(timeImages[3], (int) (x - (int) (w / 2) + 100), (int) (y - 20), 40, 40, null); // m2
        g.drawImage(images.get(11), (int) (x - (int) (w / 2) + 130), (int) (y - 20), 20, 40, null); // :
        g.drawImage(timeImages[4], (int) (x - (int) (w / 2) + 140), (int) (y - 20), 40, 40, null); // s1
        g.drawImage(timeImages[5], (int) (x - (int) (w / 2) + 160), (int) (y - 20), 40, 40, null); // s2
    }

    public void start() {
        running = true;
        currentTimeInSeconds = (int) (System.currentTimeMillis() / 1000);
        previousTimeInSeconds = currentTimeInSeconds;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void reset() {
        ;
    }

    public void timePenalty(int seconds) {
        elapsedTime += seconds;
    }
}
