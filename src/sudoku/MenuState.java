/**
 * @file MenuState.java
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Melanie Roy-Plommer
 */
public class MenuState extends State {

    private final Button playButton;
    private final Button quitButton;

    private final ImageEntity banner;
    private int transitionTimer = 0;
    private boolean timedTransition = false;

    public MenuState() {
        //Dimension size, Point position, int orientation, String buttonText
        super();
        addBackground("resources/menuBackground.jpg");

        banner = new ImageEntity(new Dimension((int) (0.3 * width), (int) (0.3 * height)), new Point(width / 2, (int) (-0.2 * height)), 0);
        banner.addImage("resources/menuBanner.png");
        banner.slide(new Point(width / 2, (int) (0.2 * height)), 10);

        playButton = new Button((int) (0.33 * width), new Point((int) (-0.3 * width), (int) (0.4 * height)), 0, "Play Game", Button.ButtonType.MENU);
        playButton.slide(new Point((int) (0.5 * width), (int) (0.4 * height)), 10);
        addMouseListener(playButton);

        quitButton = new Button((int) (0.33 * width), new Point((int) (-0.7 * width), (int) (0.8 * height)), 0, "Quit", Button.ButtonType.MENU);
        quitButton.slide(new Point((int) (0.5 * width), (int) (0.8 * height)), 30);
        addMouseListener(quitButton);
    }

    /**
     *
     */
    @Override
    public void update() {
        super.update();

        if (animationCounter == 10) {
            sounds.changeBackgroundSound("resources/sounds/oceanwaves.wav");
        }

        if (playButton.hasClick()) {
            playButton.unClick();
            animationCounter = 0;
            transitionTimer = 50;
            timedTransition = true;
            transitionToState = StateTransition.GAME;

        } else if (quitButton.hasClick()) {
            quitButton.unClick();
            transitionTimer = 50;
            timedTransition = true;
            transitionToState = StateTransition.CREDITS;
        }

        if (transitionTimer > 0) {
            transitionTimer--;
        }

        if (transitionTimer == 0 && timedTransition) {
            timedTransition = false;
            transitionTriggered = true;
        }
        playButton.update();
        quitButton.update();
        banner.update();
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        playButton.paint(bufferedGraphics);
        quitButton.paint(bufferedGraphics);
        banner.paint(bufferedGraphics);

        g.drawImage(imageBuffer, 0, 0, width, height, null);
    }
}
