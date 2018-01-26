
package me.rayzr522.astra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import me.rayzr522.astra.data.Loader;
import me.rayzr522.astra.data.Saver;
import me.rayzr522.astra.data.Score;
import me.rayzr522.astra.entity.Box;
import me.rayzr522.astra.entity.Star;
import me.rayzr522.astra.sound.Song;
import me.rayzr522.astra.sound.Sounds;
import me.rayzr522.astra.ui.Button;
import me.rayzr522.astra.ui.ButtonLAF;
import me.rayzr522.astra.util.ThreadUtils;
import me.rayzr522.astra.util.ThreadedCode;

public class Board extends JPanel implements Runnable {

	private static final long	serialVersionUID	= -88667578292361415L;
	private GraphicsEnvironment	ge					= GraphicsEnvironment.getLocalGraphicsEnvironment();
	private GraphicsDevice		gd					= ge.getDefaultScreenDevice();
	private GraphicsDevice[]	gda					= ge.getScreenDevices();
	private DisplayMode			dm					= gd.getDisplayMode();

	private long				startTime;
	private long				levelStartTime;
	private long				ellapsedTime;
	private long				lastScoreUpdateTime;
	private long				pausedStartTime;
	private long				pausedTime;
	private long				lastFrameUpdate;

	private int					frames;
	private int					fps;
	private int					width;
	private int					height;
	private int					sidebarWidth;
	private int					tileSize;
	private int					textSize;
	private int					level;
	private static int			score;
	private int					pointsToAdd;
	private int					timeBonus;
	private int					numberOfClicks;
	private int					clickBonus;
	private int					msgDisplayTime;

	private double				posx;
	private double				posy;
	private double				mousex;
	private double				mousey;
	private double				movex;
	private double				movey;
	private double				speedMod;
	private double				imageWidth;
	private double				imageHeight;

	private int					numStars;

	private Image				backgroundImage;
	private BufferedImage		background;
	private Image				ballImage;
	private Star[]				stars;
	private String[]			msgs;

	private boolean				running				= false;
	private boolean				alreadyUnpaused		= false;
	private boolean				gameStarted			= false;
	private boolean				oldPausedValue		= false;
	private boolean				fullscreen			= false;
	private boolean				displayFps			= false;
	public static boolean		paused				= false;
	private static String		dataDirectory		= constructPath(".astra", true);
	private static String		saveFile			= constructPath(dataDirectory + "/save.data");
	private static Song currentSong			= Song.RIVER;

	private ButtonLAF laf;
	private Button				saveQuit;
	private Button				resetScores;
	private Button				musicSelector;

	public void updateSize(double widthChange, double heightChange) {

		Dimension size = Start.f.getSize();
		width = (int) size.getWidth();
		height = (int) size.getHeight();
		sidebarWidth = (int) Math.floor(width / 4);
		textSize = (int) Math.floor(height * 0.026041667);
		speedMod = (int) Math.floor((96 * (width - sidebarWidth)) / (width - sidebarWidth));
		background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y <= height / tileSize; y++) {

			for (int x = 0; x <= width / tileSize; x++) {

				background.getGraphics().drawImage(backgroundImage, x * tileSize, y * tileSize, null);

			}

		}

		speedMod = (int) Math.floor((96 * (width - sidebarWidth)) / (width - sidebarWidth));

		for (Star star : stars) {

			star.setX(star.getX() * widthChange);
			star.setY(star.getY() * heightChange);

		}

		mousex *= widthChange;
		mousey *= heightChange;
		posx *= widthChange;
		posy *= heightChange;
		Start.f.setLocationRelativeTo(null);

