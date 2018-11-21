package game;

import java.awt.Graphics;
import java.util.HashSet;

public class Handler {
	
	private HashSet<objects.GameObject> objects = new HashSet<objects.GameObject>();
	
	public void tick() {
		for(objects.GameObject o : objects) {
			o.tick();
		}
	}
	
	public void render(Graphics g) {
		for(objects.GameObject o : objects) {
			o.render(g);
		}
	}
	
	public void addObject(objects.GameObject o) {
		if(o == null) {
			return;
		}
		objects.add(o);
	}
	
	public void removeObject(objects.GameObject o) {
		if(o == null) {
			return;
		}
		objects.remove(o);
	}

}
