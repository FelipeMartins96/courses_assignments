/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {

	public void run() {
		moveToNewspaper();
		collectNewspaper();
		returnHome();
	}
	
	/*
	 * Pre-condition: Karen on initial position
	 * Post-condition: Karen on newspaper(Beeper) position
	 * 				   facing east
	 */
	private void moveToNewspaper() {
		move();
		move();
		turnRight();
		move();
		turnLeft();
		move();
	}
	
	/*
	 *  Collect beeper on current position
	 *  Pre-condition: Karen on Beeper location
	 */
	private void collectNewspaper() {
		pickBeeper();
	}
	
	/*
	 * Pre-condition: Karen on newspaper(Beeper) position
	 * 				   facing east
	 * Post-condition: Karen on initial position facing east
	 */
	private void returnHome() {
		turnAround();
		move();
		move();
		move();
		turnRight();
		move();
		turnRight();
	}
}
