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

    private PuzzleManager puzzleManager; // Used to add generated puzzles to unsolvedPuzzles queue
    private int numberOfSolutions = 0;	// Used by backtrack to test for uniqueness. Visible at all levels of recursion

    // Bounding one aspect of the dificulty of the generated puzzles
    private int lowerBoundHints = 35;  // Theoretical lower bound is 17 hints
    private int upperBoundHints = 40;

    // Constructor
    public PuzzleGenerator(PuzzleManager puzzleManager) {
        this.puzzleManager = puzzleManager;
    }

    /**
     * 
     */
    public void run() {

        while (true) {
            try {
                Thread.sleep((int) (1000));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            if (puzzleManager.unsolvedPuzzles.size() < 10) {
                Puzzle newPuzzle = generatePuzzle();
                puzzleManager.unsolvedPuzzles.add(newPuzzle);
                puzzleManager.savePuzzles(puzzleManager.unsolvedPuzzlesFile, puzzleManager.unsolvedPuzzles);
            }
        }
    }

    /**
     * 
     * @return 
     */
    private Puzzle generatePuzzle() {
        Puzzle newPuzzle;

        int[] puzzleArray = new int[81];
        boolean[] maskArray = new boolean[81];
        int hints = 81;

        // Generate puzzles and masks until we get one at right dificulty
        while (hints >= upperBoundHints || hints <= lowerBoundHints) {

            // clear arrays
            for (int i = 0; i < 81; i++) {
                puzzleArray[i] = 0;
                maskArray[i] = false;
            }

            solve(puzzleArray, 0); // Fills puzzleArray
            maskArray = getUniqueMask(puzzleArray);

            hints = countHints(maskArray); // is puzzle hard enough?	

        }

        newPuzzle = new Puzzle(puzzleArrayToString(puzzleArray), maskArrayToString(maskArray));
        return newPuzzle;
    }

    // ---------------------- Getting Unique Mask ---------------------- //
    
    /**
     * Masks elements of the puzzle until just before puzzle stops having unique solution
     * @param puzzleArray
     * @return 
     */
    private boolean[] getUniqueMask(int[] puzzleArray) {
        int[] disposablePuzzleArray = puzzleArray.clone(); // don't want to overwrite actual puzzle
        boolean[] maskArray = new boolean[81];	// maskArray to be returned
        for (int i = 0; i < 81; i++) {
            maskArray[i] = false;
        } // All false means no puzzle elements masked

        while (true) {
            // select random element for deletion
            int maskingCandidate = getMaskingCandidate(maskArray);

            // Store puzzle element in case it's deletion breaks uniqueness
            int puzzleElementForMasking = disposablePuzzleArray[maskingCandidate];

            // Delete element from puzzle
            disposablePuzzleArray[maskingCandidate] = 0;
            //for(int i : disposablePuzzleArray){ System.out.print(i); System.out.println(); }

            if (hasUniqueSolution(disposablePuzzleArray)) {
                // deleting this element does not break uniqueness
                maskArray[maskingCandidate] = true; // mask this element

            } else { // deletion did break uniqueness.
                // undo deletion and stop.
                disposablePuzzleArray[maskingCandidate] = puzzleElementForMasking;
                break;
            }

        }

        return maskArray;

    }


    /**
     * Sets up entry conditions for recursive backtrack function
     * calls recursive function to check how many solutions a puzzle/mask combo hasNextLine
     * returns false if puzzle does not have unique solution
     * @param puzzleArray
     * @return 
     */
    private boolean hasUniqueSolution(int[] puzzleArray) {
        this.numberOfSolutions = 0;
        solveMultiple(puzzleArray, 0);
        if (numberOfSolutions == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Recursive backtrack function searches for solutions to puzzle.
     * stops looking when it finds 2 solutions. ie not a unique solution.
     * updates numberOfSolutions instance variable used in hasUniqueSolution() above
     * @param puzzleArray
     * @param index
     * @return 
     */
    private boolean solveMultiple(int[] puzzleArray, int index) {

        // checking for no more empty cells.
        // reached end of puzzle and no empties
        if (index == 80 && puzzleArray[index] != 0) { // puzzle complete
            this.numberOfSolutions++;
            return false;
        }

        // Get index of next empty cell
        while (puzzleArray[index] != 0) {
            index++;	// find next empty cell
            if (index > 80) { // no more empty cells
                this.numberOfSolutions++;
                return false; // solution found but backtrack and look for more
            }
        }
        
        // at this point we have found an empty cell
        // index can be 80 here but only if 80 is empty cell
        int[] candidates = candidates(); // numbers 1 - 9 in random order

        for (int candidate : candidates) {

            if (index == 80) {
                if (isLegalMove(candidate, puzzleArray, index)) { // puzzle complete
                    this.numberOfSolutions++;
                    return false;
                } else { // not legal so try next candidate -- no recursion on 80
                    continue;
                }
            }

            // if legal move but not potentially more empty cells then try to recursively
            // try to solve puzzle using this candidate. 
            if (isLegalMove(candidate, puzzleArray, index)) {
                puzzleArray[index] = candidate; // try candidate

                // recursive step. can rest of puzzle be solved by using this candidate
                // in this empty cell.
                if (solveMultiple(puzzleArray, index + 1)) { // puzzle solves
                    return true;
                } else { // puzzle does not solve with this candidate
                    puzzleArray[index] = 0; // delete candidate from cell.

                }
                // If making the move resulted in a second solution
                // run straight back up the recursion stack
                if (this.numberOfSolutions > 1) {
                    return false;
                }
            }
            // try next candidate until none left
        }
        // No candidate allowed puzzle to be solved.
        // need to back track
        return false;
    }

    // ---------------- Getting Legal Completed Puzzle ----------------- //
    /**
     * returns true if it solves the puzzle
     * @param puzzleArray
     * @param index
     * @return 
     */
    private boolean solve(int[] puzzleArray, int index) {
        int[] candidates = candidates();

        for (int candidate : candidates) {

            // test for puzzle succesfully completed
            if (index == 80 && isLegalMove(candidate, puzzleArray, index)) {
                puzzleArray[index] = candidate;
                return true;
            }

            // if legal move but not terminating then try to recursively
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

    // -------------------- Testing Move Legality ---------------------- //
    /**
     * Checks if all rows, columns and boxes are legal
     *
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
     *
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
     *
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

    // --------------------- Auxilliary Functions ---------------------- //

    /**
     * Returns numbers 1 - 9 in random order (no replacement)
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
     * Returns random unmasked index
     * @param maskArray
     * @return 
     */
    private int getMaskingCandidate(boolean[] maskArray) {
        int candidate;
        while (true) {
            candidate = (int) (Math.random() * 81);
            System.out.println("candidate is: " + candidate);
            if (!maskArray[candidate]) {
                return candidate;
            }
        }
    }

 
    /**
     * Returns string form of puzzleArray
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
     * Returns string form of maskArray
     * @param maskArray
     * @return 
     */
    private String maskArrayToString(boolean[] maskArray) {
        StringBuilder string = new StringBuilder();
        if (maskArray[0]) {
            string.append(1);
        } else {
            string.append(0);
        }
        for (int i = 1; i < 81; i++) {
            string.append(" ");
            if (maskArray[i]) {
                string.append(1);
            } else {
                string.append(0);
            }
        }
        return string.toString();
    }

    /**
     * Gets the number of hints on the board
     * @param maskArray
     * @return 
     */
    private int countHints(boolean[] maskArray) {
        int hints = 0;
        for (int i = 0; i < 81; i++) {
            if (!maskArray[i]) {
                hints++;
            }
        }
        return hints;
    }

}
