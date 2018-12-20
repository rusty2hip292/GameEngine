package systems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MessageBus implements Runnable {

	private Queue<messages.Message> normal = new LinkedList<messages.Message>(), priority = new LinkedList<messages.Message>();
	
	private static MessageBus mb;
	private game.Application owner;
	HashMap<Class<?>, System> registeredSystems = new HashMap<Class<?>, System>();
	private volatile boolean running = false;
	private Thread thread;
	
	private MessageBus(game.Application owner) {
		this.owner = owner;
	}
	
	public void registerSystem(System s) {
		if(s == null) {
			return;
		}
		registerSystem(s.messageTypeToHandle(), s);
	}
	public void registerSystem(Class<?> c, System s) {
		if(s == null) {
			return;
		}
		java.lang.System.out.println("Registering " + s + " to handle " + c);
		registeredSystems.put(c, s);
	}
	
	public static MessageBus bus(game.Application owner) {
		if(owner == null && mb == null) {
			throw new IllegalArgumentException();
		}
		if(mb == null) {
			mb = new MessageBus(owner);
		}
		return mb;
	}
	
	public void run() {
		while(running) {
			while(!this.priority.isEmpty() || !this.normal.isEmpty()) {
				if(!this.priority.isEmpty()) {
					handleMessage(this.priority.poll());
				}else {
					handleMessage(this.normal.poll());
				}
			}
			try {
				Thread.sleep(10);
			}catch(Exception e) { }
		}
		java.lang.System.out.println("exited bus");
	}
	
	private System registerAllSuperClasses(Class<?> c) {
		java.lang.System.out.println(c);
		System s = this.registeredSystems.get(c);
		if(s == null) {
			if(c == Object.class) {
				return null;
			}else {
				s = registerAllSuperClasses(c.getSuperclass());
				if(s != null) {
					java.lang.System.out.println("about to register");
					java.lang.System.out.println(c);
					this.registerSystem(c, s);
				}
			}
		}
		return s;
	}
	
	private void handleMessage(messages.Message m) {
		java.lang.System.out.println("handling " + m);
		if(m instanceof messages.types.HandleByApplication) {
			owner.handleMessage(m);
		}else {
			Class<? extends Object> c = m.clazz;
			System s = this.registeredSystems.get(c);
			//java.lang.System.out.println(String.format("%s %s %s", m.getClass(), m.clazz, s));
			if(s == null) {
				s = registerAllSuperClasses(m.getClass());
				if(s == null) {
					Logger.log("Cannot find a system registered to handle " + m.getClass() + ": " + m, Logger.LogType.ERR_LOG);
					java.lang.System.out.println(this.registeredSystems);
				}else {
					s.claimMessage(m);
				}
			}else {
				s.claimMessage(m);
			}
		}
	}
	
	public void addMessage(messages.Message m) {
		addMessage(m, false);
	}
	public void addPriorityMessage(messages.Message m) {
		addMessage(m, true);
	}
	private void addMessage(messages.Message m, boolean highpriority) {
		if(m == null) {
			return;
		}
		if(highpriority) {
			this.priority.add(m);
		}else {
			this.normal.add(m);
		}
	}

	public void start() {
		this.running = true;
		thread = new Thread(this);
		thread.setName(this.getClass().toString());
		thread.start();
	}
	
	public void stopAllRegisteredSystems() {
		for(Class<?> type : this.registeredSystems.keySet()) {
			this.registeredSystems.get(type).stop();
		}
		this.running = false;
	}
}
