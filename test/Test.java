import position.E6POS;

public class Test {

	public static void main(String[] args) {
		E6POS e = new E6POS(30, 50, 1, 35, 102, 333), e2 = new E6POS(111, 0.023, 22.9, 36, 24, 14);
		
		//System.out.println("----------------");
		
		//E6POS e3 = E6POS.compose(e, e2), e4 = e3.invert();
		//System.out.println(e3);
		//System.out.println(e4);
		
		//System.out.println(e3.ori());
		//System.out.println(e4.ori());
		
		//System.out.println(e3.toGlobal(e4).ori().condense());
		//System.out.println(e4.toGlobal(e3).ori().condense());
		
		//System.out.println(e3);
		
		E6POS e5 = new E6POS(39.357, -32.8929, 116.41, -50.207, -16.9595, -35.3577);
		System.out.println(e5);
		System.out.println(e5.invert());
		System.out.println(e5.invert().toGlobal(e5));
		System.out.println();
		E6POS e7 = new E6POS(21.461, 56.7376, -111.814, -40.2695, 36.6475, -13.5203);
		System.out.println(e7.toGlobal(e5));
	}
}
