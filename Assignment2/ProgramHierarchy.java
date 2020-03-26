/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {	
	
	/** Boxes Width in Pixels **/
	private static final int BOX_WIDTH = 150;
	
	/** Boxes Width in Pixels **/
	private static final int BOX_HEIGHT = 50;
	
	public void run() {
		int center_x = getWidth() / 2;
		int center_y = getHeight() / 2;
		
		int upper_boxes_center_y = center_y - BOX_HEIGHT;
		
		// Adds program box
		GRect Program_Box = new GRect(center_x - BOX_WIDTH / 2,
				upper_boxes_center_y - BOX_HEIGHT / 2, BOX_WIDTH,
				BOX_HEIGHT);
		add(Program_Box);
		
		int lower_boxes_center_y = center_y + BOX_HEIGHT;
		int graphics_center_x = center_x - (int)(1.5 * BOX_WIDTH);
		// Add GraphicsProgram box
		GRect Graphics_Box = new GRect(graphics_center_x - BOX_WIDTH / 2,
				lower_boxes_center_y - BOX_HEIGHT / 2,
				BOX_WIDTH, BOX_HEIGHT);
		add(Graphics_Box);
		
		// Add ConsoleProgram box
		GRect Console_Box = new GRect(center_x - BOX_WIDTH / 2,
				lower_boxes_center_y - BOX_HEIGHT / 2,
				BOX_WIDTH, BOX_HEIGHT);
		add(Console_Box);
		
		int dialog_center_x = center_x + (int)(1.5 * BOX_WIDTH);
		// Add DialogProgram box
		GRect Dialog_Box = new GRect(dialog_center_x - BOX_WIDTH / 2,
				lower_boxes_center_y - BOX_HEIGHT / 2,
				BOX_WIDTH, BOX_HEIGHT);
		add(Dialog_Box);
			
		// Add program box label
		GLabel Program_Label = new GLabel("Program");
		Program_Label.setLocation(center_x - Program_Label.getWidth() / 2,
				upper_boxes_center_y + Program_Label.getAscent() / 2);
		add(Program_Label);
		
		// Add GraphicsProgram label
		GLabel Graphics_Label = new GLabel("GraphicsProgram");
		Graphics_Label.setLocation(
				graphics_center_x - Graphics_Label.getWidth() / 2,
				lower_boxes_center_y + Graphics_Label.getAscent() / 2);
		add(Graphics_Label);
		
		// Add ConsoleProgram label
		GLabel Console_Label = new GLabel("ConsoleProgram");
		Console_Label.setLocation(
				center_x - Console_Label.getWidth() / 2,
				lower_boxes_center_y + Console_Label.getAscent() / 2);
		add(Console_Label);
		
		// Add DialogProgram label
		GLabel Dialog_Label = new GLabel("DialogProgram");
		Dialog_Label.setLocation(
				dialog_center_x - Dialog_Label.getWidth() / 2,
				lower_boxes_center_y + Dialog_Label.getAscent() / 2);
		add(Dialog_Label);
		
		// Add GraphicsProgram Line
		GLine Graphics_Line = new GLine(center_x,
				upper_boxes_center_y + BOX_HEIGHT / 2, graphics_center_x,
				lower_boxes_center_y - BOX_HEIGHT / 2);
		add(Graphics_Line);
		
		// Add ConsoleProgram Line
		GLine Console_Line = new GLine(center_x,
				upper_boxes_center_y + BOX_HEIGHT / 2, center_x,
				lower_boxes_center_y - BOX_HEIGHT / 2);
		add(Console_Line);
		
		// Add DialogProgram Line
		GLine Dialog_Line = new GLine(center_x,
				upper_boxes_center_y + BOX_HEIGHT / 2, dialog_center_x,
				lower_boxes_center_y - BOX_HEIGHT / 2);
		add(Dialog_Line);
		
	}
}

