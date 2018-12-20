package function;

public class DoubleValue extends Value {

	private final double value;
	
	public DoubleValue() {
		this(0);
	}
	public DoubleValue(double val) {
		this.value = val;
	}
	
	public double evaluate() {
		return value;
	}

}
