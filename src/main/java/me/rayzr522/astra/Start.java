
package me.rayzr522.astra;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.Random;

public class Start {

	public static final int		WIDTH	= 1024;
	public static final int		HEIGHT	= 768;
	public static final String	TITLE	= "Astra";
	public static final String	VERSION	= "0.0.1";
	public static JFrame		f		= new JFrame(TITLE + " " + VERSION);

	public Start() {

		Image cursorImage = Board.loadImage("/textures/cursor.png");
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "astra-cursor" + new Random().nextInt(1000000));
		// f.setCursor(cursor);

		Image dockIconImage = Board.loadImage("/textures/icon.png");
		setDockIconImage(dockIconImage);
		f.setIconImage(dockIconImage);

		final Board board = new Board(WIDTH, HEIGHT);

		f.add(board);
		f.pack();

		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		f.setSize(WIDTH, HEIGHT);

		f.setResizable(false);

		f.setBackground(Color.black);

		f.setLocationRelativeTo(null);

		// f.setUndecorated(true);
		f.setVisible(true);

		board.start();

	}

	public static void main(String[] args) {

		Start start = new Start();

	}

	public static boolean setDockIconImage(Image image) {

		try {

			Class util = Class.forName("com.apple.eawt.Application");
			Method getApplication = util.getMethod("getApplication", new Class[0]);
			Object application = getApplication.invoke(util, new Object[0]);
			Class[] params = new Class[1];
			params[0] = Image.class;
			Method setDockIconImage = util.getMethod("setDockIconImage", params);
			setDockIconImage.invoke(application, new Object[] {
				image
			});

			return true;

		} catch (ClassNotFoundException ignore) {} catch (Exception e) {

			System.err.println("Couldn't set dock icon image!");

			e.printStackTrace();

		}

		return false;

	}

}