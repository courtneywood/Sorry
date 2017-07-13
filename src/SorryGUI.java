

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
public class SorryGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	JPanel outerPanel;
	JButton cardButton;
	int numPlayers;
	Board b;
	String color;
	Game g;
	Deck d;
	ScoreTable scoreTable;
	P3 p3;
	P4 p4;
	ScoreServer scoreServer;
	
	private JMenuBar menuBar;
	private JMenu help;
	private JMenu scores;
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/panels/grey_panel.png");
	private Font ttfReal;
	P4 boardPanel;
	
	public SorryGUI() {
		super("Sorry!");
		initializeComponents();
		createGUI();
	}
	
	public void initializeComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		outerPanel = new JPanel(new CardLayout());
		
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      ttfReal = ttfBase.deriveFont(Font.BOLD, 24);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		scoreTable = new ScoreTable();
		
	}
	
	public void createGUI() {
		menuBar = new JMenuBar();
		help = new JMenu("Help") {
			private KeyStroke accelerator;

            @Override
            public KeyStroke getAccelerator() {
                return accelerator;
            }

            @Override
            public void setAccelerator(KeyStroke keyStroke) {
                KeyStroke oldAccelerator = accelerator;
                this.accelerator = keyStroke;
                repaint();
                revalidate();
                firePropertyChange("accelerator", oldAccelerator, accelerator);
            }
		};
		scores = new JMenu("Scores") {
			private KeyStroke accelerator;

            @Override
            public KeyStroke getAccelerator() {
                return accelerator;
            }

            @Override
            public void setAccelerator(KeyStroke keyStroke) {
                KeyStroke oldAccelerator = accelerator;
                this.accelerator = keyStroke;
                repaint();
                revalidate();
                firePropertyChange("accelerator", oldAccelerator, accelerator);
            }
		};
		
		//setSize(640,480);
		setSize(640, 550);
		setLocation(400,200);
		P1 one = new P1(outerPanel, this);
		one.repaint();
		outerPanel.add(one);
		add(outerPanel);
		setVisible(true);
		menuBar.add(help);
		menuBar.add(scores);
		setJMenuBar(menuBar);

		help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		help.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				// TODO Auto-generated method stub
				HelpMenu hm = new HelpMenu();
				hm.setVisible(true);
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}	
		});
		scores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		scores.addMenuListener(new MenuListener() {

			@Override
			public void menuSelected(MenuEvent e) {
				// TODO Auto-generated method stub
				scoreTable.setVisible(true);
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	public void paintComponent(Graphics g) {
        outerPanel.paint(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public static void main(String[] args) {
		SorryGUI sorryGame = new SorryGUI();
		sorryGame.setVisible(true);
	}
}

class P1 extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/panels/grey_panel.png");
	private JPanel outer;
	private SorryGUI sg;
	private Font ttfReal;
	ScoreServer scoreServer;
	public P1(JPanel outerPanel, SorryGUI sg) {
		outer = outerPanel;
		this.sg = sg;
		sg.scoreServer = scoreServer;
		ImageIcon sorryLogo = new ImageIcon("assets/images/sorry.png");
		JLabel titleLabel = new JLabel();
		titleLabel.setIcon(sorryLogo);
		JButton joinButton = new JButton("Join");
		Image startImage = null;
		try {
			startImage = ImageIO.read(new FileInputStream("assets/images/buttons/grey_button00.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		joinButton.setIcon(new BackgroundIcon(startImage));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = 1;
		add(titleLabel,gbc);
		gbc.gridy = 2;
		add(new JLabel(" "),gbc);
		gbc.gridy = 3;
		add(new JLabel(" "),gbc);
		gbc.gridy = 4;
		add(new JLabel(" "),gbc);
		gbc.gridy = 5;
		JPanel buttonPanel = new JPanel();
		JButton hostButton = new JButton("Host");
		hostButton.setIcon(new BackgroundIcon(startImage));
		buttonPanel.add(hostButton);
		buttonPanel.add(new JLabel("      "));
		buttonPanel.add(joinButton);
		add(buttonPanel,gbc);
		
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      ttfReal = ttfBase.deriveFont(Font.BOLD, 24);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		Cursor c;
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image image = toolkit.getImage("assets/images/cursors/cursorHand_grey.png");
	    Point hotspot = new Point(0,0);
	    c = toolkit.createCustomCursor(image, hotspot, "Hand");
	    setCursor(c);
		
	    hostButton.setFont(ttfReal);
	    hostButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
				setVisible(false);
				HostPanel hp = new HostPanel(outer, sg);
				outer.add(hp);
				hp.setVisible(true);
				hostButton.setVisible(false);
				scoreServer = new ScoreServer();
			}
	    });
		joinButton.setFont(ttfReal);
		joinButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				//show joinPanel
				JoinPanel jp = new JoinPanel(outer, sg);
				outer.add(jp);
				jp.setVisible(true);
				joinButton.setVisible(false);
			}
		});
		setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
}

class HostPanel extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/panels/grey_panel.png");
	private JPanel outer;
	private SorryGUI sg;
	private Font ttfReal;
	public HostPanel(JPanel outerPanel, SorryGUI sg) {
		outer = outerPanel;
		this.sg = sg;
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      ttfReal = ttfBase.deriveFont(Font.BOLD, 24);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		Cursor c;
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image image = toolkit.getImage("assets/images/cursors/cursorHand_grey.png");
	    Point hotspot = new Point(0,0);
	    c = toolkit.createCustomCursor(image, hotspot, "Hand");
	    setCursor(c);
	    
		setLayout(new GridLayout(2, 1));
		JPanel topPanel = new JPanel(new GridLayout(3, 1));
		JPanel middle = new JPanel();
		JLabel portLabel = new JLabel("PORT:");
		portLabel.setFont(ttfReal);
		JTextField portField = new JTextField();
		portField.setFont(ttfReal);
		portField.setColumns(10);
		portField.setBackground(Color.GRAY);
		middle.add(portLabel);
		middle.add(portField);
		topPanel.add(new JLabel(""));
		topPanel.add(middle);
		topPanel.add(new JLabel(""));
		add(topPanel);
		JPanel bottomPanel = new JPanel();
		JButton startButton = new JButton("Start");
		startButton.setFont(ttfReal);
		bottomPanel.add(startButton);
		add(bottomPanel);
		middle.setOpaque(false);
		topPanel.setOpaque(false);
		bottomPanel.setOpaque(false);
		setOpaque(false);
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//want to create a p2 instead
				if (!portField.getText().equals("")) {
					try
				    {
				      int port = Integer.parseInt(portField.getText());
				      P2 two = new P2(outer, sg, port);
				      setVisible(false);
				      outer.add(two);
				      two.setVisible(true);
				    }
				    catch (NumberFormatException nfe)
				    {
				      System.out.println("Please enter a valid port.");
				    }
				}

			}
			
		});
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}

