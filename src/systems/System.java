package systems;

import java.util.LinkedList;
import java.util.Queue;

public abstract class System implements Runnable {
	
	protected MessageBus mb;
	protected final Queue<messages.Message> mqueue = new LinkedList<messages.Message>();
	private Thread thread;
	protected volatile boolean running = true;
	
	public System(game.Application owner) {
		this.mb = MessageBus.bus(owner);
	}
	
	public void start() {
		java.lang.System.out.println("Starting " + this);
		running = true;
		mb.registerSystem(this);
		thread = new Thread(this);
		thread.setName(this.getClass().toString());
		thread.start();
	}
	
	public void stop() {
		java.lang.System.out.println("stopping " + this);
		this.running = false;
		java.lang.System.out.println(this.running);
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public void claimMessage(messages.Message m) {
		this.mqueue.add(m);
	}
	
	public void handleMessages() {
		long start = java.lang.System.currentTimeMillis();
		while(this.mqueue.size() > 0 && java.lang.System.currentTimeMillis() - start <= 15) {
			messages.Message m = mqueue.poll();
			m.log();
			handleMessage(m);
		}
	}
	
	protected abstract void handleMessage(messages.Message m);
	
	protected abstract Class<? extends messages.Message> messageTypeToHandle();
	
}
