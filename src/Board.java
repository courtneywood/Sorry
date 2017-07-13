

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

class Board {
	
	private JLabel board[][];
	private BoardInfo bi[][];
	JButton cardButton;
	Font f;
	public Board() {
		board = new JLabel[16][16];
		bi = new BoardInfo[16][16];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				JLabel l = new JLabel("");
				board[i][j] = l;
			}
		}
		
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.PLAIN, 8);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		//create board
		//top row
		for (int i = 0; i < 16; i++) {
			if ((i > 0 && i < 5) || (i > 8 && i < 14)) {
				ImageIcon yellowSlide = new ImageIcon("assets/images/sliders/yellow_slide.png");
				ImageIcon bg = new ImageIcon("assets/images/tiles/yellow_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), yellowSlide.getImage(), 0, null));
				board[0][i] = loc;
				bi[0][i] = new BoardInfo("yellow", true, false, false);
			}
			else {
				ImageIcon bg = new ImageIcon("assets/images/tiles/grey_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[0][i] = loc;
				bi[0][i] = new BoardInfo("grey", false, false, false);
			}
		}
		//leftmost column
		for (int i = 1; i < 15; i++) {
			if (i == 1 || i >=7 && i <= 10) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/grey_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[i][0] = loc;
				bi[i][0] = new BoardInfo("grey", false, false, false);
			}
			else {
				ImageIcon blueSlide = new ImageIcon("assets/images/sliders/blue_slide.png");
				ImageIcon bg = new ImageIcon("assets/images/tiles/blue_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), blueSlide.getImage(), 1, null));
				board[i][0] = loc;
				bi[i][0] = new BoardInfo("blue", true, false, false);
			}
		}
		//bottom row
		for (int i = 0; i < 16; i++) {
			if (i == 0 || i == 1 || (i >= 7 && i <= 10) || i == 15) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/grey_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[15][i] = loc;
				bi[15][i] = new BoardInfo("grey", false, false, false);
			}
			else {
				ImageIcon redSlide = new ImageIcon("assets/images/sliders/red_slide.png");
				ImageIcon bg = new ImageIcon("assets/images/tiles/red_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), redSlide.getImage(), 0, null));
				board[15][i] = loc;
				bi[15][i] = new BoardInfo("red", true, false, false);
			}
		}
		//rightmost column
		for (int i = 1; i < 15; i++) {
			if ((i > 0 && i < 5) || (i > 8 && i < 14)) {
				ImageIcon greenSlide = new ImageIcon("assets/images/sliders/green_slide.png");
				ImageIcon bg = new ImageIcon("assets/images/tiles/green_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), greenSlide.getImage(), 0, null));
				board[i][15] = loc;
				bi[i][15] = new BoardInfo("green", true, false, false);
			}
			else {
				ImageIcon bg = new ImageIcon("assets/images/tiles/grey_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[i][15] = loc;
				bi[i][15] = new BoardInfo("grey", false, false, false);
			}
		}
		//row 1
		for (int i = 1; i < 15; i++) {
			if (i == 2) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/yellow_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[1][i] = loc;
				bi[1][i] = new BoardInfo("yellow", false, false, false);
			}
			else if (i == 4) {
				JLabel loc = new JLabel("Start", SwingConstants.CENTER);
				loc.setFont(f);
				loc.setIcon(new ImageIcon("assets/images/panels/yellow_panel.png"));
				loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				board[1][i] = loc;
				bi[1][i] = new BoardInfo("yellow", false, true, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[1][i] = loc;
				bi[1][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 2
		for (int i = 1; i < 15; i++) {
			if (i == 2) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/yellow_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[2][i] = loc;
				bi[2][i] = new BoardInfo("yellow", false, false, false);
			}
			else if (i == 4) {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[2][i] = loc;
				bi[2][i] = new BoardInfo("", false, false, false);
			}
			else if (i == 8) {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[2][i] = loc;
				bi[2][i] = new BoardInfo("", false, false, false);
			}
			else if (i >= 9) {
				JLabel loc = new JLabel();
				if (i != 9) {
					ImageIcon bg = new ImageIcon("assets/images/tiles/green_tile.png");
					loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
					bi[2][i] = new BoardInfo("green", false, false, false);
				}
				else {
					loc.setText("Home");
					loc.setFont(f);
					loc.setIcon(new ImageIcon("assets/images/panels/green_panel.png"));
					loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
					loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				}
				board[2][i] = loc;
				bi[2][i] = new BoardInfo("green", false, false, true);
			}
		}
		//row 3
		for (int i = 1; i < 15; i++) {
			if (i == 2) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/yellow_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[3][i] = loc;
				bi[3][i] = new BoardInfo("yellow", false, false, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[3][i] = loc;
				bi[3][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 4
		for (int i = 1; i < 15; i++) {
			if (i == 2) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/yellow_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[4][i] = loc;
				bi[4][i] = new BoardInfo("yellow", false, false, false);
			}
			else if (i == 13) {
				JLabel loc = new JLabel("", SwingConstants.CENTER); //green start
				board[4][i] = loc;
				bi[4][i] = new BoardInfo("", false, false, false);
			}
			else if (i == 14) {
				JLabel loc = new JLabel("Start");
				loc.setFont(f);
				loc.setIcon(new ImageIcon("assets/images/panels/green_panel.png"));
				loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				board[4][i] = loc;
				bi[1][i] = new BoardInfo("green", false, true, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[4][i] = loc;
				bi[4][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 5
		for (int i = 1; i < 15; i++) {
			if (i == 2) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/yellow_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[5][i] = loc;
				bi[5][i] = new BoardInfo("yellow", false, false, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[5][i] = loc;
				bi[5][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 6
		for (int i = 1; i < 15; i++) {
			if (i == 2) {
				JLabel loc = new JLabel("Home");
				loc.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
				loc.setFont(f);
				loc.setIcon(new ImageIcon("assets/images/panels/yellow_panel.png"));
				loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				board[6][i] = loc;
				bi[6][i] = new BoardInfo("yellow", false, false, true);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[6][i] = loc;
				bi[6][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 7
		for (int i = 1; i < 15; i++) {
			if (i == 2) {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[7][i] = loc;
			}
			else if (i == 7) {
				JLabel loc = new JLabel("Cards:", SwingConstants.CENTER);
				loc.setFont(f);
				board[7][i] = loc;
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[7][i] = loc;
			}
			bi[7][i] = new BoardInfo("", false, false, false);
		}
		//row 8
		for (int i = 1; i < 15; i++) {
			JLabel loc = new JLabel("", SwingConstants.CENTER);
			board[8][i] = loc;
			bi[8][i] = new BoardInfo("", false, false, false);
		}
		//row 9
		for (int i = 1; i < 15; i++) {
			if (i == 13) {
				JLabel loc = new JLabel("Home");
				loc.setFont(f);
				loc.setIcon(new ImageIcon("assets/images/panels/red_panel.png"));
				loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				board[9][i] = loc;
				bi[9][i] = new BoardInfo("red", false, false, true);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[9][i] = loc;
				bi[9][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 10
		for (int i = 1; i < 15; i++) {
			if (i == 13) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/red_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[10][i] = loc;
				bi[10][i] = new BoardInfo("red", false, false, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[10][i] = loc;
				bi[10][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 11
		for (int i = 1; i < 15; i++) {
			if (i == 1) {
				JLabel loc = new JLabel("Start");
				loc.setFont(f);
				loc.setIcon(new ImageIcon("assets/images/panels/blue_panel.png"));
				loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				board[11][i] = loc;
				bi[11][i] = new BoardInfo("blue", false, true, false);
			}
			else if (i == 2) {
				JLabel loc = new JLabel("", SwingConstants.CENTER); //blue start
				board[11][i] = loc;
				bi[11][i] = new BoardInfo("", false, false, false);
			}
			else if (i == 13) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/red_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[11][i] = loc;
				bi[11][i] = new BoardInfo("red", false, false, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[11][i] = loc;
				bi[11][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 12
		for (int i = 1; i < 15; i++) {
			if (i == 13) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/red_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[12][i] = loc;
				bi[12][i] = new BoardInfo("red", false, false, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[12][i] = loc;
				bi[12][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 13
		for (int i = 1; i < 15; i++) {
			if (i >= 1 && i <= 6) {
				JLabel loc;
				ImageIcon bg = new ImageIcon("assets/images/tiles/blue_tile.png");
				if (i == 6) {
					loc = new JLabel("Home");
					loc.setFont(f);
					loc.setIcon(new ImageIcon("assets/images/panels/blue_panel.png"));
					loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
					loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
					bi[13][i] = new BoardInfo("blue", false, false, true);
					
				}
				else {
					loc = new JLabel();
					loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
					bi[13][i] = new BoardInfo("blue", false, false, false);
				}
				board[13][i] = loc;
			}
			else if ( i == 7 || i == 11) {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[13][i] = loc;
				bi[13][i] = new BoardInfo("", false, false, false);
			}
			else if (i == 13) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/red_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[13][i] = loc;
				bi[13][i] = new BoardInfo("red", false, false, false);
			}
			else {
				JLabel loc = new JLabel("", SwingConstants.CENTER);
				board[13][i] = loc;
				bi[13][i] = new BoardInfo("", false, false, false);
			}
		}
		//row 14
		for (int i = 1; i < 15; i++) {
			if (i == 11) {
				JLabel loc = new JLabel("Start");
				loc.setFont(f);
				loc.setIcon(new ImageIcon("assets/images/panels/red_panel.png"));
				loc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				loc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				board[14][i] = loc;
				bi[14][i] = new BoardInfo("red", false, true, false);
			}
			else if (i == 13) {
				ImageIcon bg = new ImageIcon("assets/images/tiles/red_tile.png");
				JLabel loc = new JLabel();
				loc.setIcon(new BoardIcon(bg.getImage(), null, 0, null));
				board[14][i] = loc;
				bi[14][i] = new BoardInfo("red", false, false, false);
			}
		}
	}
	
	public JLabel[][] getBoard() {
		return board;
	}
	
	public int[] findPawn(JLabel jl) {
		int[] coords = new int[2];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (board[i][j] == jl) {
					coords[0] = i;
					coords[1] = j;
				}
			}
		}
		return coords;
	}
	
	public boolean containsPawn(int r, int c) {
		JLabel l = board[r][c];
		if (l.getText().equals("\u25A0")) return true;
		else return false;
	}
	
	public void setPawn(int r, int c, String color) {		
		ImageIcon pawn = new ImageIcon("assets/images/pawns/" + color + "_pawn.png");
		String col = bi[r][c].getColor();
		ImageIcon bg = new ImageIcon("assets/images/tiles/" + col + "_tile.png");
		board[r][c].setIcon(new BoardIcon(bg.getImage(), null, 0, pawn.getImage()));
	}
	
	
	public void setUpBoard(int numPlayers, String color) {
		Font f = null;
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.BOLD, 12);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		/*
		if (numPlayers == 2) {
			if (color.equals("red")) {
				board[13][11].setText("4");
				board[13][11].setFont(f);
				board[2][4].setText("4");
				board[2][4].setFont(f);
				board[7][2].setText("0");
				board[7][2].setFont(f);
				board[8][13].setText("0");
				board[8][13].setFont(f);
			}
			else if (color.equals("blue")) {
				board[11][2].setText("4");
				board[11][2].setFont(f);
				board[4][13].setText("4");
				board[4][13].setFont(f);
				board[13][7].setText("0");
				board[13][7].setFont(f);
				board[2][8].setText("0");
				board[2][8].setFont(f);
			}
			else if (color.equals("yellow")) {
				board[13][11].setText("4");
				board[13][11].setFont(f);
				board[2][4].setText("4");
				board[2][4].setFont(f);
				board[7][2].setText("0");
				board[7][2].setFont(f);
				board[8][13].setText("0");
				board[8][13].setFont(f);
			}
			else {
				board[11][2].setText("4");
				board[11][2].setFont(f);
				board[4][13].setText("4");
				board[4][13].setFont(f);
				board[13][7].setText("0");
				board[13][7].setFont(f);
				board[2][8].setText("0");
				board[2][8].setFont(f);
			}
		}
		else if (numPlayers == 3) {
			if (color.equals("red") || color.equals("blue") || color.equals("yellow")) {
				//red, yellow
				board[13][11].setText("4");
				board[13][11].setFont(f);
				board[2][4].setText("4");
				board[2][4].setFont(f);
				board[7][2].setText("0");
				board[7][2].setFont(f);
				board[8][13].setText("0");
				board[8][13].setFont(f);
				//blue
				board[11][2].setText("4");
				board[11][2].setFont(f);
				board[13][7].setText("0");
				board[13][7].setFont(f);
			}
			else {
				//green
				board[4][13].setText("4");
				board[4][13].setFont(f);
				board[2][8].setText("0");
				board[2][8].setFont(f);
				//red, yellow
				board[13][11].setText("4");
				board[13][11].setFont(f);
				board[2][4].setText("4");
				board[2][4].setFont(f);
				board[7][2].setText("0");
				board[7][2].setFont(f);
				board[8][13].setText("0");	
				board[8][13].setFont(f);
			}
		}
		else {
			board[4][13].setText("4");
			board[4][13].setFont(f);
			board[2][8].setText("0");
			board[2][8].setFont(f);
			board[13][11].setText("4");
			board[13][11].setFont(f);
			board[2][4].setText("4");
			board[2][4].setFont(f);
			board[7][2].setText("0");
			board[7][2].setFont(f);
			board[8][13].setText("0");
			board[8][13].setFont(f);
			board[11][2].setText("4");
			board[11][2].setFont(f);
			board[13][7].setText("0");
			board[13][7].setFont(f);
		}
		*/
	}
	
	public void movePawn(int oldR, int oldC, int newR, int newC, String color) {
		//if the old location is start
		if (isStartSpace(oldR, oldC) && !isStartSpace(newR, newC)) {
			setPawn(newR, newC, color);
		}
		else if (isStartSpace(newR, newC)) {
			//if its moving to start, increase the value in start and get rid of what was at the old loc
			setToDefault(oldR, oldC);
		}
		else if (isHomeSpace(newR, newC)) {
			setToDefault(oldR, oldC);
		}
		else {
			setToDefault(oldR, oldC);
			setPawn(newR, newC, color);
		}
	}
	
	public void switchPawns(int r1, int c1, int r2, int c2, String color1, String color2) {
		setToDefault(r2, c2);
		setPawn(r2, c2, color1);
		setToDefault(r1, c1);
		setPawn(r1, c1, color2);
	}
	
	public boolean isSlideSpace(int r, int c) {
		if (r == 15) {
			if ((c >= 2 && c <= 6) || (c >= 11 && c <= 14)) return true;
			else return false;
		}
		if (r == 0) {
			if ((c >= 1 && c <= 4) || (c >= 9 && c <= 13)) return true;
			else return false;
		}
		if (c == 0) {
			if ((r >= 2 && r <= 6) || (r >= 11 && r <= 14)) return true;
			else return false;
		}
		if (c == 15) {
			if ((r >= 1 && r <= 4) || (r >= 9 && r <= 13)) return true;
			else return false;
		}
		return false;
	}
	
	public boolean isStartSpace(int r, int c) {
		if (r == 14 && c == 11) return true;
		if (r == 4 && c == 14) return true;
		if (r == 11 && c == 1) return true;
		if (r == 1 && c == 4) return true;
		return false;
	}
	
	public boolean isHomeSpace(int r, int c) {
		if (r == 9 && c == 13) return true;
		if (r == 2 && c == 9) return true;
		if (r == 13 && c == 6) return true;
		if (r == 6 && c == 2) return true;
		return false;
	}
	
	public BoardInfo[][] getBoardInfo() {
		return bi;
	}
	
	public void setToDefault(int r, int c) {
		JLabel loc = board[r][c];
		String color = bi[r][c].getColor();
		boolean slide = bi[r][c].isSlide();
		ImageIcon bg = new ImageIcon("assets/images/tiles/" + color + "_tile.png");
		ImageIcon slideimg = new ImageIcon("assets/images/sliders/" + color + "_slide.png");
		ImageIcon homeimg = new ImageIcon("assets/images/panels/" + color + "_panel.png");
		if (slide && (color.equals("red") || color.equals("yellow"))) {
			loc.setIcon(new BoardIcon(bg.getImage(), slideimg.getImage(), 0, null));
		}
		else if (slide && (color.equals("green") || color.equals("blue"))) {
			loc.setIcon(new BoardIcon(bg.getImage(), slideimg.getImage(), 1, null));
		}
		else if (isHomeSpace(r, c)) {
			loc.setIcon(new BoardIcon(homeimg.getImage(), null, 0, null));
		}
		else {
			loc.setIcon(new BoardIcon(bg.getImage(), null, 1, null));
		}
		
	}

	public void setUpBoard(ArrayList<String> allColors) {
		for (int i = 0; i < allColors.size(); i++) {
			String color = allColors.get(i);
			if (color.equals("red")) {
				board[13][11].setText("4");
				board[13][11].setFont(f);
				board[8][13].setText("0");
				board[8][13].setFont(f);
			}
			else if (color.equals("blue")) {
				board[11][2].setText("4");
				board[11][2].setFont(f);
				board[13][7].setText("0");
				board[13][7].setFont(f);
			}
			else if (color.equals("yellow")) {
				board[2][4].setText("4");
				board[2][4].setFont(f);
				board[7][2].setText("0");
				board[7][2].setFont(f);
			}
			else {
				board[4][13].setText("4");
				board[4][13].setFont(f);
				board[2][8].setText("0");
				board[2][8].setFont(f);
			}
		}
		
	}
	
}

class BoardInfo {
	String c;
	boolean s;
	boolean start;
	boolean home;
	public BoardInfo(String color, boolean slide, boolean start, boolean home) {
		c = color;
		s = slide;
	}
	
	public String getColor() {
		return c;
	}
	
	public boolean isSlide() {
		return s;
	}
	
	public boolean isStart() {
		return start;
	}
	
	public boolean isHome() {
		return home;
	}
}

class BoardIcon implements Icon {
	private Image bg;
	private Image top;
	int side = -1;
	public BoardIcon(Image bg, Image slide, int side, Image pawn) {
		//0 = top or bottom
		//1 = left or right
		this.bg = bg;
		if (slide != null && pawn == null) {
			top = slide;
			this.side = side;
		}
		else if (slide == null && pawn != null) {
			top = pawn;
		}
	}
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		g.drawImage(bg, 0, 0, c.getWidth(), c.getHeight(), c);
		if (side == 0 || side == -1) g.drawImage(top, (int) (0 + c.getWidth() / 3.5), (int) (0 + c.getHeight() / 3.5), c.getWidth() - (c.getWidth() / 2), c.getHeight() - (c.getHeight() / 2), c);
		else if (side == 1) g.drawImage(top, (int) (0 + c.getWidth() / 4), (int) (0 + c.getHeight() / 5), c.getWidth() - (c.getWidth() / 2), c.getHeight() - (c.getHeight() / 2), c);
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

