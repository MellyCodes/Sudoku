/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author melan
 */
public class GameState extends State {

    //private Image background;
    //private ImageEntity banner;
    private Button pauseButton;
    private Button menuButton;

    private Tile[] tiles = new Tile[81];

    private Puzzle puzzle;

    private TileBoard tileBoard;

    // game states
    private enum GameStates {
        ENTER_GAME, MAIN_OPTIONS, END_ROUND
    }
    private GameStates gameStates = GameStates.ENTER_GAME;
    private boolean enteringState = true;
    private boolean showingSwingDialog = false;
    private String guess = null;
    private boolean showingPlayerNameDialog = false;
    private String chosenPlayerName = null;
    private boolean gameInPlay = true;
    private int gameSleepCounter = 0;

    // transition out to main menu
    private int transitionTimer = 0;
    private boolean timedTransition = false;

    public GameState() {
        super();

        addBackground("resources/Blue-Background.jpg");

        String puzzleString = "2 4 8 5 3 9 6 7 1 6 7 5 4 1 2 8 9 3 3 9 1 6 7 8 2 4 5 7 1 2 8 6 3 9 5 4 4 3 6 1 9 5 7 8 2 5 8 9 2 4 7 1 3 6 8 2 4 9 5 6 3 1 7 9 5 7 3 2 1 4 6 8 1 6 3 7 8 4 5 2 9";
        String maskString = "1 1 0 0 0 1 1 1 1 1 1 0 1 0 0 1 1 1 0 0 0 1 1 1 1 1 1 0 1 1 1 1 0 1 0 1 0 0 1 1 1 1 1 0 0 1 0 1 0 1 1 1 1 0 1 1 1 1 1 1 0 0 0 1 1 1 0 0 1 0 1 1 1 1 1 1 0 0 0 1 1";

        puzzle = new Puzzle(puzzleString, maskString);

        tileBoard = new TileBoard(new Dimension((int) (0.72 * height), (int) (0.72 * height)), new Point((int) (0.4 * height), (int) (0.4 * height)));

        //creating tiles
        for (int i = 0; i < 81; i++) {
            if (puzzle.elementAtIndex(i) != 0) {
                tiles[i] = new Tile(new Dimension((int) (height * 0.08), (int) (height * 0.08)),
                        tileBoard.anchorAtIndex(i),
                        puzzle.elementAtIndex(i));
            } else {

                tiles[i] = new Tile(new Dimension((int) (height * 0.08), (int) (height * 0.08)),
                        new Point((int) (width / 2), (int) (height / 2)),
                        9);
                addMouseListener(tiles[i]);
                addMouseMotionListener(tiles[i]);
            }
        }

        pauseButton = new Button((int) (0.25 * width), new Point((int) (0.9 * width), (int) (0.75 * height)), 0, "Pause", Button.ButtonType.MENU);
        addMouseListener(pauseButton);

        menuButton = new Button((int) (0.25 * width), new Point((int) (0.9 * width), (int) (0.85 * height)), 0, "Menu", Button.ButtonType.MENU);
        addMouseListener(menuButton);

        int xOffset = (int) (0.01 * height);
        //int xStart = (int)();

        // create and place tiles
//        for (int i = 0; i < tiles.length; i++) {
//            tiles[i] = new Tile(new Dimension((int) (height * 0.08), (int) (height * 0.08)), new Point((int) ((height * 0.11) + i * (height * 0.11)), (int) (height * 0.85)), i + 1);
//            addMouseListener(tiles[i]);
//            addMouseMotionListener(tiles[i]);
//
//        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        tileBoard.paint(bufferedGraphics);
        //banner.paint(bufferedGraphics);
        pauseButton.paint(bufferedGraphics);
        menuButton.paint(bufferedGraphics);

        for (int i = 0; i < tiles.length; i++) {
            tiles[i].paint(bufferedGraphics);
        }

        if (screenMessageCounter > 0) {
            bufferedGraphics.setColor(Color.YELLOW);
            bufferedGraphics.setFont(new Font("TimesRoman", Font.BOLD, 60));
            bufferedGraphics.drawString(screenMessage, (int) ((width - screenMessageLength) / 2), (int) (height / 2));
        }
        g.drawImage(imageBuffer, 0, 0, width, height, null);
    }

    @Override
    public void update() {
        super.update();

        // back to menu
        if (menuButton.hasClick()) {
            menuButton.unClick();
            transitionToState = StateTransition.MENU;
            transitionTriggered = true;
        } else if (pauseButton.hasClick()) {
            pauseButton.unClick();
            transitionToState = StateTransition.OPTIONS;
            transitionTriggered = true;
        }
        if (gameSleepCounter > 0) {
            gameSleepCounter--;
        } else {
            ////////////////////////////////////////////////////////////////////
            // GameState Logic
            if (gameInPlay) {
                switch (gameStates) {
                    case ENTER_GAME:
                        newGame();
                        enteringState = true;
                        break;
                    case MAIN_OPTIONS:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            enteringState = true;
                        }
                        break;
                    case END_ROUND:
                        // Entering state (setup)
                        if (enteringState) {
                            enteringState = false;
                        }
                        break;
                    default:
                        writeToScreen("unknown error", 100);
                        break;
                }
            }
            // End GameState Logoc
            /////////////////////////////////////////////////////////////////////
            //update GUI components

            //banner.update();
            pauseButton.update();
            menuButton.update();

            // Collision detection - was the tile dropped
            for (int i = 0; i < tiles.length; i++) {

                if (tiles[i].isDropped()) {
                    tiles[i].setDropped(false);
                    tileBoard.tileDropped(tiles[i]);
                }
            }

            for (int i = 0; i < tiles.length; i++) {
                tiles[i].update();
            }

            tileBoard.update();

            //transition delay for exiting to menu
            if (transitionTimer > 0) {
                transitionTimer--;
            }

            if (transitionTimer == 0 && timedTransition) {
                gameInPlay = true;
                timedTransition = false;
                transitionTriggered = true;
            }
        }
    }

    public void sleep(int numOfFrames) {
        gameSleepCounter = numOfFrames;
    }

    private void newGame() {
        menuButton = new Button((int) (0.25 * width), new Point((int) (0.9 * width), (int) (0.85 * height)), 0, "Menu", Button.ButtonType.MENU);
        addMouseListener(menuButton);
    }
}
