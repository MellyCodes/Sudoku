/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sudoku;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.MouseEvent;


/**
 * @section Academic Integrity
 * I certify that this work is solely my own and complies with
 * NBCC Academic Integrity Policy (policy 1111)
 * @author Melanie Roy-Plommer
 */
public class Dialog1Q extends ImageEntity{

	private String question = "";
	private boolean hasAnswer = false;
	private String answer = "";
	
	private ArrayList<Button> buttons;

	public Dialog1Q(Dimension size, Point position, String question){
		super(size, position, 0);
		this.question = question;
		buttons = new ArrayList<>();
		
		addImage("resources/boxAndShadow.png");
	}
	
	public void addButton(String buttonText, String value){
		Button newButton = new Button((int)(w*0.1), new Point((int)x, (int)y), 0, buttonText, Button.ButtonType.MENU);
		newButton.setValue(value);
		buttons.add(newButton);
		
	}
	
	public boolean getHasAnswer(){return this.hasAnswer;}
	
	public String getAnswer(){
		String returnAnswer = this.answer;
		answer = "";
		hasAnswer = false;
		for (Button button: buttons){
			button.setIsEnabled(true);
		}
		return returnAnswer;
	}
	
	@Override
	public void paint(Graphics2D g){
		super.paint(g);
		if (isVisible){

			g.setColor(new Color(0, 0, 0));
			g.drawString(question, (int)(x - w*0.4), (int)(y - h*0.5 + h*0.3));
			
			if (buttons.size() > 0){
				for (Button button: buttons){
					button.paint(g);
				}
			}
		}
		
	}
	
	@Override
	public void update(){
		super.update();
		
		
		if (!hasAnswer){ // Keep looking for an answer
			for (Button button: buttons){
				if (button.hasClick()){
					button.unClick();
					hasAnswer = true;
					answer = button.getValue();
					for (int i = 0; i < buttons.size(); i++){
						buttons.get(i).setIsEnabled(false);
					}
				}
			}
		}
		
		
		int buttonSeparation = (int)(w / buttons.size()); // Assuming there is more than one button
		for (int i = 0; i < buttons.size(); i++){
			buttons.get(i).setPosition(new Point((int)((int)(x - 0.5*w) + buttonSeparation*0.5 + i*buttonSeparation), (int)(y)));
			buttons.get(i).setSize((int)(0.8*buttonSeparation));
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		// Propogate mouse events to the buttons
		for (Button button: buttons){
			button.mousePressed(e);
		}
	}
	
}