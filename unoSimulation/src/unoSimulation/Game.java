package unoSimulation;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import unoSimulation.Card.Color;

//To do: Fix empty deck and discard pile error

public class Game {
	Deck myDeck;

	ArrayList<Player> playerList;

	ArrayList<Player> playerOrder;

	int direction = 1;
	int currentTurn;

	final double gameCount = 10000;

	int victoryCounter = 0;
	int turnCounter = 0;

	int turnTotal = 0;

	int firstWins = 0;
	int secondWins = 0;
	int thirdWins = 0;

	int startingTurn = 0;

	Player currentPlayer;

	boolean victory = false;

	public Game() {
		myDeck = new Deck();
		playerList = new ArrayList<Player>();
		playerOrder = new ArrayList<Player>();
	}

	public void addPlayer(Player newPlayer) {
		playerList.add(newPlayer);
	}

	public void startGame() {
		while (victoryCounter < gameCount) {
			
			
			currentTurn = ThreadLocalRandom.current().nextInt(0,
					playerList.size() - 1 + 1);
			// Randomly decides who goes first

			for (int i = currentTurn; i < playerList.size(); i++) {
				playerOrder.add(playerList.get(i));
			}
			for (int i = 0; i < currentTurn; i++) {
				playerOrder.add(playerList.get(i));
			}

			myDeck.startingDeal(playerList);

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
	//		System.out.println("Turn finished: " + turnCounter);

	//		System.out.println(playerList.get(0).handSize() + " "
	//				+ playerList.get(1).handSize() + " "
	//				+ playerList.get(2).handSize());

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
	//			System.out.println("Turn finished: " + turnCounter);

	//			System.out.println(playerList.get(0).handSize() + " "
	//					+ playerList.get(1).handSize() + " "
	//					+ playerList.get(2).handSize());
			}

			victoryCounter++;
			
			System.out.println("Turn finished: " + turnCounter);
			System.out.println(currentPlayer.getName() + " has won game #" + victoryCounter + "!!");
			victory = false;

			turnTotal += turnCounter;
			turnCounter = 0;

			currentPlayer.addWin();
			if (currentPlayer.getName().equals(playerOrder.get(0).getName())) {
				firstWins++;
			} else if (currentPlayer.getName()
					.equals(playerOrder.get(1).getName())) {
				secondWins++;
			} else {
				thirdWins++;
			}

			playerOrder.clear();
			myDeck.resetDeck(playerList);
		}
		System.out.println("Victory!!!");
		System.out.println("Total turns: " + turnTotal);
		for (Player endPlayer : playerList) {
			System.out.println(
					endPlayer.getName() + " ends with " + endPlayer.getWins());
		}

		System.out.println("Average game length: " + turnTotal / gameCount);

		System.out.println("1st wins: " + firstWins);
		System.out.println("Second wins: " + secondWins);
		System.out.println("Third wins: " + thirdWins);
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

	/*
	 * private boolean playableCard(Card currentCard, Card givenCard){
	 * 
	 * if (card.getColor() == Card.Color.Wild) { return true; } if
	 * (card.getColor() == currentCard.getColor()) { // Do colors // matchup
	 * return true; } if (currentCard.cardColor == Card.Color.Wild &&
	 * card.getColor() == wildColor) { // If currentCard is wild, does card
	 * match picked color? return true; } if (card.getValue() ==
	 * currentCard.getValue()) { // Do values // matchup return true; } }return
	 * false; // No playable cards
	 */
}
