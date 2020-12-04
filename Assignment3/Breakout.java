/*
 * File: Breakout.java
 * -------------------
 * Name: Felipe Martins
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;
	
/** Paddle Y value */
	private static final int PADDLE_Y = 
		HEIGHT - (PADDLE_Y_OFFSET + PADDLE_HEIGHT);

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_MAX_X = WIDTH - PADDLE_WIDTH;
	
/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of colors [RED, ORANGE, YELLOW, GREEN, BLUE] */
	private static final int NCOLORS = 5;
	
/** Number rows for each color */
	private static final int NBRICK_ROWS_PER_COLOR = NBRICK_ROWS / NCOLORS;
	
/** Number of turns */
	private static final int NTURNS = 3;

/** Animation cycle delay */
	private static final int DELAY = 10;
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setUp();
		play();
	}
	
/** Peforms the initial program set up */
	private void setUp() {
		addBall();
		addBricks();
		addPaddle();
		addMouseListeners();
	}
	
/** Game play loop */
	private void play() {
		while (!gameOver()) {
			pause(DELAY);
			moveBall();
			checkCollision();
		}
	}

/** adds the bricks */
	private void addBricks() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			int brickY = BRICK_Y_OFFSET + ((BRICK_HEIGHT + BRICK_SEP) * i);
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				int brickX = (BRICK_SEP / 2) + (BRICK_WIDTH + BRICK_SEP) * j;
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				brick.setLocation(brickX, brickY);
				setBrickColor(brick, i);
				bricksRemaining++;
				add(brick);
			}
		}
	}

/** 
 * Sets the color for a brick object based on its row and predefined colors,
 * the boundary color was delibarately left as black
 * @param brick GRect objet which will have its filled color assigned
 * @param brickRow The row index value for the brick from top down
 */
	private void setBrickColor(GRect brick, int brickRow) {
		brick.setFilled(true);
		int colorIndex = brickRow / NBRICK_ROWS_PER_COLOR;
		switch (colorIndex) {
		case 0:
			brick.setFillColor(Color.RED);
			break;
		case 1:
			brick.setFillColor(Color.ORANGE);
			break;
		case 2:
			brick.setFillColor(Color.YELLOW);
			break;
		case 3:
			brick.setFillColor(Color.GREEN);
			break;
		case 4:
			brick.setFillColor(Color.BLUE);
			break;
		default:
			brick.setFillColor(Color.BLACK);
		break;
		}
	}
	
/** add Paddle to window */
	private void addPaddle() {
		int initialPaddleX = (WIDTH - PADDLE_WIDTH) / 2;
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setLocation(initialPaddleX, PADDLE_Y);
		add(paddle);
	}
	
/**
 *  Called when mouse moves position, trigering the Paddle movement
 */
	public void mouseMoved(MouseEvent e) {
		movePaddle(e.getX());
	}
	
/**
 * Moves the paddle to middle of mouse current position without moving
 * out of window boundaries
 * @param x Mouse current X position
 */
	private void movePaddle(int x) {
		x = x - (PADDLE_WIDTH / 2);
		x = Math.max(x, 0);
		x = Math.min(x, PADDLE_MAX_X);
		paddle.setLocation(x, PADDLE_Y);
	}

