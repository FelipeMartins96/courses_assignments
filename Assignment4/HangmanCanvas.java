/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

	private void removeBody() {
		remove(head);
		remove(body);
		remove(leftArm);
		remove(leftArmUpper);
		remove(rightArm);
		remove(rightArmUpper);
		remove(leftLegHip);
		remove(leftLeg);
		remove(rightLegHip);
		remove(rightLeg);
		remove(leftFoot);
		remove(rightFoot);

	}
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		if (scaffold == null) {
			addGraphicObjects();
		}
		if (guessedChars != null) remove(guessedChars);
		removeBody();
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		if (guessedWord == null) {
			guessedWord = new GLabel(word);
			guessedWord.setFont("SANS_SERIF-16");
			guessedWord.setLocation((getWidth() / 2) - BEAM_LENGTH,
					((getHeight() / 2) + OFFSET) + 2 * guessedWord.getAscent());
			add(guessedWord);
		} else {
			guessedWord.setLabel(word);
		}
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		if (guessedChars == null) {
			// Guessed Chars
			guessedChars = new GLabel("");
			guessedChars.setFont("SANS_SERIF");
			guessedChars.setLocation((getWidth() / 2) - BEAM_LENGTH,
					((getHeight() / 2) + OFFSET) + 4 * guessedWord.getAscent());
			add(guessedChars);
			incorrectGuessedChars = "";
		}
		incorrectGuessedChars += letter;
		guessedChars.setLabel(incorrectGuessedChars);
	}

	private void addGraphicObjects() {

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		
		GPoint point = new GPoint();
		point.setLocation(centerX - BEAM_LENGTH,
				centerY + OFFSET);
		
		// Add Scaffold		
		scaffold = new GLine(point.getX(), point.getY(),
				point.getX(), point.getY() - SCAFFOLD_HEIGHT);
		add(scaffold);
		
		// Add Beam
		point.setLocation(point.getX(), point.getY() - SCAFFOLD_HEIGHT);
		
		beam = new GLine(point.getX(), point.getY(),
				point.getX() + BEAM_LENGTH,  point.getY());
		add(beam);
		
		// Add Rope
		point.setLocation(point.getX() + BEAM_LENGTH, point.getY());
		
		rope = new GLine(point.getX(), point.getY(),
				point.getX(), point.getY() + ROPE_LENGTH);
		add(rope);
		
		// Add Head
		point.setLocation(point.getX() - HEAD_RADIUS,
				point.getY() + ROPE_LENGTH);
		
		head = new GOval(HEAD_RADIUS * 2, HEAD_RADIUS * 2);
		head.setLocation(point);
		add(head);
		
		// Add Body
		point.setLocation(point.getX() + HEAD_RADIUS,
				point.getY() + (2 * HEAD_RADIUS));
		
		body = new GLine(point.getX(), point.getY(),
				point.getX(), point.getY() + BODY_LENGTH);
		add(body);
		
		
		point.setLocation(point.getX(),
				point.getY() + ARM_OFFSET_FROM_HEAD);
		// add Left Arm
		leftArmUpper = new GLine(point.getX(), point.getY(),
				point.getX() - UPPER_ARM_LENGTH, point.getY());
		leftArm = new GLine(point.getX() - UPPER_ARM_LENGTH,
				point.getY(), point.getX() - UPPER_ARM_LENGTH,
				point.getY() + LOWER_ARM_LENGTH);
		add(leftArmUpper);
		add(leftArm);
		
		// add Left Arm
		rightArmUpper = new GLine(point.getX(), point.getY(),
				point.getX() + UPPER_ARM_LENGTH, point.getY());
		rightArm = new GLine(point.getX() + UPPER_ARM_LENGTH,
				point.getY(), point.getX() + UPPER_ARM_LENGTH,
				point.getY() + LOWER_ARM_LENGTH);
		add(rightArmUpper);
		add(rightArm);
		
		point.setLocation(point.getX(),
				point.getY() + BODY_LENGTH - ARM_OFFSET_FROM_HEAD);
		
		// add Left Leg
		leftLegHip = new GLine(point.getX(), point.getY(), 
				point.getX() - HIP_WIDTH, point.getY());
		leftLeg = new GLine(point.getX() - HIP_WIDTH, point.getY(),
				point.getX() - HIP_WIDTH, point.getY() + LEG_LENGTH);
		add(leftLegHip);
		add(leftLeg);
		
		// add Right Leg
		rightLegHip = new GLine(point.getX(), point.getY(), 
				point.getX() + HIP_WIDTH, point.getY());
		rightLeg = new GLine(point.getX() + HIP_WIDTH, point.getY(),
				point.getX() + HIP_WIDTH, point.getY() + LEG_LENGTH);
		add(rightLegHip);
		add(rightLeg);
		
		
		point.setLocation(point.getX() - HIP_WIDTH,
				point.getY() + LEG_LENGTH);
		
		// add Left Foot
		leftFoot = new GLine(point.getX(), point.getY(),
				point.getX() - FOOT_LENGTH, point.getY());
		add(leftFoot);
		
		// add Right Foot
		rightFoot = new GLine(point.getX() + (2 * HIP_WIDTH), point.getY(),
				point.getX() + (FOOT_LENGTH + (2 * HIP_WIDTH)), point.getY());
		add(rightFoot);
		
	}
	
	
/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	private static final int OFFSET = 150;

	
	
 /* Graphic Objects */
	private GLine scaffold;
	private GLine beam;
	private GLine rope;
	private GOval head;
	private GLine body;
	private GLine leftArm;
	private GLine leftArmUpper;
	private GLine rightArm;
	private GLine rightArmUpper;
	private GLine leftLeg;
	private GLine leftLegHip;
	private GLine rightLeg;
	private GLine rightLegHip;
	private GLine leftFoot;
	private GLine rightFoot;
	
	private GLabel guessedWord;
	private GLabel guessedChars;
	
	private String incorrectGuessedChars;
	
	
	
	
	

}
