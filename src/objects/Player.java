package objects;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject {

	public Player(double x, double y, double z) {
		super(x, y, z, Type.PLAYER);
	}
	
	public void tick() {
	
	}
	
	public void render(Graphics g) {
		
		this.jerk(random(-0.50, 0.50), random(-0.50, 0.50), 0);
		this.acc.normalize();
		this.move();
		this.vel.normalize();
		
		if(g == null) {
			return;
		}
		
		g.setColor(Color.white);
		g.fillRect((int) pos.getX(), (int) pos.getY(), 10, 10);
	}
}
