package systems;

import java.util.Scanner;

public class Console extends System {

	private static Scanner in = new Scanner(java.lang.System.in);
	
	public Console(game.Application owner) {
		super(owner);
	}
	
	public void run() {
		while(running) {
			try {
				if(java.lang.System.in.available() != 0) {
					mb.addPriorityMessage(messages.Message.readMessage(in.nextLine()));
				}
			}catch(Exception e) { }
			if(this.mqueue.size() != 0) {
				mqueue.poll();
			}
		}
	}
	
	protected void handleMessage(messages.Message m) { }
	
	protected Class<? extends messages.Message> messageTypeToHandle() {
		return messages.types.Null.class;
	}
}
