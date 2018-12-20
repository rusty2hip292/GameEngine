package gameobjects;

import java.awt.Graphics;

import position.E6POS;

public class World extends GameObject {

	public World() {
		super(0, 0, 0, 0, 0, 0);
	}
	
	public void render(Graphics g, E6POS start) {
		System.out.println("world");
	}
}
