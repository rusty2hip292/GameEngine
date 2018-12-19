package ux;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Window extends Canvas {
	
	private static long serialVersionUID = 1;
	private JFrame frame;
	
	public Window(int width, int height, String title, game.Application game) {
		
		frame = new JFrame(title);

		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}
	
	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

}
