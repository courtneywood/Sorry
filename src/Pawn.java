

public class Pawn {
	int r;
	int c;
	String color;
	int distanceFromStart;
	
	public Pawn(String color) {
		this.color = color;
		sendToStart();
	}
	
	public Pawn(String color, int r, int c) {
		this.color = color;
		this.r = r;
		this.c = c;
	}
	
	public void setLocation(int row, int col) {
		r = row;
		c = col;
	}
	
	public void sendToStart() {
		if (color.equals("red")) {
			setLocation(14,11);
		}
		else if (color.equals("green")) {
			setLocation(4,14);
		}
		else if (color.equals("blue")) {
			setLocation(11,1);
		}
		else {
			setLocation(1,4);
		}
	}
	
	public boolean isInStart() {
		if (color.equals("red")) {
			if (r == 14 && c == 11) return true;
			else return false;
		}
		else if (color.equals("green")) {
			if (r == 4 && c == 14) return true;
			else return false;
		}
		else if (color.equals("blue")) {
			if (r == 11 && c == 1) return true;
			else return false;
		}
		else {
			if (r == 1 && c == 4) return true;
			else return false;
		}
	}
	
	public boolean isHome() {
		if (color.equals("red")) {
			if (r == 9 && c == 13) return true;
			else return false;
		}
		else if (color.equals("green")) {
			if (r == 2 && c == 9) return true;
			else return false;
		}
		else if (color.equals("blue")) {
			if (r == 13 && c == 6) return true;
			else return false;
		}
		else {
			if (r == 6 && c == 2) return true;
			else return false;
		}
	}
	
	public boolean isSafe() {
		if (color.equals("red")) {
			if (c == 13 && r != 0 && r!= 15) return true;
			else return false;
		}
		else if (color.equals("blue")) {
			if (r == 13 && c != 0 && c != 15) return true;
			else return false;
		}
		else if (color.equals("green")) {
			if (r == 2 && c != 0 && c != 15) return true;
			else return false;
		}
		else {
			if (c == 2 && r != 0 && r != 15) return true;
			else return false;
		}
	}
	
	public int getRow() {
		return r;
	}
	
	public int getCol() {
		return c;
	}
	
	public String getColor() {
		return color;
	}
}

