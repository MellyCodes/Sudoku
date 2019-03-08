/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * @section Academic Integrity
 * I certify that this work is solely my own and complies with
 * NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class Tile extends ImageEntity implements MouseMotionListener{

    private int value;
    private boolean isDraggable;
    private boolean isDragging;
    
    private Point home;
    
    private boolean dropped = false;
    
    //protected enum TileValue{ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE}
    
    private ArrayList<String> imagePaths = new ArrayList<String>(){{
        add("resources/TileOne.png");add("resources/TileTwo.png");add("resources/TileThree.png");
        add("resources/TileFour.png");add("resources/TileFive.png");add("resources/TileSix.png");
        add("resources/TileSeven.png");add("resources/TileEight.png");add("resources/TileNine.png");       
    }};
    
    
    public Tile(Dimension size, Point position, int value) {
        super(size, position, 0);
        
        this.value = value;
        
        
        
        home = position;
        isDraggable = true;
        isDragging = false;
        //showBoundingBox();
        
       
        
        
        
        addImage(imagePaths.get(value - 1));
        

        
        
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
    
    
    
    
    public void snapHome(){
        //TODO
        slide(home, 25);
    }
    
    @Override
    public void paint(Graphics2D g){
        super.paint(g);
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
                //snapHome();
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
    public void mouseMoved(MouseEvent e) {
        //TODO
        ;
    }

}
