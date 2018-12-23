package ux;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Window extends com.jogamp.opengl.awt.GLCanvas {
	
	private static long serialVersionUID = 1;
	private JFrame frame;
	
	public Window(int width, int height, String title, game.Application game) {
		
		super(new com.jogamp.opengl.GLCapabilities(com.jogamp.opengl.GLProfile.get(com.jogamp.opengl.GLProfile.GL4)));
		
		this.addGLEventListener(new OpenGLEventListener());
		
		frame = new JFrame(title);
		eventlisteners.Window w = new eventlisteners.Window(this);
		frame.addWindowFocusListener(w);
		frame.addWindowListener(w);
		frame.addWindowStateListener(w);
		frame.addComponentListener(w);

		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		this.setSize(frame.size());
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}
	
	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	public void fillFrame() {
		this.setSize(frame.getSize());
		game.Application.WIDTH = this.getWidth(); game.Application.HEIGHT = this.getHeight();
	}

}
