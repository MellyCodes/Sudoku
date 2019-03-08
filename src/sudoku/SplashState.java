/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Graphics;

/**
 *
 * @author melan
 */
class SplashState extends State{
   
    
    public SplashState() {
        
        super();
        addBackground("resources/splashBackground.jpg");
    }
    
    @Override
    public void update(){
        super.update();
        
        if(animationCounter >= 250){
            transitionToState = StateTransition.MENU;
            transitionTriggered = true;            
        }
    }    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.drawImage(imageBuffer, 0, 0, width, height, null);
    }    
}
