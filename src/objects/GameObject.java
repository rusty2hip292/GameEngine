package objects;

import java.awt.Graphics;
import utils.*;

public abstract class GameObject {

	protected MyPoint pos, vel, acc;
	protected final Type type;
	
	public GameObject(double x, double y, double z, Type t) {
		pos = new MyPoint(x, y, z); vel = new MyPoint(0, 0, 0); acc = new MyPoint(0, 0, 0);
		type = t;
	}
	
	public void jerk(double x, double y, double z) {
		acc.add(x, y, z);
	}
	public void setAccel(double x, double y, double z) {
		acc.set(x, y, z);
	}
	public void setVel(double x, double y, double z) {
		vel.set(x, y, z);
	}
	private void accel() {
		vel.add(acc);
	}
	protected void move() {
		this.pos.add(vel);
		this.pos.add(acc.getX() / 2, acc.getY() / 2, acc.getZ() / 2);
		accel();
	}
	
	public MyPoint getPos() {
		return pos;
	}
	public MyPoint getVel() {
		return vel;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	protected double random(double min, double max) {
		return (Math.random() * (max - min) + min);
	}
	protected int random(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}
}
