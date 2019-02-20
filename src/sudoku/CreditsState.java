/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author melan
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
