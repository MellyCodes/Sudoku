/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author melan
 */
public class MenuState extends State{
    private Button playButton;
    private Button optionsButton;
    private Button quitButton;
    
    private ImageEntity banner;
    private int transitionTimer=0;
    private boolean timedTransition = false;
    
    public MenuState() {
        //Dimension size, Point position, int orientation, String buttonText
        super();
        addBackground("resources/menuBackground.jpg");
        
        banner = new ImageEntity(new Dimension((int)(0.3*width), (int)(0.3*height)), new Point(width/2, (int)(-0.2*height)), 0);
        banner.addImage("resources/menuBanner.png");
        banner.slide(new Point(width/2, (int)(0.2*height)), 10);
        
        playButton = new Button((int)(0.33*width), new Point((int)(-0.3*width), (int)(0.4*height)), 0, "Play Game", Button.ButtonType.MENU);
        playButton.slide(new Point((int)(0.5*width), (int)(0.4*height)), 10);
        addMouseListener(playButton);
        
        optionsButton = new Button((int)(0.33*width), new Point((int)(-0.5*width), (int)(0.6*height)), 0, "Options", Button.ButtonType.MENU);
        optionsButton.slide(new Point((int)(0.5*width), (int)(0.6*height)), 20);
        addMouseListener(optionsButton);
        
        quitButton = new Button((int)(0.33*width), new Point((int)(-0.7*width), (int)(0.8*height)), 0, "Quit", Button.ButtonType.MENU);
        quitButton.slide(new Point((int)(0.5*width), (int)(0.8*height)), 30);
        addMouseListener(quitButton);
    }    
    
    @Override
    public void update(){
        super.update();
        
        if(playButton.hasClick()){
            playButton.unClick();
            transitionTimer = 50;
            timedTransition = true;
            transitionToState = StateTransition.GAME;
            
        }
        else if (optionsButton.hasClick()){
            optionsButton.unClick();
            transitionTimer = 50;
            timedTransition = true;
            transitionToState = StateTransition.OPTIONS;
        }
        else if (quitButton.hasClick()){
            quitButton.unClick();
            transitionTimer = 50;
            timedTransition = true;
            transitionToState = StateTransition.CREDITS;
        }
        
        if(transitionTimer>0){
            transitionTimer--;
        }
        
        if(transitionTimer == 0 && timedTransition){
            timedTransition = false;
            transitionTriggered = true;
        }
        playButton.update();
        optionsButton.update();
        quitButton.update();
        banner.update();        
    }          
           
    @Override
    public void paintComponent(Graphics g){        
        super.paintComponent(g);
        playButton.paint(bufferedGraphics);        
        optionsButton.paint(bufferedGraphics);
        quitButton.paint(bufferedGraphics);
        banner.paint(bufferedGraphics);
        
        g.drawImage(imageBuffer, 0, 0,width, height, null);
    }       
}

