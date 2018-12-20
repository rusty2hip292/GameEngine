package function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class Function {

	public static final String tab = "  ";
	
	public Value toValue(Map<String, Double> params) {
		return Value.newValue(this.evaluate(params));
	}
	
	public Function sqrt() {
		return new Exponent(this, Value.newValue(0.5));
	}
	public Function square() {
		return this.mult(this);
	}
	
	private static String clean(String s) {
		for(char c : "\t\r\n".toCharArray()) {
			s = s.replace(c, ' ');
		}
		String s2 = s.replaceAll("  ", " ");
		if(s.length() == s2.length()) {
			if(s2.length() > 0 && s2.charAt(0) == ' ') {
				return s2.substring(1);
			}
			return s2;
		}
		return clean(s2);
	}
	
	public double evaluate() {
		return evaluate(new HashMap<String, Double>());
	}
	public double evaluate(Map<String, Double> params) {
		if(params == null) {
			throw new IllegalArgumentException();
		}
		Map<String, Double> cleaned = new HashMap<String, Double>();
		for(String k : params.keySet()) {
			cleaned.put(k.toLowerCase(), params.get(k));
		}
		return evaluate(cleaned, true);
	}
	protected abstract double evaluate(Map<String, Double> params, boolean cleaned);
	
	public abstract void parseTree(StringBuffer sb, String tabs);
	public String parseTree() {
		StringBuffer sb = new StringBuffer();
		parseTree(sb, "");
		return sb.toString();
	}
	
	/*
	public static Function parseFunction(String function) throws Exception {
		function = function.toLowerCase();
		System.out.println(function);
		return null;
	}
	*/
	
	public static Function parseFunctionFromCleanRPNString(String function) throws Exception {
		function = clean(function).toLowerCase();
		
		if(function.length() == 0) {
			return Value.newValue(0);
		}
		
		Stack<Function> parts = new Stack<Function>();
		
		String[] tokens = function.split(" ");
		
		for(String tok : tokens) {
			try {
				int i = Integer.parseInt(tok);
				parts.push(Value.newValue(i));
				continue;
			}catch(Exception e) {
				try {
					double d = Double.parseDouble(tok);
					parts.push(Value.newValue(d));
					continue;
				}catch(Exception e2) { }
			}
			for(String s : "+ - * / ^ sin cos tan csc sec cot ln log log10".split(" ")) {
				if(tok.equals(s)) {
					if(parts.size() < 1) {
						throw new Exception("No functions to perform an operation on: " + tok);
					}
				}
			}
			Function f1 = null, f2 = null;
			if(parts.size() > 0) {
				f2 = parts.pop();
				while(parts.size() > 0 && parts.peek() == null) {
					parts.pop();
				}
			}
			if(parts.size() >= 1) {
				f1 = parts.pop();
			}
			if(tok.equals("+")) {
				parts.push(new Add(f1, f2));
			}else if(tok.equals("-")) {
				if(f1 != null) {
					parts.push(new Sub(f1, f2));
				}else {
					parts.push(new Neg(f2));
				}
			}else if(tok.equals("*")) {
				parts.push(new Mult(f1, f2));
			}else if(tok.equals("/")) {
				parts.push(new Div(f1, f2));
			}else if(tok.equals("^")) {
				parts.push(new Exponent(f1, f2));
			}else if(tok.equals("log10")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new Log(Value.newValue(10), f2));
			}else if(tok.equals("log")) {
				parts.push(new Log(f1, f2));
			}else if(tok.equals("ln")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new NaturalLog(f2));
			}else if(tok.equals("sin")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new Sin(f2));
			}else if(tok.equals("cos")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new Cos(f2));
			}else if(tok.equals("tan")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new Tan(f2));
			}else if(tok.equals("csc")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new Csc(f2));
			}else if(tok.equals("sec")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new Sec(f2));
			}else if(tok.equals("cot")) {
				if(f1 != null) { parts.push(f1); }
				parts.push(new Cot(f2));
			}else {
				if(f1 != null) { parts.push(f1); }
				if(f2 != null) { parts.push(f2); }
				parts.push(new Var(tok));
			}
		}
		
		if(parts.size() != 1) {
			throw new Exception("Could not parse " + function + "\n" + parts);
		}
		
		return parts.pop();
	}
	
	public static void main(String[] args) {
		ArrayList<Function> functions = new ArrayList<Function>();
		
		for(String s : new String[] {
				"10 7 + 6 * 8 - 12 / sin",
				"x sec tan ln",
				"6 7 + x y / -"
		}) {
			try {
				functions.add(Function.parseFunctionFromCleanRPNString(s));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		HashMap<String, Double> vars = new HashMap<String, Double>();
		vars.put("X", 8.45);
		vars.put("y", -12.0);
		for(Function f : functions) {
			System.out.println(f);
			System.out.println(f.evaluate(vars));
			System.out.println(f.derivative(new HashMap<String, Function>()));
			System.out.println(f.derivative(new HashMap<String, Function>()).evaluate(vars));
		}
	}
	
	public Function mult(Function f) {
		return new Mult(this, f);
	}
	public Function mult(double d) {
		return mult(Value.newValue(d));
	}
	public Function mult(int i) {
		return mult(Value.newValue(i));
	}
	public Function div(Function f) {
		return new Div(this, f);
	}
	public Function div(double d) {
		return div(Value.newValue(d));
	}
	public Function div(int i) {
		return div(Value.newValue(i));
	}
	public Function add(Function f) {
		return new Add(this, f);
	}
	public Function add(double d) {
		return add(Value.newValue(d));
	}
	public Function add(int i) {
		return add(Value.newValue(i));
	}
	public Function sub(Function f) {
		return new Sub(this, f);
	}
	public Function sub(double d) {
		return sub(Value.newValue(d));
	}
	public Function sub(int i) {
		return sub(Value.newValue(i));
	}
	public Function neg() {
		return new Neg(this);
	}
	public Function reciprocal() {
		return new Div(Value.newValue(1), this);
	}
	
	public Function derivative() {
		return derivative(new HashMap<String, Function>());
	}
	public abstract Function derivative(Map<String, Function> derivatives);
}

class Neg extends Function {
	
	private final Function f;
	
	public Neg(Function f) {
		if(f == null) {
			throw new IllegalArgumentException();
		}
		this.f = f;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return -f.evaluate(params, true);
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		return new Neg(f.derivative(derivatives));
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sNEG\n", tabs));
		f.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("-%s", f.toString());
	}
}

class Mult extends Function {
	
	private final Function f1, f2;
	
	public Mult(Function f1, Function f2) {
		if(f1 == null || f2 == null) {
			throw new IllegalArgumentException();
		}
		this.f1 = f1; this.f2 = f2;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return f1.evaluate(params, true) * f2.evaluate(params, true);
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		Function m1 = new Mult(f1.derivative(derivatives), f2), m2 = new Mult(f1, f2.derivative(derivatives));
		return new Add(m1, m2);
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sMULT\n", tabs));
		f1.parseTree(sb, tabs + tab);
		f2.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("(%s) * (%s)", f1.toString(), f2.toString());
	}
}

class Div extends Function {
	
	private final Function f1, f2;
	
	public Div(Function f1, Function f2) {
		if(f1 == null || f2 == null) {
			throw new IllegalArgumentException();
		}
		this.f1 = f1; this.f2 = f2;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return f1.evaluate(params, true) / f2.evaluate(params, true);
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		Function m1 = new Mult(f1.derivative(derivatives), f2), m2 = new Mult(f1, f2.derivative(derivatives));
		Function d = new Exponent(f2, Value.newValue(2));
		return new Div(new Sub(m1, m2), d);
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sDIV\n", tabs));
		f1.parseTree(sb, tabs + tab);
		f2.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("(%s)/(%s)", f1.toString(), f2.toString());
	}
}

class Exponent extends Function {
	
	private final Function f1, f2;
	
	public Exponent(Function f1, Function f2) {
		if(f1 == null || f2 == null) {
			throw new IllegalArgumentException();
		}
		this.f1 = f1; this.f2 = f2;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return Math.pow(f1.evaluate(params, true), f2.evaluate(params, true));
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		return new Add(new Mult(this, new Mult(f2.derivative(derivatives), new NaturalLog(f1))), new Mult(f2, new Exponent(f1, new Sub(f2, Value.newValue(1)))));
	}

	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sEXP\n", tabs));
		f1.parseTree(sb, tabs + tab);
		f2.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("(%s)^(%s)", f1.toString(), f2.toString());
	}
}

