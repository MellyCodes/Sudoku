/**
* @file SplashState.java
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
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Melanie Roy-Plommer
 */
class SplashState extends State{
   
    private final ImageEntity rainbow;
    private final ImageEntity logo;
    
    public SplashState() {
        
        super();
        addBackground("resources/splashBackground.jpg");                
        rainbow = new ImageEntity(new Dimension((int)(0.8*height), (int)(0.85*height)), new Point(width/2, (int)(height*0.45)), 0);
        rainbow.addImage("resources/rainbow.png");
        
        logo = new ImageEntity(new Dimension((int)(0.85*height), (int)(0.85*height)), new Point(width/2, height/2), 0);
        logo.addImage("resources/sudokuLogo.png");
    }
    
    @Override
    public void update(){
        super.update();
        
        if(animationCounter == 10){
            sounds.playSound("background", 0);
        }
        
        if(animationCounter >= 250){
            transitionToState = StateTransition.MENU;
            transitionTriggered = true;            
        }
        
        rainbow.setOrientation(rainbow.getOrientation()+0.3);
        
        rainbow.update();
        logo.update();
    }    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
                
        rainbow.paint(bufferedGraphics);
        logo.paint(bufferedGraphics);
        
        g.drawImage(imageBuffer, 0, 0, width, height, null);
    }    
}