class JoinPanel extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/panels/grey_panel.png");
	private JPanel outer;
	private SorryGUI sg;
	private Font ttfReal;
	public JoinPanel(JPanel outerPanel, SorryGUI sg) {
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      ttfReal = ttfBase.deriveFont(Font.BOLD, 24);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		Cursor c;
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image image = toolkit.getImage("assets/images/cursors/cursorHand_grey.png");
	    Point hotspot = new Point(0,0);
	    c = toolkit.createCustomCursor(image, hotspot, "Hand");
	    setCursor(c);
		outer = outerPanel;
		this.sg = sg;
		setLayout(new GridLayout(2, 1));
		JPanel topPanel = new JPanel(new GridLayout(2, 1));
		topPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
		JPanel ipPanel = new JPanel();
		JPanel portPanel = new JPanel();
		JLabel ipLabel = new JLabel("IP:");
		ipLabel.setFont(ttfReal);
		JTextField ipField = new JTextField();
		ipField.setColumns(10);
		ipField.setFont(ttfReal);
		ipField.setBackground(Color.GRAY);
		//ipField.setText("localhost");
		ipPanel.add(ipLabel);
		ipPanel.add(ipField);
		JLabel portLabel = new JLabel("PORT:");
		portLabel.setFont(ttfReal);
		JTextField portField = new JTextField();
		portField.setFont(ttfReal);
		portField.setBackground(Color.GRAY);
		portField.setColumns(10);
		//portField.setText("6789");
		portPanel.add(portLabel);
		portPanel.add(portField);
		topPanel.add(ipPanel);
		topPanel.add(portPanel);
		add(topPanel);
		JPanel bottomPanel = new JPanel();
		JButton connectButton = new JButton("Connect");
		connectButton.setFont(ttfReal);
		bottomPanel.add(connectButton);
		add(bottomPanel);
		bottomPanel.setOpaque(false);
		topPanel.setOpaque(false);
		portPanel.setOpaque(false);
		ipPanel.setOpaque(false);
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!ipField.getText().equals("") && !portField.getText().equals("")) {
					try
				    {
						int port = Integer.parseInt(portField.getText());
						String ipAddress = ipField.getText();
						//create sorry client
						try {
							SorryClient sc = new SorryClient(ipAddress, port, sg);
							//System.out.println("canjoin: " + sc.canJoin);
							//if (sc.canJoin) {
								P3 three = new P3(outer, sg, sc);
								setVisible(false);
								outer.add(three);
								three.setVisible(true);
							//}
						} catch (UnknownHostException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				      
				    }
				    catch (NumberFormatException nfe)
				    {
				      System.out.println("Please enter a valid port.");
				    }
				}
			}
		});
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}

