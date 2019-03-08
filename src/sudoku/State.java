/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JPanel;

/**
 *
 * @author melan
 */
public class State extends JPanel{  // used to paint on it, it will cover the JFrame (StateController object)

    protected int width = 1600;
    protected int height = 900;
    
    public static enum StateTransition {SPLASH, MENU, OPTIONS, GAME, CREDITS, EXIT}
    protected StateTransition transitionToState = State.StateTransition.MENU;
    protected boolean transitionTriggered = false;

    protected int animationCounter = 0;

    protected BufferedImage imageBuffer;
    protected Graphics2D bufferedGraphics;
    
    private Toolkit tk= Toolkit.getDefaultToolkit();
    private AffineTransform at = new AffineTransform();
    private Image background;
    
    protected String screenMessage = "";
    protected int screenMessageCounter = 0;
    protected int screenMessageLength = 0;
    
    public State() {
        super();    
        repaint();
        imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedGraphics = (Graphics2D)imageBuffer.getGraphics();
        
        bufferedGraphics.setTransform(at);
        bufferedGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);        
    }
    
    @Override
    public void setSize(Dimension d){
        this.width = (int) d.getWidth();
        this.height = (int) d.getHeight();
    }
    
    public void update(){
        animationCounter++;
        if (animationCounter >= Integer.MAX_VALUE -2){  // prevents overflow of animationCounter
            animationCounter = 0;
        }
        if (screenMessageCounter>0){
            screenMessageCounter--;
        }
    }
    
    @Override
    public void paintComponent(Graphics g){        
        super.paintComponent(g);

        bufferedGraphics.setColor(new Color(0,0,0));
        bufferedGraphics.fillRect(0,0,width, height);
        bufferedGraphics.drawImage(background, 0, 0, width, height, null);
        g.drawImage(imageBuffer, 0, 0, width, height, null);              
    }    
    
    public StateTransition getTransitionToState() {
        return transitionToState;
    }

    public boolean isTransitionTriggered() {
        return transitionTriggered;
    }

    public void resetTransitionTriggered() {
        transitionTriggered = false;
    }
    
    public void addBackground(String imgPath){
        URL url = getClass().getResource(imgPath);
        background = tk.getImage(url);    
    }
    
    public void writeToScreen(String message, int counter){   
        screenMessageLength = (int)bufferedGraphics.getFontMetrics().getStringBounds(message, bufferedGraphics).getWidth();
        this.screenMessage = message;
        this.screenMessageCounter = counter;    
    }    
}
