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

/**
 *
 * @author melan
 */
public class GameState extends State {

    // Set up the model.
    private PuzzleManager puzzleManager = new PuzzleManager();
    private Puzzle puzzle;

    // Set up visual components
    private Button pauseButton;
    private Button menuButton;
    private Tile[] tiles = new Tile[81];	// All the number tiles (on board and off)
    private Tile tileInPlay = null;		// A tile that has been dragged or just dropped
    private TileBoard tileBoard;		// Represents the sudoku puzzle visually
    private TileStacks tilestacks;		// Number tiles the player can pick up and drop on the board
    private Clock timer;
    private Dialog1Q chooseDificultyDialogue;
    private Dialog1Q playAgainDialogue;

    // game states
    private enum GameStates {
        CHOOSE_DIFICULTY, SETUP, TILES_UNCLICKED, DRAGGING_TILE, MISS_BOARD, BOARD_REJECTS, BOARD_ACCEPTS, PLAY_AGAIN, EXIT
    }
    private GameStates gameState = GameStates.CHOOSE_DIFICULTY;

    // Initialize flags
    private boolean enteringState = true;
    private boolean showingSwingDialog = false;
    private boolean gameInPlay = true;
    private boolean gamePaused = false;
    private int gameSleepCounter = 0;
    private int transitionTimer = 0; // transition out to main menu
    private boolean timedTransition = false; // transition out to main menu

    public GameState() {
        super();

        addBackground("resources/blackBackground.jpg");

        puzzle = puzzleManager.getCurrentPuzzle();
        initializeVisualComponents();

        // set up dialogues
        chooseDificultyDialogue = new Dialog1Q(new Dimension((int) (width * 0.6), (int) (height * 0.6)), new Point((int) (width * 0.5), (int) (height * 0.5)),
                "Choose a dificulty:");
        chooseDificultyDialogue.addButton("Easy", "easy");
        chooseDificultyDialogue.addButton("Medium", "medium");
        chooseDificultyDialogue.addButton("Hard", "hard");
        addMouseListener(chooseDificultyDialogue);
        chooseDificultyDialogue.setVisible(false);
        chooseDificultyDialogue.setEnabled(false);

        playAgainDialogue = new Dialog1Q(new Dimension((int) (width * 0.6), (int) (height * 0.6)), new Point((int) (width * 0.5), (int) (height * 0.5)),
                "PLay Again?:");
        playAgainDialogue.addButton("Yes", "yes");
        playAgainDialogue.addButton("No", "no");
        addMouseListener(playAgainDialogue);
        playAgainDialogue.setVisible(false);
        playAgainDialogue.setEnabled(false);

        pauseButton = new Button((int) (0.25 * width), new Point((int) (0.9 * width), (int) (0.75 * height)), 0, "Pause", Button.ButtonType.MENU);
        addMouseListener(pauseButton);

        menuButton = new Button((int) (0.25 * width), new Point((int) (0.9 * width), (int) (0.85 * height)), 0, "Menu", Button.ButtonType.MENU);
        addMouseListener(menuButton);

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        tileBoard.paint(bufferedGraphics);
        tilestacks.paint(bufferedGraphics);
        pauseButton.paint(bufferedGraphics);
        menuButton.paint(bufferedGraphics);

        for (int i = 0; i < tiles.length; i++) {
            tiles[i].paint(bufferedGraphics);
        }

        if (tileBoard.isBoardHidden()) {
            tileBoard.paint(bufferedGraphics);
        }

        if (tileInPlay != null) {
            tileInPlay.paint(bufferedGraphics);
        }

        timer.paint(bufferedGraphics);
        chooseDificultyDialogue.paint(bufferedGraphics);
        playAgainDialogue.paint(bufferedGraphics);

        if (screenMessageCounter > 0) {
            bufferedGraphics.setColor(Color.YELLOW);
            bufferedGraphics.setFont(new Font("TimesRoman", Font.BOLD, 30));
            bufferedGraphics.drawString(screenMessage, (int) ((width - screenMessageLength) / 2), (int) (height / 2));
        }
        g.drawImage(imageBuffer, 0, 0, width, height, null);
    }

