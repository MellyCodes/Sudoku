/*
 * This assignment represents my own work and is in accordance with the College Academic Policy
 * Melanie Roy-Plommer - Gex 2018
 */
package sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author melan
 */
public class GameState extends State{

    //private Image background;
    //private ImageEntity banner;
    private Button pauseButton;
    private Button menuButton;
    private Button buyVowelButton;
//    private Button solveButton;
    
    private ArrayList<String> puzzles = new ArrayList<String>();
//    private final int MAX_ROUNDS = 3;
//    private int currentRound = 1;
       
    // game states
    private enum GameStates {ENTER_GAME, MAIN_OPTIONS, END_ROUND}
    private GameStates gameStates = GameStates.ENTER_GAME;
    private boolean enteringState = true;
    private boolean showingSwingDialog = false; 
    private String guess = null;
    private boolean showingPlayerNameDialog = false;
    private String chosenPlayerName = null;
    private boolean gameInPlay = true;
    private int gameSleepCounter = 0;
    
    // transition out to main menu
    private int transitionTimer=0;
    private boolean timedTransition = false;
    
    public GameState() {
        super();     
        
        addBackground("resources/Blue-Background.jpg");
//        loadPuzzles();
               
        // create banner image and add animation
//        banner = new ImageEntity(new Dimension((int)(0.4*width),(int) (0.3*height)), new Point((int)(width/2.0), (int) (-0.3*height)), 0);
//        banner.addImage("resources/banner.png");
//        banner.slide(new Point((int)(0.5*width), (int)(0.15*height)), 15);
      

        pauseButton = new Button((int) (0.25*width), new Point((int)(0.9*width), (int)(0.75*height)), 0, "Pause", Button.ButtonType.MENU);
        addMouseListener(pauseButton);

        menuButton = new Button((int)(0.25*width), new Point((int)(0.9*width), (int)(0.85*height)), 0, "Menu", Button.ButtonType.MENU);
        addMouseListener(menuButton);
        
        

        
//         playButton = new Button((int)(0.33*width), new Point((int)(-0.3*width), (int)(0.4*height)), 0, "Play Game", Button.ButtonType.MENU);
//        playButton.slide(new Point((int)(0.5*width), (int)(0.4*height)), 10);
//        addMouseListener(playButton);
//        
//        optionsButton = new Button((int)(0.33*width), new Point((int)(-0.5*width), (int)(0.6*height)), 0, "Options", Button.ButtonType.MENU);
//        optionsButton.slide(new Point((int)(0.5*width), (int)(0.6*height)), 20);
//        addMouseListener(optionsButton);
//        
//        quitButton = new Button((int)(0.33*width), new Point((int)(-0.7*width), (int)(0.8*height)), 0, "Quit", Button.ButtonType.MENU);
//        quitButton.slide(new Point((int)(0.5*width), (int)(0.8*height)), 30);
//        addMouseListener(quitButton);
               
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);       
       
        //banner.paint(bufferedGraphics);
        pauseButton.paint(bufferedGraphics);
        menuButton.paint(bufferedGraphics);
        
        if(screenMessageCounter > 0){
            bufferedGraphics.setColor(Color.YELLOW);
            bufferedGraphics.setFont(new Font("TimesRoman", Font.BOLD, 60));
            bufferedGraphics.drawString(screenMessage, (int)((width - screenMessageLength)/2), (int)(height/2));
        }       
        g.drawImage(imageBuffer, 0,0, width, height,null);
    }
    
    @Override
    public void update(){
        super.update();
        
        // back to menu
        if(menuButton.hasClick()){
            menuButton.unClick();            
            transitionToState = StateTransition.MENU;
            transitionTriggered = true;
        }
        else if(pauseButton.hasClick()){
            pauseButton.unClick();
            transitionToState = StateTransition.OPTIONS;
            transitionTriggered = true;
        }
        if (gameSleepCounter > 0){
            gameSleepCounter--;
        }
        else{
            ////////////////////////////////////////////////////////////////////
            // GameState Logic
            if(gameInPlay){
                switch (gameStates){
                    case ENTER_GAME:
                        newGame();                        
                        enteringState = true;                        
                        break;
                    case MAIN_OPTIONS:
                        if(enteringState){
                            enteringState = false;
                        // Entering state (setup)
                            enteringState = true; 
                        }
                        break;
                    case END_ROUND:
                        // Entering state (setup)
                        if(enteringState){
                            enteringState = false;
                         }
                        break;
                    default:
                        writeToScreen("unknown error", 100);
                        break; 
                }
            } 
            // End GameState Logoc
            /////////////////////////////////////////////////////////////////////
            //update GUI components
            
            //banner.update();
           
            pauseButton.update();
            menuButton.update();           
            
            
            //transition delay for exiting to menu
            if(transitionTimer>0){
                transitionTimer--;
            }

            if(transitionTimer == 0 && timedTransition){
                gameInPlay = true;
                timedTransition = false;
                transitionTriggered = true;
            }        
        }
    }
    
//    private void loadPuzzles(){
//        URL filePath = getClass().getResource("resources/puzzles.txt");
//                
//        String line = null;
//
//        try {
//            // FileReader reads text file
//            FileReader fileReader = 
//                new FileReader(new File(filePath.toURI()));
//                
//            // wrap FileReader in BufferedReader.
//            BufferedReader bufferedReader = 
//                new BufferedReader(fileReader);
//
//            while((line = bufferedReader.readLine()) != null) {                
//                puzzles.add(line);
//            }   
//
//            // close file
//            bufferedReader.close();         
//        }
//        catch(FileNotFoundException ex) {
//            writeToScreen("Unable to open file '", 150);                
//        }
//        catch(IOException ex) {
//            writeToScreen("Error reading file '", 150);            
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    

    
    public void sleep(int numOfFrames){ 
        gameSleepCounter = numOfFrames;
    }
    
    private void newGame(){
        //////////////////////////////////////////////////
        // reinitialize objects for new 3 round game /////
        //////////////////////////////////////////////////
//        banner = new ImageEntity(new Dimension((int)(0.4*width),(int) (0.3*height)), new Point((int)(width/2.0), (int) (-0.3*height)), 0);
//        banner.addImage("resources/banner.png");
//        banner.slide(new Point((int)(0.5*width), (int)(0.15*height)), 15);
        
//        buyVowelButton = new Button((int)(width/12), new Point((int)(width*0.5), (int)(height * 1.35)), 
//                0, "Buy Vowel", Button.ButtonType.GAME);
//        addMouseListener(buyVowelButton);
//        
//        solveButton = new Button((int)(width/12), new Point((int)(width*0.6), (int)(height * 1.35)), 
//                0, "Solve", Button.ButtonType.GAME);
//        addMouseListener(solveButton);
//        
//        hubButton = new Button((int)(0.25*height), new Point((int)(-0.5*width), (int)(0.6*height)), 0, "Spin", Button.ButtonType.HUB);
//        addMouseListener(hubButton);        
//        
           
        
        menuButton = new Button((int)(0.25*width), new Point((int)(0.9*width), (int)(0.85*height)), 0, "Menu", Button.ButtonType.MENU);
        addMouseListener(menuButton);
        

                
        
    }
}
