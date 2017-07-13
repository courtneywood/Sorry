

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class ScoreTable extends JFrame {
	private DefaultTableModel tableModel;
	private String fileName = "assets/topscores.txt";
	private Object[][] data;
	public ScoreTable() {
		super("Score Table");
		JPanel scorePanel = new JPanel();
		String[] columnNames = {"Name", "Score"};
		tableModel = new DefaultTableModel(data, columnNames);
		fillData();
		JTable scores = new JTable(tableModel);
		add(new JScrollPane(scores));
		pack();
		setSize(400,400);
		setLocation(300,100);
	}
	
	public void addRow(String name, int score) {
		tableModel.addRow(new Object[]{name, score});
		
		
		//add it to text file
		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(name + " " + score);
			pw.flush();
			pw.close();
			fw.close();
		}
		catch (IOException ioe) {
			System.out.println("Error: " + ioe.getMessage());
		}
		
		sortData();
	}
	
	private void fillData() {
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> scores = new ArrayList<>();
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
		data = new Object[scores.size()][2];
		
		for (int i = 0; i < scores.size(); i++) {
			Scanner sc = new Scanner(scores.get(i));
			String name = sc.next();
			int score = sc.nextInt();
			tableModel.addRow(new Object[]{name, score});
		}
		
	}
	
	public void sortData() {
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> scores = new ArrayList<>();
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
		
		
		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);
			for (int i = 0; i < scoreArray.length; i++) {
				pw.println(scoreArray[i].getName() + " " + scoreArray[i].getScore());
			}
			pw.flush();
			pw.close();
			fw.close();
		}
		catch (IOException ioe) {
			System.out.println("Error: " + ioe.getMessage());
		}
	}
}

class Score {
	private int score;
	private String name;
	public Score(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getName() {
		return name;
	}
}
