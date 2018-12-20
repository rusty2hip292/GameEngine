package gameobjects;

import java.awt.Graphics;

import position.E6POS;

public class Camera extends GameObject {

	public Camera() {
		super(0, 0, 0, 0, 0, 0);
		GameObject.activeCam = this;
	}
	
	public void render(Graphics g, E6POS p) { } // do not show camera
}
