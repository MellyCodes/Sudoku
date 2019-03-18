/**
* @file Button.java
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Melanie Roy-Plommer
 */
public class Button extends ImageEntity{
    private boolean isButtonDown = false;
    private String buttonText;
    private boolean hasClick = false;
    private int buttonReleaseCounter = 0;
    
    private Color textColor = new Color(255,255,255);    
    
    protected enum ButtonType{MENU, KEY, HUB, GAME}
    
    private ArrayList<String> imagePaths = new ArrayList<String>(){{
        add("resources/buttons/menuButtonUp.png");add("resources/buttons/menuButtonDown.png");add("resources/buttons/menuButtonDisabled.png");
        add("resources/buttons/keyButtonUp.png");add("resources/buttons/keyButtonDown.png");add("resources/buttons/keyButtonDisabled.png");
        add("resources/buttons/hubButtonUp.png");add("resources/buttons/hubButtonDown.png");add("resources/buttons/hubButtonDisabled.png");
        add("resources/buttons/gameButtonUp.png");add("resources/buttons/gameButtonDown.png");add("resources/buttons/gameButtonDisabled.png");       
    }};
    
    private double yScale; // aspect ratio
    private double fontScale;   // font size relative to buttons     
    
    /***
     * 
     * @param size
     * @param position
     * @param orientation
     * @param buttonText
     * @param type 
     */
    public Button(int size, Point position, int orientation, String buttonText, ButtonType type) {        
        super(new Dimension(size, size), position, orientation);
        
        this.buttonText = buttonText;
        
        int imageBaseIndex; //offset index to select particular button types
        
        if(null == type){
            imageBaseIndex = 0;
        }else switch (type) {
            case MENU:
                imageBaseIndex = 0;
                yScale = 0.25;
                fontScale = 0.25;
                break;
            case KEY:
                imageBaseIndex = 3;
                yScale = 1;
                fontScale = 0.7;
                break;
            case HUB:
                imageBaseIndex = 6;
                yScale = 1;
                fontScale = 0.2;
                break;
            case GAME:
                imageBaseIndex = 9;
                yScale = 0.5;
                fontScale = 0.3;
                setTextColor(new Color(0,0,0));
                break;
            default:
                imageBaseIndex = 0;
                break;
        }
        setSize(new Dimension(size, (int)(size * yScale))); //fix aspect ratio according to button type
        
        addImage(imagePaths.get(imageBaseIndex));
        addImage(imagePaths.get(imageBaseIndex + 1));
        addImage(imagePaths.get(imageBaseIndex + 2));        
    }
    
    @Override
    public void update(){
        super.update();
        if(buttonReleaseCounter > 0){
            buttonReleaseCounter--;
        }
        
        if(buttonReleaseCounter == 0 && isButtonDown){
            isButtonDown = false;
        }    
    }   
    
    @Override
    public void paint(Graphics2D g){
        
        Image currentImage;
 
        if(!isEnabled){
            currentImage = images.get(2); //disabled version of button image
        }
        else if(isButtonDown){
            currentImage = images.get(1); 
        }
        else{
            currentImage = images.get(0);
        }
        
        // draw button if visible
        if(isVisible){
            //draw button image
            g.drawImage(currentImage, (int)(x-0.5*w), (int)(y-0.5*h), (int)w, (int)h, null);
            g.setFont(new Font("TimesRoman", Font.BOLD, (int) (fontScale * h)));
            g.setColor(textColor);
            // centering text
            int stringLength = (int)g.getFontMetrics().getStringBounds(buttonText, g).getWidth();
            int stringHeight = (int)g.getFontMetrics().getStringBounds(buttonText, g).getHeight();
            
            // draw text
            g.drawString(buttonText, (int)(x-0.5*stringLength), (int) ((int) (y+0.4*stringHeight)));
        }
        
        if(showingBoundingBox){
            g.draw(boundingBox);
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e){
        if(isEnabled){
            if(contains(e.getPoint())){
                isButtonDown = true;
                hasClick = true;
                buttonReleaseCounter = 15;
            }        
        }
    }
    
    public void setTextColor(Color color) {
        textColor = color;
    }
    
    public void setButtonText(String text){
        buttonText = text;
    }
    
    public boolean hasClick(){
        return hasClick;
    }
    
    public void unClick(){
        hasClick = false;    
    }
}
