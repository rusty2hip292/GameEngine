package eventlisteners;

import java.awt.event.KeyEvent;

import game.Application;

public class Keyboard implements java.awt.event.KeyListener {

	private Application a;
	private String console = "";
	private boolean inConsoleState = false;
	
	public Keyboard(Application application) {
		this.a = application;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		char c = e.getKeyChar();
		if(c == '\\' && !this.inConsoleState) {
			this.inConsoleState = true;
			return;
		}else if(c == '\n' && this.inConsoleState) {
			this.inConsoleState = false;
			systems.MessageBus.bus(null).addPriorityMessage(new messages.PipeToConsole(console));
			console = "";
			return;
		}
		if(this.inConsoleState) {
			console += c;
			return;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("pressed " + e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("released " + e.getKeyChar());
	}
}
