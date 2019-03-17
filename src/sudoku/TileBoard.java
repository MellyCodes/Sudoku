/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

import java.awt.Dimension;
import java.awt.Point;

/**
 * @section Academic Integrity
 * I certify that this work is solely my own and complies with
 * NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class TileBoard extends AnchorManager{
    
    private boolean hasMove = false;
    private Move move = null;
    
    public TileBoard(Dimension size, Point position) {
        super(size, position, 0);
        
        int yIncrement = (int)(h/9);
        int xIncrement = (int)(w/9);
        int yOffset = (int)(yIncrement/2 + x - w/2);
        int xOffset = (int)(xIncrement/2 + y - h/2);
        
        for (int i = 0; i < 81; i++){
            addAnchor(new Point(xOffset + (i%9)*xIncrement,yOffset + (int)(i/9)*yIncrement));
        }
        
        addImage("resources/SudokuGrid.png");
    }
    

    
    /**
     * 
     * @param tile 
     */
    public void tileDropped(Tile tile){
        if(contains(tile.getPosition())){  // Tile was dropped on board
            // Goes through all the anchors (eg: each square on the board)            
            for(int i = 0; i< anchors.size();i++){
                // Checks if the dropped tile is touching any of the anchors
                if(tile.contains(anchors.get(i))){
                    hasMove = true;
                    // Makes the move in the proper index with the tiles value
                    move = new Move(i, tile.getValue());
                }
            }
        }
       
    }
    
    
    public boolean hasMove(){ return hasMove;}
    
    /**
     * Puts the move in a temp variable, clears it for the next play
     * and returns the temp move
     * @return 
     */
    public Move getMove(){
        Move returnMove = move;
        //clear out move for next move
        move = null;
        hasMove = false;
        return returnMove;                 
    }
    
    /**
     * Accept the tile onto the board in its proper position
     * and resets isDraggable to false (can't move the tile once it's accepted).
     * @param move
     * @param tile 
     */
    public void acceptMove(Move move, Tile tile){
        tile.setHome(anchorAtIndex(move.getIndex()));
        tile.setIsDraggable(false);    
    }
    

}