class P2 extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/panels/grey_panel.png");
	private JPanel outer;
	private int numPlayers;
	private Font f;
	private SorryGUI sg;
	public P2(JPanel outerPanel, SorryGUI sg, int port) {
		outer = outerPanel;
		this.sg = sg;
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.BOLD, 24);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		Cursor c;
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image image = toolkit.getImage("assets/images/cursors/cursorHand_grey.png");
	    Point hotspot = new Point(0,0);
	    c = toolkit.createCustomCursor(image, hotspot, "Hand");
	    setCursor(c);
		
		setLayout(new GridLayout(3, 1));
		JLabel playerSelectLabel = new JLabel("Select the number of players: ", SwingConstants.CENTER);
		playerSelectLabel.setFont(f);
		add(playerSelectLabel);
		ButtonGroup playerButtons = new ButtonGroup();
		JPanel buttonPanel = new JPanel(new GridLayout(1,4));
		buttonPanel.add(new JLabel(""));
		JRadioButton pl2 = new JRadioButton("  2");
		ImageIcon radioImage = new ImageIcon("assets/images/checkboxes/grey_circle.png");
		pl2.setIcon(new RadioIcon(radioImage.getImage()));
		pl2.setFont(f);
		buttonPanel.add(pl2);
		playerButtons.add(pl2);
		JRadioButton pl3 = new JRadioButton("  3");
		pl3.setFont(f);
		pl3.setIcon(new RadioIcon(radioImage.getImage()));
		buttonPanel.add(pl3);
		playerButtons.add(pl3);
		JRadioButton pl4 = new JRadioButton("  4");
		pl4.setFont(f);
		pl4.setIcon(new RadioIcon(radioImage.getImage()));
		buttonPanel.add(pl4);
		playerButtons.add(pl4);
		buttonPanel.setOpaque(false);
		add(buttonPanel);
		JButton confirm1 = new JButton("Confirm");
		confirm1.setEnabled(false);
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.BOLD, 20);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		confirm1.setFont(f);
		Image confirmImage = null;
		try {
			confirmImage = ImageIO.read(new FileInputStream("assets/images/buttons/grey_button00.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		confirm1.setIcon(new BackgroundIcon(confirmImage));
		JPanel confirmPanel = new JPanel();
		for (int i = 0; i < 35; i++) {
			confirmPanel.add(new JLabel(" "));
		}
		confirmPanel.add(confirm1);
		confirmPanel.setOpaque(false);
		add(confirmPanel);
		confirm1.setEnabled(false);
		
		confirm1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//create server and client
				try {
					SorryServer ss = new SorryServer(port, numPlayers);
					SorryClient sc = new SorryClient("localhost", port, sg);
					sc.setHost(true);
					setVisible(false);
					P3 three = new P3(outer, sg, sc);
					outer.add(three);
					three.setVisible(true);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		ImageIcon selectedImage = new ImageIcon("assets/images/checkboxes/grey_boxTick.png");
		
		pl2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm1.setEnabled(true);
				numPlayers = 2;
				pl2.setIcon(new RadioIcon(selectedImage.getImage()));
				pl3.setIcon(new RadioIcon(radioImage.getImage()));
				pl4.setIcon(new RadioIcon(radioImage.getImage()));
			}
		});
		
		pl3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm1.setEnabled(true);
				numPlayers = 3;
				pl3.setIcon(new RadioIcon(selectedImage.getImage()));
				pl2.setIcon(new RadioIcon(radioImage.getImage()));
				pl4.setIcon(new RadioIcon(radioImage.getImage()));
			}
		});
		
		pl4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm1.setEnabled(true);
				numPlayers = 4;
				pl4.setIcon(new RadioIcon(selectedImage.getImage()));
				pl2.setIcon(new RadioIcon(radioImage.getImage()));
				pl3.setIcon(new RadioIcon(radioImage.getImage()));
			}
		});
		setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}

