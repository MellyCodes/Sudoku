/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 * @section Academic Integrity I certify that this work is solely my own and
 * complies with NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class TileStacks extends AnchorManager {

    private ArrayList<ArrayList<Tile>> stacks = new ArrayList<ArrayList<Tile>>();

    public TileStacks(Dimension size, Point position) {
        super(size, position, 0);

        addImage("resources/blank.png");
        addImage("resources/tileout.png");

        //stacks
        for (int i = 0; i < 9; i++) {
            stacks.add(new ArrayList<Tile>());
        }

        //Set up anchors
        for (int i = 0; i < 9; i++) {
            addAnchor(new Point((int) (x - w / 2 + i * w / 9 + w / 18), (int) (y)));
        }

    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        for (int i = 0; i < anchors.size(); i++) {
            g.drawImage(images.get(1), (int)(anchors.get(i).getX()- 10), (int)(anchors.get(i).getY()- 10), 20, 20, null);
        }

    }
    
    
    /**
     * 
     * @param tile 
     */
    public void pushTile(Tile tile){
        // if not empty, lock previous tile
        int stackSize = stacks.get(tile.getValue() - 1).size();
        
        if(stackSize > 0){
            stacks.get(tile.getValue()-1).get(stackSize - 1).setIsDraggable(false);
        }
        
        // set tile homee
        tile.setHome(anchorAtIndex(tile.getValue()-1));
        // add tile to appropriate stack
        stacks.get(tile.getValue()-1).add(tile);       
        
    }
    
    public void popTile(Tile tile){
        stacks.get(tile.getValue()-1).remove(stacks.get(tile.getValue()-1).size()-1);
    }
    
    public void popAllTiles(){
        for(int i = 0; i < stacks.size(); i++){
                stacks.get(i).clear();
        }
    }
	
    public void lockTiles(){
        for(int i = 0; i < stacks.size(); i++){
            for( int j = 0; j < stacks.get(i).size(); j++){
                    stacks.get(i).get(j).setIsDraggable(false);
            }
        }
    }
	
    public void unlockTiles(){
        for(int i = 0; i < stacks.size(); i++){
            if (stacks.get(i).size() > 0){
                    stacks.get(i).get(stacks.get(i).size() - 1).setIsDraggable(true);
            }
        }
    }
	
    // maybe dont need to ask about anchors.. simply call push tile.
    // similarly for tileboard. just say acceptMove(move, tile)
    // still need to send tile home


}
