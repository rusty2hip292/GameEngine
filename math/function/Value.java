package function;

import java.util.Map;

public abstract class Value extends Function {

	public abstract double evaluate();
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return evaluate();
	}
	
	public Function derivative(Map<String, Function> params) {
		return Value.newValue(0);
	}
	
	public static Value newValue(double val) {
		return new DoubleValue(val);
	}
	public static Value newValue(int val) {
		return new IntValue(val);
	}
	
	public Value derivative() {
		return newValue(0);
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(tabs + evaluate() + "\n");
	}
	
	public String toString() {
		return String.format("%4.3f", this.evaluate());
	}
}
