/**
* @file Tile.java
* @author Melanie Roy-Plommer
* @version 1.0
*
* @section DESCRIPTION
* <  >
*
* @section LICENSE
* Copyright 2018 - 2019
* Permission to use, copy, modify, and/or distribute this software for
* any purpose with or without fee is hereby granted, provided that the
* above copyright notice and this permission notice appear in all copies.
*
* THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
* WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
* ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
* WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
* ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
* OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*
* @section Academic Integrity
* I certify that this work is solely my own and complies with
* NBCC Academic Integrity Policy (policy 1111)
*/

package sudoku;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * 
 * @author Melanie Roy-Plommer
 */
public class Tile extends ImageEntity implements MouseMotionListener{

    private int value;
    private boolean isDraggable;
    private boolean isDragging;
    private boolean isBoardTile = false;
    
    private Point home;
    
    private boolean dropped = false;
    
      
    private ArrayList<String> imagePaths = new ArrayList<String>(){{
        add("resources/TileOne.png");add("resources/TileTwo.png");add("resources/TileThree.png");
        add("resources/TileFour.png");add("resources/TileFive.png");add("resources/TileSix.png");
        add("resources/TileSeven.png");add("resources/TileEight.png");add("resources/TileNine.png");     
        add("resources/TileOneWhite.png");add("resources/TileTwoWhite.png");add("resources/TileThreeWhite.png");
        add("resources/TileFourWhite.png");add("resources/TileFiveWhite.png");add("resources/TileSixWhite.png");
        add("resources/TileSevenWhite.png");add("resources/TileEightWhite.png");add("resources/TileNineWhite.png");     
    }};
    
    
    public Tile(Dimension size, Point position, int value) {
        super(size, position, 0);
        
        this.value = value;
        
        home = position;
        isDraggable = true;
        isDragging = false;
        //showBoundingBox();
        
        addImage(imagePaths.get(value - 1)); // Red Tile
        addImage(imagePaths.get(value - 1 + 9)); // White Tile
        
        
    }   

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean getIsDraggable() {
        return isDraggable;
    }

    public void setIsDraggable(boolean isDraggable) {
        this.isDraggable = isDraggable;
    }

    public boolean getIsDragging() {
        return isDragging;
    }

    public void setIsDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    public Point getHome() {
        return home;
    }

    public void setHome(Point home) {
        this.home = home;
    }

    public boolean isDropped() {
        return dropped;
    }

    public void setDropped(boolean dropped) {
        this.dropped = dropped;
    }
    
    public void setIsBoardTile(boolean isBoardTile){this.isBoardTile = isBoardTile;}
    
    public void snapHome(){
        
        slide(home, 25);
    }
    
    @Override
    public void paint(Graphics2D g){
        super.paint(g);
        if(isBoardTile){
            g.drawImage(images.get(1),
                    (int)(x-w/2),
                    (int)(y-h/2),
                    (int)(w),
                    (int)(h),
                    null);
        }
    }
    
    @Override
    public void update(){
        super.update();
        
    }
    
    
    @Override
    public void mousePressed(MouseEvent e){
        System.out.println("Mouse Pressed");
        if(isDraggable){
            if(contains(e.getPoint())){
                setIsDragging(true);
                
            }        
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        System.out.println("Mouse Released");
        if(isDraggable){
            if(contains(e.getPoint())){
                setIsDragging(false);
                setDropped(true);             
            }
        }        
    }
    
    

    @Override
    public void mouseDragged(MouseEvent e) {    
System.out.println("Mouse Dragged");
        if(isDragging)
            setPosition(e.getPoint());
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void mouseMoved(MouseEvent e) {
        ;
    }

}
