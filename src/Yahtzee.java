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
        playerScores = new int[nPlayers][N_CATEGORIES + 1];
        categoriesUsed = new boolean[nPlayers][N_CATEGORIES];
        playGame();
	}

	private void playGame() {
		/* You fill this in */
		display.printMessage("Game Started...");

        dice = new int[N_DICE];
//        for(int i=0; i< N_DICE; i++) dice[i] = i+1;

        for(int turn = 0; turn < N_SCORING_CATEGORIES; turn++) {
            for (int playerUp = 1; playerUp <= nPlayers; playerUp++) {
                display.printMessage(playerNames[playerUp-1] + "'s turn. Click \"Roll Dice\" to begin.");
                display.waitForPlayerToClickRoll(playerUp);
//            display.displayDice(dice);
                initialRoll();
                for (int roll = 1; roll < 3; roll++) {
                    display.printMessage("Roll #" + roll + ". Select dice to reroll.");
                    display.waitForPlayerToSelectDice();
                    rollDice();
                }
                display.printMessage("Last Roll. Select Category to Score...");
                int categoryChoice;
                while(true) {
                    categoryChoice = display.waitForPlayerToSelectCategory();
                    if(!categoriesUsed[playerUp - 1][categoryChoice]) {
                        categoriesUsed[playerUp - 1][categoryChoice] = true;
                        break;
                    }
                    display.printMessage("You have used that category already. Please choose another one.");
                }
                int score = scoreChoice(categoryChoice);
                playerScores[playerUp - 1][categoryChoice] = score;
                display.updateScorecard(categoryChoice, playerUp, score);
//                soutDice();
            }
        }

        int winner = totalScores();
        display.printMessage(playerNames[winner] + " is the winner!");
	}

	private int totalScores() {
        int highScore = 0;
        int winner = -1;
        int upperScore;
        int lowerScore;
        int total;
        for (int player = 0; player < nPlayers; player++) {
            // compute Upper Score
            upperScore = 0;
            for(int category = ONES; category <= SIXES; category++) {
                upperScore += playerScores[player][category];
            }
            playerScores[player][UPPER_SCORE] = upperScore;
            display.updateScorecard(UPPER_SCORE, player+1, upperScore);

            // compute Upper Bonus
            if(upperScore >= 63) {
                playerScores[player][UPPER_BONUS] = 35;
            } else {
                playerScores[player][UPPER_BONUS] = 0;
            }
            display.updateScorecard(UPPER_BONUS, player+1, playerScores[player][UPPER_BONUS]);

            // compute Lower Score
            lowerScore = 0;
            for(int category = THREE_OF_A_KIND; category <= CHANCE; category++) {
                lowerScore += playerScores[player][category];
            }
            playerScores[player][LOWER_SCORE] = lowerScore;
            display.updateScorecard(LOWER_SCORE, player+1, lowerScore);
            total = playerScores[player][UPPER_SCORE] +
                    playerScores[player][UPPER_BONUS] +
                    playerScores[player][LOWER_SCORE];
            playerScores[player][TOTAL] = total;
            display.updateScorecard(TOTAL, player+1, total);

            // track high score for the game
            if (total > highScore) {
                highScore = total;
                winner = player;
            }
        }
        // return the player with the winning score
        return winner;
    }

	private int scoreChoice(int category) {
        if(category <= 6) {
            return countPips(category);
        } else {
            if(YahtzeeLogic.checkCategory(dice, category)) {
                switch (category) {
                    case THREE_OF_A_KIND:
                    case FOUR_OF_A_KIND:
                    case CHANCE:
                        int score = 0;
                        for (int i = 0; i < N_DICE; i++) {
                            score += dice[i];
                        }
                        return score;
                    case FULL_HOUSE:
                        return 25;
                    case SMALL_STRAIGHT:
                        return 30;
                    case LARGE_STRAIGHT:
                        return 40;
                    case YAHTZEE:
                        return 50;
                    default:
                }
            }
        }
        return 0;
    }
    private int countPips(int value) {
        int count = 0;
        for (int i = 0; i < N_DICE; i++) {
            if(dice[i] == value) count += value;
        }
        return count;
    }
	private void soutDice() {
        System.out.print("Current dice state: ");
        for (int i = 0; i < N_DICE; i++) System.out.print((i+1) + ":" + dice[i] + " ");
        System.out.println("");
    }
	private void initialRoll() {
        for(int i = 0; i < N_DICE; i++) {
            dice[i] = rgen.nextInt(1, 6);
        }
        display.displayDice(dice);
    }
	private void rollDice() {
        for(int i = 0; i < N_DICE; i++) {
            if(display.isDieSelected(i)) {
                dice[i] = rgen.nextInt(1, 6);
            }
        }
        display.displayDice(dice);
    }
		
/* Private instance variables */
	private int nPlayers;
    private int[][] playerScores;
    private boolean[][] categoriesUsed;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
    private int[] dice;

}
