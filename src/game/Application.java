package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import systems.Systems;

public class Application extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH * 3 / 4;
	public double fps = 60.0;
	private Thread thread;
	private volatile boolean running = false;
	private ux.Window window;
	
	public static void main(String[] args) {
		new Application();
	}
	
	public Application() {
		window = new ux.Window(WIDTH, HEIGHT, "Game", this);
	}
	
	public synchronized void start() {
		if(!running) {
			thread = new Thread(this);
			thread.setName(this.getClass().toString());
			thread.start();
		}
	}
	public synchronized void stop() {
		if(running) {
			try {
				Systems.stopAll();
				running = false;
				thread.join();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void run() {
		long time = System.currentTimeMillis();
		int framesThisSecond = 0;
		long startNanos = System.nanoTime();
		long nanosPerTick = 1000000000;
		systems.Systems.startAll(this);
		while(!running) {
			try {
				Thread.sleep(10);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		while(running) {
			
			while(System.nanoTime() - startNanos > nanosPerTick) {
				startNanos += nanosPerTick;
			}
			
			if(System.currentTimeMillis() - time > (framesThisSecond + 1) * 1000.0 / fps) {
				framesThisSecond++;
				render();
			}else {
				systems.Logger.forceLog();
			}
			if(System.currentTimeMillis() - time >= 1000) {
				time = System.currentTimeMillis();
				systems.Logger.log("FPS: " + framesThisSecond, systems.Logger.LogType.INFO_LOG);
				framesThisSecond = 0;
			}
		}
		System.out.println("done");
		systems.Logger.log("EXIT", systems.Logger.LogType.INFO_LOG);
		systems.Logger.forceLog();
		window.close();
	}
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.dispose();
		bs.show();
	}
	
	public void handleMessage(messages.Message m) {
		if(m instanceof messages.Start) {
			this.running = true;
			m.log();
			return;
		}
		if(m instanceof messages.Exit) {
			m.log();
			this.stop();
			return;
		}
		
		m.errlog();
	}
}