class NaturalLog extends Function {
	
	private final Function f;
	
	public NaturalLog(Function f) {
		if(f == null) {
			throw new IllegalArgumentException();
		}
		this.f = f;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return Math.log(f.evaluate(params, true));
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		return new Div(f.derivative(derivatives), f);
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sLN\n", tabs));
		f.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("ln(%s)", f.toString());
	}
}

class Log extends Div {
	
	public Log(Function f1, Function f2) {
		super(new NaturalLog(f2), new NaturalLog(f1));
	}
}

class Sub extends Add {
	public Sub(Function f1, Function f2) {
		super(f1, new Neg(f2));
	}
}

class Add extends Function {

	private final Function f1, f2;
	
	public Add(Function f1, Function f2) {
		if(f1 == null || f2 == null) {
			throw new IllegalArgumentException();
		}
		this.f1 = f1; this.f2 = f2;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return f1.evaluate(params, true) + f2.evaluate(params, true);
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		return new Add(f1.derivative(derivatives), f2.derivative(derivatives));
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sADD\n", tabs));
		f1.parseTree(sb, tabs + tab);
		f2.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("(%s) + (%s)", f1.toString(), f2.toString());
	}
}

class Sin extends Function {
	
	private final Function f;
	
	public Sin(Function f) {
		if(f == null) {
			throw new IllegalArgumentException();
		}
		this.f = f;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return Math.sin(f.evaluate(params, true));
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		return new Mult(new Cos(f), f.derivative(derivatives));
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sSIN\n", tabs));
		f.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("sin(%s)", f.toString());
	}
}
class Cos extends Function {
	
