package bitstructures;
import java.util.ArrayList;
	public interface IMatrix {
	public int nrows();
	public int ncolumns();
	public boolean get(int i,int j);
	public BitVector product(final BitVector v);
	public BitMatrix product(final BitMatrix right);
	public BitMatrix and(final BitMatrix right);
	public BitMatrix or(final BitMatrix right);
	public BitMatrix not();
	public BitMatrix transpose();
	public BitMatrix transitive_closure();
	public ArrayList<Integer> scalar_product(BitVector v);
	public BitVector getColumn(int i);
	public BitVector getRow(int i);
}
