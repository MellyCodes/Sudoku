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

    private Point[] anchors = new Point[81];
    
    public TileBoard(Dimension size, Point position) {
        super(size, position, 0);
        
        int yIncrement = (int)(h/9);
        int xIncrement = (int)(w/9);
        int yOffset = (int)(yIncrement/2 + x - w/2);
        int xOffset = (int)(xIncrement/2 + y - h/2);
        
        for (int i = 0; i < anchors.length;i++){
            anchors[i] = new Point(xOffset + (i%9)*xIncrement,yOffset + (int)(i/9)*yIncrement);
        }
        
        addImage("resources/SudokuGrid.png");
    }
    
    public Point anchorAtIndex(int index){
        
        return anchors[index];
    }
    
    
    public void tileDropped(Tile tile){
        if(contains(tile.getPosition())){
            for(int i = 0; i< anchors.length;i++){
                if(tile.contains(anchors[i])){
                    tile.setHome(anchors[i]);
                    tile.setIsDraggable(false);
                }
            }
        }
        tile.snapHome();
    }

}
