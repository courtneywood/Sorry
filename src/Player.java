

public class Player {
	Pawn[] pawns;
	String color;
	boolean bot;
	
	Player(String color) {
		this.color = color;
		bot = false;
		pawns = new Pawn[4];
		for (int i = 0; i < 4; i++) {
			pawns[i] = new Pawn(color);
		}
	}
	
	public String getColor() {
		return color;
	}
	
	public void setBot(boolean bot) {
		this.bot = bot;
	}
	
	public boolean allPawnsInStart() {
		for (int i = 0; i < 4; i++) {
			if (!pawns[i].isInStart()) return false;
		}
		return true;
	}
	
	public boolean allPawnsHome() {
		for (int i = 0; i < 4; i++) {
			if (!pawns[i].isHome()) return false;
		}
		return true;
	}
	
	public Pawn getPawnAt(int r, int c) {
		for (int i = 0; i < 4; i++) {
			Pawn p = pawns[i];
			if (p.getRow() == r && p.getCol() == c) return p;
		}
		return null;
	}
	
	public Pawn[] getPawns() {
		return pawns;
	}
	
	public int getNumInStart() {
		int total = 0;
		for (int i = 0; i < 4; i++) {
			if (pawns[i].isInStart()) total++;
		}
		return total;
	}
	
	public int getNumHome() {
		int total = 0;
		for (int i = 0; i < 4; i++) {
			if (pawns[i].isHome()) total++;
		}
		return total;
	}
	
}
