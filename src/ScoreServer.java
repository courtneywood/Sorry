import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreServer extends Thread{
	private ServerSocket listener;
	private PrintWriter pw;
	private ArrayList<String> scores;
	public ScoreServer() {
		try {
			listener = new ServerSocket(2002);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}
	
	public void run() {
		while (true) {
			try {
				Socket s = listener.accept();
				pw = new PrintWriter(s.getOutputStream());
				pw.println("HTTP/1.1 200 OK");
				pw.println("Content-Type: text/html");
				fillScores();
				pw.println("\r\n");
				String htmlString = "<html> <body> <p> Sorry! Top Scores List </p> ";
				htmlString += "<table border = \"1\"> <tr> <th> Name </th> <th> Score </th> </tr> ";
				for (int i = 0; i < scores.size(); i++) {
					Scanner scan = new Scanner(scores.get(i));
					String name = scan.next();
					int score = scan.nextInt();
					htmlString += "<tr> <td> " + name + " </td> <td> " + score + " </td> </tr> ";
				}
				pw.println(htmlString);
				pw.flush();
				pw.close();
				pw.flush();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void fillScores() {
		FileReader fr = null;
		try {
			fr = new FileReader("assets/topscores.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		scores = new ArrayList<>();
		try {
			String player = br.readLine();
			while (player != null) {
				scores.add(player);
				player = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Score[] scoreArray = new Score[scores.size()];
		for (int i = 0; i < scores.size(); i++) {
			Scanner sc = new Scanner(scores.get(i));
			String name = sc.next();
			int score = sc.nextInt();
			scoreArray[i] = new Score(name, score);
		}
		
		//sort
		int minScore = scoreArray[0].getScore();
		int minLoc = 0;
		for (int i = 0; i < scoreArray.length; i++) {
			minLoc = i;
			minScore = scoreArray[i].getScore();
			for (int j = i + 1; j < scoreArray.length; j++) {
				if (scoreArray[j].getScore() > minScore) {
					Score temp = scoreArray[minLoc];
					scoreArray[minLoc] = scoreArray[j];
					scoreArray[j] = temp;
					minLoc = i;
					minScore = scoreArray[j].getScore();
				}
			}
		}
	}
}