		laf.setFont(new Font("Monospace", Font.PLAIN, (int) Math.floor(height * 0.026041667)));
		saveQuit.setX((sidebarWidth - (int) Math.floor(sidebarWidth * 0.8)) / 2);
		saveQuit.setY((int) Math.floor(height * 0.234375));
		saveQuit.setWidth((int) Math.floor(sidebarWidth * 0.8));
		saveQuit.setHeight((int) Math.floor(height * 0.052083333));
		resetScores.setX((sidebarWidth - (int) Math.floor(sidebarWidth * 0.8)) / 2);
		resetScores.setY((int) Math.floor(height * 0.3125));
		resetScores.setWidth((int) Math.floor(sidebarWidth * 0.8));
		resetScores.setHeight((int) Math.floor(height * 0.052083333));
		musicSelector.setX((sidebarWidth - (int) Math.floor(sidebarWidth * 0.8)) / 2);
		musicSelector.setY((int) Math.floor(height * 0.390625));
		musicSelector.setWidth((int) Math.floor(sidebarWidth * 0.8));
		musicSelector.setHeight((int) Math.floor(height * 0.052083333));

	}

	public Board(int width, int height) {

		File datadir = new File(dataDirectory);
		if (!datadir.exists()) {
			System.out.println("Making directory at '" + datadir + "'");
			datadir.mkdir();
		}

		msgDisplayTime = 5000;
		msgs = new String[10];

		for (int i = 0; i < msgs.length; i++)
			msgs[i] = "";

		this.width = width;
		this.height = height;
		sidebarWidth = (int) Math.floor(width / 4);
		textSize = (int) Math.floor(height * 0.026041667);
		backgroundImage = loadImage("/textures/air.png");
		ballImage = loadImage("/textures/ball2.png");
		imageWidth = ballImage.getWidth(null);
		imageHeight = ballImage.getHeight(null);
		tileSize = backgroundImage.getWidth(null);
		background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y <= height / tileSize; y++) {

			for (int x = 0; x <= width / tileSize; x++) {

				background.getGraphics().drawImage(backgroundImage, x * tileSize, y * tileSize, null);

			}

		}

		posx = sidebarWidth + 20;
		posy = 20f;
		mousex = ((width + sidebarWidth) / 2) - (imageWidth / 2);
		mousey = (height / 2) - (imageHeight / 2);
		movex = 0f;
		movey = 0f;
		speedMod = (int) Math.floor((96 * (width - sidebarWidth)) / (width - sidebarWidth));

		lastFrameUpdate = System.nanoTime();

		ActionListener quit = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				saveQuitDialog();

			}

		};

		ActionListener reset = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				resetDialog();

			}

		};

		ActionListener music = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				musicDialog();

			}

		};

		laf = new ButtonLAF(new Color(50, 160, 200), new Color(60, 20, 200), new Color(0, 100, 200), ButtonLAF.BORDER_ROUND, 2, new Font("Monospace", Font.PLAIN, 20));
		saveQuit = new Button("Save and Quit", laf, (sidebarWidth - (int) Math.floor(sidebarWidth * 0.8)) / 2, 180, (int) Math.floor(sidebarWidth * 0.8), 40, quit);
		resetScores = new Button("Reset High Scores", laf, (sidebarWidth - (int) Math.floor(sidebarWidth * 0.8)) / 2, 240, (int) Math.floor(sidebarWidth * 0.8), 40, reset);
		musicSelector = new Button("Change Music", laf, (sidebarWidth - (int) Math.floor(sidebarWidth * 0.8)) / 2, 300, (int) Math.floor(sidebarWidth * 0.8), 40, music);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

				if (!paused) {
					if (saveQuit.checkBounds(e.getX(), e.getY())) {
						saveQuit.execute();
						return;
					}
					if (resetScores.checkBounds(e.getX(), e.getY())) {
						resetScores.execute();
						return;
					}
					if (musicSelector.checkBounds(e.getX(), e.getY())) {
						musicSelector.execute();
						return;
					}
					if (e.getX() > sidebarWidth) {
						numberOfClicks++;
						mousex = e.getX() - (imageWidth / 2);
						mousey = e.getY() - (imageHeight / 2);
					} else {
						numberOfClicks++;
						mousex = sidebarWidth - (imageWidth / 2);
						mousey = e.getY() - (imageHeight / 2);
					}

				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});

		// Key code
		Start.f.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

				int key = e.getKeyCode();
				if (key == KeyEvent.VK_SPACE) {

					if (paused) paused = false;
					else if (!paused) paused = true;

				}
				if (key == KeyEvent.VK_F) {

					double oldWidth = Start.f.getSize().getWidth();
					double oldHeight = Start.f.getSize().getHeight();

					fullscreen = !fullscreen;

					if (fullscreen) {

						Start.f.setSize(dm.getWidth(), dm.getHeight());
						gd.setFullScreenWindow(Start.f);

					} else if (!fullscreen) {

						gd.setFullScreenWindow(null);
						Start.f.setSize(Start.WIDTH, Start.HEIGHT);

					}

					Dimension size = Start.f.getSize();
					double widthChange = size.getWidth() / oldWidth;
					double heightChange = size.getHeight() / oldHeight;
					updateSize(widthChange, heightChange);

				}

				if (key == KeyEvent.VK_ESCAPE) {
					gd.setFullScreenWindow(null);
					saveQuitDialog();

				}

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

		});

		Start.f.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {

				System.out.println("Closing...");
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}

		});

	}

	private void saveQuitDialog() {

		String name = JOptionPane.showInputDialog(Start.f, "Please enter your name to record your score", "Joe Shmoe");
		if (name != null) {
			if (save(name)) JOptionPane.showMessageDialog(Start.f, "New highscore!");
			Start.f.dispose();
			System.exit(0);
		}

	}

	private void resetDialog() {

		int reset = JOptionPane.showConfirmDialog(Start.f, "Are you sure you want to reset?", "Reset High Scores", JOptionPane.YES_NO_CANCEL_OPTION);

		if (reset == 0) Saver.reset(saveFile);

	}

	private void musicDialog() {

		paused = true;
		final JFrame musicSelector = new JFrame("Select music");

		musicSelector.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {

			}

			@Override
			public void windowIconified(WindowEvent arg0) {

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {

			}

			@Override
			public void windowClosing(WindowEvent arg0) {

				paused = false;
				musicSelector.dispose();

			}

			@Override
			public void windowClosed(WindowEvent arg0) {

			}

			@Override
			public void windowActivated(WindowEvent arg0) {

			}

		});

		musicSelector.setSize(480, 480);
		musicSelector.setResizable(false);
		musicSelector.setLocationRelativeTo(null);
		musicSelector.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JPanel musicSelectorPanel = new JPanel(new SpringLayout());
		musicSelectorPanel.setLayout(new BoxLayout(musicSelectorPanel, BoxLayout.Y_AXIS));
		musicSelectorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20), "Music Selector", TitledBorder.CENTER, TitledBorder.TOP, new Font("Monospace", Font.PLAIN, 20), Color.black));

		String[] options = new String[] {
			"River", "Lazy", "Funk"
		};
		final JComboBox musicList = new JComboBox(options);
		musicList.setFont(new Font("Monospace", Font.PLAIN, 15));
		musicList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Select a Song", TitledBorder.CENTER, TitledBorder.TOP, new Font("Monospace", Font.PLAIN, 15), Color.black));

		JButton setMusic = new JButton("Set Music");
		setMusic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String selection = (String) musicList.getSelectedItem();

				currentSong = Song.valueOf(selection.toUpperCase());
				musicSelector.dispose();
				paused = false;

			}

		});

		musicSelectorPanel.add(musicList);
		musicSelectorPanel.add(setMusic);
		musicSelector.add(musicSelectorPanel);
		musicSelector.setVisible(true);

	}

	public void start() {

		if (running) return;
		Thread thread = new Thread(this);
		thread.start();
		running = true;

	}

	public static Image loadImage(String location) {

		try {
			return ImageIO.read(Board.class.getResource(location));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getFullLocation(String location) {

		String url = Board.class.getResource(location).getPath().replace("%20", "").replace("file:", "").replace("jar:", "");

		return url;

	}

	public static URI getURI(String location) {

		try {
			URI uri = new URI(Board.class.getResource(location).getPath().replace("%20", ""));
			return uri;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String constructPath(String string, boolean inHome) {

		String home = System.getProperty("user.home");
		boolean pathLargerThanHome = home.length() > string.length();
		boolean alreadyInHome = false;
		if (!pathLargerThanHome) alreadyInHome = string.substring(0, home.length()) != home;

		if (inHome && !alreadyInHome) return System.getProperty("user.home") + File.separator + string.replace("/", File.separator);
		else return string.replace("/", File.separator);

	}

	public static String constructPath(String string) {

		return string.replace("/", File.separator);

	}

	public void startLevel() {

		levelStartTime = System.currentTimeMillis();
		if (gameStarted) {

			pausedTime = 0;
			lastScoreUpdateTime = levelStartTime;
			timeBonus = (int) Math.floor(level / 7);
			if (timeBonus < 1) timeBonus = 1;
			timeBonus = (timeBonus * 4) - (int) (Math.floor(ellapsedTime / 5000));
			clickBonus = numStars - numberOfClicks;
			if (timeBonus < 0) timeBonus = 0;
			if (clickBonus < 0) clickBonus = 0;
			msgs[0] = "Time Bonus: +" + timeBonus;
			msgs[1] = "Click Bonus: +" + clickBonus;
			pointsToAdd += timeBonus + clickBonus;
			numberOfClicks = 0;

		}

		gameStarted = true;
		startTime = System.currentTimeMillis();
		level++;
		numStars = (int) Math.floor(level / 2);
		if (numStars == 0) numStars = 1;
		stars = new Star[numStars];
		int value = (int) Math.floor(level / 4);
		if (value < 1) value = 1;

		for (int i = 0; i < numStars; i++) {

			stars[i] = new Star(value, 15, 15, width, height, sidebarWidth);

		}

	}

	public boolean isLevelFinished() {

		if (gameStarted) {

			for (Star star : stars) {

				if (star.isAlive()) return false;

			}

		}

		return true;

	}

	public void checkCollected() {

		for (Star star : stars) {

			if (star.isAlive()) {

				boolean collect = false;
				Box ballBox = new Box(posx, posy, ballImage.getWidth(null), ballImage.getHeight(null));
				Box starBox = new Box(star.getX(), star.getY(), star.getWidth(), star.getHeight());
				collect = ballBox.checkBounds(starBox);

				if (collect == true) {
					star.setAlive(false);
					pointsToAdd += star.getValue();
					Sounds.beep.play();
				}

			}

		}

	}

	public void pauseHandling() {

		if (!oldPausedValue && paused) {
			pausedStartTime = System.currentTimeMillis();
		}

		if (paused) {
			currentSong.song.stop();
			alreadyUnpaused = false;
		}

		if (!paused) {
			if (!alreadyUnpaused) currentSong.song.loop();
			pausedStartTime = 0;
			alreadyUnpaused = true;
		}

		oldPausedValue = paused;
	}

	@Override
	public void run() {

		while (running) {
			update();
		}

	}

	public void update() {

		pauseHandling();

		if (paused && pausedStartTime != 0) pausedTime = System.currentTimeMillis() - pausedStartTime;

		if (gameStarted) {

			ellapsedTime = System.currentTimeMillis() - startTime;
			ellapsedTime -= pausedTime;

		}

		if (isLevelFinished() && !paused) {
			startLevel();
		}

		if (!paused) {
			new ThreadedCode() {

				@Override
				public void run() {

					movex = (mousex - posx) / speedMod;
					movey = (mousey - posy) / speedMod;
					posx += movex;
					posy += movey;
					checkCollected();
				}

			};

			if (pointsToAdd > 0) {
				new ThreadedCode() {

					@Override
					public void run() {

						if (System.currentTimeMillis() - lastScoreUpdateTime - pausedTime >= 50) {
							lastScoreUpdateTime = System.currentTimeMillis();
							score++;
							pointsToAdd--;
						}

					}

				};

			}

		}

		new ThreadedCode() {

			@Override
			public void run() {

				repaint();
			}
		};

		ThreadUtils.sleep(1);

	}

	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if (!paused) {
			g2d.drawImage(background, 0, 0, null);
			try {
				for (Star star : stars) {
					try {
						if (star.isAlive()) {
							g2d.drawImage(star.getImage(), (int) star.getX(), (int) star.getY(), star.getWidth(), star.getHeight(), null);
						}
					} catch (Exception e1) {
					}
				}
			} catch (Exception e) {
			}
			g2d.drawImage(ballImage, (int) Math.floor(posx), (int) Math.floor(posy), null);
			g2d.setColor(Color.black);
			g2d.fillRect(0, 0, sidebarWidth, height);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Menlo", Font.PLAIN, textSize));
			g2d.drawString("SCORE: " + score, textSize, textSize);
			g2d.drawString("LEVEL: " + level, textSize, textSize + (int) (textSize * 1.5));
			if (displayFps) g2d.drawString("FPS: " + fps, textSize, textSize + (int) (textSize * 1.5) * 2);
			if (System.currentTimeMillis() - levelStartTime - pausedTime <= msgDisplayTime && level > 1) {
				for (int i = 0; i < msgs.length; i++) {
					g2d.drawString(msgs[i], textSize, (int) (i * (textSize * 1.5)) + (int) (((textSize * 1.5) * ((displayFps) ? 3 : 2)) + textSize));
				}
			}
			if (System.currentTimeMillis() - levelStartTime - pausedTime <= msgDisplayTime) {
				int gameWidth = width - sidebarWidth;
				Color color = new Color(255, 255, 255, 170);
				g2d.setColor(color);
				g2d.setFont(new Font("Menlo", Font.PLAIN, textSize * 2));
				String text = "LEVEL " + level;
				g2d.drawString(text, sidebarWidth + ((gameWidth / 2) - ((text.length() / 2) * textSize * 2)), textSize * 2);
			}
			saveQuit.draw(g2d);
			resetScores.draw(g2d);
			musicSelector.draw(g2d);
		} else {

			g2d.setColor(Color.DARK_GRAY);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Menlo", Font.PLAIN, width / 8));
			g2d.drawString("PAUSED", (width / 2) - (width / 5), (height / 2));

			g2d.setColor(Color.white);
			g2d.setFont(new Font("Menlo", Font.PLAIN, textSize));

			Score[] scores = Loader.load(saveFile);
			for (int i = 0; i < scores.length; i++) {

				int x = 10;
				int y = 30 + (i * textSize);
				g2d.drawString(i + 1 + ". " + scores[i].name + " - " + scores[i].score, x, y);

			}

		}
		frames++;
		if (System.nanoTime() - lastFrameUpdate >= 1000000000) {
			lastFrameUpdate = System.nanoTime();
			fps = frames;
			frames = 0;
		}

	}

	public static boolean save(String name) {

		boolean bump = false;
		boolean looped = false;
		boolean highScore = false;

		Score newScore = new Score(name, score);
		Score[] scores = Loader.load(saveFile);
		Score[] newScores = new Score[scores.length + 1];

		if (newScore.score != 0) {
			for (int i = 0; i < scores.length; i++) {

				looped = true;
				Score tempScore = scores[i];
				if (tempScore.score <= newScore.score && !bump) {
					newScores[i] = newScore;
					bump = true;
					if (i == 0) highScore = true;
				}
				if (!bump) newScores[i] = scores[i];
				else if (bump) newScores[i + 1] = scores[i];

			}

			if (!looped) {

				newScores[0] = newScore;
				highScore = true;

			}

			if (!bump) {

				newScores[newScores.length - 1] = newScore;

			}
			Saver.write(newScores, saveFile, 10);
		} else {
			Saver.write(scores, saveFile, 10);
		}

		return highScore;

	}
}