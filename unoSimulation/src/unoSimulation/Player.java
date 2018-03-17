package unoSimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import unoSimulation.Card.Color;

public class Player {

	protected List<Card> Hand;

	private String myName;

	private static int playerCount = 1;

	int randomNum;
	private int wins = 0;

	public Player() {
		Hand = new ArrayList<>();
		myName = "" + playerCount;
		playerCount++;
	}

	public Player(String name) {
		Hand = new ArrayList<>();
		myName = name;
	}

	public String getName() {
		return myName;
	}

	public int getWins() {
		return wins;
	}

	public void addWin() {
		wins++;
	}

	public void removeCard(Card card) {
		Hand.remove(card);
	}

	public void addCard(Card newCard) {
		Hand.add(newCard);
	}

	// This method returns true if playing a card is possible, and false
	// if it is not.

	/*
	 * public boolean canPlay(Card currentCard) { for (Card card : Hand) { if
	 * (card.getColor() == Card.Color.Wild) { return true; } if (card.getColor()
	 * == currentCard.getColor()) { // Do colors // matchup return true; } if
	 * (card.getValue() == currentCard.getValue()) { // Do values // matchup
	 * return true; } } return false; // No playable cards }
	 */

	// This method returns a playable card, and null if there
	// are no playable cards.

	protected Card playableCard(Color wildColor, Card currentCard) {
		for (Card card : Hand) {
			if (card.getValue() == currentCard.getValue()) { // Do values
																// matchup
				return card;
			}
			if (card.getColor().equals(currentCard.getColor())) { // Do colors
																// matchup
				return card;
			}
			if (currentCard.getColor().equals(Color.Wild)
					&& card.getColor().equals(wildColor)) {
				// If currentCard is wild, does card match picked color?
				return card;
			}
			if (card.getColor() == Card.Color.Wild) {
				return card;
			}
		}
		return null; // No playable cards
	}

	// This method randomly selects a card to use. Simplest AI.

	public Card Ai(Color wildColor, Card currentCard) {
		Card possibleCard = playableCard(wildColor, currentCard);
		// Finds playable card

		return possibleCard; // Plays card
	}

	public int handSize() {
		return Hand.size();
	}

	public List<Card> clearHand() {
		List<Card> deadList = new ArrayList<Card>(Hand);

		Hand.clear();

		return deadList;
	}

	public Color wildColorAi() {
		randomNum = ThreadLocalRandom.current().nextInt(0,
				Color.values().length - 2 + 1);

		if (randomNum == 0) {
			return Color.Blue;
		}
		if (randomNum == 1) {
			return Color.Red;
		}
		if (randomNum == 2) {
			return Color.Green;
		} else {
			return Color.Yellow;
		}
	}
	
	public String toString(){
		int numRed = 0;
		int numBlue = 0;
		int numGreen = 0;
		int numYellow = 0;
		int numWild = 0;
		
		for(Card myCard : Hand){
			if(myCard.getColor() == Color.Red){
				numRed++;
			}
		}
		
		for(Card myCard : Hand){
			if(myCard.getColor() == Color.Blue){
				numBlue++;
			}
		}
		
		for(Card myCard : Hand){
			if(myCard.getColor() == Color.Green){
				numGreen++;
			}
		}
		
		for(Card myCard : Hand){
			if(myCard.getColor() == Color.Yellow){
				numYellow++;
			}
		}
		
		for(Card myCard : Hand){
			if(myCard.getColor() == Color.Wild){
				numWild++;
			}
		}
		
		return numRed + " " + numBlue + " " + numGreen + " " + numYellow + " " + numWild;
	}
}
