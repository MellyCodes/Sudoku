/**
* @file GameState.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Melanie Roy-Plommer
 */
public class GameState extends State {

    //Set up the model
    private PuzzleManager puzzleManager = new PuzzleManager();
    private Puzzle puzzle;

    // Set up visual components
    private Button pauseButton;
    private Button menuButton;
    private Tile[] tiles = new Tile[81];    // All the tiles, whether on the board or not
    private Tile tileInPlay = null;         // A Tile that has been dragged or just dropped
    private TileBoard tileBoard;            // Represents the Sudoku puzzle visually
    private TileStacks tileStacks;          // Number of tiles the player can pick up and drop on the board

    // game states
    private enum GameStates {
        CHOOSE_DIFICULTY, SETUP, TILES_UNCLICKED, DRAGGING_TILE, MISS_BOARD, BOARD_REJECTS, BOARD_ACCEPTS, PLAY_AGAIN, EXIT
    }
    private GameStates gameState = GameStates.CHOOSE_DIFICULTY;

    // Initialize flags
    private boolean enteringState = true;
    private boolean showingSwingDialog = false;
    private boolean gameInPlay = true;
    private int gameSleepCounter = 0;

    // transition out to main menu
    private int transitionTimer = 0;
    private boolean timedTransition = false;


    public GameState() {
        super();

        addBackground("resources/blackBackground.jpg");

        puzzle = puzzleManager.getCurrentPuzzle();
        initializeVisualComponents();

        pauseButton = new Button((int) (0.25 * width), new Point((int) (0.9 * width), (int) (0.75 * height)), 0, "Pause", Button.ButtonType.MENU);
        addMouseListener(pauseButton);

        menuButton = new Button((int) (0.25 * width), new Point((int) (0.9 * width), (int) (0.85 * height)), 0, "Menu", Button.ButtonType.MENU);
        addMouseListener(menuButton);

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        tileBoard.paint(bufferedGraphics);
        tileStacks.paint(bufferedGraphics);
        pauseButton.paint(bufferedGraphics);
        menuButton.paint(bufferedGraphics);

        for (Tile tile : tiles) {
            tile.paint(bufferedGraphics);
        }

        if (tileInPlay != null) {
            tileInPlay.paint(bufferedGraphics);
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
                switch (gameState) {
                    case CHOOSE_DIFICULTY:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            writeToScreen("Entering choose dificulty state", 100);
                            sleep(150);
                        } // else already in state

                        // upon state transition
                        gameState = GameStates.SETUP;
                        enteringState = true;
                        break;
                    case SETUP:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            writeToScreen("setup", 100);
                            puzzle = puzzleManager.getCurrentPuzzle();
                            initializeVisualComponents();

                        } // else already in state

                        // upon state transition
                        gameState = GameStates.TILES_UNCLICKED;
                        enteringState = true;

                        break;
                    case TILES_UNCLICKED:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            writeToScreen("tilesunclicked", 100);
                            tileStacks.lockTiles();
                            tileStacks.unlockTiles();

                        } // else already in state
                // listen for tile being dragged
                for (Tile tile : tiles) {
                    if (tile.getIsDragging()) {
                        tileInPlay = tile;
                        tileStacks.popTile(tileInPlay);
                        tileStacks.lockTiles();
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
                            writeToScreen("dragging", 100);

                        } // else already in state
                        // Check for tile dropped.
                        if (tileInPlay.isDropped()) {
                            tileBoard.tileDropped(tileInPlay); // this generates a move if on board
                            tileInPlay.setDropped(false);
                            // check if dropped on board
                            if (!tileBoard.hasMove()) { // not on board
                                tileStacks.pushTile(tileInPlay);
                                tileInPlay.snapHome();
                                gameState = GameStates.MISS_BOARD;
                            } else { // tile was dropped on board
                                Move theMove = tileBoard.getMove();
                                System.out.println(theMove.getIndex() + " , " + theMove.getValue());
                                if (puzzle.makeMove(theMove)) { // move is good!!
                                    tileBoard.acceptMove(theMove, tileInPlay);
                                    tileInPlay.snapHome();
                                    gameState = GameStates.BOARD_ACCEPTS;
                                } else { // move is bad
                                    tileStacks.pushTile(tileInPlay);
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
                            // Entering state (setup)
                            writeToScreen("miss", 100);

                        } // Checks if the tile has reached its home position yet
                        if (tileInPlay.getHome().equals(tileInPlay.getPosition())) {
                            tileInPlay = null;
                            gameState = GameStates.TILES_UNCLICKED;
                            enteringState = true;
                        }

                        break;
                    case BOARD_REJECTS:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            writeToScreen("reject", 100);

                        } // else already in state

                        // Checks if the tile has reached its home position yet
                        if (tileInPlay.getHome().equals(tileInPlay.getPosition())) {
                            tileInPlay = null;
                            gameState = GameStates.TILES_UNCLICKED;
                            enteringState = true;
                        }
                        break;
                    case BOARD_ACCEPTS:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            writeToScreen("accept", 100);
                        } 
                        // Checks if the tile has reached its home position yet
                        // In this case, the home position has already been changed in TileBoard to the correct square
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
                            writeToScreen("YOU WIN!!!.. PLAY_AGAIN?", 100);
                            sleep(150);

                        } // else already in state

                        // Exit for now just remains in the current state
                        // Will transition to main menu in the future
                        gameState = GameStates.EXIT;
                        enteringState = true;

                        break;
                    case EXIT:
                        if (enteringState) {
                            enteringState = false;
                            // Entering state (setup)
                            writeToScreen("EXIT", 100);
                            sleep(150);
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
            tileStacks.update();
            tileBoard.update();

            for (Tile tile : tiles) {
                tile.update();
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

    public void initializeVisualComponents() {
        tileBoard = new TileBoard(new Dimension((int) (0.72 * height), (int) (0.72 * height)), new Point((int) (0.4 * height), (int) (0.4 * height)));
        tileStacks = new TileStacks(new Dimension((int) (width * 0.6), (int) (height * 0.2)), new Point((int) (width * 0.3), (int) (height * 0.85)));

        //creating tiles
        for (int i = 0; i < 81; i++) {
            if (puzzle.maskedElementAtIndex(i) != 0) {
                tiles[i] = new Tile(new Dimension((int) (height * 0.08), (int) (height * 0.08)),
                        tileBoard.anchorAtIndex(i),
                        puzzle.maskedElementAtIndex(i));
            } else {
                tiles[i] = new Tile(new Dimension((int) (height * 0.08), (int) (height * 0.08)),
                        tileStacks.anchorAtIndex(puzzle.elementAtIndex(i) - 1),
                        puzzle.elementAtIndex(i));
                tileStacks.pushTile(tiles[i]);
                addMouseListener(tiles[i]);
                addMouseMotionListener(tiles[i]);
            }
        }
        tileStacks.lockTiles();
        tileStacks.unlockTiles();
    }

    public void sleep(int numOfFrames) {
        gameSleepCounter = numOfFrames;
    }

}