class RadioIcon extends ImageIcon {
	private Image image;
	public RadioIcon(Image img) {
		image = img;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(image, 0, (int) (c.getHeight() /2.18), c.getWidth() / 10, c.getHeight() / 10, c);
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


class P3 extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/panels/grey_panel.png");
	private Font f;
	Timer timer;
	JPanel outer;
	String color = "";
	SorryGUI sg;
	SorryClient sc;
	JButton red, yellow, green, blue;
	JLabel timerLabel;
	int countdown = 30;
	public P3(JPanel outer, SorryGUI sg, SorryClient sc) {
		this.outer = outer;
		this.sg = sg;
		this.sc = sc;
		sg.p3 = this;
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.BOLD, 24);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		
		Cursor c;
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image image = toolkit.getImage("assets/images/cursors/cursorHand_grey.png");
	    Point hotspot = new Point(0,0);
	    c = toolkit.createCustomCursor(image, hotspot, "Hand");
	    setCursor(c);
	    
		setLayout(new GridLayout(4, 1));
		
		timer = new Timer(3000, (ActionListener) new TimerListener());
		timer.setDelay(1000);
		timer.setInitialDelay(1000);
		timer.start();
		timerLabel = new JLabel("Time: 30", SwingConstants.CENTER);
		timerLabel.setFont(f);
		add(timerLabel);
		
		JLabel colorSelectLabel = new JLabel("Select your color: ", SwingConstants.CENTER);
		colorSelectLabel.setFont(f);
		add(colorSelectLabel);
		
		JPanel colorPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		colorPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));	
		
		Image imageForRed = null;
		Image imageForBlue = null;
		Image imageForGreen = null;
		Image imageForYellow = null;
		try {
			imageForRed = ImageIO.read(new FileInputStream("assets/images/buttons/red_button00.png"));
			imageForBlue = ImageIO.read(new FileInputStream("assets/images/buttons/blue_button00.png"));
			imageForGreen = ImageIO.read(new FileInputStream("assets/images/buttons/green_button00.png"));
			imageForYellow = ImageIO.read(new FileInputStream("assets/images/buttons/yellow_button00.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		red = new JButton();
		red.setIcon(new BackgroundIcon(imageForRed));
		red.setFont(f);
		red.setText("Red");
		colorPanel.add(red);
		blue = new JButton();
		blue.setIcon(new BackgroundIcon(imageForBlue));
		blue.setFont(f);
		blue.setText("Blue");
		colorPanel.add(blue);
		green = new JButton();
		green.setIcon(new BackgroundIcon(imageForGreen));
		green.setFont(f);
		green.setText("Green");
		colorPanel.add(green);
		yellow = new JButton();
		yellow.setIcon(new BackgroundIcon(imageForYellow));
		yellow.setFont(f);
		yellow.setText("Yellow");
		colorPanel.add(yellow);
		colorPanel.setOpaque(false);
		add(colorPanel);
		sc.out.println("NEED_TAKEN_COLORS");
		sc.out.flush();
		
		JPanel confirmPanel = new JPanel();
		confirmPanel.setBorder(BorderFactory.createEmptyBorder(30, 350, 0, 30));
		JButton confirm2 = new JButton("Confirm");
		Image confirmImage = null;
		try {
			confirmImage = ImageIO.read(new FileInputStream("assets/images/buttons/grey_button00.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		confirm2.setIcon(new BackgroundIcon(confirmImage));
		confirm2.setEnabled(false);
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.BOLD, 20);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		confirm2.setFont(f);
		confirmPanel.add(confirm2);
		confirmPanel.setOpaque(false);
		add(confirmPanel);
		
		red.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm2.setEnabled(true);
				color = "red";
				Image b1 = null;
				Image g1 = null;
				Image y1 = null;
				Image r2 = null;
				try {
					b1 = ImageIO.read(new FileInputStream("assets/images/buttons/blue_button00.png"));
					g1 = ImageIO.read(new FileInputStream("assets/images/buttons/green_button00.png"));
					y1 = ImageIO.read(new FileInputStream("assets/images/buttons/yellow_button00.png"));
					r2 = ImageIO.read(new FileInputStream("assets/images/buttons/red_button01.png"));
					red.setIcon(new BackgroundIcon(r2));
					blue.setIcon(new BackgroundIcon(b1));
					green.setIcon(new BackgroundIcon(g1));
					yellow.setIcon(new BackgroundIcon(y1));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm2.setEnabled(true);
				color = "blue";
				Image b2 = null;
				Image r1 = null;
				Image y1 = null;
				Image g1 = null;
				try {
					b2 = ImageIO.read(new FileInputStream("assets/images/buttons/blue_button01.png"));
					r1 = ImageIO.read(new FileInputStream("assets/images/buttons/red_button00.png"));
					y1 = ImageIO.read(new FileInputStream("assets/images/buttons/yellow_button00.png"));
					g1 = ImageIO.read(new FileInputStream("assets/images/buttons/green_button00.png"));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				blue.setIcon(new BackgroundIcon(b2));
				red.setIcon(new BackgroundIcon(r1));
				green.setIcon(new BackgroundIcon(g1));
				yellow.setIcon(new BackgroundIcon(y1));
			}
		});
		
		green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm2.setEnabled(true);
				color = "green";
				Image g2 = null;
				Image r1 = null;
				Image b1 = null;
				Image y1 = null;
				try {
					g2 = ImageIO.read(new FileInputStream("assets/images/buttons/green_button01.png"));
					r1 = ImageIO.read(new FileInputStream("assets/images/buttons/red_button00.png"));
					b1 = ImageIO.read(new FileInputStream("assets/images/buttons/blue_button00.png"));
					y1 = ImageIO.read(new FileInputStream("assets/images/buttons/yellow_button00.png"));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				green.setIcon(new BackgroundIcon(g2));
				red.setIcon(new BackgroundIcon(r1));
				blue.setIcon(new BackgroundIcon(b1));
				yellow.setIcon(new BackgroundIcon(y1));
			}
		});
		
		yellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm2.setEnabled(true);
				color = "yellow";
				Image y2 = null;
				Image r1 = null;
				Image b1 = null;
				Image g1 = null;
				try {
					y2 = ImageIO.read(new FileInputStream("assets/images/buttons/yellow_button01.png"));
					r1 = ImageIO.read(new FileInputStream("assets/images/buttons/red_button00.png"));
					b1 = ImageIO.read(new FileInputStream("assets/images/buttons/blue_button00.png"));
					g1 = ImageIO.read(new FileInputStream("assets/images/buttons/green_button00.png"));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				yellow.setIcon(new BackgroundIcon(y2));
				red.setIcon(new BackgroundIcon(r1));
				blue.setIcon(new BackgroundIcon(b1));
				green.setIcon(new BackgroundIcon(g1));
			}
		});
		
		confirm2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				setVisible(false);
				P4 four = new P4(outer, color, sg, sc);
				//P4 four = new P4(outer, countdown, color, sg, sc);
				outer.add(four);
				four.setVisible(true);
				sc.out.println("SELECTED_" + color);
				sc.out.flush();
			}
		});
		
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	public void removefromGame() {
		System.exit(0);
	}
	
	class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			countdown--;
			timerLabel.setText("Time: " + countdown);
	        if (countdown == 0) {
	        	//timer.removeActionListener(this);
		        timer.stop();
		        removefromGame();
	        }
		}
		
	}

}