    @Override
    public void update() {
        super.update();
        if (animationCounter == 10) {
            sounds.changeBackgroundSound("resources/sounds/mandown.wav");
        }

        // back to menu
        if (menuButton.hasClick()) {
            menuButton.unClick();
            animationCounter = 0;
            transitionToState = StateTransition.MENU;
            transitionTriggered = true;
        } else if (pauseButton.hasClick()) {
            pauseButton.unClick();
            if (timer.isRunning()) {
                timer.stop();
                tileBoard.hideBoard(true);
                sounds.pauseBackgroundSound();
                gamePaused = true;
            } else {
                timer.start();
                tileBoard.hideBoard(false);
                sounds.unpauseBackgroundSound();
                gamePaused = false;
            }

        }
        if (gameSleepCounter > 0) {
            gameSleepCounter--;
        } else {
            ////////////////////////////////////////////////////////////////////
            // GameState Logic
            if (gameInPlay) {
                switch (gameState) {
                    case CHOOSE_DIFICULTY:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            chooseDificultyDialogue.setVisible(true);
                            chooseDificultyDialogue.setEnabled(true);

                        } // else already in state

                        // upon state transition
                        if (chooseDificultyDialogue.getHasAnswer()) {
                            chooseDificultyDialogue.getAnswer();
                            chooseDificultyDialogue.setVisible(false);
                            chooseDificultyDialogue.setEnabled(false);

                            gameState = GameStates.SETUP;
                            enteringState = true;
                        }

                        break;
                    case SETUP:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            puzzle = puzzleManager.getCurrentPuzzle();
                            initializeVisualComponents();
                            tilestacks.unlockTiles();
                            tileBoard.hideBoard(false);
                            timer.start();

                        } // else already in state

                        // upon state transition
                        gameState = GameStates.TILES_UNCLICKED;
                        enteringState = true;

                        break;
                    case TILES_UNCLICKED:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            //writeToScreen("tilesunclicked", 100);
                            tilestacks.lockTiles();
                            tilestacks.unlockTiles();

                        } // else already in state
                        // listen for tile being dragged
                        for (int i = 0; i < tiles.length; i++) {
                            if (tiles[i].getIsDragging()) {
                                tileInPlay = tiles[i];
                                tilestacks.popTile(tileInPlay);
                                tilestacks.lockTiles();

                                // upon state transition
                                gameState = GameStates.DRAGGING_TILE;
                                enteringState = true;
                            }
                        }

                        break;
                    case DRAGGING_TILE:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            //writeToScreen("dragging", 100);

                        } // else already in state
                        // Check for tile dropped.
                        if (tileInPlay.isDropped()) {
                            tileBoard.tileDropped(tileInPlay); // this generates a move if on board
                            tileInPlay.setDropped(false);
                            // check if dropped on board
                            if (!tileBoard.hasMove()) { // not on board
                                tilestacks.pushTile(tileInPlay);
                                tileInPlay.snapHome();
                                gameState = GameStates.MISS_BOARD;
                            } else { // tile was dropped on board
                                Move theMove = tileBoard.getMove();
                                if (puzzle.makeMove(theMove)) { // move is good!!
                                    tileBoard.acceptMove(theMove, tileInPlay);
                                    tileInPlay.snapHome();
                                    gameState = GameStates.BOARD_ACCEPTS;
                                } else { // move is bad
                                    tilestacks.pushTile(tileInPlay);
                                    tileInPlay.snapHome();
                                    gameState = GameStates.BOARD_REJECTS;
                                }
                            }
                            //tileInPlay = null;
                            enteringState = true;
                            //tileBoard.tileDropped(tiles[i]);
                        }

                        break;
                    case MISS_BOARD:
                        if (enteringState) {
                            enteringState = false;
                            sounds.playSound("tilehome", 1);
                            // Entering state (setup)
                            //writeToScreen("miss", 100);

                        } // else already in state
                        if (tileInPlay.getHome().equals(tileInPlay.getPosition())) {
                            tileInPlay = null;
                            gameState = GameStates.TILES_UNCLICKED;
                            enteringState = true;
                        }

                        break;
                    case BOARD_REJECTS:
                        if (enteringState) {
                            enteringState = false;
                            sounds.playSound("tilehome", 1);
                            // Entering state (setup)
                            writeToScreen("Wrong Guess: 10s Penalty!", 100);
                            timer.timePenalty(10);

                        } // else already in state

