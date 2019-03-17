/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

/**
 * @section Academic Integrity
 * I certify that this work is solely my own and complies with
 * NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class PuzzleManager {

    private Puzzle currentPuzzle;
    
    public PuzzleManager(){
        String puzzleString = "2 4 8 5 3 9 6 7 1 6 7 5 4 1 2 8 9 3 3 9 1 6 7 8 2 4 5 7 1 2 8 6 3 9 5 4 4 3 6 1 9 5 7 8 2 5 8 9 2 4 7 1 3 6 8 2 4 9 5 6 3 1 7 9 5 7 3 2 1 4 6 8 1 6 3 7 8 4 5 2 9";
        String maskString = "1 1 0 0 0 1 1 1 1 1 1 0 1 0 0 1 1 1 0 0 0 1 1 1 1 1 1 0 1 1 1 1 0 1 0 1 0 0 1 1 1 1 1 0 0 1 0 1 0 1 1 1 1 0 1 1 1 1 1 1 0 0 0 1 1 1 0 0 1 0 1 1 1 1 1 1 0 0 0 1 1";

        currentPuzzle = new Puzzle(puzzleString, maskString);
    }
 
    
    public Puzzle getCurrentPuzzle(){    
        return currentPuzzle;
    }
}
