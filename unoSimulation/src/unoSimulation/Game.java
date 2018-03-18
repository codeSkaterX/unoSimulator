package unoSimulation;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import unoSimulation.Card.Color;


public class Game {
	private Deck myDeck;  // Deck variable

	private ArrayList<Player> playerList; // Player list

	private ArrayList<Player> playerOrder; // Player order

	private int direction = 1; // Direction game is going
	private int currentTurn; // Current turn

	private final int gameCount; // Number of games to be played

	private int victoryCounter = 0; // Keeps track of wins
	private int turnCounter = 0; // Keeps track of turns
	private int turnTotal = 0; // Keeps track of turn total

	private int[] winTracker; // Keeps track of wins for each position

	private Player currentPlayer; // Keeps track of player taking current turn

	private boolean victory; // Used for deciding if game is over or not

	public Game() {
		gameCount = 1000;
		
		myDeck = new Deck();
		playerList = new ArrayList<Player>();
		playerOrder = new ArrayList<Player>();
	
		victory = false;
	}
	
	public Game(int gamesToBePlayed){
		gameCount = gamesToBePlayed;
		
		myDeck = new Deck();
		playerList = new ArrayList<Player>();
		playerOrder = new ArrayList<Player>();
	
		victory = false;
	}

	public void addPlayer(Player newPlayer) {
		playerList.add(newPlayer);
	}

	public void startGame() {
		winTracker = new int[playerList.size()];
		
		while (victoryCounter < gameCount) {
			
			
			currentTurn = ThreadLocalRandom.current().nextInt(0,
					playerList.size() - 1 + 1);
			// Randomly decides who goes first

			for (int i = currentTurn; i < playerList.size(); i++) { // Adds players
				playerOrder.add(playerList.get(i)); // in order of which they play
			}
			
			// Has two loops so it can loop around whole player list
			for (int i = 0; i < currentTurn; i++) {
				playerOrder.add(playerList.get(i));
			}
			
			myDeck.startingDeal(playerList); // Deals out the cards

			System.out.println("Hand sizes: ");
			for(Player myPlayer : playerList){
				System.out.print(myPlayer.handSize() + " ");
			}
			
			System.out.println();
			
			System.out.println("Deck size: " + myDeck.getSize());
			System.out.println("Discard size: " + myDeck.getDiscardSize());
			
			currentPlayer = playerList.get(currentTurn);
			playerTurn();

			turnCounter++; 
			
			while (victory == false) {
				currentTurn = currentTurn + direction;
				if (currentTurn < 0) {
					currentTurn = playerList.size() + currentTurn;
				} else if (currentTurn >= playerList.size()) {
					currentTurn = currentTurn - playerList.size();
				}
				currentPlayer = playerList.get(currentTurn);
				victory = playerTurn();

				turnCounter++;
			}

			victoryCounter++; // Adds to victory count
			
			System.out.println("Turn finished: " + turnCounter);
			System.out.println(currentPlayer.getName() + " has won game #" + victoryCounter + "!!");

			turnTotal += turnCounter; // Adds to total turn count
			turnCounter = 0;  // Resets turn counter

			currentPlayer.addWin(); // Adds win to the player
			
			int i = 0;
			while(victory == true){
				if (currentPlayer.getName().equals(playerOrder.get(i).getName())) {
					winTracker[i]++;
					victory = false;
				}
				i++;
			}

			playerOrder.clear(); // Resets player order
			myDeck.resetDeck(playerList); // Resets deck to restart game
		}
		System.out.println("Victory!!!");
		System.out.println("Total turns: " + turnTotal);
		for (Player endPlayer : playerList) {
			System.out.println(
					endPlayer.getName() + " ends with " + endPlayer.getWins());
		}

		System.out.println("Average game length: " + turnTotal / gameCount);

		System.out.println("Wins per player:\n");
		
		for(int playerWins : winTracker){
			System.out.println(playerWins);
		}
		
	}

	private boolean playerTurn() {
		if (currentPlayer.handSize() == 0) { // Player won the game!
			return true;
		}
		
		Card possibleCard = currentPlayer.Ai(myDeck.wildColor,
				myDeck.getCurrentCard()); // Trys to see if player will play

		// If player can't play...
		while (possibleCard == null) {
			System.out.println(
					"Can't play on " + myDeck.getCurrentCard().toString());
			System.out.println(currentPlayer);
			if (myDeck.getCurrentCard().getColor() == Color.Wild) {
				System.out.println("Wild color is " + myDeck.wildColor);
				
			}
			
			if(!myDeck.giveCard(currentPlayer)){ // Gives card to player
				for(Player myPlayer : playerList){
					System.out.println(myPlayer);
				}
				System.exit(0);
			}	
				
			// Now sees if they can play
			possibleCard = currentPlayer.Ai(myDeck.getWildColor(),
					myDeck.getCurrentCard());
		}

		if (possibleCard.getValue() == 10) {
			currentTurn += direction; // Skips next player

		} else if (possibleCard.getValue() == 11) {
			direction = 0 - direction; // Reverses direction of turns

		} else if (possibleCard.getValue() == 12) {
			int nextTurn;
			
			nextTurn = currentTurn + direction;
			while (nextTurn < 0) {
				nextTurn = playerList.size() + nextTurn;
			}
			while (nextTurn > playerList.size() - 1) {
				nextTurn = nextTurn - playerList.size();
			}

			Player targetPlayer = playerList.get(nextTurn);

			myDeck.normalDraw(targetPlayer);

			currentTurn += direction;
		} else if (possibleCard.getValue() == 13) {
			myDeck.setWildColor(currentPlayer.wildColorAi());
			// Player sets wild color
			
		} else if (possibleCard.getValue() == 14) {
			int nextTurn;
			
			nextTurn = currentTurn + direction;
			if (nextTurn < 0) {
				nextTurn = playerList.size() + nextTurn;
			} else if (nextTurn > playerList.size() - 1) {
				nextTurn = nextTurn - playerList.size();
			}

			//Target player is next player in turn order
			Player targetPlayer = playerList.get(nextTurn);

			myDeck.wildDraw(targetPlayer); // Player draws cards
			myDeck.setWildColor(currentPlayer.wildColorAi()); 
			// Player sets wild color

			currentTurn += direction; // Skips next player
		}

		//Player plays selected card
		myDeck.playCard(currentPlayer, possibleCard); 

		if (currentPlayer.handSize() == 0) { // Player won the game!
			return true;
		}

		return false;
	}
	
}
