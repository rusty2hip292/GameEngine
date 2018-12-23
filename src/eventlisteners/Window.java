package eventlisteners;

import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

public class Window implements java.awt.event.WindowFocusListener, java.awt.event.WindowListener, java.awt.event.WindowStateListener, java.awt.event.ComponentListener {

	private ux.Window w;
	
	public Window(ux.Window w) {
		this.w = w;
	}
	
	@Override
	public void windowStateChanged(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("state change");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("opened");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		systems.MessageBus.bus(null).addPriorityMessage(new messages.Exit());
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("closed");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("iconified");
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("deconified");
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("activated");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("deactivated");
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("focused");
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("out of focus");
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		System.out.println("resize");
		w.fillFrame();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
