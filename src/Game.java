

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Game {
	int numPlayers;
	String color;
	Board b;
	Deck d;
	ArrayList<Player> players;
	Player currentPlayer;
	ArrayList<JLabel> labelsWithListeners;
	boolean gameOver;
	SorryGUI sg;
	boolean last;
	SorryClient sc;
	ArrayList<String> allColors;
	Pawn movingPawn;
	Timer movingPawnTimer;
	int amountToMove;
	int amountAlreadyMoved = 0;
	boolean goAgain;
	Pawn pawnAtPrev = null;
	boolean foundOnThisMove = false;
	public Game(int numPlayers, String color, Board b, Deck d, SorryGUI sg, SorryClient sc, ArrayList<String> allColors) {
		this.numPlayers = numPlayers;
		this.color = color;
		this.b = b;
		this.d = d;
		this.sc = sc;
		this.allColors = allColors;
		currentPlayer = new Player(color);
		players = new ArrayList<Player>();
		players.add(currentPlayer);
		for (int i = 0; i < allColors.size(); i++) {
			if (!allColors.get(i).equals(color)) {
				players.add(new Player(allColors.get(i)));
			}
		}
		b.setUpBoard(allColors);

		labelsWithListeners = new ArrayList<>();
		this.sg = sg;
	}
	
	public void createBot(String color) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getColor().equals(color)) {
				players.get(i).setBot(true);
			}
		}
	}
	
	public boolean hasBots() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).bot == true) return true;
		}
		return false;
	}
	
	public void promptUser() {
		if (!gameOver()) {
			SoundPlayer sp = new SoundPlayer("your_turn");
			sp.play();
			//if (hasBots()) JOptionPane.showMessageDialog(sg, "Please draw a card.");
			sg.cardButton.setEnabled(true);
			sg.p4.startTimer();
		}
		else endGame();
	}
	
	public void showUsersCard(String card) {
		int cardValue = getCardValue(card);
		Card c = new Card(cardValue, card, this);
		c.setLocationRelativeTo(sg.cardButton);
		c.setVisible(true);
	}
	
	public void highlightTile(JLabel toHighlight, boolean hasPawn) {
		ImageIcon img = new ImageIcon("assets/images/panels/" + color + "_panel.png");
		ImageIcon pawn = new ImageIcon("assets/images/pawns/" + color + "_pawn.png");
		if (hasPawn) {
			toHighlight.setIcon(new BoardIcon(img.getImage(), null, 0, pawn.getImage()));
		}
		else {
			toHighlight.setIcon(new BoardIcon(img.getImage(), null, 0, null));
		}
	}
	
	
	public void unhighlightTile(JLabel hovered, boolean hasPawn) {
		BoardInfo[][] bi = b.getBoardInfo();
		int[] coords = b.findPawn(hovered);
		int r = coords[0];
		int c = coords[1];
		String bgcolor = bi[r][c].getColor();
		if ((r == 9 && c == 13) || (r == 14 && c == 11) || (r == 13 && c == 6) || (r == 11 && c == 1) || (r == 6 && c == 2) || (r == 1 && c == 4) || (r == 2 && c == 9) || (r == 4 && c == 14)) {
			ImageIcon img = new ImageIcon("assets/images/panels/" + color + "_panel.png");
			hovered.setIcon(new BoardIcon(img.getImage(), null, 0, null));
		}
		else {
			ImageIcon img = new ImageIcon("assets/images/tiles/" + bgcolor + "_tile.png");
			ImageIcon pawn = new ImageIcon("assets/images/pawns/" + color + "_pawn.png");
			ImageIcon slide = new ImageIcon("assets/images/sliders/" + bgcolor + "_slide.png");
			if (hasPawn) {
				hovered.setIcon(new BoardIcon(img.getImage(), null, 0, pawn.getImage()));
			}
			else {
				if (bi[r][c].isSlide()) hovered.setIcon(new BoardIcon(img.getImage(), slide.getImage(), 0, null));
				else hovered.setIcon(new BoardIcon(img.getImage(), null, 0, null));
			}
		}
	}
	
	public void userTurn(String card) {
		int cardValue = getCardValue(card);
		currentPlayer = players.get(0);
		ArrayList<Pawn> validPawns = getValidPawns(cardValue);
		if (cardValue == 0) {
			ArrayList<Pawn> opposingPawns = getOpposingPawnsOnBoard();
			Pawn toSwitch = null;
			if (validPawns.size() > 0) toSwitch = validPawns.get(0);
			if (toSwitch != null) {
				if (opposingPawns.size() == 0) {
					JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");
					sc.out.println("TURN_OVER");
					sg.cardButton.setEnabled(false);
					sg.p4.stopTimer();
				}
				//they have a pawn in start
				else if (opposingPawns.size() == 1) {
					sg.cardButton.setEnabled(false);
					//there is one opposing pawn on the board
					Pawn toStart = opposingPawns.get(0);
					JOptionPane.showMessageDialog(null, "Click the pawn at (" 
							+ toStart.getRow() + ", " + toStart.getCol() + ") to send it back to start");
					JLabel[][] board = b.getBoard();
					JLabel label = board[toStart.getRow()][toStart.getCol()];
					labelsWithListeners.add(label);
					label.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent me) {
							JLabel clicked = (JLabel) me.getSource();
							int[] pawnCoords = b.findPawn(clicked);
							Pawn pts = validPawns.get(0);
							Pawn sendToStart = getPawnAt(pawnCoords[0], pawnCoords[1]);
							sorry(pts, sendToStart);
							removeListeners();
							sg.cardButton.setEnabled(true);
							sc.out.println("TURN_OVER");
							sg.cardButton.setEnabled(false);
							sg.p4.stopTimer();
						}
					});
				}
				else {
					//there are multiple opposing pawns on the board
					sg.cardButton.setEnabled(false);
					JLabel[][] board = b.getBoard();
					for (int i = 0; i < opposingPawns.size(); i++) {
						Pawn toStart = opposingPawns.get(i);
						JLabel currLabel = board[toStart.getRow()][toStart.getCol()];
						labelsWithListeners.add(currLabel);
						currLabel.addMouseListener(new MouseAdapter(){
							public void mouseClicked(MouseEvent me) {
								JLabel clicked = (JLabel) me.getSource();
								int[] pawnCoords = b.findPawn(clicked);
								Pawn pts = validPawns.get(0);
								Pawn sendToStart = getPawnAt(pawnCoords[0], pawnCoords[1]);
								sorry(pts, sendToStart);
								removeListeners();
								sg.cardButton.setEnabled(true);
								sc.out.println("TURN_OVER");
								sg.cardButton.setEnabled(false);
								sg.p4.stopTimer();
							}
						});
					}
					JOptionPane.showMessageDialog(null, "Click a pawn to send it back to start");
				}
			}
			else {
				//they have no pawns in start
				JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");
				sc.out.println("TURN_OVER");
				sg.cardButton.setEnabled(false);
				sg.p4.stopTimer();
			}
		}
		else if (cardValue == 1 || cardValue == 2) {
			if (validPawns.size() == 1) {
				//String pawnMoved = validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
				movePawn(validPawns.get(0), cardValue);
				//pawnMoved += " " + validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
				//sc.out.println("MOVE " + pawnMoved);
				if (cardValue == 2 && !gameOver()) {
					//need to have a special case
					//could have a separate if statement for 2
					//could have a boolean in moveTurn to end the turn
					JOptionPane.showMessageDialog(null, "Go again!");
					sg.cardButton.setEnabled(true);
					sg.p4.startTimer();
				}
				else if (gameOver()) endGame();
				else {
					//sc.out.println("TURN_OVER");
					//sg.cardButton.setEnabled(false);
				}
			}
			else if (validPawns.size() > 1) {
				sg.cardButton.setEnabled(false);
				for (int i = 0; i < validPawns.size(); i++) {
					Pawn currentPawn = validPawns.get(i);
					JLabel[][] board = b.getBoard();
					JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
					labelsWithListeners.add(currLabel);
					currLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent me) {
							JLabel clicked = (JLabel) me.getSource();
							int[] pawnCoords = b.findPawn(clicked);
							Pawn ptm = getPawnAt(pawnCoords[0], pawnCoords[1]);
							//String pawnMoved = pawnCoords[0] + " " + pawnCoords[1];
							movePawn(ptm, cardValue);
							//pawnMoved += " " + ptm.getRow() + " " + ptm.getCol();
							//sc.out.println("MOVE " + pawnMoved);
							removeListeners();
							sg.cardButton.setEnabled(true);
							if (cardValue == 2) {
								JOptionPane.showMessageDialog(null, "Go again!");
								sg.p4.startTimer();
							}
							else {
								//sc.out.println("TURN_OVER");
								//sg.cardButton.setEnabled(false);
							}
						}
						public void mouseEntered(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							highlightTile(hovered, true);
						}
						
						public void mouseExited(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							unhighlightTile(hovered, true);
						}
					});	
				}
				JOptionPane.showMessageDialog(null, "Select the pawn you want to move.");
			}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");
				sg.p4.stopTimer();
				if (cardValue == 2) {
					JOptionPane.showMessageDialog(null, "Go again!");
					sg.p4.startTimer();
				}
				else {
					sc.out.println("TURN_OVER");
					sg.cardButton.setEnabled(false);
				}
			}
		}
		else if (cardValue == 3 || cardValue == 4 || cardValue == 5 || cardValue == 8 || cardValue == 12) {
			if (validPawns.size() == 1) {
				//String pawnMoved = validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
				if (cardValue != 4) {
					movePawn(validPawns.get(0), cardValue);
				}
				else movePawn(validPawns.get(0), -4);
			}
			else if (validPawns.size() > 1) {
				sg.cardButton.setEnabled(false);
				for (int i = 0; i < validPawns.size(); i++) {
					Pawn currentPawn = validPawns.get(i);
					JLabel[][] board = b.getBoard();
					JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
					labelsWithListeners.add(currLabel);
					currLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent me) {
							JLabel clicked = (JLabel) me.getSource();
							int[] pawnCoords = b.findPawn(clicked);
							Pawn ptm = getPawnAt(pawnCoords[0], pawnCoords[1]);
							highlightTile(clicked, true);

							//String pawnMoved = pawnCoords[0] + " " + pawnCoords[1];
							if (cardValue != 4) movePawn(ptm, cardValue);
							else movePawn(ptm, -cardValue);
							//pawnMoved += " " + ptm.getRow() + " " + ptm.getCol();
							//sc.out.println("MOVE " + pawnMoved);
							removeListeners();
						}
						
						public void mouseEntered(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							highlightTile(hovered, true);
						}
						
						public void mouseExited(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							unhighlightTile(hovered, true);
						}
					});	
				}
				JOptionPane.showMessageDialog(null, "Select the pawn you want to move.");
			}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");
				sc.out.println("TURN_OVER");
				sg.cardButton.setEnabled(false);
				sg.p4.stopTimer();
			}
		}
		else if (cardValue == 7) {
			if (validPawns.size() == 1 && isValidMove(validPawns.get(0), 7)) {
				//String pawnMoved = validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
				movePawn(validPawns.get(0), 7);
			}
			else if (validPawns.size() > 1) {
				sg.cardButton.setEnabled(false);
				Object[] choices = {"Move 1", "Move 2"};
				//int selection = JOptionPane.showOptionDialog(null, "Move one pawn 7 spaces or split between 2 pawns?", "Select an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				int selection = 0;
				if (selection == 0) {
					for (int i = 0; i < validPawns.size(); i++) {
						Pawn currentPawn = validPawns.get(i);
						JLabel[][] board = b.getBoard();
						JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
						labelsWithListeners.add(currLabel);
						currLabel.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent me) {
								JLabel clicked = (JLabel) me.getSource();
								int[] pawnCoords = b.findPawn(clicked);
								Pawn p1 = getPawnAt(pawnCoords[0], pawnCoords[1]);
								//String pawnMoved = p1.getRow() + " " + p1.getCol();
								movePawn(p1, 7);
								//pawnMoved += " " + p1.getRow() + " " + p1.getCol();
								//sc.out.println("MOVE " + pawnMoved);
								removeListeners();
								//sg.cardButton.setEnabled(true);
								//sc.out.println("TURN_OVER");
								//sg.cardButton.setEnabled(false);
							}
							public void mouseEntered(MouseEvent me) {
								JLabel hovered = (JLabel) me.getSource();
								highlightTile(hovered, true);
							}
							
							public void mouseExited(MouseEvent me) {
								JLabel hovered = (JLabel) me.getSource();
								unhighlightTile(hovered, true);
							}
						});	
					}
					JOptionPane.showMessageDialog(null, "Select the pawn you want to move.");
				}
				else {
					for (int i = 0; i < validPawns.size(); i++) {
						Pawn currentPawn = validPawns.get(i);
						JLabel[][] board = b.getBoard();
						JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
						labelsWithListeners.add(currLabel);
						currLabel.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent me) {
								JLabel clicked = (JLabel) me.getSource();
								int[] pawnCoords = b.findPawn(clicked);
								Pawn p1 = getPawnAt(pawnCoords[0], pawnCoords[1]);
								ArrayList<Pawn> pickedPawn = new ArrayList<>();
								pickedPawn.add(p1);
								validPawns.removeAll(pickedPawn);
								removeListeners();
								sg.cardButton.setEnabled(true);
								selectSecondPawn(validPawns, p1);
							}
							
							public void mouseEntered(MouseEvent me) {
								JLabel hovered = (JLabel) me.getSource();
								highlightTile(hovered, true);
							}
							
							public void mouseExited(MouseEvent me) {
								JLabel hovered = (JLabel) me.getSource();
								unhighlightTile(hovered, true);
							}
						});	
					}
					JOptionPane.showMessageDialog(null, "Select the pawn you want to move first.");
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");
				sc.out.println("TURN_OVER");
				sg.cardButton.setEnabled(false);
				sg.p4.stopTimer();
			}
		}
		else if (cardValue == 10) {
			if (validPawns.size() == 1) {
				if (!isValidMove(validPawns.get(0), -1)) {
					if (isValidMove(validPawns.get(0), 10)) {
						//moving forward is valid, backwards is not
						//String pawnMoved = validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
						movePawn(validPawns.get(0), 10);
						//pawnMoved += " " + validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
						//sc.out.println("MOVE " + pawnMoved);
						//sc.out.println("TURN_OVER");
						//sg.cardButton.setEnabled(false);
					}
				}
				else {
					//moving -1 is valid, moving 10 is not
					if (!isValidMove(validPawns.get(0), 10)) {
						//String pawnMoved = validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
						movePawn(validPawns.get(0), -1);
						//pawnMoved += " " + validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
						//sc.out.println("MOVE " + pawnMoved);
						//sc.out.println("TURN_OVER");
						//sg.cardButton.setEnabled(false);
					}
					//both are valid
					else {
						Object[] choices = {"Forward 10", "Backward 1"};
						int selection = JOptionPane.showOptionDialog(null, "Move forward 10 or backwards 1?", "Select an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
						if (selection == 0) {
							//String pawnMoved = validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
							movePawn(validPawns.get(0), 10);
							//pawnMoved += " " + validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
							//sc.out.println("MOVE " + pawnMoved);
						}
						else {
							//String pawnMoved = validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
							movePawn(validPawns.get(0), -1);
							//pawnMoved += " " + validPawns.get(0).getRow() + " " + validPawns.get(0).getCol();
							//sc.out.println("MOVE " + pawnMoved);
						}
						//sc.out.println("TURN_OVER");
						//sg.cardButton.setEnabled(false);
					}
				}
			}
			else if (validPawns.size() > 1) {
				sg.cardButton.setEnabled(false);
				for (int i = 0; i < validPawns.size(); i++) {
					Pawn currentPawn = validPawns.get(i);
					JLabel[][] board = b.getBoard();
					JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
					labelsWithListeners.add(currLabel);
					currLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent me) {
							JLabel clicked = (JLabel) me.getSource();
							int[] pawnCoords = b.findPawn(clicked);
							Pawn ptm = getPawnAt(pawnCoords[0], pawnCoords[1]);
							if (!isValidMove(ptm, -1) && isValidMove(ptm, 10)) {
								//String pawnMoved = pawnCoords[0] + " " + pawnCoords[1];
								movePawn(ptm, 10);
								//pawnMoved += " " + ptm.getRow() + " " + ptm.getCol();
								//sc.out.println("MOVE " + pawnMoved);
								removeListeners();
								//sg.cardButton.setEnabled(true);
								//sc.out.println("TURN_OVER");
								//sg.cardButton.setEnabled(false);
							}
							else if (isValidMove(ptm, -1) && !isValidMove(ptm, 10)) {
								//String pawnMoved = pawnCoords[0] + " " + pawnCoords[1];
								movePawn(ptm, -1);
								//pawnMoved += " " + ptm.getRow() + " " + ptm.getCol();
								//sc.out.println("MOVE " + pawnMoved);
								removeListeners();
								//sg.cardButton.setEnabled(true);
								//sc.out.println("TURN_OVER");
								//sg.cardButton.setEnabled(false);
							}
							else if (!isValidMove(ptm, 10) && !isValidMove(ptm, -1)) {
								JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");	
								sc.out.println("TURN_OVER");
								removeListeners();
								sg.cardButton.setEnabled(true);
							}
							else {
								Object[] choices = {"Forward 10", "Backward 1"};
								int selection = JOptionPane.showOptionDialog(null, "Move forward 10 or backwards 1?", "Select an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
								if (selection == 0) {
									//String pawnMoved = pawnCoords[0] + " " + pawnCoords[1];
									movePawn(ptm, 10);
									//pawnMoved += " " + ptm.getRow() + " " + ptm.getCol();
									//sc.out.println("MOVE " + pawnMoved);
								}
								else {
									//String pawnMoved = pawnCoords[0] + " " + pawnCoords[1];
									movePawn(ptm, -1);
									//pawnMoved += " " + ptm.getRow() + " " + ptm.getCol();
									//sc.out.println("MOVE " + pawnMoved);
								}
								removeListeners();
								//sg.cardButton.setEnabled(true);
								//sc.out.println("TURN_OVER");
							}
						}
						public void mouseEntered(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							highlightTile(hovered, true);
						}
						
						public void mouseExited(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							unhighlightTile(hovered, true);
						}
					});						
				}
				JOptionPane.showMessageDialog(null, "Select the pawn you want to move.");	
			}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");	
				sc.out.println("TURN_OVER");
				sg.cardButton.setEnabled(false);
				sg.p4.stopTimer();
			}
		}
		else if (cardValue == 11) {
			if (validPawns.size() == 1) {
				Pawn p = validPawns.get(0);
				//see if they want to move forward 11 or sorry
				ArrayList<Pawn> opposing = getOpposingPawnsOnBoard();
				if (opposing.size() > 0 && !validPawns.get(0).isSafe()) {
					Object[] choices = {"Forward 11", "Switch places"};
					int selection = JOptionPane.showOptionDialog(null, "Move forward 11 or switch places with an opponent?", "Select an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					if (selection == 0) {
						//String pawnMoved = p.getRow() + " " + p.getCol();
						movePawn(p, 11);
						//pawnMoved += " " + p.getRow() + " " + p.getCol();
						//sc.out.println("MOVE " + pawnMoved);
						//sc.out.println("TURN_OVER");
						//sg.cardButton.setEnabled(false);
					}
					else {
						//select the pawn they want to switch with
						sg.cardButton.setEnabled(false);
						for (int i = 0; i < opposing.size(); i++) {
							Pawn currentPawn = opposing.get(i);
							JLabel[][] board = b.getBoard();
							JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
							labelsWithListeners.add(currLabel);
							currLabel.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent me) {
									JLabel clicked = (JLabel) me.getSource();
									int[] pawnCoords = b.findPawn(clicked);
									Pawn s = getPawnAt(pawnCoords[0], pawnCoords[1]);
									sc.out.println("SWITCH " + p.getRow() + " " + p.getCol() + " " + s.getRow() + " " + s.getCol());
									switchPawns(p, s);
									removeListeners();
									sg.cardButton.setEnabled(true);
									sc.out.println("TURN_OVER");
								}
							});						
						}
						JOptionPane.showMessageDialog(null, "Select the pawn you want to switch with.");
					}
				}
				else {
					//String pawnMoved = p.getRow() + " " + p.getCol();
					movePawn(p, 11);
					//pawnMoved += " " + p.getRow() + " " + p.getCol();
					//sc.out.println("MOVE " + pawnMoved);
					//sc.out.println("TURN_OVER");
					//sg.cardButton.setEnabled(false);
				}
			}
			else if (validPawns.size() > 1) {
				sg.cardButton.setEnabled(false);
				for (int i = 0; i < validPawns.size(); i++) {
					Pawn currentPawn = validPawns.get(i);
					JLabel[][] board = b.getBoard();
					JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
					labelsWithListeners.add(currLabel);
					currLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent me) {
							JLabel clicked = (JLabel) me.getSource();
							int[] pawnCoords = b.findPawn(clicked);
							Pawn p = getPawnAt(pawnCoords[0], pawnCoords[1]);
							ArrayList<Pawn> opposing = getOpposingPawnsOnBoard();
							removeListeners();
							if (opposing.size() > 0 && !p.isSafe()) {
								Object[] choices = {"Forward 11", "Switch places"};
								int selection = JOptionPane.showOptionDialog(null, "Move forward 11 or switch places with an opponent?", "Select an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
								if (selection == 0) {
									//String pawnMoved = p.getRow() + " " + p.getCol();
									movePawn(p, 11);
									//pawnMoved += " " + p.getRow() + " " + p.getCol();
									//sc.out.println("MOVE " + pawnMoved);
									//sg.cardButton.setEnabled(true);
									//sc.out.println("TURN_OVER");
								}
								else {
									selectPawnToSwitch(p, opposing);
									sg.cardButton.setEnabled(true);
								}
							}
							else {
								//String pawnMoved = p.getRow() + " " + p.getCol();
								movePawn(p, 11);
								//pawnMoved += " " + p.getRow() + " " + p.getCol();
								//sc.out.println("MOVE " + pawnMoved);
								//sg.cardButton.setEnabled(true);
								//sc.out.println("TURN_OVER");
							}
						}
						public void mouseEntered(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							highlightTile(hovered, true);
						}
						
						public void mouseExited(MouseEvent me) {
							JLabel hovered = (JLabel) me.getSource();
							unhighlightTile(hovered, true);
						}
					});	
				}
				JOptionPane.showMessageDialog(null, "Select the pawn you want to move.");
			}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, you cannot move.");
				sc.out.println("TURN_OVER");
				sg.cardButton.setEnabled(false);
				sg.p4.stopTimer();
			}
		}
		//sg.p4.stopTimer();
		if (currentPlayer.allPawnsHome()) endGame();
	}
	
	public void movePawn(int oldR, int oldC, int newR, int newC) {
		Pawn toMove = getPawnAt(oldR, oldC);
		toMove.setLocation(newR, newC);
		b.movePawn(oldR, oldC, newR, newC, toMove.getColor());
		updatePawnValue(getPlayer(getPawnAt(newR, newC)), true);
		updatePawnValue(getPlayer(getPawnAt(newR, newC)), false);
	}
	
	public void botTurns(String certainBot) {
		if (!gameOver()) {
			for (int i = 0; i < numPlayers; i++) {
				if (players.get(i).bot == true && (players.get(i).getColor().equals(certainBot) || certainBot.equals(""))) {
					currentPlayer = players.get(i);
					int cardValue = getCPUCard();
					ArrayList<Pawn> validPawns = getValidPawns(cardValue);
					if (cardValue == 0) {
						//sorry
						ArrayList<Pawn> opposingPawns = getOpposingPawnsOnBoard();
						Pawn toSwitch = null;
						if (validPawns.size() > 0) toSwitch = validPawns.get(0);
						if (toSwitch != null) {
							//they have a pawn in start
							if (opposingPawns.size() >= 1) {
								//there is an opposing pawn on the board
								Pawn toStart = opposingPawns.get(0);
								sorry(toSwitch, toStart);
								//if (toStart.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " sent your pawn to start.");
							}
							
						}
					}
					else if (cardValue == 1 || cardValue == 2) {
						if (validPawns.size() >= 1) {
							boolean moved = false;
							for (int j = 0; j < validPawns.size(); j++) {
								if (validPawns.get(j).isInStart()) {
									Pawn p = validPawns.get(j);
									//String pawnMoved = p.getRow() + " " + p.getCol();
									movePawn(validPawns.get(j), cardValue);
									//pawnMoved += " " + p.getRow() + " " + p.getCol();
									//sc.out.println("MOVE " + pawnMoved);
									j = 5;
									moved = true;
								}
							}
							if (!moved) {
								//try to land on enemy
								for (int j = 0; j < validPawns.size(); j++) {
									if (willLandOnOpponent(validPawns.get(j), cardValue)) {
										Pawn p = validPawns.get(j);
										//String pawnMoved = p.getRow() + " " + p.getCol();
										movePawn(validPawns.get(j), cardValue);
										//pawnMoved += " " + p.getRow() + " " + p.getCol();
										//sc.out.println("MOVE " + pawnMoved);
										j = 5;
										moved = true;
									}
								}
								if (!moved) {
									Pawn p = validPawns.get(0);
									//String pawnMoved = p.getRow() + " " + p.getCol();
									movePawn(p, cardValue);
									//pawnMoved += " " + p.getRow() + " " + p.getCol();
									//sc.out.println("MOVE " + pawnMoved);
								}
							}
							if (cardValue == 2) i = i - 1;
						}
						else {
							if (cardValue == 2) i = i - 1;
						}
					}
					else if (cardValue == 3 || cardValue == 4 || cardValue == 5 || cardValue == 8 || cardValue == 12) {
						if (validPawns.size() >= 1) {
							boolean moved = false;
							if (cardValue != 4) {
								for (int j = 0; j < validPawns.size(); j++) {
									if (willLandOnOpponent(validPawns.get(j), cardValue)) {
										Pawn p = validPawns.get(0);
										//String pawnMoved = p.getRow() + " " + p.getCol();
										movePawn(p, cardValue);
										//pawnMoved += " " + p.getRow() + " " + p.getCol();
										//sc.out.println("MOVE " + pawnMoved);
										j = 5;
										moved = true;
									}
								}
								//move most progressed pawn
								if (!moved) {
									Pawn p = validPawns.get(0);
									//String pawnMoved = p.getRow() + " " + p.getCol();
									movePawn(p, cardValue);
									//pawnMoved += " " + p.getRow() + " " + p.getCol();
									//sc.out.println("MOVE " + pawnMoved);
								}
							}
							else {
								Pawn p = validPawns.get(validPawns.size() - 1);
								//String pawnMoved = p.getRow() + " " + p.getCol();
								movePawn(p, cardValue);
								//pawnMoved += " " + p.getRow() + " " + p.getCol();
								//sc.out.println("MOVE " + pawnMoved);
								movePawn(p, -4);
							}
						}
					}
					else if (cardValue == 7) {
						if (validPawns.size() == 1) {
							boolean moved = false;
							for (int j = 0; j < validPawns.size(); j++) {
								if (isValidMove(validPawns.get(j), 7) && !moved) {
									Pawn p = validPawns.get(j);
									//String pawnMoved = p.getRow() + " " + p.getCol();
									movePawn(p, cardValue);
									//pawnMoved += " " + p.getRow() + " " + p.getCol();
									//sc.out.println("MOVE " + pawnMoved);
									moved = true;
								}
							}
						}
						else if (validPawns.size() > 1) {
							boolean moved = false;
							for (int j = 0; j < validPawns.size(); j++) {
								for (int k = 7; k > 0; k--) {
									if (isValidMove(validPawns.get(j), k) && !moved) {
										Pawn first = validPawns.get(j);
										ArrayList<Pawn> remove = new ArrayList<>();
										remove.add(validPawns.get(j));
										validPawns.removeAll(remove);
										for (int n = 0; n < validPawns.size(); n++) {
											if (isValidMove(validPawns.get(n), 7 -k) && !moved) {
												//String pawnMoved = first.getRow() + " " + first.getCol();
												movePawn(first, k);
												//pawnMoved += " " + first.getRow() + " " + first.getCol();
												//sc.out.println("MOVE " + pawnMoved);
												
												//pawnMoved = validPawns.get(n).getRow() + " " + validPawns.get(n).getCol();
												movePawn(validPawns.get(n), 7 - k);
												//pawnMoved += " " + validPawns.get(n).getRow() + " " + validPawns.get(n).getCol();
												//sc.out.println("MOVE " + pawnMoved);
												moved = true;
											}
										}
									}
								}
							}
						}
					}
					else if (cardValue == 10) {
						if (validPawns.size() >= 1) {
							boolean moved = false;
							for (int j = 0; j < validPawns.size(); j++) {
								if (!moved) {
									if (!isValidMove(validPawns.get(j), -1)) {
										if (isValidMove(validPawns.get(j), 10)) {
											//moving forward is valid, backwards is not
											Pawn p = validPawns.get(j);
											//String pawnMoved = p.getRow() + " " + p.getCol();
											movePawn(p, cardValue);
											//pawnMoved += " " + p.getRow() + " " + p.getCol();
											//sc.out.println("MOVE " + pawnMoved);
											moved = true;
										}
									}
									else {
										//moving -1 is valid, moving 10 is not
										if (!isValidMove(validPawns.get(j), 10)) {
											Pawn p = validPawns.get(j);
											String pawnMoved = p.getRow() + " " + p.getCol();
											movePawn(p, -1);
											pawnMoved += " " + p.getRow() + " " + p.getCol();
											//sc.out.println("MOVE " + pawnMoved);
											moved = true;
										}
										//both are valid
										else {
											Pawn p = validPawns.get(j);
											String pawnMoved = p.getRow() + " " + p.getCol();
											movePawn(p, cardValue);
											pawnMoved += " " + p.getRow() + " " + p.getCol();
											//sc.out.println("MOVE " + pawnMoved);
											moved = true;
										}
									}
								}
							}
						}
							
					}
					else if (cardValue == 11) {
						ArrayList<Pawn> ops = getOpposingPawnsOnBoard();
						if (ops.size() == 0) {
							if (validPawns.size() != 0) {
								Pawn p = validPawns.get(0);
								String pawnMoved = p.getRow() + " " + p.getCol();
								movePawn(p, 11);
								pawnMoved += " " + p.getRow() + " " + p.getCol();
								//sc.out.println("MOVE " + pawnMoved);
							}
						}
						else {
							if (validPawns.size() > 0) {
								if (isValidMove(validPawns.get(0), 11)) {
									Pawn p = validPawns.get(0);
									String pawnMoved = p.getRow() + " " + p.getCol();
									movePawn(p, 11);
									pawnMoved += " " + p.getRow() + " " + p.getCol();
									//sc.out.println("MOVE " + pawnMoved);
								}
								else {
									sc.out.println("SWITCH " + validPawns.get(0).getRow() + " " + validPawns.get(0).getCol() + " " + ops.get(0).getRow() + " " + ops.get(0).getCol());
									switchPawns(validPawns.get(0), ops.get(0));
								}
							}
						}
					}
				}
			}
			sc.out.println("BOTS_DONE");
		}
		else endGame();
	}
	
	private boolean willLandOnOpponent(Pawn p, int amount) {
		int oldR = p.getRow();
		int oldC = p.getCol();
		int r = p.getRow();
		int c = p.getCol();
		Pawn copy = new Pawn(p.getColor(), r, c);
		boolean last = false;
		if (amount > 0) {
			for (int i = 0; i < amount; i++) {
				if (i == amount - 1) last = true;
				int[] newCoords = getNextSpace(copy, last, false);
				r = newCoords[0];
				c = newCoords[1];
				if (getPawnAt(r, c) != null && getPawnAt(r, c).getColor() != p.getColor()) {
					return true;
				}
			}
		}
		else {
			for (int i = amount; i < 0; i++) {
				int[] newCoords = getPreviousSpace(copy);
				r = newCoords[0];
				c = newCoords[1];
				if (getPawnAt(r, c) != null && getPawnAt(r, c).getColor() != p.getColor()) {
					return true;
				}
			}
		}
		if (getPawnAt(r, c) != null && getPawnAt(r, c).getColor() != p.getColor()) {
			return true;
		}
		return false;
	}
	
	private int getCPUCard() {
		String card = d.getNextCard();
		return getCardValue(card);
	}
	
	
	private void movePawn(Pawn p, int amount) {
		//move pawn animates the pawn
		//so create a timer, and call moveToNextSpace, then end the turn and stop the timer
		pawnAtPrev = null;
		foundOnThisMove = false;
		sg.p4.stopTimer();
		goAgain = false;
		if (amount == 2) goAgain = true;
		//System.out.println("moving pawn " + amount);
		amountAlreadyMoved = 0;
		sg.p4.stopTimer();
		sg.p4.cardButton.setEnabled(false);
		amountToMove = amount;
		movingPawn = p;
		movingPawnTimer = new Timer(amount * 250, new TimerListener());
		movingPawnTimer.setDelay(250);
		movingPawnTimer.setInitialDelay(0);
		movingPawnTimer.start();
	}
	
	private void moveToNextSpace() {
		if (pawnAtPrev != null) foundOnThisMove = false;
		//System.out.println("client: " + color);
		amountAlreadyMoved++;
		//System.out.println("amount moved: " + amountAlreadyMoved);
		int oldR = movingPawn.getRow();
		int oldC = movingPawn.getCol();
		if (amountToMove > 0) {
			boolean last = (amountToMove == amountAlreadyMoved);
			Pawn copy = new Pawn(movingPawn.getColor(), movingPawn.getRow(), movingPawn.getCol());
			int[] nextSpace = getNextSpace(copy, last, true);
			//System.out.println("checking to see if there's a pawn at " + nextSpace[0] +", " + nextSpace[1]);
			//System.out.println(getPawnAt(nextSpace[0], nextSpace[1]) != null);
			if (getPawnAt(nextSpace[0], nextSpace[1]) != null) {
				pawnAtPrev = getPawnAt(nextSpace[0], nextSpace[1]);
				if (!pawnAtPrev.isHome()) {
					foundOnThisMove = true;
					if (amountToMove == amountAlreadyMoved) {
						Pawn sendToStart = pawnAtPrev;
						sendToStart.sendToStart();
						sc.out.println("MOVE " + nextSpace[0] + " " + nextSpace[1] + " " + sendToStart.getRow() + " " + sendToStart.getCol());
						sc.out.flush();
						b.movePawn(nextSpace[0], nextSpace[1], sendToStart.getRow(), sendToStart.getCol(), sendToStart.getColor());
						updatePawnValue(getPlayer(sendToStart), true);
					}
				}
				else pawnAtPrev = null;
			}
			movingPawn.setLocation(nextSpace[0], nextSpace[1]);
			//b.movePawn(oldR, oldC, movingPawn.getRow(), movingPawn.getCol(), color);
			b.movePawn(oldR, oldC, nextSpace[0], nextSpace[1], color);

			sc.out.println("MOVE " + oldR + " " + oldC + " " + nextSpace[0] + " " + nextSpace[1]);
			if (pawnAtPrev != null && !foundOnThisMove) {
				//right now the moving pawn is one ahead of the one it passed, need to put the one it passed back
				b.movePawn(pawnAtPrev.getRow(), pawnAtPrev.getCol(), pawnAtPrev.getRow(), pawnAtPrev.getCol(), pawnAtPrev.getColor());
				sc.out.println("MOVE " + pawnAtPrev.getRow() + " " + pawnAtPrev.getCol() + " " + pawnAtPrev.getRow() + " " + pawnAtPrev.getCol());
				//need to send a message to the server saying to put it back
				sc.out.println("SWITCH " + nextSpace[0] + " " + nextSpace[1] + " " + pawnAtPrev.getRow() + " " + pawnAtPrev.getCol());
				pawnAtPrev = null;
				foundOnThisMove = false;
			}
			if (amountToMove == amountAlreadyMoved || movingPawn.isHome()) {
				//System.out.println("turn ended, stopping timer");
				movingPawnTimer.stop();

				movingPawn.setLocation(nextSpace[0], nextSpace[1]);
				updatePawnValue(currentPlayer, true);
				updatePawnValue(currentPlayer, false);
				
				if (!goAgain) {
					sc.out.println("TURN_OVER");
					sg.cardButton.setEnabled(false);
				}
				sg.p4.stopTimer();
			}
		}
		else {
			Pawn copy = new Pawn(movingPawn.getColor(), movingPawn.getRow(), movingPawn.getCol());
			int[] prevSpace = getPreviousSpace(copy);
			
			if (getPawnAt(prevSpace[0], prevSpace[1]) != null) {
				pawnAtPrev = getPawnAt(prevSpace[0], prevSpace[1]);
				foundOnThisMove = true;
				if (amountToMove == (-1)*amountAlreadyMoved) {
					Pawn sendToStart = pawnAtPrev;
					sendToStart.sendToStart();
					sc.out.println("MOVE " + prevSpace[0] + " " + prevSpace[1] + " " + sendToStart.getRow() + " " + sendToStart.getCol());
					sc.out.flush();
					b.movePawn(prevSpace[0], prevSpace[1], sendToStart.getRow(), sendToStart.getCol(), sendToStart.getColor());
					updatePawnValue(getPlayer(sendToStart), true);
				}
			}
			
			movingPawn.setLocation(prevSpace[0], prevSpace[1]);
			//b.movePawn(oldR, oldC, movingPawn.getRow(), movingPawn.getCol(), color);
			b.movePawn(oldR, oldC, prevSpace[0], prevSpace[1], color);
			sc.out.println("MOVE " + oldR + " " + oldC + " " + prevSpace[0] + " " + prevSpace[1]);
			if (pawnAtPrev != null && !foundOnThisMove) {
				b.movePawn(pawnAtPrev.getRow(), pawnAtPrev.getCol(), pawnAtPrev.getRow(), pawnAtPrev.getCol(), pawnAtPrev.getColor());
				sc.out.println("MOVE " + pawnAtPrev.getRow() + " " + pawnAtPrev.getCol() + " " + pawnAtPrev.getRow() + " " + pawnAtPrev.getCol());
				sc.out.println("SWITCH " + prevSpace[0] + " " + prevSpace[1] + " " + pawnAtPrev.getRow() + " " + pawnAtPrev.getCol());
				pawnAtPrev = null;
				foundOnThisMove = false;
			}
			if (amountToMove == (-1)*amountAlreadyMoved) {
				movingPawnTimer.stop();

				movingPawn.setLocation(prevSpace[0], prevSpace[1]);
				updatePawnValue(currentPlayer, true);
				updatePawnValue(currentPlayer, false);
				
				sc.out.println("TURN_OVER");
				sg.cardButton.setEnabled(false);
				sg.p4.stopTimer();
				
				
			}
		}
	}
	
	class TimerListener implements ActionListener {
		int counter = 0;
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			counter++;
			moveToNextSpace();
		}
	}
	
	private ArrayList<Pawn> getValidPawns(int card) {
		ArrayList<Pawn> validPawns = new ArrayList<>();
		Pawn[] pawns = currentPlayer.getPawns();
		if (card > 0) {
			for (int i = 0; i < 4; i++) {
				Pawn pawn = pawns[i];
				if (pawn.isInStart() == false && pawn.isHome() == false) {
					validPawns.add(pawn);
				}
			}
		}
		if (card == 0 || card == 1 || card == 2) {
			for (int i = 0; i < 4; i++) {
				Pawn pawn = pawns[i];
				if (pawn.isInStart()) {
					validPawns.add(pawn);
					i = 5;
				}
			}
		}
		//remove any invalid pawns
		if (card != 7 && card != 10) {
			ArrayList<Pawn> invalidPawns = new ArrayList<>();
			for (Pawn pawn : validPawns) {
				if (!isValidMove(pawn, card)) {
					invalidPawns.add(pawn);
				}
			}
			validPawns.removeAll(invalidPawns);
		}
		return validPawns;
	}
	
	private boolean isValidMove(Pawn p, int amountToMove) {
		if (amountToMove == 4) amountToMove = -4;
		Pawn copy = new Pawn(p.getColor(), p.getRow(), p.getCol());
		//make sure it does not pass or land on another pawn
		if (amountToMove > 0) {
			boolean last = false;
			for (int i = 0; i < amountToMove; i++) {
				//check if there is a pawn of that color on that space
				if (i == amountToMove - 1) last = true;
				int[] nextCoords = getNextSpace(copy, last, false);
				Pawn pawnAt = getPawnAt(nextCoords[0], nextCoords[1]);
				if (pawnAt != null && pawnAt.getColor() == p.getColor()) {
					if (!pawnAt.isHome()) return false;
				}
				copy.setLocation(nextCoords[0], nextCoords[1]);
			}
		}
		else {
			for (int i = amountToMove; i < 0; i++) {
				int[] prevCoords = getPreviousSpace(copy);
				Pawn pawnAt = getPawnAt(prevCoords[0], prevCoords[1]);
				if (pawnAt != null && pawnAt.getColor() == p.getColor()) {
					return false;
				}
				copy.setLocation(prevCoords[0], prevCoords[1]);
			}
		}
		return true;
	}
	
	private int[] getPreviousSpace(Pawn p) {
		int r = p.getRow();
		int c = p.getCol();
		int[] coords = new int[2];
		int prevR = r;
		int prevC = c;
		if (p.isSafe()) {
			if (p.getColor().equals("red")) prevR = r + 1;
			else if (p.getColor().equals("blue")) prevC = c - 1;
			else if (p.getColor().equals("green")) prevC = c + 1;
			else prevR = r - 1;
		}
		if (r == 15) {
			if (c != 15) {
				prevC = c + 1;
			}
			else {
				prevR = 14;
			}
		}
		else if (c == 0) {
			if (r != 0) {
				prevR = r + 1;
			}
			else prevC = 1;
		}
		else if (r == 0) {
			if (c != 0) {
				prevC = c - 1;
			}
			else prevR = 1;
		}
		else if (c == 15) {
			if (r != 0) {
				prevR = r - 1;
			}
			else prevC = 14;
		}
		p.setLocation(prevR, prevC);
		coords[0] = prevR;
		coords[1] = prevC;
		return coords;
	}
	
	public int[] getNextSpace(Pawn p, boolean last, boolean sendPawns) {
		int c = p.getCol();
		int r = p.getRow();
		if (p.isInStart()) {
			if (p.getColor().equals("red")) {
				r = 15;
				c = 11;
			}
			else if (p.getColor().equals("blue")) {
				r = 11;
				c = 0;
			}
			else if (p.getColor().equals("yellow")) {
				r = 0;
				c = 4;
			}
			else {
				r = 4;
				c = 15;		
			}
		}
		else {
			if (c == 13 &&  r > 9 && currentPlayer.getColor().equals("red")) {
				if (!p.isHome()) r = r - 1;
			}
			else if (c < 7 &&  r == 13 && currentPlayer.getColor().equals("blue")) {
				if (!p.isHome()) c = c + 1;
			}
			else if (c == 2 &&  r < 7 && currentPlayer.getColor().equals("yellow")) {
				if (!p.isHome()) r = r + 1;
			}
			else if (c > 9 && r == 2 && currentPlayer.getColor().equals("green")) {
				if (!p.isHome()) c = c - 1;
			}
			else {
				if (r == 15) {
					if (c == 0) {
						if (last) {
							r = 11;
							if (sendPawns) sendAnyPawnsToStart(14, 0);
						}
						else r = r - 1;
					}
					else 
						if (last) {
							if (c == 15) {
								c = 11;
								if (sendPawns) sendAnyPawnsToStart(15, 14);
							}
							else if (c == 7) {
								c = 2;
								if (sendPawns) sendAnyPawnsToStart(15, 6);
							}
							else c = c - 1;
						}
						else c = c - 1;
				}
				else if (c == 0) {
					if (r == 0) {
						if (last) {
							c = 4;
							if (sendPawns) sendAnyPawnsToStart(0, 1);
						}
						else c = 1;
					}
					else if (r == 13 && p.getColor().equals("blue")) {
						if (!p.isHome()) c = r + 1;
					}
					else {
						if (last) {
							if (r == 15) {
								r = 11;
								if (sendPawns) sendAnyPawnsToStart(14, 0);
							}
							else if (r == 7) {
								r = 2;
								if (sendPawns) sendAnyPawnsToStart(6, 0);
							}
							else r = r - 1;
						}
						else r = r - 1;
					}
				}
				else if (r == 0) {
					if (c == 15) {
						if (last) {
							r = 4;
							if (sendPawns) sendAnyPawnsToStart(1, 15);
						}
						else r = 1;
					}
					else if (c == 2 && p.getColor().equals("yellow")) {
						if (!p.isHome()) r = r + 1;
					}
					else {
						if (last) {
							if (c == 0) {
								c = 4;
								if (sendPawns) sendAnyPawnsToStart(0, 1);
							}
							else if (c == 8) {
								c = 13;
								if (sendPawns) sendAnyPawnsToStart(0, 9);
							}
							else c = c + 1;
						}
						else c = c + 1;
					}
				}
				else if (c == 15) {
					if (r == 15) {
						if (last) {
							c = 11;
							if (sendPawns) sendAnyPawnsToStart(15, 14);
						}
						else c = 14;
					}
					else if (r == 2 && p.getColor().equals("green")) {
						if (!p.isHome()) c = c - 1;
					}
					else {
						if (last) {
							if (r == 0) {
								r = 4;
								if (sendPawns) sendAnyPawnsToStart(1, 15);
							}
							else if (r == 8) {
								r = 13;
								if (sendPawns) sendAnyPawnsToStart(9, 15);
							}
							else r = r + 1;
						}
						else r = r + 1;
					}
				}
			}
			}
		int[] coords = new int[2];
		coords[0] = r;
		coords[1] = c;
		p.setLocation(r, c);
		return coords;
	}
	
	public void switchPawns(Pawn playersPawn, Pawn other) {
		int r1 = playersPawn.getRow();
		int c1 = playersPawn.getCol();
		int r2 = other.getRow();
		int c2 = other.getCol();
		playersPawn.setLocation(r2, c2);
		other.setLocation(r1, c1);
		b.switchPawns(r1, c1, r2, c2, playersPawn.getColor(), other.getColor());
	}
	
	private ArrayList<Integer> getValidAmountsToMove(Pawn p1, Pawn p2) {
		ArrayList<Integer> amounts = new ArrayList<>();
		for (int i = 0; i <= 7; i++) {
			if (isValidMove7(p1, p2, i) && isValidMove7(p2, p1, 7 - i)) {
				amounts.add(i);
			}
		}
		return amounts;
	}
	
	private boolean isValidMove7(Pawn toMove, Pawn other, int amount) {
		Pawn copy = new Pawn(toMove.getColor(), toMove.getRow(), toMove.getCol());
		//make sure it does not pass or land on another pawn
		boolean last = false;
		for (int i = 0; i < amount; i++) {
			if (i == amount - 1) last = true;
			//check if there is a pawn of that color on that space
			int[] nextCoords = getNextSpace(copy, last, false);
			Pawn pawnAt = getPawnAt(nextCoords[0], nextCoords[1]);
			if (pawnAt != null && pawnAt.getColor() == toMove.getColor() && pawnAt != other) {
				if (!pawnAt.isHome()) return false;
			}
			copy.setLocation(nextCoords[0], nextCoords[1]);
		}
		return true;
	}
	
	private void sendAnyPawnsToStart(int r, int c) {
		//takes in the starting location of a slide and sends any pawns on that slide to start
		if (r == 15) {
			if (c == 14) {
				for (int i = c; i > 10; i--) {
					if (getPawnAt(r, i) != null) {
						Pawn p = getPawnAt(r, i);
						if (getPawnAt(r, i).getColor() != currentPlayer.getColor()) {
							p.sendToStart();
							b.movePawn(r, i, p.getRow(), p.getCol(), p.getColor());
							updatePawnValue(getPlayer(p), true);
							if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
						}
					}
				}
			}
			else if (c == 6) {
				for (int i = c; i > 1; i--) {
					if (getPawnAt(r, i) != null) {
						Pawn p = getPawnAt(r, i);
						if (getPawnAt(r, i).getColor() != currentPlayer.getColor()) {
							p.sendToStart();
							b.movePawn(r, i, p.getRow(), p.getCol(), p.getColor());
							updatePawnValue(getPlayer(p), true);
							if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
						}
					}
				}
			}
		}
		else if (c == 0) {
			if (r == 14) {
				for (int i = r; i > 10; i--) {
					if (getPawnAt(i, c) != null) {
						Pawn p = getPawnAt(i, c);
						if (getPawnAt(i, c).getColor() != currentPlayer.getColor()) {
							p.sendToStart();
							b.movePawn(i, c, p.getRow(), p.getCol(), p.getColor());
							updatePawnValue(getPlayer(p), true);
							if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
						}
					}
				}
			}
			else if (r == 6) {
				for (int i = r; i > 1; i--) {
					if (getPawnAt(i, c) != null) {
						if (getPawnAt(i, c) != null) {
							Pawn p = getPawnAt(i, c);
							if (getPawnAt(i, c).getColor() != currentPlayer.getColor()) {
								p.sendToStart();
								b.movePawn(i, c, p.getRow(), p.getCol(), p.getColor());
								updatePawnValue(getPlayer(p), true);
								if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
							}
						}
					}
				}
			}
		}
		else if (r == 0) {
			if (c == 1) {
				for (int i = c; i < 5; i++) {
					if (getPawnAt(r, i) != null) {
						Pawn p = getPawnAt(r, i);
						if (getPawnAt(r, i).getColor() != currentPlayer.getColor()) {
							p.sendToStart();
							b.movePawn(r, i, p.getRow(), p.getCol(), p.getColor());
							updatePawnValue(getPlayer(p), true);
							if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
						}
					}
				}
			}
			else if (c == 9) {
				for (int i = c; i < 14; i++) {
					if (getPawnAt(r, i) != null) {
						Pawn p = getPawnAt(r, i);
						if (getPawnAt(r, i).getColor() != currentPlayer.getColor()) {
							p.sendToStart();
							b.movePawn(r, i, p.getRow(), p.getCol(), p.getColor());
							updatePawnValue(getPlayer(p), true);
							if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
						}
					}
				}
			}
		}
		else if (c == 15) {
			if (r == 1) {
				for (int i = r; i < 5; i++) {
					if (getPawnAt(i, c) != null) {
						Pawn p = getPawnAt(i, c);
						if (getPawnAt(i, c).getColor() != currentPlayer.getColor()) {
							p.sendToStart();
							b.movePawn(i, c, p.getRow(), p.getCol(), p.getColor());
							updatePawnValue(getPlayer(p), true);
							if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
						}
					}
				}
			}
			else if (r == 9) {
				for (int i = r; i < 14; i++) {
					if (getPawnAt(i, c) != null) {
						Pawn p = getPawnAt(i, c);
						if (getPawnAt(i, c).getColor() != currentPlayer.getColor()) {
							p.sendToStart();
							b.movePawn(i, c, p.getRow(), p.getCol(), p.getColor());
							updatePawnValue(getPlayer(p), true);
							if (p.getColor().equals(color)) JOptionPane.showMessageDialog(null, currentPlayer.getColor() + " player landed on a slide and sent your pawn to start.");
						}
					}
				}
			}
		}
	}
	
	private void sorry(Pawn toSwitch, Pawn sendToStart) {
		//take toSwitch out of start and put it at the location of sendToStart
		//send sendToStart to start
		int oldR1 = toSwitch.getRow(); //start
		int oldC1 = toSwitch.getCol();
		int oldR2 = sendToStart.getRow(); //send to start's initial space
		int oldC2 = sendToStart.getCol();
		int newR1 = oldR2; //new location is where the other pawn was
		int newC1 = oldC2;
		sendToStart.sendToStart();
		sc.out.println("MOVE " + oldR2 + " " + oldC2 + " " + sendToStart.getRow() + " " + sendToStart.getCol());
		sc.out.flush();
		int newR2 = sendToStart.getRow();
		int newC2 = sendToStart.getCol();
		toSwitch.setLocation(newR1, newC1);
		sc.out.println("MOVE " + oldR1 + " " + oldC1 + " " + toSwitch.getRow() + " " + toSwitch.getCol());
		sc.out.flush();
		b.movePawn(oldR2, oldC2, newR2, newC2, sendToStart.getColor());
		b.movePawn(oldR1, oldC1, newR1, newC1, toSwitch.getColor());
		updatePawnValue(currentPlayer, true);
		updatePawnValue(getPlayer(sendToStart), true);
	}
	
	private void updatePawnValue(Player p, boolean start) {
		//must be called after the pawn is already in that location
		int val = 0;
		if (start) val = p.getNumInStart();
		else val = p.getNumHome();
		JLabel[][] board = b.getBoard();
		if (p.getColor().equals("red")) {
			if (start) board[13][11].setText("" + val);
			else board[8][13].setText("" + val);
		}
		else if (p.getColor().equals("blue")) {
			if (start) board[11][2].setText("" + val);
			else board[13][7].setText("" + val);
		}
		else if (p.getColor().equals("yellow")) {
			if (start) board[2][4].setText("" + val);
			else board[7][2].setText("" + val);
		}
		else {
			if (start) board[4][13].setText("" + val);
			else board[2][8].setText("" + val);
		}
	}
	
	private Player getPlayer(Pawn p) {
		for (int i = 0; i < numPlayers; i++) {
			Pawn[] pawns = players.get(i).getPawns();
			for (int j = 0; j < 4; j++) {
				if (pawns[j] == p) return players.get(i);
			}
		}
		return null;
	}
	
	public Pawn getPawnAt(int r, int c) {
		for (int i = 0; i < players.size(); i++) {
			Player u = players.get(i);
			if(u.getPawnAt(r, c) != null) return u.getPawnAt(r, c);
		}
		return null;
	}
	
	private ArrayList<Pawn> getOpposingPawnsOnBoard() {
		ArrayList<Player> otherPlayers = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) != currentPlayer) {
				otherPlayers.add(players.get(i));
			}
		}
		ArrayList<Pawn> pawnsOnBoard = new ArrayList<>();
		for (int i = 0; i < otherPlayers.size(); i++) {
			Player u = otherPlayers.get(i);
			Pawn[] pawns = u.getPawns();
			for (int j = 0; j < 4; j++) {
				if (!pawns[j].isInStart() && !pawns[j].isHome() && !pawns[j].isSafe()) {
					pawnsOnBoard.add(pawns[j]);
				}
			}
		}
		return pawnsOnBoard;
	}
	
	private void selectSecondPawn(ArrayList<Pawn> validPawns, Pawn p1) {
		for (int i = 0; i < validPawns.size(); i++) {
			Pawn currentPawn = validPawns.get(i);
			JLabel[][] board = b.getBoard();
			JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
			labelsWithListeners.add(currLabel);
			currLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					JLabel clicked = (JLabel) me.getSource();
					int[] pawnCoords = b.findPawn(clicked);
					Pawn p2 = getPawnAt(pawnCoords[0], pawnCoords[1]);
					ArrayList<Integer> validAmounts = getValidAmountsToMove(p1, p2);
					Object[] moveOptions = new Object[validAmounts.size()];
					for (int i = 0; i < validAmounts.size(); i++) {
						moveOptions[i] = validAmounts.get(i);
					}
					Object initialSelection = moveOptions[0];
					int selection = (int) JOptionPane.showInputDialog(null, "Select the amount to move the first pawn (" + p1.getRow() + "," + p1.getCol() + ")", "Move Pawn 1", JOptionPane.QUESTION_MESSAGE, null, moveOptions, initialSelection);
					String pawnMoved = p1.getRow() + " " + p1.getCol();
					movePawn(p1, selection);
					pawnMoved += " " + p1.getRow() + " " + p1.getCol();
					sc.out.println("MOVE " + pawnMoved);
					
					pawnMoved = p2.getRow() + " " + p2.getCol();
					movePawn(p2, 7 - selection);
					pawnMoved += " " + p2.getRow() + " " + p2.getCol();
					sc.out.println("MOVE " + pawnMoved);
					removeListeners();
					//CPUTurns();
					sc.out.println("TURN_OVER");
				}
			});	
		}
		JOptionPane.showMessageDialog(null, "Select the pawn you want to move second.");
	}
	
	private void selectPawnToSwitch(Pawn p, ArrayList<Pawn> opposing) {
		for (int i = 0; i < opposing.size(); i++) {
			Pawn currentPawn = opposing.get(i);
			JLabel[][] board = b.getBoard();
			JLabel currLabel = board[currentPawn.getRow()][currentPawn.getCol()];
			labelsWithListeners.add(currLabel);
			currLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					JLabel clicked = (JLabel) me.getSource();
					int[] pawnCoords = b.findPawn(clicked);
					Pawn s = getPawnAt(pawnCoords[0], pawnCoords[1]);
					sc.out.println("SWITCH " + p.getRow() + " " + p.getCol() + " " + s.getRow() + " " + s.getCol());
					switchPawns(p, s);
					removeListeners();
					//CPUTurns();
					sc.out.println("TURN_OVER");
				}
			});						
		}
		JOptionPane.showMessageDialog(null, "Select the pawn you want to switch with.");
	}
	
	private int getCardValue(String card) {
		int c = 0;
		if (card.charAt(0) == '1') {
			if (card.charAt(1) == ' ') c = 1;
			else if (card.charAt(1) == '0') c = 10;
			else if (card.charAt(1) == '1') c = 11;
			else if (card.charAt(1) == '2') c = 12;
		}
		else if (card.charAt(0) == '2') c = 2;
		else if (card.charAt(0) == '3') c = 3;
		else if (card.charAt(0) == '4') c = 4;
		else if (card.charAt(0) == '5') c = 5;
		else if (card.charAt(0) == '7') c = 7;
		else if (card.charAt(0) == '8') c = 8;
		
		return c;
	}
	
	private void removeListeners() {
		for (int i = 0; i < labelsWithListeners.size(); i++) {
			JLabel currLabel = labelsWithListeners.get(i);
			for (MouseListener ml : currLabel.getMouseListeners()) {
				currLabel.removeMouseListener(ml);
			}
		}
		labelsWithListeners.clear();
	}
	
	private boolean gameOver() {
		for (int i = 0; i < numPlayers; i++) {
			if (players.get(i).allPawnsHome() == true) return true;
		}
		return false;
	}
	
	private void endGame() {
		String winningColor = "";
		for (int i = 0; i < numPlayers; i++) {
			if (players.get(i).allPawnsHome() == true) winningColor = players.get(i).getColor();
		}
		sc.out.println("END_GAME " + winningColor);
		
		System.out.println("color: " + color);
		System.out.println("winning color: " + winningColor);
		if (color.equals(winningColor)) {
			SoundPlayer sp = new SoundPlayer("victory");
			sp.play();
		}
		else {
			SoundPlayer sp = new SoundPlayer("lose");
			sp.play();
		}
		
		JOptionPane.showMessageDialog(sg, winningColor + " player won!");
		int points = calculatePoints();
		String name = JOptionPane.showInputDialog("Your score: " + points + "\nEnter your name:");
		sg.scoreTable.addRow(name, points);
		sg.setVisible(false);
		SorryGUI newGame = new SorryGUI();
		newGame.setVisible(true);
	}
	
	public void playEndSound(String winner) {
		if (color.equals(winner)) {
			SoundPlayer sp = new SoundPlayer("victory");
			sp.play();
		}
		else {
			SoundPlayer sp = new SoundPlayer("lose");
			sp.play();
		}
	}
	
	private int calculatePoints() {
		int points = 0;
		//5 points for each pawn they get home
		Player p = players.get(0);
		Pawn[] playersPawns = p.getPawns();
		for (int i = 0; i < 4; i++) {
			if (playersPawns[i].isHome()) points = points + 5;
		}
		//3 points for each opponent that is not in home
		//1 point for each opponent pawn in start
		players.remove(0);
		for (int i = 0; i < players.size(); i++) {
			Player curr = players.get(i);
			Pawn[] pawns = curr.getPawns();
			for (int j = 0; j < 4; j++) {
				if (pawns[j].isInStart()) points = points + 1;
				else if (pawns[j].isHome() == false) points = points + 3;
				
			}
		}
		return points;
	}
}
