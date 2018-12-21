package systems;

import java.util.Scanner;

public class Console extends System {

	private InputStreamMerger in = new InputStreamMerger(java.lang.System.in);
	
	public Console(game.Application owner) {
		super(owner);
	}
	
	public void run() {
		while(running) {
			if(this.mqueue.size() != 0) {
				handleMessage(mqueue.poll());
			}
			try {
				if(in.hasNextLine()) {
					String s = in.nextLine();
					println("read " + s);
					mb.addPriorityMessage(messages.Message.readMessage(s));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			if(this.mqueue.size() != 0) {
				mqueue.poll();
			}
		}
		in.running = false;
	}
	
	protected void handleMessage(messages.Message m) {
		java.lang.System.out.println(m);
		if(m instanceof messages.PipeToConsole) {
			java.lang.System.out.println("here");
			in.addStringToStream(((messages.PipeToConsole) m).getMsg().toString());
		}
	}
	
	protected Class<? extends messages.Message> messageTypeToHandle() {
		return messages.types.HandleByConsole.class;
	}
}
