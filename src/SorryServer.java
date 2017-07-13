

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

public class SorryServer extends Thread {
	private int port;
	private ServerSocket listener;
	int numPlayers;
	int playersPressedConfirm = 0;
	ArrayList<String> allColors = new ArrayList<>();
	volatile ArrayList<SorryUser> players = new ArrayList<>();
	SorryUser currPlayer;
	int currLoc = 0;
	public SorryServer(int port, int numPlayers) throws IOException {
		this.port = port;
		this.numPlayers = numPlayers;
		listener = new ServerSocket(port);
		start();
	}
	
	public void run() {
		try {
	    	 while (true) {
	    		 //if (players.size() < numPlayers) {
	    			 Socket s = null;
					try {
						s = listener.accept();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		 SorryUser u = null;
					try {
						u = new SorryUser(s, this);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (players.size() < numPlayers) {
						players.add(u);
			    		 currPlayer = players.get(0);
			    		 u.start();
			    		 if (players.size() == numPlayers) {
			    			 sendToAllPlayers("ALL_PLAYERS_READY " + numPlayers);
			    		 }
					}
					else {
						u.sendMessage("CANNOT_JOIN");
					}
	    		 }
	    		 
	         //}
	      } finally {
	            try {
					listener.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      }
	}
	
	public void sendMessageToOtherPlayers(String message, SorryUser orig) {
		for (SorryUser u : players) {
			if (orig != u) u.sendMessage(message);
		}
	}
	
	public void sendToAllPlayers(String message) {
		for (SorryUser u : players) {
			u.sendMessage(message);
		}
	}
	
	public void sendToCurrPlayer(String message) {
		currPlayer.sendMessage(message);
	}
	
}

class SorryUser extends Thread {
	PrintWriter pw;
	BufferedReader br;
	Socket socket;
	SorryServer ss;
	String selectedColor = "";
	boolean startedGame = false;
	public SorryUser(Socket socket, SorryServer ss) throws IOException {
		this.ss = ss;
		this.socket = socket;
		pw = new PrintWriter(socket.getOutputStream());
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//System.out.println("new sorryUser created");
	}
	
	public void run() {
		while (true) {
			String command = null;
			try {
				command = br.readLine();
				//System.out.println("Server Received message: " + command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (command == null) {
				if (selectedColor.equals("")) {
					//if they have not yet chosen a color, give up their spot
					ss.players.remove(this);
				}
				else {
					//give up their color and spot
					//remove the color as well
					ss.players.remove(this);
					ss.allColors.remove(selectedColor);
					String message = "LEFT_GAME";
					if (ss.currPlayer == this) {
						message += " DURING TURN";
					}
					ss.sendToAllPlayers(message + " " + selectedColor);
					//remove from allColors and send taken colors to all players
				}
				return;
			}
			else if (command.startsWith("CHAT")) ss.sendToAllPlayers(command);
			else if (command.startsWith("SELECTED")) {
				ss.allColors.add(command.substring(9));
				ss.sendMessageToOtherPlayers(command, this);
				selectedColor = command.substring(9);
				if (ss.allColors.size() == ss.numPlayers && ss.numPlayers == ss.players.size()) {
					String message = "CREATE_GAME";
					for (int i = 0; i < ss.allColors.size(); i++) {
						message = message + " " + ss.allColors.get(i);
					}
					startedGame = true;
					ss.sendToAllPlayers(message);
					ss.sendToCurrPlayer("TAKE_TURN");
				}
			}
			else if (command.startsWith("NEED_TAKEN_COLORS")) {
				String takenColors = "TAKEN_COLORS";
				for (int i = 0; i < ss.allColors.size(); i++) {
					takenColors = takenColors + " " + ss.allColors.get(i);
				}
				sendMessage(takenColors);
			}
			else if (command.startsWith("NEED_ALL_PLAYERS_READY")) {
				if (ss.players.size() == ss.numPlayers) {
	    			 sendMessage("ALL_PLAYERS_READY " + ss.numPlayers);
	    		}
				else sendMessage("NOT_READY");
			}
			else if (command.startsWith("TURN_OVER")) {
				//System.out.println("sending message to take bots turns");
				//take any bots turns
				ss.sendToCurrPlayer("BOTS_TURNS");
			}
			else if (command.startsWith("BOTS_DONE")) {
				if (ss.currLoc < ss.players.size() - 1) {
					ss.currLoc++;
				}
				else ss.currLoc = 0;
				ss.currPlayer = ss.players.get(ss.currLoc);
				ss.sendToCurrPlayer("TAKE_TURN");
			}
			
			else if (command.startsWith("END_GAME")) {
				System.out.println("received end game message");
				ss.sendToAllPlayers(command);
			}
			
			else ss.sendMessageToOtherPlayers(command, this);
		}
		
	}
	
	public void sendMessage(String c) {
		pw.println(c);
		pw.flush();
	}
}
