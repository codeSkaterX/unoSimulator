package unoSimulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import unoSimulation.Card.Color;

public class Deck {

	// 1-9 for normal numbers
	// 10 for skip
	// 11 for reverse
	// 12 for draw 2
	// 13 for wild
	// 14 for wild draw 4

	List<Card> myDeck;
	List<Card> discardPile;
	
	private final int startingHandSize = 7;
	private final int normalDrawCount = 2;
	private final int wildDrawCount = 4;
	private final int wildCount = 4;

	Card currentCard;
	
	Color wildColor;

	public Deck() {
		Color cardColor;
		
		myDeck = new ArrayList<>();
		discardPile = new ArrayList<>();

		cardColor = Color.Red;
		for (int x = 0; x < 5; x++) {
			if (x == 1) {
				cardColor = Color.Blue;
			} else if (x == 2) {
				cardColor = Color.Yellow;
			} else if (x == 3) {
				cardColor = Color.Green;
			} else if (x == 4) {
				cardColor = Color.Wild;
			}
			if (cardColor != Color.Wild) {
				myDeck.add(new Card(cardColor, 0));
				for (int i = 1; i < 13; i++) {
					myDeck.add(new Card(cardColor, i));
					myDeck.add(new Card(cardColor, i));
				}
			} else {
				for (int i = 13; i < 15; i++) {
					for (int y = 0; y < wildCount; y++) {
						myDeck.add(new Card(Card.Color.Wild, i));
					}
				}
			}
		}
	}

	public void Shuffle() {
		Collections.shuffle(myDeck);
	}
	
	public int getSize(){
		return myDeck.size();
	}
	
	public int getDiscardSize(){
		return discardPile.size();
	}

	public String toString() {
		String myString = "";

		for (int i = 0; i < myDeck.size(); i++) {
			myString = myString + myDeck.get(i) + " ";
		}

		return myString;
	}

	public boolean giveCard(Player myPlayer) {
		if (myDeck.size() == 0) {
			if(discardPile.size() == 0){
				System.out.println("Error! Empty discard pile and deck");
			}
			for (int i = 0; i < discardPile.size(); i++) {
				myDeck.add(discardPile.get(i));
			}
			discardPile.clear();
			Shuffle();
		}
		if(myDeck.size() == 0){
			System.out.println("Error! Deck still empty! "
		+ myDeck.size() + " " + discardPile.size());
			System.out.println(currentCard.getColor()
					+ " " + currentCard.getValue());
			return false;
		}
		myPlayer.addCard(myDeck.get(0));
		myDeck.remove(0);
		
		return true;
	}

	public void startingDeal(ArrayList<Player> playerList) {
		if (discardPile.size() != 0) {
			for (int i = 0; i < discardPile.size(); i++) {
				myDeck.add(discardPile.get(i));
			}
			for (int i = 0; !discardPile.isEmpty(); ) {
				discardPile.remove(i);
			}
			Shuffle();
		}
		if (myDeck.size() != 108) {
			System.out.println("Error!" + myDeck.size());
			System.exit(0);
		}

		for (Player newPlayer : playerList) {
			for (int i = 0; i < startingHandSize; i++) { 
				// Deals out startingHandSize cards to each player.
				giveCard(newPlayer); // Uses the giveCard method
			}
		}
		currentCard = myDeck.get(0);
		myDeck.remove(0);
		discardPile.add(currentCard);

		while (currentCard.getColor() == Color.Wild) {
			Shuffle();
			currentCard = myDeck.get(0);
			myDeck.remove(0);
			discardPile.add(currentCard);
		}
	}

	public void playCard(Player myPlayer, Card usedCard) {
		myPlayer.removeCard(usedCard);

		currentCard = usedCard;
		discardPile.add(usedCard);
	}

	public void normalDraw(Player targetPlayer) {
		for (int i = 0; i < normalDrawCount; i++) {
			giveCard(targetPlayer);
		}
	}

	public void wildDraw(Player targetPlayer) {
		for (int i = 0; i < wildDrawCount; i++) {
			giveCard(targetPlayer);
		}
	}

	public void setWildColor(Color newColor) {
		wildColor = newColor;
	}
	
	public Color getWildColor(){
		return wildColor;
	}

	public Card getCurrentCard() {
		return currentCard;
	}

	public void resetDeck(ArrayList<Player> playerList) {
		
		for (Player myPlayer : playerList) {
			for (Card card : myPlayer.clearHand()) {
				discardPile.add(card);
			}
		}
		myDeck.addAll(discardPile);
		discardPile.clear();
		
		System.out.print(fullDeckString());
	}
	
	public String fullDeckString(){
		int numRed = 0;
		int numBlue = 0;
		int numGreen = 0;
		int numYellow = 0;
		int numWild = 0;
		
		for(Card myCard : myDeck){
			if(myCard.getColor() == Color.Red){
				numRed++;
			}
		}
		
		for(Card myCard : myDeck){
			if(myCard.getColor() == Color.Blue){
				numBlue++;
			}
		}
		
		for(Card myCard : myDeck){
			if(myCard.getColor() == Color.Green){
				numGreen++;
			}
		}
		
		for(Card myCard : myDeck){
			if(myCard.getColor() == Color.Yellow){
				numYellow++;
			}
		}
		
		for(Card myCard : myDeck){
			if(myCard.getColor() == Color.Wild){
				numWild++;
			}
		}
		
		return numRed + " " + numBlue + " " + numGreen + " " + numYellow + " " + numWild;
	}
}
