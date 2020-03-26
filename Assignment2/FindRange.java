/*
 * File: FindRange.java
 * Name: Felipe Martins
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	
	/** Sentinel value which triggers the results execution */
	private static final int SENTINEL_VALUE = 0;
	
	public void run() {
		println("This program finds the largest and smallest numbers.");
		int input = readInt("? ");
		int smallest = input;
		int largest = input;
		

		if (input != SENTINEL_VALUE) {
			while (input != SENTINEL_VALUE) {
				if (input < smallest) {
					smallest = input;
				}
				if (input > largest) {
					largest = input;
				}
				input = readInt("? ");
			}
			println("smallest: " + smallest);
			println("largest: " + largest);
		} else {
			println("Sentinel entered as first input!");
		}
		
	}
}

