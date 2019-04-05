/**
* @file CreditsState.java
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Melanie Roy-Plommer
 */
class CreditsState extends State{
    private int yOffset = height; 
    private ArrayList<String> credits = new ArrayList<>();
    
    public CreditsState() {
        
        super();
        addBackground("resources/blueBackground1Translucent.png");
        credits.add("CREDITS");
        credits.add("by: Melanie Roy-Plommer");
        credits.add("for: Comprehensive Development Project");
        credits.add("Gaming Experience Development");
        credits.add("NBCC Moncton 2019");
        credits.add("Special Thanks to Jeffery Ottar");
        credits.add("For the name idea");
        
        credits.add("Testing Text for QA");
        credits.add("Melanie Roy-Plommer for QA Testing");
    }
    
    @Override
    public void update(){
        super.update();
        if(animationCounter >= 75){
            yOffset -= 2;        
        }
        
        if(animationCounter >= 900){
            transitionToState = StateTransition.EXIT;
            transitionTriggered = true;
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        
        
        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.setFont(new Font("TimesRoman", Font.BOLD, 30));
        
        // draw first credits line
        int stringLength = (int)bufferedGraphics.getFont().getStringBounds(credits.get(0), bufferedGraphics.getFontRenderContext()).getWidth();
        bufferedGraphics.drawString(credits.get(0), (width - stringLength)/2, yOffset - height /2);
        
        //draw remaining lines
        for(int i = 1; i < credits.size(); i++){
            stringLength = (int)bufferedGraphics.getFontMetrics().getStringBounds(credits.get(i), bufferedGraphics).getWidth();
            bufferedGraphics.drawString(credits.get(i), (width - stringLength)/2, yOffset + i*150);
        }
        g.drawImage(imageBuffer, 0,0,width, height, null);
    }
    
    
}
