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
public class Move {
    
    private int index;
    private int value;
    
    public Move(int index, int value){
        this.index = index;
        this.value = value;
    }
    
    public int getIndex(){return index;}
    public int getValue(){return value;}

}
