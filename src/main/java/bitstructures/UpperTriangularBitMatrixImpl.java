package bitstructures;
import java.util.BitSet;
/**
 * Abstracts a representation of Upper triangular Matrix of bits.
 * <br> It allocates memory space for storing only upper part of matrix since the lower part must be always 0.<br>
 * @author Gianluca
 */
public class UpperTriangularBitMatrixImpl extends UpperTriangularBitMatrix implements ImplementedMatrix {

	protected BitSet bitset;
	/**instantiate an  upper triangular Matrix by allocating n*(m-n)-n space
	 * @param m number of rows
	 * @param n number of columns
	 */
	public UpperTriangularBitMatrixImpl(int m, int n) {
		super(Math.min(m, n),n);
		this.bitset=new BitSet(ncolumns*nrows-(ncolumns*(ncolumns+1))/2);
	}
	/**instantiate an  upper triangular Matrix by allocating n*(n-1)/2 space
	 * @param n number of columns
	 */
	public UpperTriangularBitMatrixImpl(int n) {
		super(n);
		this.bitset=new BitSet((nrows*(nrows-1))/2);
	}
	/**
	 *instantiate a zero matrix (all the cells are false)
	 */
	public UpperTriangularBitMatrixImpl() {
	}
	public UpperTriangularBitMatrixImpl(BitMatrix matrix)
	{
		super(matrix.nrows(),matrix.ncolumns());
		bitset=new BitSet(ncolumns*nrows-(ncolumns*(ncolumns+1))/2);
		for(int i=0;i<nrows-1;i++)
			for(int j=i+1;j<ncolumns;j++)
					set(i, j,matrix.get(i, j));
		
	}
/*
 * (non-Javadoc)
 * @see matrix.BitMatrix#get(int, int)
 */
	public boolean get(int i,int j)
	{
		if(!super.get(i, j)) return false;
		else if(i>=j) 
			return false;
		else if(j<nrows)
			return bitset.get((j*(j-1))/2 //number of possible non-zero elements before i-th rows
							+i );					//number of columns 
		else return bitset.get((nrows*(nrows-1))/2 	//number of elements in the square matrix of size nrows
								+(j-nrows)*nrows+i); 	//position of (i,j)-th element in rectangular matrix
	}
	/*
	 * (non-Javadoc)
	 * @see matrix.ImplementedMatrix#set(int, int, boolean)
	 */
	public void set(int i,int j,boolean value){
		if(i>=j||i>=nrows||j>=ncolumns) return;
			if(j < nrows)
				bitset.set((j*(j-1))/2 //number of possible non-zero elements before i-th rows
							+i,		//number of columns
							value);
			else 
				bitset.set((nrows*(nrows-1))/2 //number of elements in the square matrix of size ncolumns
							+(j-nrows)*nrows+i);
	}
}
