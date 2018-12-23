package systems;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class InputStreamMerger implements Runnable {

	private HashMap<InputStream, Scanner> streams = new HashMap<InputStream, Scanner>(10);
	private Queue<String> lines = new LinkedList<String>();
	public volatile boolean running = true;
	
	public InputStreamMerger() {
		Thread t = new Thread(this);
		t.setName(this.getClass().toString());
		t.start();
	}
	public InputStreamMerger(InputStream... iss) {
		this();
		for(InputStream is : iss) {
			if(is == null || streams.containsKey(is)) {
				continue;
			}else {
				streams.put(is, new Scanner(is));
			}
		}
	}
	
	public void run() {
		while(running) {
			for(InputStream is : streams.keySet()) {
				try {
					if(is.available() <= 0) {
						continue;
					}
					lines.add(streams.get(is).nextLine());
				}catch(Exception e) { }
			}
		}
	}
	
	public void addStringToStream(String s) {
		//java.lang.System.out.println("adding " + s);
		if(s == null) {
			return;
		}
		lines.add(s);
	}
	
	public boolean hasNextLine() {
		return this.lines.size() > 0;
	}
	public String nextLine() {
		if(lines.size() == 0) {
			return "";
		}
		//java.lang.System.out.println("polling " + lines.peek());
		return lines.poll();
	}
}
