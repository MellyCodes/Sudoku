/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @section Academic Integrity I certify that this work is solely my own and
 * complies with NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class Puzzle {

    public int[] puzzle = new int[81];
    public boolean[] mask = new boolean[81];

    public Puzzle(String puzzle, String mask) {

        setPuzzle(puzzle);
        setMask(mask);

    }
    
    public int elementAtIndex(int index){
        if(!mask[index])
            return puzzle[index]; 
        
        return 0;    
    }

//    public boolean guessDigit(int pos, int guess) {
//        if (mask[pos] && guess == puzzle[pos]) {
//            mask[pos] = false;
//            return true;
//        }
//        return false;
//    }

    private void setPuzzle(String puzzle) {
        Scanner line = new Scanner(puzzle);
        int puzzleIndex = 0;
        while (line.hasNextInt()) {
            this.puzzle[puzzleIndex++] = line.nextInt();
        }
        line.close();
    }

    private void setMask(String mask) {
        Scanner line = new Scanner(mask);
        int maskIndex = 0;
        while (line.hasNextInt()) {
            int temp = line.nextInt();
            if(temp == 1)
                this.mask[maskIndex++]= true;
            else
                this.mask[maskIndex++]= false;
        }
        line.close();
    }

    /**
     * *
     * returns the current puzzle with remaining masks applied
     *
     * @return
     */
    public int[] getCurrentMaskedPuzzle() {
        int[] currentMaskedPuzzle = new int[81];

        for (int i = 0; i < 81; i++) {
            // Setting an element to false uncovers a hint
            if (!mask[i]) {
                currentMaskedPuzzle[i] = puzzle[i];
            } else {
                currentMaskedPuzzle[i] = 0;
            }
        }

        return currentMaskedPuzzle;
    }

    /**
     * *
     * checks if all the hints are visible
     *
     * @return
     */
    public boolean isPuzzleSolved() {

        for (int j = 0; j < 81; j++) {
            //as soon as the check is false the puzzle is not solved
            if (!mask[j]) {
                return false;
            }
        }
        return true;
    }

}
