package matrix;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

import function.Function;
import function.Value;
import vector.Vector;

public class Matrix {

	private Function[][] matrix;
	private HashMap<String, Double> params = new HashMap<String, Double>();
	public final int rows, cols;
	
	public void setParam(String var, double val) {
		params.put(var, val);
	}
	
	public Matrix(int rows, int cols) {
		if(rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException(String.format("Cannot create a %sx%s matrix", rows, cols));
		}
		this.rows = rows;
		this.cols = cols;
		matrix = new Function[rows][cols];
	}
	
	public Matrix(int rows, int cols, Object... vals) {
		this(rows, cols);
		this.setMatrix(vals);
	}
	
	public Matrix(Matrix m) {
		this(m.rows, m.cols);
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				this.matrix[r][c] = m.matrix[r][c];
			}
		}
	}
	
	private void checkBounds(int row, int col) {
		if(row < 0 || row >= matrix.length || col < 0 || col >= matrix[0].length) {
			throw new NoSuchElementException(String.format("%s %s", row, col));
		}
	}
	public void setValue(int row, int col, double value) {
		checkBounds(row, col);
		matrix[row][col] = Value.newValue(value);
	}
	public void setValue(int row, int col, int value) {
		checkBounds(row, col);
		matrix[row][col] = Value.newValue(value);
	}
	public void setValue(int row, int col, Function value) {
		checkBounds(row, col);
		matrix[row][col] = value;
	}
	public void setValue(int row, int col, String value) {
		checkBounds(row, col);
		try {
			matrix[row][col] = Function.parseFunctionFromCleanRPNString(value);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	public String toString() {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for(Function[] fs : matrix) {
			if(first) {
				first = false;
			}else {
				sb.append("\n");
			}
			sb.append("| ");
			for(Function f : fs) {
				double eval = f.evaluate(params);
				int intpart = (int) eval;
				//System.out.println((eval - intpart) * 1000);
				int fpart = Math.abs(Math.round((float) (eval - intpart) * 1000));
				//System.out.println(String.format("%s %s %s", eval, intpart, fpart));
				sb.append(String.format((eval < 0 && intpart == 0) ? "-%4d.%03d" : "%5d.%03d", intpart, fpart));
			}
			sb.append(" |");
		}
		return sb.toString();
	}
	*/
	
	public String toString() {
		String total = "";
		for(String s : getLines()) {
			total += s + "\n";
		}
		return total;
	}
	
	public String[] getLines() {
		String[][] strings = new String[this.matrix.length][this.matrix[0].length];
		String[] lines = new String[this.matrix.length];
		int maxLength = 0;
		for(int r = 0; r < this.matrix.length; r++) {
			for(int c = 0; c < this.matrix[0].length; c++) {
				strings[r][c] = this.matrix[r][c].toString();
				if(strings[r][c].length() > maxLength) {
					maxLength = strings[r][c].length();
				}
			}
		}
		int lin = 0;
		for(String[] ss : strings) {
			lines[lin] = "| ";
			for(String s : ss) {
				int l = s.length();
				int pre = (maxLength - l) / 2;
				int post = maxLength - pre - l;
				//System.out.println(l + pre + post);
				lines[lin] += spaces(pre + 2) + s + spaces(post + 2);
			}
			lines[lin++] += " |";
		}
		return lines;
	}
	private String spaces(int numSpaces) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < numSpaces; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}
	
	public void setMatrix(Object... arr) {
		//System.out.println(Arrays.toString(arr));
		if(arr.length != matrix.length * matrix[0].length) {
			throw new IndexOutOfBoundsException(String.format("Matrix has %s elements, but %s provided", matrix.length * matrix[0].length, arr.length));
		}
		for(int r = 0; r < matrix.length; r++) {
			for(int c = 0; c < matrix[0].length; c++) {
				Object o = arr[r*matrix[0].length+c];
				//System.out.println(o);
				if(o instanceof Integer) {
					setValue(r, c, ((Integer) o).intValue());
				}else if(o instanceof Double) {
					setValue(r, c, ((Double) o).doubleValue());
				}else {
					setValue(r, c, o.toString());
				}
			}
		}
	}
	
	public Matrix multAndEval(Matrix m) {
		return mult(m, true);
	}
	public Matrix mult(Matrix m) {
		return mult(m, false);
	}
	public Matrix mult(Matrix m, boolean condense) {
		if(m == null || this.matrix[0].length != m.matrix.length) {
			throw new IllegalArgumentException();
		}
		Matrix t = new Matrix(this.matrix.length, m.matrix[0].length);
		for(int row = 0; row < t.matrix.length; row++) {
			for(int col = 0; col < m.matrix[0].length; col++) {
				if(condense) {
					t.matrix[row][col] = this.matrix[row][0].toValue(this.params).mult(m.matrix[0][col].toValue(m.params));
				}else {
					t.matrix[row][col] = this.matrix[row][0].mult(m.matrix[0][col]);
				}
				for(int i = 1; i < m.matrix.length; i++) {
					if(condense) {
						t.matrix[row][col] = t.matrix[row][col].add(this.matrix[row][i].toValue(this.params).mult(m.matrix[i][col].toValue(m.params)));
					}else {
						t.matrix[row][col] = t.matrix[row][col].add(this.matrix[row][i].mult(m.matrix[i][col]));
					}
				}
				if(condense) {
					t.matrix[row][col] = t.matrix[row][col].toValue(this.params);
				}
			}
		}
		return t;
	}
	
	public Function sumElements() {
		Function func = null;
		for(Function[] fs : this.matrix) {
			for(Function f : fs) {
				if(func == null) {
					func = f;
					continue;
				}
				func.add(f);
			}
		}
		return func;
	}
	
	public Matrix transpose() {
		Matrix m = new Matrix(this.matrix[0].length, this.matrix.length);
		for(int r = 0; r < this.matrix.length; r++) {
			for(int c = 0; c < this.matrix[0].length; c++) {
				m.setValue(c, r, this.matrix[r][c]);
			}
		}
		return m;
	}
	
	public Function get(int r, int c) {
		if(r < 0 || c < 0 || r >= rows || c >= cols) {
			throw new NoSuchElementException();
		}
		return matrix[r][c];
	}
	
	public Matrix invert() {
		if(rows != cols) {
			throw new IllegalArgumentException();
		}
		Matrix minors = this.minors();
		Matrix cofactors = minors.altSigns();
		Matrix adj = cofactors.transpose();
		Function det = this.matrix[0][0].mult(minors.matrix[0][0]);
		for(int r = 1; r < rows; r++) {
			det = det.add(this.matrix[r][0].mult(cofactors.matrix[r][0]));
		}
		adj.scale(det.reciprocal());
		return adj;
	}
	
	public Function determinant() {
		if(rows != cols) {
			throw new IllegalArgumentException();
		}
		if(rows == 2) {
			return get(0, 0).mult(get(1, 1)).sub(get(1, 0).mult(get(0, 1)));
		}
		Matrix m = this.minors().altSigns();
		Function f = this.matrix[0][0].mult(m.matrix[0][0]);
		for(int r = 1; r < rows; r++) {
			f = f.mult(this.matrix[r][0].mult(m.matrix[r][0]));
		}
		return f;
	}
	
	public void scale(Function f) {
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				matrix[r][c] = matrix[r][c].mult(f);
			}
		}
	}
	
	public Matrix minors() {
		Matrix m = new Matrix(rows, cols);
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				m.setValue(r, c, this.subMatrix(r, c).determinant());
			}
		}
		return m;
	}
	
	public Matrix altSigns() {
		Matrix m = new Matrix(this);
		boolean neg = false;
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				if(neg) {
					m.matrix[r][c] = m.matrix[r][c].neg();
				}
				neg = !neg;
			}
		}
		return m;
	}
	
	public Matrix subMatrix(int skipRow, int skipCol) {
		this.checkBounds(skipRow, skipCol);
		Matrix m = new Matrix(rows - 1, cols - 1);
		int r2 = 0;
		for(int r = 0; r < rows; r++) {
			if(r == skipRow) {
				continue;
			}
			int c2 = 0;
			for(int c = 0; c < cols; c++) {
				if(c == skipCol) {
					continue;
				}
				m.setValue(r2, c2, matrix[r][c]);
				c2++;
			}
			r2++;
		}
		return m;
	}
	
	public Matrix copy() {
		return new Matrix(this);
	}
	
	public Vector colVector(int col) {
		Function[] column = new Function[this.rows];
		for(int r = 0; r < rows; r++) {
			column[r] = matrix[r][col];
		}
		return new Vector(column);
	}
	public Vector rowVector(int row) {
		return new Vector(matrix[row]);
	}
	
	public Matrix condense() {
		return condense(params);
	}
	public Matrix condense(HashMap<String, Double> p) {
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				matrix[r][c] = Value.newValue(this.matrix[r][c].evaluate(p));
			}
		}
		return this;
	}
}