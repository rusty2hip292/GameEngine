package gameobjects;

import java.awt.Graphics;
import java.util.LinkedList;

import position.E6POS;

public abstract class GameObject {

	private GameObject parent = null;
	E6POS pos; // relative to the parent object
	protected static Camera activeCam = new Camera(); // just ensure not null
	
	private boolean key;
	private static boolean staticKey;
	
	public static LinkedList<GameObject> objects;
	
	private GameObject() {
		if(objects == null) {
			objects = new LinkedList<GameObject>();
		}
		System.out.println(objects);
		objects.add(this);
	}
	public GameObject(double x, double y, double z, double a, double b, double c) {
		this();
		moveTo(x, y, z, a, b, c);
	}
	
	public E6POS pos() {
		return pos;
	}
	
	public void moveTo(double x, double y, double z, double a, double b, double c) {
		this.pos = new E6POS(x, y, z, a, b, c);
	}
	
	private void render(Graphics g, Camera c) {
		render(g, relToCam(c));
	}
	
	public abstract void render(Graphics g, E6POS start);
	
	public E6POS relToCam(Camera c) {
		return E6POS.compose(c.pos.invert(), this.pos);
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
			go.render(g, activeCam);
		}
	}
}
