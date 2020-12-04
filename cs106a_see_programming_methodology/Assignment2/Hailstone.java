/*
 * File: Hailstone.java
 * Name: Felipe Martins
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {
		int steps = 0;
		int n = readInt("Enter a number: ");
		int previous_n = n;
		
		if (n >= 1){
			while (n != 1) {
				previous_n = n;
				
				// if even
				if (n % 2 == 0) {
					n = n / 2;
					println(previous_n + " is even so I take half: " + n);
				} else {
					n = 3 * n + 1;
					println(previous_n + " is odd, so I make 3n + 1: " + n);
				}
				steps++;	
			}
			
			println("The process took " + steps + " to reach 1");
		} else {
			println("Please input a number larger than 1");
		}
	}
}

