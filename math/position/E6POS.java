package position;

import matrix.Matrix;
import vector.Vector;

import java.util.HashMap;

import function.*;

public class E6POS {

	public static final Matrix rotZ = new Matrix(3, 3, "a cos", "a sin -", "0", "a sin", "a cos", "0", "0", "0", "1"), rotY = new Matrix(3, 3, "b cos", "0", "b sin", "0", "1", "0", "b sin -", "0", "b cos"), rotX = new Matrix(3, 3, "1", "0", "0", "0", "c cos", "c sin -", "0", "c sin", "c cos");
	public static final Matrix rotZ2 = new Matrix(3, 3, "a2 cos", "a2 sin -", "0", "a2 sin", "a2 cos", "0", "0", "0", "1"), rotY2 = new Matrix(3, 3, "b2 cos", "0", "b2 sin", "0", "1", "0", "b2 sin -", "0", "b2 cos"), rotX2 = new Matrix(3, 3, "1", "0", "0", "0", "c2 cos", "c2 sin -", "0", "c2 sin", "c2 cos");
	public static final Matrix rotABC = rotX.invert().mult(rotY.invert()).mult(rotZ.invert());
	public static final Matrix rotABC2 = rotX2.invert().mult(rotY2.invert()).mult(rotZ2.invert()).mult(rotABC);
	public static final Matrix invertOri = rotZ.invert().mult(rotY.invert()).mult(rotX.invert());
	public static final Matrix identity = new Matrix(3, 3, 1, 0, 0, 0, 1, 0, 0, 0, 1);
	public static final Matrix planes = new Matrix(3, 3, 0, 1, 1, 1, 0, 1, 1, 1, 0);
	private static Function cos, sin;
	private static Matrix temp;
	private static HashMap<String, Double> theta = new HashMap<String, Double>();
	
	private vector.Vector pos;
	private Matrix ori = identity.copy();
	private double a, b, c;
	
	static {
		init();
	}
	
	private static E6POS invert(E6POS p) {
		Matrix invO = p.ori.invert().condense();
		Vector temp = p.ori.mult(p.pos.colVector()).colVector(0).neg();
		return new E6POS(temp, invO);
	}
	public E6POS invert() {
		return invert(this);
	}

	public E6POS(double x, double y, double z, double a, double b, double c) {
		pos = new vector.Vector(x, y, z);
		ori = transformAxisABC(ori, a, b, c);
		this.a = a; this.b = b; this.c = c;
	}
	
	public E6POS(Vector pos, Matrix ori) {
		this.pos = pos; this.ori = ori; this.recalcABC();
	}
	
	public String toString() {
		//System.out.println(pos);
		return pos.evalString() + new Vector(a, b, c).evalString();
	}
	
	private void recalcABC() {
		Matrix units = this.ori;
		//System.out.println(units);
		Vector y = units.rowVector(1), z = units.rowVector(2);
		double sign = y.get(2).div(z.get(2)).evaluate();
		Vector t = y.sub(z.mult(sign));
		double d = t.dot(y).div(t.mag()).evaluate();
		double C = Math.acos(d);
		if(sign < 0) {
			C *= -1;
		}
		if(degrees) {
			C *= 180 / Math.PI;
		}
		units = transformAxisABC(units, 0, 0, -C);
		Vector x = units.rowVector(0);
		double B = -Math.atan2(x.get(2).evaluate(), x.get(0).square().add(x.get(1).square()).sqrt().evaluate());
		double A = Math.atan2(x.get(1).evaluate(), x.get(0).evaluate());//x.get(0).square().sqrt().evaluate());
		if(degrees) {
			A *= 180 / Math.PI;
			B *= 180 / Math.PI;
		}
		this.a = A; this.b = B; this.c = C;
	}
	
	public static E6POS compose(E6POS a, E6POS b) {
		Matrix o = b.ori.mult(new Matrix(a.ori).condense()).condense();
		//Vector pos = a.pos.add(a.ori.colVector(0).mult(b.pos.get(0))).add(a.ori.colVector(1).mult(b.pos.get(1))).add(a.ori.colVector(2).mult(b.pos.get(2)));
		Function[] fs = new Function[3];
		for(int i = 0; i < 3; i++) {
			fs[i] = a.pos.get(i).add(b.pos.get(0).mult(a.ori.get(0, i))).add(b.pos.get(1).mult(a.ori.get(1, i))).add(b.pos.get(2).mult(a.ori.get(2, i)));
		}
		Vector pos = new Vector(fs);
		//System.out.println(a + " + " + b + "\n = " + new E6POS(pos, o));
		return new E6POS(pos, o);
	}
	
	public E6POS toGlobal(E6POS base) {
		return compose(base, this);
	}
	
	/*
	public E6POS invert() {
		Vector p = this.pos.clone().evaluate().neg();
		Matrix o = invertOri(identity, this.a, this.b, this.c);
		return new E6POS(this.rotation().multAndEval(p.colVector()).colVector(0), o);
	}
	*/
	
	public Function x() {
		return this.pos.get(0);
	}
	
	public Function y() {
		return this.pos.get(1);
	}
	public Function z() {
		return this.pos.get(2);
	}
	
