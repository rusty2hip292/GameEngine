package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Application extends Canvas implements Runnable {
	
	public static final int WIDTH = 640, HEIGHT = WIDTH * 3 / 4;
	public double fps = 60.0;
	private Thread thread;
	private boolean running = false;
	
	public static void main(String[] args) {
		new Application();
	}
	
	public Application() {
		new ux.Window(WIDTH, HEIGHT, "Game", this);
	}
	
	public synchronized void start() {
		if(!running) {
			thread = new Thread(this);
			thread.start();
			running = true;
		}
	}
	public synchronized void stop() {
		if(running) {
			try {
				thread.join();
				running = false;
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
		while(running) {
			
			while(System.nanoTime() - startNanos > nanosPerTick) {
				startNanos += nanosPerTick;
				tick();
			}
			
			if(System.currentTimeMillis() - time > (framesThisSecond + 1) * 1000.0 / fps) {
				framesThisSecond++;
				render();
			}
			if(System.currentTimeMillis() - time >= 1000) {
				time = System.currentTimeMillis();
				System.out.println("FPS: " + framesThisSecond);
				framesThisSecond = 0;
			}
		}
	}
	
	private void tick() {
		
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
	
}
