package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    public int maskedElementAtIndex(int index){
        if(!mask[index])
            return puzzle[index]; 
        return 0;    
    }
    
    public int elementAtIndex(int index){
    	return puzzle[index];
    }

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

    public boolean isPuzzleSolved() {

        for (int j = 0; j < 81; j++) {
            //as soon as the check is false the puzzle is not solved
            if (mask[j]) {
                return false;
            }
        }
        return true;
    }
    
    public boolean makeMove(Move move){
    	if (mask[move.getIndex()]){
    		// Puzzle has empty slot.
    		if(puzzle[move.getIndex()] == move.getValue()){ // Guess correct
    			mask[move.getIndex()] = false; // add the the guess
    			return true;
    		}
    	}
    	return false; // Guess was wrong
    }
    
    public String getPuzzleString(){ return this.puzzleString; }
    
    public String getMaskString(){ return this.maskString; }

}