	public static void init() {
		try {
			cos = Function.parseFunctionFromCleanRPNString("theta cos");
			sin = Function.parseFunctionFromCleanRPNString("theta sin");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean degrees = true;
	
	public static double degToRad(double deg) {
		return deg * Math.PI / 180;
	}
	
	public static void main(String[] args) {
		init();
		Matrix m = transformAxisABC_OLD(identity, 30.0, 40.0, -60.0);
		System.out.println(m);
		calcABC(m);
		Matrix m2 = transformAxisABC(identity, 30.0, 40.0, -60.0);
		System.out.println(m);
		calcABC(m2);
	}
	
	/**
	 * 
	 * @param initial composed of X, Y, and Z row vectors
	 * @param W rot around X
	 * @param P rot around Y
	 * @param R rot around Z
	 * @return composed of X, Y, and Z row vectors after rotations around Z, Y, and X
	 */
	public static Matrix transformAxisWPR(Matrix initial, double W, double P, double R) {
		return transformAxisABC(initial, R, P, W);
	}
	
	/**
	 * 
	 * @param initial composed of X, Y, and Z row vectors
	 * @param A rot around Z
	 * @param B rot around Y
	 * @param C rot around X
	 * @return composed of X, Y, and Z row vectors after rotations around Z, Y, and X
	 */
	public static Matrix transformAxisABC(Matrix initial, double A, double B, double C) {
		if(initial.rows != 3 && initial.cols != 3) {
			throw new IllegalArgumentException();
		}
		temp = new Matrix(initial);
		if(degrees) {
			A = degToRad(A);
			B = degToRad(B);
			C = degToRad(C);
		}
		rotABC.setParam("A", A);
		rotABC.setParam("B", B);
		rotABC.setParam("C", C);
		//System.out.println(rotABC.getParams());
		//System.out.println(rotABC.copy().condense());
		return rotABC.multAndEval(temp);
	}
	public static Matrix invertOri(Matrix initial, double A, double B, double C) {
		if(initial.rows != 3 && initial.cols != 3) {
			throw new IllegalArgumentException();
		}
		temp = new Matrix(initial);
		if(degrees) {
			A = degToRad(A);
			B = degToRad(B);
			C = degToRad(C);
		}
		invertOri.setParam("A", -A);
		invertOri.setParam("B", -B);
		invertOri.setParam("C", -C);
		//System.out.println(invertOri.copy().condense());
		return invertOri.multAndEval(temp);
	}
	public static Matrix transformAxisABC_OLD(Matrix initial, double A, double B, double C) {
		if(initial.rows != 3 && initial.cols != 3) {
			throw new IllegalArgumentException();
		}
		temp = new Matrix(initial);
		if(degrees) {
			A = degToRad(A);
			B = degToRad(B);
			C = degToRad(C);
		}
		rotABC.setParam("A", A);
		rotABC.setParam("B", B);
		rotABC.setParam("C", C);
		return transformC(transformB(transformA(initial, A), B), C);
	}
	
	private static Matrix transformA(Matrix init, double A) {
		if(A == 0) {
			return init;
		}
		theta.put("theta", A);
		Function x, y;
		for(int r = 0; r < 3; r++) {
			x = temp.get(0, r);
			y = temp.get(1, r);
			temp.setValue(0, r, x.mult(cos).add(y.mult(sin)).toValue(theta));
			temp.setValue(1, r, y.mult(cos).sub(x.mult(sin)).toValue(theta));
		}
		return temp;
	}
	private static Matrix transformB(Matrix init, double B) {
		if(B == 0) {
			return init;
		}
		theta.put("theta", B);
		Function x, z;
		for(int r = 0; r < 3; r++) {
			x = temp.get(0, r);
			z = temp.get(2, r);
			temp.setValue(2, r, z.mult(cos).add(x.mult(sin)).toValue(theta));
			temp.setValue(0, r, x.mult(cos).sub(z.mult(sin)).toValue(theta));
		}
		return temp;
	}
	private static Matrix transformC(Matrix init, double C) {
		if(C == 0) {
			return init;
		}
		theta.put("theta", C);
		Function z, y;
		for(int r = 0; r < 3; r++) {
			z = temp.get(2, r);
			y = temp.get(1, r);
			temp.setValue(1, r, y.mult(cos).add(z.mult(sin)).toValue(theta));
			temp.setValue(2, r, z.mult(cos).sub(y.mult(sin)).toValue(theta));
		}
		return temp;
	}
	
	private static Matrix showMult(Matrix m1, Matrix m2, boolean eval) {
		String[] s1 = m1.getLines(), s2 = m2.getLines();
		Matrix m3;
		if(eval) {
			m3 = m1.multAndEval(m2);
		}else {
			m3 = m1.mult(m2);
		}
		String[] s3 = m3.getLines();
		for(int i = 0; i < s1.length; i++) {
			System.out.println(s1[i] + s2[i] + s3[i]);
		}
		System.out.println();
		return m3;
	}
	
	private static void calcABC(Matrix units) {
		Vector y = units.rowVector(1), z = units.rowVector(2);
		double sign = y.get(2).div(z.get(2)).evaluate();
		Vector t = y.sub(z.mult(sign));
		double d = t.dot(y).div(t.mag()).evaluate();
		double C = Math.acos(d);
		if(sign < 0) {
			C *= -1;
		}
		if(degrees) {
			C *= 180 / Math.PI;
		}
		units = transformAxisABC(units, 0, 0, -C);
		Vector x = units.rowVector(0);
		double B = -Math.atan2(x.get(2).evaluate(), x.get(0).square().add(x.get(1).square()).sqrt().evaluate());
		double A = Math.atan2(x.get(1).evaluate(), x.get(0).square().sqrt().evaluate());
		if(degrees) {
			A *= 180 / Math.PI;
			B *= 180 / Math.PI;
		}
		System.out.println(String.format("%4.3f\n%4.3f\n%4.3f", A, B, C));
	}
	
	public Matrix ori() {
		return this.ori;
	}
	public Matrix rotation() {
		rotABC.setParam("A", this.a);
		rotABC.setParam("B", this.b);
		rotABC.setParam("C", this.c);
		return rotABC;
	}
}