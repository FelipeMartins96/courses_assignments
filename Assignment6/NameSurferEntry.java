/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		// Create an int array of size NDECADES to store ranks
		rank = new int[NDECADES];
		
		// Tokenize the inputed string
		StringTokenizer tokenizer = new StringTokenizer(line);
		
		// Assign first token to name
		name = tokenizer.nextToken();
		
		// Assign the following tokens to each decade rank
		for (int i = 0; i < NDECADES; i++) {
			rank[i] = Integer.parseInt(tokenizer.nextToken());
		}
	}

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		return name;
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		return rank[decade];
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		String str = name + " [";
		for (int i = 0; i < NDECADES; i++) {
			str = str + rank[i];
			
			if (i == NDECADES - 1) {
				str = str + "]";
			}
			else {
				str = str + " ";
			}
		}
		return str;
	}
	
	/* Private instance variables */
	private String name;
	private int[] rank;
}

