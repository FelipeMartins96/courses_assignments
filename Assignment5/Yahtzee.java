/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		int[] dice = new int[N_DICE];
		int[][] scores = new int[N_CATEGORIES][nPlayers];
		// Initializes as false
		boolean[][] usedCategories = new boolean[N_CATEGORIES][nPlayers];
		
		
		// Number of rounds is equal number of categories
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			
			for (int j = 0; j < nPlayers; j++) {
				resetDice(dice);
				
				display.printMessage(playerNames[j] + 
						"'s turn. Click \"Roll Dice\" button to roll the dice.");
				
				// + 1 because YahtzeeDisplay class count players starting at 1
				display.waitForPlayerToClickRoll(j + 1);
				
				rollDice(dice);
				display.displayDice(dice);
				
				// Prompt to re-roll dices
				for (int k = 0; k < 2; k++) {
					display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
					display.waitForPlayerToSelectDice();
					selectDice(dice);
					rollDice(dice);
					display.displayDice(dice);
				}
				
				// DEBUG //////////////////////
				//IODialog dialog = getDialog();
				//for (int k = 0; k < N_DICE; k++) {
				//	dice[k] = dialog.readInt("Enter value for die " + (k + 1));
				//}
				//display.displayDice(dice);
				// DEBUG END //////////////////
				
				// Prompt to select category
				int selectedCategory;
				while (true) {
					display.printMessage("Select a category for this roll");
					selectedCategory = display.waitForPlayerToSelectCategory();
					if (!usedCategories[selectedCategory][j]) break;
				}
				usedCategories[selectedCategory][j] = true;
				int score = calculateScore(dice, selectedCategory);
				display.updateScorecard(selectedCategory, j + 1, score);
				
				scores[selectedCategory - 1][j] = score;
			}
		}
		
		displayFinalScores(scores);
		
		int winner = calculateWinner(scores[TOTAL - 1]);
		display.printMessage("Congratulations, " + playerNames[winner] + 
		", you're the winner with a total score of " + scores[TOTAL - 1][winner] + "!");
	}


	// return the index of the player with the highest total score
	private int calculateWinner(int[] total) {
			int winner = 0;
			int highestScore = 0;
			for (int i = 0; i < nPlayers; i++) {
				if (total[i] > highestScore) {
					winner = i;
					highestScore = total[i];
				}
			}
			return winner;
		}
	
	// Calculate and add to display the end of game scores
	private void displayFinalScores(int[][] scores) {
			for (int i = 0; i < nPlayers; i++) {
				
				// Calculate Upper Score
				int upperScore = 0;
				for ( int j = ONES - 1; j < SIXES; j++) {
					upperScore += scores[j][i];
				}
				scores[UPPER_SCORE - 1][i] = upperScore;
				display.updateScorecard(UPPER_SCORE, i + 1, scores[UPPER_SCORE - 1][i]);
				
				// Calculate Upper Bonus
				int upperBonus = 0;
				if (upperScore >= UPPER_BONUS_TRIGGER) {
					upperBonus = 35;
				}
				scores[UPPER_BONUS - 1][i] = upperBonus;
				display.updateScorecard(UPPER_BONUS, i + 1, scores[UPPER_BONUS - 1][i]);
				
				// Calculate Lower Score
				int lowerScore = 0;
				for (int j = THREE_OF_A_KIND - 1; j < TOTAL; j++) {
					lowerScore += scores[j][i];
				}
				scores[LOWER_SCORE - 1][i] = lowerScore;
				display.updateScorecard(LOWER_SCORE, i + 1, scores[LOWER_SCORE - 1][i]);
	
				// Calculate Total
				int total = upperScore + upperBonus + lowerScore;
				scores[TOTAL - 1][i] = total;
				display.updateScorecard(TOTAL, i + 1, scores[TOTAL - 1][i]);
			}
			
		}
	
	// Calculate the score for a selected category
	private int calculateScore(int[] dice, int category) {
		int[] distribution = {0, 0, 0, 0, 0, 0};
		diceDistribution(distribution, dice);
		int sequenceCount = 0;
		
			switch (category) {
				case ONES:
					return sumMatchingDice(dice, ONES);
					
				case TWOS:
					return sumMatchingDice(dice, TWOS);
					
				case THREES:
					return sumMatchingDice(dice, THREES);
					
				case FOURS:
					return sumMatchingDice(dice, FOURS);
					
				case FIVES:
					return sumMatchingDice(dice, FIVES);
					
				case SIXES:
					return sumMatchingDice(dice, SIXES);
					
				case THREE_OF_A_KIND:
					for (int i = 0; i < 6; i++) {
						if (distribution[i] >= 3) {
							return sumDice(dice);
						}
					}
					break;
					
				case FOUR_OF_A_KIND:
					for (int i = 0; i < 6; i++) {
						if (distribution[i] >= 4) {
							return sumDice(dice);
						}
					}
					break;
					
				case FULL_HOUSE:
					for (int i = 0; i < 6; i++) {
						if (distribution[i] == 3) {
							for (int j = 0; j < 6; j++) {
								if (distribution[j] == 2) {
									return 25;
								}
							}
						}
					}
					break;
					
				case SMALL_STRAIGHT:
					for (int i = 0; i < 6; i++) {
						if (distribution[i] != 0) {
							sequenceCount += 1;
						} else {
							sequenceCount = 0;
						}
						if (sequenceCount == 4) {
							return 30;
						}
					}
					break;
					
				case LARGE_STRAIGHT:
					for (int i = 0; i < 6; i++) {
						if (distribution[i] != 0) {
							sequenceCount += 1;
						} else {
							sequenceCount = 0;
						}
						if (sequenceCount == 4) {
							return 40;
						}
					}
					break;
					
				case YAHTZEE:
					for (int i = 0; i < 6; i++) {
						if (distribution[i] == 5) {
							return 50;
						}
					}
					break;
					
				case CHANCE:
					return sumDice(dice);
			}
		return 0;
	}
	
	private void diceDistribution(int[] distribution, int[] dice) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < N_DICE; j++) {
				if (dice[j] == (i + 1)) {
					distribution[i]++;
				}
			}
		}
	}

	// Sum of values of every dice
	private int sumDice(int[] dice) {
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			score += dice[i];
		}
		return score;
	}
	
	// Sum of values for dice of specific value
	private int sumMatchingDice(int[] dice, int value) {
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == value) {
				score += value;
			}
		}
		return score;
	}
	
	// flag dice selected to be re-rolled
	private void selectDice(int[] dice) {
			for (int i = 0; i < N_DICE; i++) {
				if (display.isDieSelected(i)) {
					dice[i] = 0;
				}
			}
			
		}
	
	// roll flagged dice
	private void rollDice(int[] dice) {
			for (int i = 0; i < N_DICE; i++) {
				if (dice[i] == 0) {
					dice[i] = rgen.nextInt(1, 6);
				}
			}
		}
	
	// flag every dice to be re-rolled
	private void resetDice(int[] dice) {
			for (int i = 0; i < N_DICE; i ++) {
				dice[i] = 0;
			}
			
		}

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
