// GameManager.java
package com.akstudios.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class GameManager {

    private static final int EMPTY_SLOT = 2; // Represents an empty slot on the board
    private static final int YELLOW = 0; // Represents the yellow player
    private static final int RED = 1; // Represents the red player

    private int activePlayer; // Tracks the current active player
    private int[] gameState; // Tracks the state of the board (each slot)
    private boolean gameActive; // Indicates whether the game is active or not

    private static final int[][] WINNING_POSITIONS = {
            // All possible winning combinations on the board
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    private Context context; // Context for accessing resources
    private Button playAgainButton; // Button to restart the game
    private TextView winnerTextView; // TextView to display the game result
    private GridLayout gridLayout; // Layout representing the game board

    public GameManager(Context context, Button playAgainButton, TextView winnerTextView, GridLayout gridLayout) {
        this.context = context;
        this.playAgainButton = playAgainButton;
        this.winnerTextView = winnerTextView;
        this.gridLayout = gridLayout;
        resetGame(); // Initialize the game state
    }

    public void resetGame() {
        // Reset the game state to start a new game
        gameState = new int[9];
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = EMPTY_SLOT; // Set all slots to empty
        }
        activePlayer = YELLOW; // Yellow starts the game
        gameActive = true; // Set the game as active

        playAgainButton.setVisibility(View.INVISIBLE); // Hide the play again button
        winnerTextView.setVisibility(View.INVISIBLE); // Hide the winner text

        clearGrid(); // Clear the board visuals
    }

    private void clearGrid() {
        // Clear all images from the board
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.setImageDrawable(null); // Remove the drawable from the slot
        }
    }

    public void dropIn(View view) {
        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString()); // Get the tapped slot index

        if (isMoveValid(tappedCounter)) {
            updateGameState(tappedCounter, counter); // Update the game state for the move
            checkForWinner(); // Check if there's a winner or a draw
        }
    }

    private boolean isMoveValid(int tappedCounter) {
        // Check if the move is valid (slot is empty and game is active)
        return gameState[tappedCounter] == EMPTY_SLOT && gameActive;
    }

    private void updateGameState(int tappedCounter, ImageView counter) {
        // Update the state of the game and animate the move
        gameState[tappedCounter] = activePlayer;
        counter.setTranslationY(-1500); // Start the counter above the board

        if (activePlayer == YELLOW) {
            counter.setImageResource(R.drawable.yellow); // Set yellow counter
            activePlayer = RED; // Switch to red player
        } else {
            counter.setImageResource(R.drawable.red); // Set red counter
            activePlayer = YELLOW; // Switch to yellow player
        }

        counter.animate().translationYBy(1500).rotation(3600).setDuration(800); // Animate the drop-in
    }

    private void checkForWinner() {
        for (int[] position : WINNING_POSITIONS) {
            if (isWinningPosition(position)) {
                gameActive = false; // Stop the game
                displayWinner(); // Display the winner
                return;
            }
        }

        if (isBoardFull()) {
            gameActive = false; // Stop the game
            displayDraw(); // Display draw message
        }
    }

    private boolean isWinningPosition(int[] position) {
        // Check if a specific position is a winning combination
        return gameState[position[0]] == gameState[position[1]] &&
                gameState[position[1]] == gameState[position[2]] &&
                gameState[position[0]] != EMPTY_SLOT;
    }

    private boolean isBoardFull() {
        // Check if the board is completely filled
        for (int state : gameState) {
            if (state == EMPTY_SLOT) {
                return false; // Found an empty slot
            }
        }
        return true; // Board is full
    }

    private void displayWinner() {
        // Display the winner's name
        String winner = (activePlayer == RED) ? "Yellow" : "Red"; // Determine the winner
        winnerTextView.setText(winner + " has won!"); // Set the winner message
        playAgainButton.setVisibility(View.VISIBLE); // Show the play again button
        winnerTextView.setVisibility(View.VISIBLE); // Show the winner text
    }

    private void displayDraw() {
        // Display a draw message
        winnerTextView.setText("It's a draw!"); // Set the draw message
        playAgainButton.setVisibility(View.VISIBLE); // Show the play again button
        winnerTextView.setVisibility(View.VISIBLE); // Show the draw text
    }
}
