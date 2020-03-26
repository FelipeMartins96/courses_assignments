/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {

	/** Number of pixels in an inch */
	private static final int PIXELS_PER_INCH = 72;
	
	/** Outer circle radius in pixels */
	private static final int OUTER_CIRCLE_RADIUS = 1 * PIXELS_PER_INCH;
	
	/** Outer circle radius in pixels */
	private static final int MIDDLE_CIRCLE_RADIUS = (int)(0.65 * PIXELS_PER_INCH);
	
	/** Outer circle radius in pixels */
	private static final int INNER_CIRCLE_RADIUS = (int)(0.3 * PIXELS_PER_INCH);
	
	public void run() {
		int window_center_x = getWidth() / 2;
		int window_center_y = getHeight() / 2;
		
		// Outer Circle
		int outer_x = window_center_x - OUTER_CIRCLE_RADIUS;
		int outer_y = window_center_y - OUTER_CIRCLE_RADIUS;
		int outer_diameter = OUTER_CIRCLE_RADIUS * 2;
		GOval Outer_Circle = new GOval (outer_x, outer_y, outer_diameter, outer_diameter);  
		Outer_Circle.setFilled(true);
		Outer_Circle.setColor(Color.RED);
		add(Outer_Circle);
		
		// Middle Circle
		int mid_x = window_center_x - MIDDLE_CIRCLE_RADIUS;
		int mid_y = window_center_y - MIDDLE_CIRCLE_RADIUS;
		int mid_diameter = MIDDLE_CIRCLE_RADIUS * 2;
		GOval Middle_Circle = new GOval (mid_x, mid_y, mid_diameter, mid_diameter);
		Middle_Circle.setFilled(true);
		Middle_Circle.setColor(Color.WHITE);
		add(Middle_Circle);
		
		// Inner Circle
		int inner_x = window_center_x - INNER_CIRCLE_RADIUS;
		int inner_y = window_center_y - INNER_CIRCLE_RADIUS;
		int inner_diameter = INNER_CIRCLE_RADIUS * 2;
		GOval Inner_Circle = new GOval (inner_x, inner_y, inner_diameter, inner_diameter);  
		Inner_Circle.setFilled(true);
		Inner_Circle.setColor(Color.RED);
		add(Inner_Circle);
		
	}
}
