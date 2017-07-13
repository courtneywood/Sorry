

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class HelpMenu extends JFrame{
	public HelpMenu() {
		super("Help");
		setSize(350,400);
		setLocation(300,100);
		Menu m = new Menu();
		m.setVisible(true);
		add(new JScrollPane(m));
	}

}

class Menu extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/cards/card_brown.png");
	private Font f; 
	public Menu() {
		
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.BOLD, 16);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		setLayout(new GridLayout(7, 1));
		JLabel a = new JLabel("Starting the Game");
		a.setFont(f);
		add(a);
		JLabel b = new JLabel("     Press Start");
		b.setFont(f);
		add(b);
		JLabel c = new JLabel("     Select the Number of Players");
		c.setFont(f);
		add(c);
		JLabel d = new JLabel("     Select your Color");
		d.setFont(f);
		add(d);
		JLabel e = new JLabel("Playing the Game");
		e.setFont(f);
		add(e);
		JLabel g = new JLabel("     Draw a Card");
		g.setFont(f);
		add(g);
		JLabel h = new JLabel("     Select a square that is valid");
		h.setFont(f);
		add(h);
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		 g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
