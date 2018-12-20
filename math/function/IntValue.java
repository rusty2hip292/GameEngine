package function;

public class IntValue extends Value {

	private final int value;
	
	public IntValue() {
		this(0);
	}
	public IntValue(int val) {
		this.value = val;
	}
	
	public double evaluate() {
		return value;
	}
}
