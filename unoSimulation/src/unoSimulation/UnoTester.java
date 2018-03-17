package unoSimulation;

import java.util.concurrent.ThreadLocalRandom;

public class UnoTester {

	public static void main(String[] args) {
		Deck myUnoDeck = new Deck();

		System.out.println(myUnoDeck.toString());
		
		myUnoDeck.Shuffle();
		
		System.out.println(myUnoDeck.toString());
		
		Game myFirstGame = new Game();
		
		
	//	myFirstGame.addPlayer(new Player("Earendil"));
		
		myFirstGame.addPlayer(new Player("Paul"));
		myFirstGame.addPlayer(new Player("Giorgio"));
		myFirstGame.addPlayer(new Player("Mom"));
		myFirstGame.addPlayer(new Player("Maria"));
		myFirstGame.addPlayer(new Player("Dad"));
		myFirstGame.addPlayer(new Player("Mary"));
		
		int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		
		System.out.println(randomNum);
		
		randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		
		System.out.println(randomNum);
		
		randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		
		System.out.println(randomNum);
		
		myFirstGame.startGame();
		
	}

}
