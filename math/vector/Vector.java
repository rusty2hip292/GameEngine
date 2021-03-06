package vector;

import java.util.HashMap;

import function.Function;
import function.Value;

public class Vector {

	private final Function[] vect;
	private HashMap<String, Double> params = new HashMap<String, Double>();
	
	public Vector(Function... componants) {
		this.vect = componants;
	}
	public Vector(int... componants) {
		vect = new Function[componants.length];
		for(int i = 0; i < componants.length; i++) {
			vect[i] = Value.newValue(componants[i]);
		}
	}
	public Vector(double... componants) {
		vect = new Function[componants.length];
		for(int i = 0; i < componants.length; i++) {
			vect[i] = Value.newValue(componants[i]);
		}
	}
	
	public Vector neg() {
		Vector t = this.clone();
		for(int i = 0; i < t.size(); i++) {
			t.vect[i] = t.vect[i].neg();
		}
		return t;
	}
	
	public Function dot(Vector o) {
		if(o.size() != this.size()) {
			throw new IndexOutOfBoundsException();
		}
		Function dot = this.get(0).mult(o.get(0));
		for(int i = 1; i < this.size(); i++) {
			dot = dot.add(this.get(i).mult(o.get(i)));
		}
		return dot;
	}
	
	public Function mag() {
		return this.dot(this).sqrt();
	}
	
	public Function get(int index) {
		return this.vect[index];
	}
	
	public int size() {
		return this.vect.length;
	}
	
	public Vector mult(Function f) {
		Function[] fs = new Function[this.size()];
		for(int i = 0; i < this.size(); i++) {
			fs[i] = this.get(i).mult(f);
		}
		return new Vector(fs);
	}
	public Vector mult(double d) {
		return mult(Value.newValue(d));
	}
	
	public Vector add(Vector v) {
		if(v.size() > this.size()) {
			return v.add(this);
		}
		Vector t = this.clone();
		for(int i = 0; i < v.size(); i++) {
			t.vect[i] = t.vect[i].add(v.vect[i]);
		}
		return t;
	}
	
	public Vector sub(Vector v) {
		if(v.size() > this.size()) {
			return v.sub(this);
		}
		Vector t = this.clone();
		for(int i = 0; i < v.size(); i++) {
			t.vect[i] = t.vect[i].sub(v.vect[i]);
		}
		return t;
	}
	
	public Vector innerProduct(Vector v) {
		if(v.size() > this.size()) {
			return v.innerProduct(this);
		}
		Vector t = this.clone();
		for(int i = 0; i < v.size(); i++) {
			t.vect[i] = t.vect[i].mult(v.vect[i]);
		}
		return t;
	}
	
	public Vector clone() {
		Function[] fs = new Function[this.vect.length];
		for(int i = 0; i < fs.length; i++) {
			fs[i] = this.vect[i];
		}
		return new Vector(fs);
	}
	
	public String toString() {
		return toString(this.vect);
	}
	
	public String evalString() {
		String[] s = new String[this.size()];
		for(int i = 0; i < this.size(); i++) {
			s[i] = "" + this.vect[i].evaluate(params);
		}
		return toString(s);
	}
	
	public String toString(Object[] o) {
		String s = "<" + o[0];
		for(int i = 1; i < o.length; i++) {
			s += ", " + o[i];
		}
		return s + ">";
	}
	
	public matrix.Matrix colVector() {
		matrix.Matrix m = new matrix.Matrix(this.size(), 1);
		for(int i = 0; i < this.size(); i++) {
			m.setValue(i, 0, vect[i]);
		}
		return m;
	}
	public matrix.Matrix rowVector() {
		matrix.Matrix m = new matrix.Matrix(1, this.size());
		for(int i = 0; i < this.size(); i++) {
			m.setValue(0, i, vect[i]);
		}
		return m;
	}
	
	public void setParams(HashMap<String, Double> params) {
		this.params = params;
	}
	
	public Vector evaluate() {
		for(int i = 0; i < this.size(); i++) {
			vect[i] = vect[i].toValue(params);
		}
		return this;
	}
}