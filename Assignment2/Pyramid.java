/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	public void run() {
		// Total width of pyramid base
		int base_width = BRICKS_IN_BASE * BRICK_WIDTH;
		
		// X coordinate to center of window
		int window_center_x = getWidth() / 2;
		
		// X coordinate for first brick of a row
		int first_brick_x = window_center_x - (base_width / 2);
		
		// Y coordinates for a row
		int row_y = getHeight() - BRICK_HEIGHT;
		
		/**
		 * Iterates over number of levels of the pyramid
		 * where i is the number of bricks in each level
		 */

		for (int i = BRICKS_IN_BASE; i > 0; i--) {
			
			// Adds row of bricks for a level
			for (int j = 0; j < i; j++) {
				int brick_x = first_brick_x + ( j * BRICK_WIDTH);
				GRect brick = new GRect(brick_x, row_y, BRICK_WIDTH, BRICK_HEIGHT);
				add(brick);
			}
			
			// Adjusts x and y for each level
			first_brick_x += (BRICK_WIDTH / 2.0);
			row_y -= BRICK_HEIGHT;
		}
	}
}

