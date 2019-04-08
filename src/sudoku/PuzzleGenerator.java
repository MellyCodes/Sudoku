/**
 * @file PuzzleGenerator.java
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

public class PuzzleGenerator extends Thread {

    private PuzzleManager puzzleManager;

    //private String puzzleString = "5 3 9 8 2 4 6 7 1 6 1 2 3 7 9 5 4 8 4 7 8 1 5 6 9 2 3 7 4 1 2 6 5 3 8 9 3 9 6 4 8 1 7 5 2 8 2 5 7 9 3 1 6 4 2 5 3 6 1 8 4 9 7 9 8 4 5 3 7 2 1 6 1 6 7 9 4 2 8 3 5";
    private String maskString = "0 0 1 1 1 0 1 1 1 0 1 0 1 1 1 1 1 1 1 0 0 0 0 1 1 1 1 1 1 0 0 1 1 1 1 0 1 1 0 1 0 1 0 1 1 0 1 1 1 1 0 0 1 1 1 1 1 1 0 0 0 0 1 1 1 1 1 1 1 0 1 0 1 1 1 0 1 1 1 0 0";

    // Created by the PuzzleManager and the PuzzleManager hands a reference to itself to the PuzzleGenerator
    public PuzzleGenerator(PuzzleManager puzzleManager) {
        this.puzzleManager = puzzleManager;
    }

    /**
     * 
     */
    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep((int) (1000));
            } catch (InterruptedException ie) {
                ie.printStackTrace(System.out);
            }
            //System.out.println("size of puzzlemanager" + puzzleManager.unsolvedPuzzles.size());
            if (puzzleManager.unsolvedPuzzles.size() < 10) {
                //System.out.println("Trying to generate puzzle");
                Puzzle newPuzzle = generatePuzzle();
                puzzleManager.unsolvedPuzzles.add(newPuzzle);
                puzzleManager.savePuzzles(puzzleManager.unsolvedPuzzlesFile, puzzleManager.unsolvedPuzzles);
            }
        }
    }

    /**
     * Creates a new legal Sudoku board.
     * Mask is set to ensure unique solution
     * @return
     */
    private Puzzle generatePuzzle() {
        Puzzle newPuzzle;

        int[] puzzleArray = new int[81];
        // Setting all elements to 0
        for (int i = 0; i < 81; i++) {
            puzzleArray[i] = 0;
        }
        
        // Array is filled in solve
        solve(puzzleArray, 0);

        newPuzzle = new Puzzle(puzzleArrayToString(puzzleArray), maskString);

        return newPuzzle;
    }

    /**
     * Recursively fills the elements of the array to produce a legal Sudoku
     * 
     * @param puzzleArray
     * @param index
     * @return true if it solves the puzzle
     */
    private boolean solve(int[] puzzleArray, int index) {
        int[] candidates = candidates();  

        // Iterating through candidated gives random number without replacement
        for (int candidate : candidates) {

            // test for puzzle succesfully completed
            if (index == 80 && isLegalMove(candidate, puzzleArray, index)) {
                puzzleArray[index] = candidate;
                return true;
            }

            // if legal move but not termnating then try to recursively
            // solve puzzle using this candidate. 
            if (isLegalMove(candidate, puzzleArray, index)) {
                puzzleArray[index] = candidate; // try candidate
                if (solve(puzzleArray, index + 1)) { // puzzle solves
                    return true;
                } else { // puzzle does not solve with this candidate
                    puzzleArray[index] = 0;
                }
            }
        }
        // No candidate allowed puzzle to be solved.
        // need to back track
        return false;
    }

    /**
     * Numbers 1 to 9 in random order
     * @return
     */
    private int[] candidates() {
        int[] candidates = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < 20; i++) { // 20 shuffles
            int index1 = (int) (Math.random() * 9);
            int index2 = (int) (Math.random() * 9);
            // Perform swap
            int temp = candidates[index1];
            candidates[index1] = candidates[index2];
            candidates[index2] = temp;
        }
        return candidates;
    }

    /**
     *
     * @param puzzleArray
     * @return
     */
    private String puzzleArrayToString(int[] puzzleArray) {
        StringBuilder string = new StringBuilder();
        string.append(puzzleArray[0]);
        for (int i = 1; i < 81; i++) {
            string.append(" " + puzzleArray[i]);
        }
        return string.toString();
    }

    /**
     * Checks if all rows, columns and boxes are legal
     * @param candidate
     * @param puzzleArray
     * @param index
     * @return true if all are legal, false if any one of them fails
     */
    private boolean isLegalMove(int candidate, int[] puzzleArray, int index) {
        if (isLegalMoveRow(candidate, puzzleArray, index)
                && isLegalMoveColumn(candidate, puzzleArray, index)
                && isLegalMoveBox(candidate, puzzleArray, index)) {
            return true;
        }
        return false;
        //return true;
    }

    /**
     * Checks if the row already contains the candidate
     * @param candidate
     * @param puzzleArray
     * @param index
     * @return false if it contains the candidate
     */
    private boolean isLegalMoveRow(int candidate, int[] puzzleArray, int index) {
        
        // Gets the index of the first element of the row
        int baseIndex = 9 * (int) (index / 9);

        for (int i = baseIndex; i < baseIndex + 9; i++) {
            if (puzzleArray[i] == candidate) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the column already contains the candidate
     * @param candidate
     * @param puzzleArray
     * @param index
     * @return false if it contains the candidate
     */
    private boolean isLegalMoveColumn(int candidate, int[] puzzleArray, int index) {
        
        // Gets how many elements into the row to be able to 
        // check the numbers below and above it
        int columnOffset = (int) (index % 9);

        for (int i = 0; i < 9; i++) {
            // Get the starting index of the row + the offset gives 
            // you the cell you want to check, and checks it against the candidate
            // If it matches it returns false
            if (puzzleArray[i * 9 + columnOffset] == candidate) {
                return false;
            }
        }
        // If it makes it all the way through without matching, the candidate is good
        return true;
    }

    /**
     * Checks if the box already contains the candidate
     * @param candidate
     * @param puzzleArray
     * @param index
     * @return false if it contains the candidate
     */
    private boolean isLegalMoveBox(int candidate, int[] puzzleArray, int index) {
        
        // Combining the logic of rows and columns
        int columnOffset = (int) (index % 9); // Which column are we in?
        int rowOffset = (int) (index / 9); // Which row are we in?

        // Gets the index of the starting column of the current box
        int boxColumnOffset = (int) (columnOffset / 3) * 3;
        // Gets the index of the starting row of the current box
        int boxRowOffset = (int) (rowOffset / 3) * 3;

        // Cycles through the box and checks for matching candidates
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (puzzleArray[9 * (boxRowOffset + row) + boxColumnOffset + col] == candidate) {
                    // If any matches, it is not a legal box
                    return false;
                }
            }
        }
        return true;
    }

}
