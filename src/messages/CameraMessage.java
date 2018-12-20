package messages;

import java.util.Scanner;

public class CameraMessage extends messages.types.HandleByCamera {
	
	public final double x, y, z, a, b, c;
	protected static String type = "camera";

	public CameraMessage(double x, double y, double z, double a, double b, double c) {
		this.x = x; this.y = y; this.z = z; this.a = a; this.b = b; this.c = c;
	}
	public CameraMessage(Scanner s) throws Exception {
		this(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble());
	}
	
	public String toString() {
		return type + " " + x + " " + y + " " + z + " " + a + " " + b + " " + c;
	}
}