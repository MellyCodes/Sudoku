/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author melan
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
    
    public StateController(){
        super("Super Sudoku Seven"); // inherits from JFrame constructor
        setSize(new Dimension(WIDTH, HEIGHT)); // set dimensions
        setResizable(false); // cannot resize while during runtime
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // sets close button to exit
        setVisible(true); // shows JFrame    
        
        // Instantiate SplashState   
        splash = new SplashState();
        splash.setSize(new Dimension(WIDTH, HEIGHT));
        
        // Instantiate MenuState        
        menu = new MenuState();
        menu.setSize(new Dimension(WIDTH, HEIGHT));     
        
        // Instantiate OptionsState
        options = new OptionsState();
        options.setSize(new Dimension(WIDTH, HEIGHT));
        
        // Instaniate GameState     
        game = new GameState();
        game.setSize(new Dimension(WIDTH, HEIGHT));
        
        // instantiate CreditsState
        credits = new CreditsState();
        credits.setSize(new Dimension(WIDTH, HEIGHT));      
       
        
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