class BackgroundIcon implements Icon {
	private Image image;
	public BackgroundIcon(Image img) {
		image = img;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(image, 0, 0, c.getWidth(), c.getHeight(), c);
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

class P4 extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/images/panels/grey_panel.png");
	private Font f;
	private Font timerFont;
	SorryGUI sg;
	JPanel outer;
	String color;
	Board b;
	Deck d;
	Game g;
	JButton cardButton;
	SorryClient sc;
	JLabel[] rows;
	String timerText = "Time: 15";
	JPanel boardPanel;
	ArrayList<String> colors;
	Timer timer;
	int countdown = 15;
	public P4(JPanel outer, String color, SorryGUI sg, SorryClient sc) {
		this.outer = outer;
		this.color = color;
		this.sg = sg;
		this.sc = sc;
		sg.p4 = this;
		sg.boardPanel = this;
		setLayout(new BorderLayout());
		boardPanel = new JPanel(new GridLayout(16, 16));
		boardPanel.setOpaque(false);
		b = new Board();
		d = new Deck();
		JLabel board[][] = b.getBoard();
		cardButton = new JButton();
		sg.cardButton = cardButton;
		cardButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("assets/images/cards/cardBack_red.png")));
		cardButton.setEnabled(false);
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (i == 7 && j == 8) {
					boardPanel.add(cardButton);
				}
				else {
					board[i][j].setOpaque(false);
					boardPanel.add(board[i][j]);
				}
			}
		}
		add(boardPanel, BorderLayout.CENTER);
		JPanel chatPanel = new JPanel(new GridLayout(5, 1));
		add(chatPanel, BorderLayout.SOUTH);
		try {
		      InputStream in = new BufferedInputStream(new FileInputStream("assets/fonts/kenvector_future_thin.ttf"));
		      Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		      f = ttfBase.deriveFont(Font.PLAIN, 16);
		      timerFont = ttfBase.deriveFont(Font.PLAIN, 30);
		
		} catch (FontFormatException e) {
		        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		}
		rows = new JLabel[4];
		for (int i = 0; i < 4; i++) {
			rows[i] = new JLabel();
			rows[i].setBackground(Color.BLACK);
			rows[i].setOpaque(true);
			rows[i].setText("");
			rows[i].setForeground(Color.WHITE);
			rows[i].setFont(f);
			chatPanel.add(rows[i]);
		}
		JPanel typePanel = new JPanel(new BorderLayout());
		JTextField typeMessage = new JTextField();
		typeMessage.setBackground(Color.BLACK);
		typeMessage.setForeground(Color.WHITE);
		typeMessage.setOpaque(true);
		typeMessage.setEditable(true);
		typeMessage.setFont(f);
		typePanel.add(typeMessage,BorderLayout.CENTER);
		JButton sendButton = new JButton("Send");
		typePanel.add(sendButton, BorderLayout.EAST);
		chatPanel.add(typePanel);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMessage(typeMessage.getText());
				typeMessage.setText("");
			}
			
		});
		typeMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMessage(typeMessage.getText());
				typeMessage.setText("");
			}
		});
		Cursor c;
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image image = toolkit.getImage("assets/images/cursors/cursorHand_grey.png");
	    Point hotspot = new Point(0,0);
	    c = toolkit.createCustomCursor(image, hotspot, "Hand");
	    setCursor(c);
		
		cardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String card = d.getNextCard();
				g.showUsersCard(card);
				//g.userTurn(card);
				SoundPlayer sp = new SoundPlayer("draw_card");
				sp.play();
			}
		});
		
		sg.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				Cursor c;
			    Toolkit toolkit = Toolkit.getDefaultToolkit();
			    Image image = toolkit.getImage("assets/images/cursors/cursorHand_grey.png");
			    Point hotspot = new Point(0,0);
			    c = toolkit.createCustomCursor(image, hotspot, "Hand");
			    setCursor(c);
			}
			
		});
		
	}
	
	public void startTimer() {
		timer = new Timer(3000, (ActionListener) new TimerListener());
		timer.setInitialDelay(1000);
		timerText = "Time: 15";
		countdown = 15;
		repaint();
		validate();
		timer.setDelay(1000);
		timer.start();
	}
	
	public void stopTimer() {
		//System.out.println("stopping timer");
		timer.stop();
		timerText = "Time: 15";
		repaint();
		validate();
	}
	
	public void paintComponent(Graphics g) {
		ImageIcon sorry = new ImageIcon("assets/images/sorry.png");
		g.drawImage(sorry.getImage(), getWidth() / 4, getHeight() / 6, getWidth() / 2, getHeight() / 7, boardPanel);
		g.setFont(timerFont);
		g.drawString(timerText, (int) (getWidth() / 2.5), getHeight() - (getHeight() / 2));
		//g.setFont(f);
		//g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public void createGame(ArrayList<String> colors) {
		b.setUpBoard(sc.getNumPlayers(), color);
		g = new Game(sc.getNumPlayers(), color, b, d, sg, sc, colors);
	}
	

	public void sendChatMessage(String message) {
		sc.out.println("CHAT " + color + " " + message);
		sc.out.flush();
	}
	
	class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			countdown--;
			timerText = "Time: " + countdown;
	        if (countdown == 0) {
	        	timer.stop();
	    		timerText = "Time: 15";
	        	sc.out.println("TURN_OVER");
				sg.cardButton.setEnabled(false);
	        }
	        repaint();
	        validate();
		}
		
	}
}

