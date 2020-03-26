/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here 
 * solves the "repair the quad" problem from Assignment 1.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {

	public void run() {
		while (frontIsClear()){
			repairColumn();
			moveToNextColumn();
		}
		repairColumn();
	}
	
	/*
	 * Pre-condition: Bottom of unfixed column facing east
	 * Post-condition: Bottom of fixed column facing east
	 */
	private void repairColumn() {
		turnLeft();
		placeMissingStones();
		turnAround();
		returnBottomColumn();
		turnLeft();
	}
	
	/*
	 * Pre-condition: Bottom of column facing east
	 * Post-condition: Bottom of next column facing east
	 */
	private void moveToNextColumn() {
		for (int i = 0; i < 4; i++) {
			move();
		}
	}
	
	/*
	 * Pre-condition: Bottom of column facing north
	 * Post-condition: Top of column facing north and column fixed
	 */
	private void placeMissingStones() {
		while (frontIsClear()) {
			repairIfNeeded();
			move();
		}
		repairIfNeeded();
	}
	
	
	/*
	 * Pre-condition: Top of column facing north
	 * Post-condition: Bottom of column facing south
	 */
	private void returnBottomColumn() {
		while (frontIsClear()) {
			move();
		}
	}
	
	
	// Places Beeper in current location if there is none
	private void repairIfNeeded() {
		if (noBeepersPresent()) {
			putBeeper();
		}
	}
}