                        // upon state transition
                        if (tileInPlay.getHome().equals(tileInPlay.getPosition())) {
                            tileInPlay = null;
                            gameState = GameStates.TILES_UNCLICKED;
                            enteringState = true;

                        }

                        break;
                    case BOARD_ACCEPTS:
                        if (enteringState) {
                            enteringState = false;
                            //System.out.println("what da fugicle??");
                            sounds.playSound("tileaccepted", 1);
                            // Entering state (setup)
                            //writeToScreen("accept", 100);

                        } // else already in state
                        if (tileInPlay.getHome().equals(tileInPlay.getPosition())) {
                            tileInPlay = null;
                            // has game been won?
                            if (puzzle.isPuzzleSolved()) { // Game is won!!
                                gameState = GameStates.PLAY_AGAIN;
                                enteringState = true;
                            } else { // Game still playing.
                                gameState = GameStates.TILES_UNCLICKED;
                                enteringState = true;
                            }

                        }

                        break;
                    case PLAY_AGAIN:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            writeToScreen("YOU WIN!!!", 100);
                            timer.stop();
                            //System.out.println("has answer?" + playAgainDialogue.getHasAnswer());
                            playAgainDialogue.setVisible(true);
                            playAgainDialogue.setEnabled(true);
                            playAgainDialogue.getAnswer();
                            puzzleManager.markPuzzleComplete();
                            puzzleManager.loadNextPuzzle();
                        } // else already in state
                        if (playAgainDialogue.getHasAnswer()) {

                            String play = playAgainDialogue.getAnswer();
                            System.out.println("dialogue says: " + play);
                            playAgainDialogue.setVisible(false);
                            playAgainDialogue.setEnabled(false);

                            if (play.equals("yes")) {
                                gameState = GameStates.SETUP;
                                enteringState = true;
                            } else {
                                gameState = GameStates.EXIT;
                                enteringState = true;
                            }

                        }

                        break;
                    case EXIT:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)

                        } // else already in state
                        // Set up for next game
                        enteringState = true;
                        gameState = GameStates.CHOOSE_DIFICULTY;
                        // Game over - back to menu
                        animationCounter = 0;
                        transitionToState = StateTransition.MENU;
                        transitionTriggered = true;

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
            tilestacks.update();
            tileBoard.update();
            timer.update();
            chooseDificultyDialogue.update();
            playAgainDialogue.update();

            for (int i = 0; i < tiles.length; i++) {
                tiles[i].update();
            }

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

    public void initializeVisualComponents() {
        puzzle = puzzleManager.getCurrentPuzzle();
        tiles = new Tile[81];
        tileBoard = new TileBoard(new Dimension((int) (0.72 * height), (int) (0.72 * height)), new Point((int) (0.4 * height), (int) (0.4 * height)));
        tilestacks = new TileStacks(new Dimension((int) (width * 0.6), (int) (height * 0.2)), new Point((int) (width * 0.3), (int) (height * 0.85)));
        timer = new Clock(new Dimension((int) (width * 0.3), (int) (width * 0.05)), new Point((int) (0.8 * width), (int) (0.2 * height)), 0);

        //creating tiles
        for (int i = 0; i < 81; i++) {
            if (puzzle.maskedElementAtIndex(i) != 0) {
                tiles[i] = new Tile(new Dimension((int) (height * 0.075), (int) (height * 0.075)),
                        tileBoard.anchorAtIndex(i),
                        puzzle.elementAtIndex(i));
                tiles[i].setIsBoardTile(true);
            } else {
                tiles[i] = new Tile(new Dimension((int) (height * 0.075), (int) (height * 0.075)),
                        tilestacks.anchorAtIndex(puzzle.elementAtIndex(i) - 1),
                        puzzle.elementAtIndex(i));
                tilestacks.pushTile(tiles[i]);
                addMouseListener(tiles[i]);
                addMouseMotionListener(tiles[i]);
            }
        }
        tilestacks.lockTiles();
        //tilestacks.unlockTiles();
        tileBoard.hideBoard(true);
    }

    public void sleep(int numOfFrames) {
        gameSleepCounter = numOfFrames;
    }

}
