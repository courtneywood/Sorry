

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SorryClient extends Thread {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	SorryGUI sg;
	int numPlayers = 0;
	boolean allPlayersReady = false;
	boolean host = false;
	boolean hasBots = false;
	boolean canJoin = true;
	ArrayList<String> colors = new ArrayList<String>();
	public SorryClient(String ipAddress, int port, SorryGUI sg) throws UnknownHostException, IOException {
		socket = new Socket(ipAddress, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		this.sg = sg;
		out.println("NEED_ALL_PLAYERS_READY");
		out.flush();
		start();
	}
	
	public void setHost(boolean host) {
		this.host = host;
	}
	
	public boolean allPlayersReady() {
		return allPlayersReady;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}
	
	public ArrayList<String> getColors() {
		return colors;
	}
	
	public void run() {
		try {
			while (true) {
				String response = in.readLine();
				//System.out.println("Client received message: " + response);
				if (response == null) {
					JOptionPane.showMessageDialog(sg, "The host has left the game!");
					sg.setVisible(false);
					Point loc = sg.getLocation();
					SorryGUI newGame = new SorryGUI();
					newGame.setLocation(loc);
					newGame.setVisible(true);
					break;
				}
				else if (response.startsWith("CANNOT_JOIN")) {
					canJoin = false;
					JOptionPane.showMessageDialog(sg, "Sorry, this game is full                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        .");
					sg.setVisible(false);
					Point loc = sg.getLocation();
					SorryGUI newGame = new SorryGUI();
					newGame.setLocation(loc);
					newGame.setVisible(true);
				}
				else if (response.startsWith("SELECTED")) {
					String color = response.substring(9);
					if (color.equals("red")) {
						sg.p3.red.setEnabled(false);
						sg.p3.red.setForeground(Color.GRAY);
					}
					else if (color.equals("blue")) {
						sg.p3.blue.setEnabled(false);
						sg.p3.blue.setForeground(Color.GRAY);
					}
					else if (color.equals("green")) {
						sg.p3.green.setEnabled(false);
						sg.p3.green.setForeground(Color.GRAY);
					}
					else {
						sg.p3.yellow.setEnabled(false);
						sg.p3.yellow.setForeground(Color.GRAY);
					}
					
				}
				else if (response.startsWith("TAKEN_COLORS")) {
					Scanner scan = new Scanner(response);
					String taken = scan.next();
					while (scan.hasNext()) {
						String color = scan.next();
						if (color.equals("red")) {
							sg.p3.red.setEnabled(false);
							sg.p3.red.setForeground(Color.GRAY);
						}
						else if (color.equals("blue")) {
							sg.p3.blue.setEnabled(false);
							sg.p3.blue.setForeground(Color.GRAY);
						}
						else if (color.equals("green")) {
							sg.p3.green.setEnabled(false);
							sg.p3.green.setForeground(Color.GRAY);
						}
						else {
							sg.p3.yellow.setEnabled(false);
							sg.p3.yellow.setForeground(Color.GRAY);
						}
					}
				}
				else if (response.startsWith("LEFT_GAME")) {
					boolean duringTurn = false;
					String color = "";
					if (response.startsWith("LEFT_GAME DURING TURN")) {
						//finish turn
						color = response.substring(22);
						duringTurn = true;
					}
					else color = response.substring(10);
					numPlayers--;
					allPlayersReady = false;
					if (color.equals("red")) {
						sg.p3.red.setEnabled(true);
						sg.p3.red.setForeground(Color.BLACK);
					}
					else if (color.equals("blue")) {
						sg.p3.blue.setEnabled(true);
						sg.p3.blue.setForeground(Color.BLACK);
					}
					else if (color.equals("green")) {
						sg.p3.green.setEnabled(true);
						sg.p3.green.setForeground(Color.BLACK);
					}
					else {
						sg.p3.yellow.setEnabled(true);
						sg.p3.yellow.setForeground(Color.BLACK);
					}
					if (sg.p4 != null) {
						JLabel[] rows = sg.p4.rows;
						rows[0].setText(rows[1].getText());
						rows[1].setText(rows[2].getText());
						rows[2].setText(rows[3].getText());
						String colorUpper = color.toUpperCase();
						rows[3].setText("<html><font color=" + color + ">" + colorUpper + " HAS LEFT THE GAME"  + "</font></html>");
						if (host) {
							//take over by bot
							hasBots = true;
							//create a bot for this color
							sg.p4.g.createBot(color);
							if (duringTurn) {
								sg.p4.g.botTurns(color);
							}
							sg.cardButton.setEnabled(true);
							sg.p4.g.promptUser();
						}
					}
				}
				
				else if (response.startsWith("BOTS_TURNS")) {
					if (hasBots) {
						sg.p4.g.botTurns("");
						//only pass in a color if the bot needs to finish their turn
					}
					else out.println("BOTS_DONE");
				}
				
				else if (response.startsWith("ALL_PLAYERS_READY")) {
					allPlayersReady = true;
					Scanner scan = new Scanner(response);
					String ready = scan.next();
					numPlayers = scan.nextInt();
				}
				else if (response.startsWith("NOT")) {
					//System.out.println("not all players are ready");
				}
				else if (response.startsWith("CREATE_GAME")) {
					Scanner scan = new Scanner(response);
					String c = scan.next();
					while (scan.hasNext()) {
						String color = scan.next();
						colors.add(color);
					}
					sg.p4.createGame(colors);
					sg.cardButton.setEnabled(false);
					
				}
				else if (response.startsWith("CHAT")) {
					Scanner scan = new Scanner(response);
					String chat = scan.next();
					String color = scan.next();
					String message = scan.nextLine();
					JLabel[] rows = sg.p4.rows;
					rows[0].setText(rows[1].getText());
					rows[1].setText(rows[2].getText());
					rows[2].setText(rows[3].getText());
					String colorUpper = color.toUpperCase();
					rows[3].setText("<html><font color=" + color + ">" + colorUpper + ":</font><font color=white>" + message + "</font></html>");
				}
				else if (response.equals("TAKE_TURN")) {
					sg.cardButton.setEnabled(true);
					sg.p4.g.promptUser();
				}
				else if (response.startsWith("MOVE")) {
					Scanner scan = new Scanner(response);
					String move = scan.next();
					while (scan.hasNext()) {
						int oldR = scan.nextInt();
						int oldC = scan.nextInt();
						int newR = scan.nextInt();
						int newC = scan.nextInt();
						sg.p4.g.movePawn(oldR, oldC, newR, newC);
					}
				}
				else if (response.startsWith("SWITCH")) {
					Scanner scan = new Scanner(response);
					String switchword = scan.next();
					int r1 = scan.nextInt();
					int c1 = scan.nextInt();
					int r2 = scan.nextInt();
					int c2 = scan.nextInt();
					sg.p4.g.switchPawns(sg.p4.g.getPawnAt(r1, c1), sg.p4.g.getPawnAt(r2,  c2));
				}
				
				else if (response.startsWith("END_GAME")) {
					Scanner scan = new Scanner(response);
					String endgame = scan.next();
					String winner = scan.next();
					sg.p4.g.playEndSound(winner);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
