

import java.util.Random;

public class Deck {
	private String[] cards;
	private int curr = 0;
	public Deck() {
		cards = new String[40];
		for (int i = 0; i < 4; i++) {
			cards[i] = "1 - Move a pawn from Start or move a pawn one space forward.";
			cards[i + 4] = "2 - Move a pawn from Start or move a pawn two spaces forward. Drawing a two entitles the player to draw again at the end of their turn.";
			cards[i + 8] = "3 - Move a pawn three spaces forward.";
			cards[i + 12] = "4 - Move a pawn four spaces backwards.";
			cards[i + 16] = "5 - Move a pawn five spaces forward.";
			cards[i + 20] = "7 - Move one pawn seven spaces forward or split the seven spaces between two pawns.";
			cards[i + 24] = "8 - Move a pawn eight spaces forward.";
			cards[i + 28] = "10 - Move a pawn ten spaces forward or one space backward.";
			cards[i + 32] = "12 - Move a pawn twelve spaces forward.";
			cards[i + 36] = "Sorry! - Move any one pawn from Start to a square occupied by any opponent, sending that pawn back to its own Start.";
		}
		shuffle();
	}
	
	public void shuffle() {
		Random rand = new Random();
		for (int i = 0; i < 40; i++) {
			int newLoc = rand.nextInt(40);
			String temp = cards[newLoc];
			cards[newLoc] = cards[i];
			cards[i] = temp;
		}
	}
		
	public String getNextCard() {
		/*
		curr++;
		//if (curr == 0) return cards[0];
		if (curr == 1) return cards[0];
		if (curr == 2) return cards[12];
		if (curr == 3) return cards[32];
		if (curr == 4) return cards[0];
		if (curr == 5) return cards[12];
		if (curr == 6) return cards[32];
		if (curr == 7) return cards[0];
		if (curr == 8) return cards[12];
		if (curr == 9) return cards[32];
		if (curr == 10) return cards[0];
		if (curr == 11) return cards[12];
		if (curr == 12) return cards[32];
		if (curr == 13) return cards[32];
		if (curr == 14) return cards[32];
		return cards[0];
		*/
		
		if (curr == 39) {
			shuffle();
			curr = 0;
		}
		curr++;
		return cards[curr - 1];
		
	}
}