/**
 * Sets ball initial speeds and add it to the program window
 */
	private void addBall() {
		// random number generator used as described in the assignment
		vx = rgen.nextDouble (1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = - vx;
		vy = 3.0;
		if (ball == null) {
			ball = new GOval(BALL_RADIUS * 2, BALL_RADIUS * 2);
			ball.setFilled(true);
			ball.setLocation((WIDTH / 2) - BALL_RADIUS, (HEIGHT / 2) - BALL_RADIUS);
			add(ball);
		}
		ball.setLocation((WIDTH / 2) - BALL_RADIUS, (HEIGHT / 2) - BALL_RADIUS);
	}
	
/**
 * Move the ball using its velocities and bounces it if it went over the window
 * boundaries
 */
	private void moveBall() {
		ball.move(vx, vy);
		// Check for window boudaries
		
		// Top wall
		if (ball.getY() <= 0) {
			ball.setLocation(ball.getX(), -ball.getY());
			vy = -vy;
		}
		// Left wall
		if (ball.getX() <= 0) {
			ball.setLocation(-ball.getX(), ball.getY());
			vx = -vx;
		}
		// Right wall
		if (ball.getX() + (2 * BALL_RADIUS) >= WIDTH) {
			// How much in x direction the ball went over the wall
			double xOver = ball.getX() - (WIDTH - (2 * BALL_RADIUS));
			// Reflected ball x
			double reflectedX = (WIDTH - xOver) - (2 * BALL_RADIUS);
			
			ball.setLocation(reflectedX, ball.getY());
			vx = -vx;
		}
	}
	
/**
 * Remove objects colliding with the ball and changes the ball velocities 
 * to bounce according to the collision
 */
	private void checkCollision() {
		GObject collider = getHorizontalCollidingObject();
		if (collider != null) {
			if (collider == paddle) {
				vx = -vx;
			} else {
				vx = -vx;
				remove(collider);
				bricksRemaining--;
			}
		} else {
			collider = getVerticalCollidingObject();
			if (collider != null) {
				if (collider == paddle) {
					if (ball.getY() <= PADDLE_Y) vy = - Math.abs(vy);
				} else {
					vy = -vy;
					remove(collider);
					bricksRemaining--;
				}
			}
		}		
	}
	
/**
 * Checks if any object is colliding with a ball boundary box vertices
 * @return the object collinding or null if there isn't any
 */
	private GObject getVerticalCollidingObject() {
		if (getElementAt(ball.getX(), ball.getY()) != null) {
			return getElementAt(ball.getX(), ball.getY());
		}
		if (getElementAt(ball.getX() + (2 * BALL_RADIUS), ball.getY()) != null) {
			return getElementAt(ball.getX() + (2 * BALL_RADIUS), ball.getY());
		}
		if (getElementAt(ball.getX(), ball.getY() + (2 * BALL_RADIUS)) != null) {
			return getElementAt(ball.getX(), ball.getY() + (2 * BALL_RADIUS));
		}
		if (getElementAt(ball.getX() + (2 * BALL_RADIUS),
				ball.getY()+ (2 * BALL_RADIUS)) != null) {
			return getElementAt(ball.getX() + (2 * BALL_RADIUS),
					ball.getY() + (2 * BALL_RADIUS));
		}
		return null;
	}
	
/**
 * Checks if there is any object colliding with the ball sides at half the ball height
 * @return element colliding with sides of the ball or null if there isn't
 */
	private GObject getHorizontalCollidingObject() {
		if (getElementAt(ball.getX(), ball.getY() + BALL_RADIUS) != ball) {
			return getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
		}
		if (getElementAt(ball.getX() + (2 * BALL_RADIUS),
				ball.getY() + BALL_RADIUS) != ball) {
			return getElementAt(ball.getX() + (2 * BALL_RADIUS),
					ball.getY() + BALL_RADIUS);
		}
		return (null);
	}
	
/**
 * Checks for end of turn conditions and end of game condition
 * @return true if the player has turns remaining or all bricks were destroyed
 */
	private boolean gameOver() {
		if (ball.getY() + (2 * BALL_RADIUS) >= HEIGHT) {
			turnsPlayed++;
			addBall();
		}
		if (bricksRemaining == 0) return true;
		if (turnsPlayed < NTURNS) return false;
		return true;
	}
		
	/* Private instance variables */
	private GRect paddle;
	private GOval ball;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	// Ball x and y velocities
	private double vx, vy;
	private int turnsPlayed = 0;
	private int bricksRemaining = 0;
}
