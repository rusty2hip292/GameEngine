package eventlisteners;

import java.awt.event.KeyEvent;

import game.Application;

public class Keyboard implements java.awt.event.KeyListener {

	private Application a;
	
	public Keyboard(Application application) {
		this.a = application;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("typed " + e.getKeyChar());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("pressed " + e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("released " + e.getKeyChar());
	}
}
