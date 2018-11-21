package utils;

public class MyPoint {

	double x = 0, y = 0, z = 0;
	
	public MyPoint(double x, double y) {
		this(x, y, 0);
	}
	public MyPoint(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;
	}
	
	public MyPoint add(double xoff, double yoff) {
		return add(xoff, yoff, 0);
	}
	public MyPoint add(double xoff, double yoff, double zoff) {
		x += xoff; y += yoff; z += zoff;
		return this;
	}
	public MyPoint add(MyPoint p) {
		if(p == null) {
			return this;
		}
		return add(p.x, p.y, p.z);
	}
	public MyPoint sub(MyPoint p) {
		if(p == null) {
			return this;
		}
		return add(-p.x, -p.y, -p.z);
	}
	public MyPoint sub(double x, double y) {
		return sub(x, y, 0);
	}
	public MyPoint sub(double x, double y, double z) {
		return add(-x, -y, -z);
	}
	
	public MyPoint neg() {
		x = -x; y = -y; z = -z;
		return this;
	}
	
	public MyPoint set(double x, double y) {
		return set(x, y, 0);
	}
	public MyPoint set(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;
		return this;
	}
	
	public MyPoint copy() {
		return new MyPoint(x, y, z);
	}
	
	public MyPoint scale(double factor) {
		x *= factor;
		y *= factor;
		z *= factor;
		return this;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public MyPoint normalize() {
		return scale(1.0 / mag());
	}
	
	public double mag() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public String toString() {
		return String.format("<%.3f, %.3f, %.3f>", x, y, z);
	}
}