	private final Function f;
	
	public Cos(Function f) {
		if(f == null) {
			throw new IllegalArgumentException();
		}
		this.f = f;
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		return Math.cos(f.evaluate(params, true));
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		return new Neg(new Mult(new Sin(f), f.derivative(derivatives)));
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%sCOS\n", tabs));
		f.parseTree(sb, tabs + tab);
	}
	
	public String toString() {
		return String.format("cos(%s)", f.toString());
	}
}
class Tan extends Div {
	public Tan(Function f) {
		super(new Sin(f), new Cos(f));
	}
}
class Csc extends Div {
	public Csc(Function f) {
		super(Value.newValue(1), new Sin(f));
	}
}
class Sec extends Div {
	public Sec(Function f) {
		super(Value.newValue(1), new Cos(f));
	}
}
class Cot extends Div {
	public Cot(Function f) {
		super(new Cos(f), new Sin(f));
	}
}

class Var extends Function {
	
	private final String varname;
	private String lastValue = "?";
	
	public Var(String variable) {
		if(variable == null) {
			throw new IllegalArgumentException();
		}
		varname = variable.toLowerCase();
	}
	
	public double evaluate(Map<String, Double> params, boolean cleaned) {
		if(!params.containsKey(varname)) {
			throw new IllegalArgumentException(String.format("variable '%s' not found in %s", varname, params));
		}
		double v = params.get(varname);
		lastValue = "" + v;
		return v;
	}
	
	public Function derivative(Map<String, Function> derivatives) {
		if(derivatives.size() == 0) {
			return Value.newValue(1);
		}else if(!derivatives.containsKey(varname)) {
			return Value.newValue(0);
		}else {
			return derivatives.get(varname);
		}
	}
	
	public void parseTree(StringBuffer sb, String tabs) {
		sb.append(String.format("%s%s=%s\n", tabs, varname, lastValue));
	}
	
	public String toString() {
		return varname;
	}
}