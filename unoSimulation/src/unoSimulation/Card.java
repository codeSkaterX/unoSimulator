package unoSimulation;

public class Card {

	public enum Color { Red, Blue, Yellow, Green, Wild }
	
	private Color cardColor;
	private int cardValue;
	
	public Card(Color initColor, int initValue){
		cardValue = initValue;
		cardColor = initColor;
	}
	
	public int getValue(){
		return cardValue;
	}
	
	public Color getColor(){
		return cardColor;
	}
	
	public String toString(){
		return cardColor + " " + cardValue;
	}
}
