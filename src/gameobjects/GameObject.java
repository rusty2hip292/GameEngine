package gameobjects;

import java.awt.Graphics;
import java.util.LinkedList;

import position.E6POS;

public abstract class GameObject {

	private GameObject parent = null;
	E6POS pos; // relative to the parent object
	
	private boolean key;
	private static boolean staticKey;
	
	public static LinkedList<GameObject> objects = new LinkedList<GameObject>();
	
	private GameObject() {
		objects.add(this);
	}
	public GameObject(double x, double y, double z, double a, double b, double c) {
		this();
		moveTo(x, y, z, a, b, c);
	}
	
	public void moveTo(double x, double y, double z, double a, double b, double c) {
		this.pos = new E6POS(x, y, z, a, b, c);
	}
	
	public void render(Graphics g) {
		
	}
	
	public static void recalcPositions() {
		staticKey = !staticKey;
		for(GameObject go : objects) {
			go.recalcPosotion(staticKey);
		}
	}
	private E6POS recalcPosotion(boolean key) {
		if(this.key == key || this.parent == null) {
			return this.pos;
		}
		this.key = key;
		this.pos = this.pos.toGlobal(this.parent.pos);
		return this.pos;
	}
	
	public static void renderObjects(Graphics g) {
		recalcPositions();
		for(GameObject go : objects) {
			go.render(g);
		}
	}
}
