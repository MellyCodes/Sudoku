/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 * @section Academic Integrity
 * I certify that this work is solely my own and complies with
 * NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class OptionsState extends State{
    private Button playButton;
    private Button scoresButton;
    private Button quitButton;
    
    private ImageEntity banner;
    private int transitionTimer=0;
    private boolean timedTransition = false;
    
    public OptionsState() {
        //Dimension size, Point position, int orientation, String buttonText
        super();
        addBackground("resources/menuBackground.jpg");
        
        banner = new ImageEntity(new Dimension((int)(0.3*width), (int)(0.3*height)), new Point(width/2, (int)(-0.2*height)), 0);
        banner.addImage("resources/menuBanner.png");
        banner.slide(new Point(width/2, (int)(0.2*height)), 10);
        
        playButton = new Button((int)(0.33*width), new Point((int)(-0.3*width), (int)(0.4*height)), 0, "Play Game", Button.ButtonType.MENU);
        playButton.slide(new Point((int)(0.5*width), (int)(0.4*height)), 10);
        addMouseListener(playButton);
        
        scoresButton = new Button((int)(0.33*width), new Point((int)(-0.5*width), (int)(0.6*height)), 0, "Leaderboard", Button.ButtonType.MENU);
        scoresButton.slide(new Point((int)(0.5*width), (int)(0.6*height)), 20);
        addMouseListener(scoresButton);
        
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
        else if (scoresButton.hasClick()){
            scoresButton.unClick();
            transitionTimer = 50;
            timedTransition = true;
            transitionToState = StateTransition.SCOREBOARD;
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
        scoresButton.update();
        quitButton.update();
        banner.update();        
    }          
           
    @Override
    public void paintComponent(Graphics g){        
        super.paintComponent(g);
        playButton.paint(bufferedGraphics);        
        scoresButton.paint(bufferedGraphics);
        quitButton.paint(bufferedGraphics);
        banner.paint(bufferedGraphics);
        
        g.drawImage(imageBuffer, 0, 0,width, height, null);
    }  
}
