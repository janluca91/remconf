package bitstructures;
import java.util.ArrayList;
	public interface IMatrix {
	int nrows();
	int ncolumns();
	boolean get(int i, int j);
	BitVector product(final BitVector v);
	BitMatrix product(final BitMatrix right);
	BitMatrix and(final BitMatrix right);
	BitMatrix or(final BitMatrix right);
	BitMatrix not();
	BitMatrix transpose();
	BitMatrix transitive_closure();
	ArrayList<Integer> scalar_product(BitVector v);
	BitVector getColumn(int i);
	BitVector getRow(int i);
}
