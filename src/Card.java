

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class Card extends JFrame {
	public Card(int cardValue, String card, Game g) {
		super("Card");
		setSize(250,400);
		setLocation(300,100);
		NewCard nc = new NewCard(cardValue, card, g, this);
		add(nc);
	}
}

class NewCard extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/cards/card_beigeLight.png");
	private Font f1;
	private Font f2;
	private Game g;
	public NewCard(int cardValue, String card, Game g, Card c) {
		this.g = g;
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f1 = ttfBase.deriveFont(Font.BOLD, 16);
		      f2 = ttfBase.deriveFont(Font.BOLD, 24);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		setLayout(new GridLayout(3, 1));
		String val = "" + cardValue;
		if (val.equals("0")) val = "Sorry!";
		JLabel value = new JLabel(val, SwingConstants.CENTER);
		value.setFont(f2);
		add(value);
		JLabel cardText = new JLabel("<html><center>" + card + "</center></html>");
		cardText.setFont(f1);
		add(cardText);
		JButton okButton = new JButton("OK");
		okButton.setFont(f2);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		add(buttonPanel);
		buttonPanel.setOpaque(false);
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				c.dispose();
				g.userTurn(card);
			}
			
		});
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		 g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
