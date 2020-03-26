/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	public void run() {
		fillOddRow();
		while (frontIsClear()) {
			moveUp();
			fillEvenRow();
			if (frontIsClear()) {
				moveUp();
				fillOddRow();
			}
		}
	}

	/*
	 * Pre-condition: Leftmost position on row facing north
	 * Post-condition: One row above facing east
	 */
	private void moveUp() {
		move();
		turnRight();
	}
	
	/*
	 * Pre-condition: start of unfilled Odd numbered row facing east
	 * Post-condition: start of filled Odd numbered row facing North
	 */
	private void fillOddRow() {
		fillRow();
		returnToStartOfRow();
	}
	
	/*
	 * Pre-condition: start of unfilled Even numbered row facing east
	 * Post-condition: start of filled Even numbered row facing North
	 */
	private void fillEvenRow() {
		if (frontIsClear()) {
			move();
			fillRow();
		}
		returnToStartOfRow();
	}
	
	/*
	 * Fills row horizontally
	 */
	private void fillRow() {
		putBeeper();
		while (frontIsClear()) {
			move();
			if (frontIsClear()) {
				move();
				putBeeper();
			}
		}
	}
	
	/*
	 * Return to leftmost position of row and faces North
	 */
	private void returnToStartOfRow() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnRight();
	}
			
}
