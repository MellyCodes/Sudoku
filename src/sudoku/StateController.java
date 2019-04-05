/**
* @file StateCntroller.java
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
import javax.swing.JFrame;

/**
 *
 * @author Melanie Roy-Plommer
 */

public class StateController extends JFrame implements Runnable{
    private final int WIDTH = 1600;
    private final int HEIGHT = 900;
    private final int REFRESH_TIME = 1000/50;
    
    private Thread gameLoop;   
    private SplashState splash;
    private OptionsState options;
    private CreditsState credits;
    private GameState game;
    private MenuState menu;
    private State currentState;  
    
    private SoundManager sounds = new SoundManager(); 
    
    public StateController(){
        super("Super Sudoku Seven"); // inherits from JFrame constructor
        setSize(new Dimension(WIDTH, HEIGHT)); // set dimensions
        setResizable(false); // cannot resize while during runtime
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // sets close button to exit
        setVisible(true); // shows JFrame    
        
        // Load sounds
        sounds.addSound("background", "resources/sounds/oceanwaves.wav");
        sounds.addSound("tilehome", "resources/sounds/tileHome.wav");
        sounds.addSound("tileaccepted", "resources/sounds/tileAccepted.wav");
        
        // Instantiate SplashState   
        splash = new SplashState();
        splash.setSize(new Dimension(WIDTH, HEIGHT));
        splash.setSoundManager(sounds);
        
        // Instantiate MenuState        
        menu = new MenuState();
        menu.setSize(new Dimension(WIDTH, HEIGHT));    
        menu.setSoundManager(sounds);
        
        // Instantiate OptionsState
        options = new OptionsState();
        options.setSize(new Dimension(WIDTH, HEIGHT));
        options.setSoundManager(sounds);
        
        // Instaniate GameState     
        game = new GameState();
        game.setSize(new Dimension(WIDTH, HEIGHT));
        game.setSoundManager(sounds);
        
        // instantiate CreditsState
        credits = new CreditsState();
        credits.setSize(new Dimension(WIDTH, HEIGHT)); 
        credits.setSoundManager(sounds);
       
        
        // set current state to splash
        //  add current state
        currentState = splash;
        add(currentState);        
        
        revalidate();
        repaint();
        
        gameLoop = new Thread(this);
        gameLoop.start();        
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        while(currentThread == gameLoop){
            try{
                Thread.sleep(REFRESH_TIME);            
            }
            catch(InterruptedException e){
                e.printStackTrace();            
            }
            update();
            repaint();
        }
    }

    private void update() {
        currentState.update();
        
        if(currentState.isTransitionTriggered()){
            remove(currentState);
            State.StateTransition toState = currentState.getTransitionToState();
            currentState.resetTransitionTriggered();
            if(null != toState)switch (toState) {
                case SPLASH:                    
                    currentState = splash;
                    break;
                case MENU:
                    currentState = menu;
                    break;
                case OPTIONS:
                    currentState = options;
                    break;
                case GAME:
                    currentState = game;
                    break;
                case CREDITS:
                    currentState = credits;
                    break;                
                case EXIT:
                    System.exit(0);
                    break;
                default:
                    break;
            }
            
            add(currentState);
            revalidate();
        }
    }
}
