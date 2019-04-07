package sudoku;

import java.lang.Math;
import java.lang.StringBuilder;

public class PuzzleGenerator extends Thread{

	private PuzzleManager puzzleManager;

	//private String puzzleString = "5 3 9 8 2 4 6 7 1 6 1 2 3 7 9 5 4 8 4 7 8 1 5 6 9 2 3 7 4 1 2 6 5 3 8 9 3 9 6 4 8 1 7 5 2 8 2 5 7 9 3 1 6 4 2 5 3 6 1 8 4 9 7 9 8 4 5 3 7 2 1 6 1 6 7 9 4 2 8 3 5";
	private String maskString = "0 0 1 1 1 0 1 1 1 0 1 0 1 1 1 1 1 1 1 0 0 0 0 1 1 1 1 1 1 0 0 1 1 1 1 0 1 1 0 1 0 1 0 1 1 0 1 1 1 1 0 0 1 1 1 1 1 1 0 0 0 0 1 1 1 1 1 1 1 0 1 0 1 1 1 0 1 1 1 0 0";

	public PuzzleGenerator(PuzzleManager puzzleManager){
		this.puzzleManager = puzzleManager;
	}
	
	public void run(){
		
		while (true){
			try{
				Thread.sleep((int)(1000));
			}
			catch (InterruptedException ie){ ie.printStackTrace(); }
			//System.out.println("size of puzzlemanager" + puzzleManager.unsolvedPuzzles.size());
			if (puzzleManager.unsolvedPuzzles.size() < 10){
				//System.out.println("Trying to generate puzzle");
				Puzzle newPuzzle = generatePuzzle();
				puzzleManager.unsolvedPuzzles.add(newPuzzle);
				puzzleManager.savePuzzles(puzzleManager.unsolvedPuzzlesFile, puzzleManager.unsolvedPuzzles);
			}
		}
	}
	
	private Puzzle generatePuzzle(){
		Puzzle newPuzzle;
		
		int[] puzzleArray = new int[81];
		for (int i = 0; i < 81; i++){ puzzleArray[i] = 0; }
		
		solve(puzzleArray, 0);
		
		newPuzzle = new Puzzle(puzzleArrayToString(puzzleArray), maskString);
		
		return newPuzzle;
	}
	
	// returns true if it solves the puzzle
	private boolean solve (int[] puzzleArray, int index){
		int[] candidates = candidates();
		
		for (int candidate : candidates){
			
			// test for puzzle succesfully completed
			if (index == 80 && isLegalMove(candidate, puzzleArray, index)){
				puzzleArray[index] = candidate;
				return true;
			}
			
			// if legal move but not termnating then try to recursively
			// solve puzzle using this candidate. 
			if (isLegalMove(candidate, puzzleArray, index)){
				puzzleArray[index] = candidate; // try candidate
				if (solve(puzzleArray, index + 1)){ // puzzle solves
					return true;
				}
				else{ // puzzle does not solve with this candidate
					puzzleArray[index] = 0;
				}
			}
		}
		// No candidate allowed puzzle to be solved.
		// need to back track
		return false;	
	}
	
	private int[] candidates(){
		int[] candidates = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		for (int i = 0; i < 20; i++){ // 20 shuffles
			int index1 = (int)(Math.random() * 9);
			int index2 = (int)(Math.random() * 9);
			// Perform swap
			int temp = candidates[index1];
			candidates[index1] = candidates[index2];
			candidates[index2] = temp;
		}
		return candidates;
	}
	
	private String puzzleArrayToString(int[] puzzleArray){
		StringBuilder string = new StringBuilder();
		string.append(puzzleArray[0]);
		for(int i = 1; i < 81; i++){
			string.append(" " + puzzleArray[i]);
		}
		return string.toString();
	}
	
	private boolean isLegalMove(int candidate, int[] puzzleArray, int index){
		if(	
			
			isLegalMoveRow(candidate, puzzleArray, index) &&
			isLegalMoveColumn(candidate, puzzleArray, index) &&
			isLegalMoveBox(candidate, puzzleArray, index)		
		){
			return true;
		}
		return false;
		//return true;
	}
	
	private boolean isLegalMoveRow(int candidate, int[] puzzleArray, int index){
		//System.out.println("isLegalRow");
		int baseIndex = 9 * (int)(index / 9);
		
		for (int i = baseIndex; i < baseIndex + 9; i++){
			if (puzzleArray[i] == candidate){
				return false;
			}
		}
		return true;
	}
	
	private boolean isLegalMoveColumn(int candidate, int[] puzzleArray, int index){
		//System.out.println("isLegalColumn");
		int columnOffset = (int)(index % 9);
		
		for (int i = 0; i < 9; i++){
			if (puzzleArray[i * 9 + columnOffset] == candidate){
				return false;
			}
		}
		return true;
	}
	
	private boolean isLegalMoveBox(int candidate, int[] puzzleArray, int index){
		//System.out.println("isLegalBox");
		int columnOffset = (int)(index % 9); 
		int rowOffset = (int)(index / 9); 
		
		int boxColumnOffset = (int)(columnOffset / 3) * 3;
		int boxRowOffset = (int)(rowOffset / 3) * 3;
		
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 3; col++){
				if (puzzleArray[9 * (boxRowOffset + row) + boxColumnOffset + col] == candidate){
					return false;
				}
			}
		}
		return true;
	}

}
