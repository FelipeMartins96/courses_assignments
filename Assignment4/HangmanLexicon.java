/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import acm.util.*;
import java.io.*;
import java.util.ArrayList;

public class HangmanLexicon {
	
	public HangmanLexicon() {
		
		BufferedReader rd;	
		try {
			rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
		
		try {
			while (true) {
				String line = rd.readLine();
				if (line == null) break;
				lexicon.add(line);
			} 
			rd.close();
		}catch (IOException ex) {
			throw new ErrorException(ex);
		}
		
		
	}

/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return lexicon.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return lexicon.get(index);
	}
	
/* Instance Variables */
	ArrayList<String> lexicon = new ArrayList<String>();

}
