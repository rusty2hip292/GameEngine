package gameobjects;

import java.awt.Color;
import java.awt.Graphics;

import position.E6POS;

public class Box extends GameObject {

	private function.Function f = function.Value.newValue(1);
	
	public Box() {
		super(50, 0, 0, 0, 0, 0);
	}
	
	public void render(Graphics g, E6POS p) {
		g.setColor(new Color(255, 255, 255));
		g.fillRect((int) p.x().evaluate(), (int) p.y().evaluate(), 10, 10);
		if(p.x().evaluate() < 50 || p.x().evaluate() > 590) {
			f = f.neg();
		}
		this.moveTo(this.pos.x().add(f).evaluate(), 0, 0, 0, 0, 0);
	}
}
