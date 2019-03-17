/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 * @section Academic Integrity
 * I certify that this work is solely my own and complies with
 * NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class AnchorManager extends ImageEntity{
    protected ArrayList<Point> anchors = new ArrayList<Point>();
    
    public AnchorManager(Dimension size, Point position, int orientation) {
        super(size, position, orientation);
    }
    
    public void addAnchor(Point anchor){
        anchors.add(anchor);
    }
    
    public Point anchorAtIndex(int index){
        return anchors.get(index);
    }
    
}
