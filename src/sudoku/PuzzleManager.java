/**
 * @file PuzzleManager.java
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

import java.util.LinkedList;
import java.util.Scanner;
import java.net.URL;
import java.net.URISyntaxException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

class PuzzleManager {

    volatile LinkedList<Puzzle> unsolvedPuzzles = new LinkedList<Puzzle>(); // Package-Private
    private LinkedList<Puzzle> solvedPuzzles = new LinkedList<Puzzle>();
    private Puzzle currentPuzzle = null;

    private File solvedPuzzlesFile;
    File unsolvedPuzzlesFile; // Package-Private	

    private PuzzleGenerator puzzleGenerator;

    public PuzzleManager() {
        // Get the files as file objects
        URL pathToSolvedPuzzleFile = getClass().getResource("./solvedPuzzles.txt");
        URL pathToUnolvedPuzzleFile = getClass().getResource("./unsolvedPuzzles.txt");
        try {
            unsolvedPuzzlesFile = new File(pathToUnolvedPuzzleFile.toURI());
            solvedPuzzlesFile = new File(pathToSolvedPuzzleFile.toURI());
        } catch (URISyntaxException URISE) {
            URISE.printStackTrace();
        }

        // Load puzzle ArrayLists
        loadPuzzles(unsolvedPuzzlesFile, unsolvedPuzzles);
        loadPuzzles(solvedPuzzlesFile, solvedPuzzles);

        loadNextPuzzle();

        puzzleGenerator = new PuzzleGenerator(this);
        puzzleGenerator.start();
    }

    /**
     *
     * @return
     */
    public Puzzle getCurrentPuzzle() {
        if (currentPuzzle == null) {
            loadNextPuzzle();
        }
        return currentPuzzle;
    }

    /**
     *
     */
    public void markPuzzleComplete() {
        if (currentPuzzle != null) {
            solvedPuzzles.add(currentPuzzle);
            unsolvedPuzzles.poll();
            currentPuzzle = null;
        }

        // Save the changes so that the file data matches the object stack data
        savePuzzles(solvedPuzzlesFile, solvedPuzzles);
        savePuzzles(unsolvedPuzzlesFile, unsolvedPuzzles);
    }

    /**
     *
     */
    private void loadNextPuzzle() {
        if (unsolvedPuzzles.peek() != null) {
            currentPuzzle = unsolvedPuzzles.peek();
        } else {
            currentPuzzle = null;
        }

    }

    /**
     *
     * @param puzzlesFile
     * @param puzzlesStack
     */
    private void loadPuzzles(File puzzlesFile, LinkedList<Puzzle> puzzlesStack) {
        System.out.println("trying to load puzzles");
        try {
            Scanner fileScanner = new Scanner(puzzlesFile);
            while (true) {
                if (!fileScanner.hasNextLine()) {
                    break;
                }

                String puzzleString = fileScanner.nextLine();
                System.out.println("Puzzle String in loading: " + puzzleString);
                String maskString = fileScanner.nextLine();
                System.out.println("mask String in loading: " + maskString);
                puzzlesStack.add(new Puzzle(puzzleString, maskString));
            }
            fileScanner.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     *
     * @param puzzlesFile
     * @param puzzlesStack
     */
    void savePuzzles(File puzzlesFile, LinkedList<Puzzle> puzzlesStack) { // Package-Private
        System.out.println("trying to save puzzles");
        try {

            PrintWriter printWriter = new PrintWriter(puzzlesFile);
            popPrintPush(puzzlesStack, printWriter);
            printWriter.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Recursively prints puzzles from the stack to file
     *
     * @param puzzlesStack
     * @param printWriter
     */
    private void popPrintPush(LinkedList<Puzzle> puzzlesStack, PrintWriter printWriter) {
        // Terminating Case
        if (puzzlesStack.peek() == null) {
            return;
        }
        // else
        // get the next puzzle
        Puzzle puzzleToPrint = puzzlesStack.poll();

        // Print the puzzle to file
        //try {
        printWriter.println(puzzleToPrint.getPuzzleString());
        printWriter.println(puzzleToPrint.getMaskString());
        //printWriter.println();

        //}
        //catch (IOException ioe){ ioe.printStackTrace(); }
        // Print the rest of the stack
        popPrintPush(puzzlesStack, printWriter);

        // Push puzzle back onto stack
        puzzlesStack.add(puzzleToPrint);
    }

}
