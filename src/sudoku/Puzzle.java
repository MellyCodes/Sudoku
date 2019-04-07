/**
 * @file Puzzle.java
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

import java.util.Scanner;

public class Puzzle {

    private String puzzleString;
    private String maskString;
    public int[] puzzle = new int[81];
    public boolean[] mask = new boolean[81];

    public Puzzle(String puzzle, String mask) {
        this.puzzleString = puzzle;
        this.maskString = mask;
        setPuzzle(puzzle);
        setMask(mask);

    }

    /**
     *
     * @param index
     * @return
     */
    public int maskedElementAtIndex(int index) {
        if (!mask[index]) {
            return puzzle[index];
        }
        return 0;
    }

    /**
     *
     * @param index
     * @return
     */
    public int elementAtIndex(int index) {
        return puzzle[index];
    }

    /**
     *
     * @param puzzle
     */
    private void setPuzzle(String puzzle) {
        Scanner line = new Scanner(puzzle);
        int puzzleIndex = 0;
        while (line.hasNextInt()) {
            this.puzzle[puzzleIndex++] = line.nextInt();
        }
        line.close();
    }

    /**
     *
     * @param mask
     */
    private void setMask(String mask) {
        Scanner line = new Scanner(mask);
        int maskIndex = 0;
        while (line.hasNextInt()) {
            int temp = line.nextInt();
            if (temp == 1) {
                this.mask[maskIndex++] = true;
            } else {
                this.mask[maskIndex++] = false;
            }
        }
        line.close();
    }

    /**
     *
     * @return
     */
    public boolean isPuzzleSolved() {

        for (int j = 0; j < 81; j++) {
            //as soon as the check is false the puzzle is not solved
            if (mask[j]) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param move
     * @return
     */
    public boolean makeMove(Move move) {
        if (mask[move.getIndex()]) {
            // Puzzle has empty slot.
            if (puzzle[move.getIndex()] == move.getValue()) { // Guess correct
                mask[move.getIndex()] = false; // add the the guess
                return true;
            }
        }
        return false; // Guess was wrong
    }

    /**
     *
     * @return
     */
    public String getPuzzleString() {
        return this.puzzleString;
    }

    /**
     * '
     *
     * @return
     */
    public String getMaskString() {
        return this.maskString;
    }

}
