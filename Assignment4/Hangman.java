/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {

/** Max number of errors for a turn */
private static final int MAX_NUM_ERRORS = 8;
	
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);		
	}

    public void run() {
		lexicon = new HangmanLexicon();
		
		while (true) {
			// Get a random word from the lexicon
			String secretWord = lexicon.getWord(rgen.nextInt(0, lexicon.getWordCount() - 1));
			// Generate the guessed string
			String guessedWord = generateGuessedWord(secretWord.length());
			// Counter for number of errors
			int numErrors = 0;
			
			canvas.reset();
			
			println("Welcome to Hangman!");
			canvas.displayWord(guessedWord);
			// Continue looping until secret word is found or no guesses left
			while (numErrors < MAX_NUM_ERRORS && guessedWord.indexOf('-') != -1) {
				
				println("The word now looks like this: " + guessedWord);
				println("You have " + (MAX_NUM_ERRORS - numErrors) + " guesses left.");
				
				char ch = readChar("Your Guess: ");
				if (secretWord.indexOf(ch) != -1) {
					println("The guess is correct.");
					guessedWord = updateGuess(ch, secretWord, guessedWord);
				} else {
					println("There are no " + ch + "'s in the word.");
					canvas.noteIncorrectGuess(ch);	
					numErrors++;
				}
				canvas.displayWord(guessedWord);
			}
			
			if (numErrors < MAX_NUM_ERRORS) {
				println("You guessed the word: " + secretWord);
				println("You Win.");
			} else {
				println("You're completly hung.");
				println("The word was: " + secretWord);
				println("You lose.");
			}
			
			char cont;
			while (true) {
				cont = readChar("Continue (Y/N): ");
				if (cont == 'Y' || cont == 'N') break;
			}
			
			if (cont == 'N') break;
		}
	}
    
/**
 * Generate a guessed word initial string containing only '-' characters
 * @param secretWordLength length of the selected secret word
 * @return string containing only '-' characters with same size given by the param
 */
    private String generateGuessedWord(int secretWordLength) {
    	String guessedWord = "";
    	for (int i = 0; i < secretWordLength; i++) {
    		guessedWord += "-";
    	}
    	return guessedWord;
    }
    
/**
 * Reads a letter only char and converts to upper case
 * @param str string prompted to user for its input
 * @return upper case char inputed by user
 */
    private char readChar(String str) {
    	while (true) {
    		String input = readLine(str);
    		if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
    			return Character.toUpperCase(input.charAt(0));
    		} else {
    			println("Invalid Guess");
    		}
    	}
    }
    
/**
 * Update guessed word string substituting '-' character by guessed character
 * @param ch guessed character
 * @param secretWord string the player is trying to guess
 * @param guessedWord current string the player guessed
 * @return updated string of player guesses
 */
    private String updateGuess(char ch, String secretWord, String guessedWord) {
    	int index = secretWord.indexOf(ch);
		while (index != -1) {
			guessedWord = guessedWord.substring(0, index) +
					ch + guessedWord.substring(index + 1);
			if (index != (secretWord.length() - 1) && secretWord.substring(index + 1).indexOf(ch) != -1) {
				index += secretWord.substring(index + 1).indexOf(ch) + 1;
			} else {
				index = -1;
			}
		}
		return guessedWord;
    }
    
/* Private instance variables */
    private RandomGenerator rgen = RandomGenerator.getInstance();
    private HangmanLexicon lexicon;
    private HangmanCanvas canvas;
}