package unoSimulation;

import unoSimulation.Card.Color;

public class PaulAI extends Player{
	public Card Ai(Color wildColor, Card currentCard) {
		Card possibleCard;
		if(allWilds()){
			possibleCard = super.playableCard(wildColor, currentCard);
		}
		else{
			possibleCard = playableCard(wildColor, currentCard);
			// Finds playable card
		}
		
		return possibleCard; // Plays card
	}
	
	private boolean allWilds(){
		for(Card myCard : Hand){
			if(myCard.getColor() != Card.Color.Wild){
				return false;
			}
		}
		
		return true;
	}
	
	protected Card playableCard(Color wildColor, Card currentCard){
		for (Card card : Hand) {
			if (card.getColor() == currentCard.getColor()) { // Do colors
																// matchup
				return card;
			}
			if (currentCard.getColor() == Color.Wild
					&& card.getColor() == wildColor) {
				// If currentCard is wild, does card match picked color?
				return card;
			}

			if (card.getValue() == currentCard.getValue()) { // Do values
																// matchup
				return card;
			}
		}
		return null; // No playable cards
	}
}
