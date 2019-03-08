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
public class TileBoard extends ImageEntity{

    public TileBoard(Dimension size, Point position) {
        super(size, position, 0);
        
        addImage("resources/SudokuGrid.png");
    }
    
    
    public void tileDropped(Tile tile){
        if(contains(tile.getPosition())){
            tile.setHome(this.getPosition());
        
        }
        tile.snapHome();
    }

}
