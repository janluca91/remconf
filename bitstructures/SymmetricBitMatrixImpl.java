package bitstructures;
import java.util.BitSet;

/**
 * Abstracts a representation of a Symmetric Matrix of bits. <br>
 * It optimize the memory space allocation by storing only the upper part of the matrix since the lower part is a reflex of upper one
 * @author Gianluca
 */
public class SymmetricBitMatrixImpl extends SymmetricBitMatrix implements ImplementedMatrix {

	private BitSet bitset;
	/**instantiate a symmetric Matrix by allocating n*(n+1)/2 space
	 * @param n size of matrix
	 */
	public SymmetricBitMatrixImpl(int n){
		super(n);
		this.bitset=new BitSet((n*(n+1))/2);
	}
	public SymmetricBitMatrixImpl() {
		super();
	}
	public SymmetricBitMatrixImpl(BitMatrix matrix)
	{
		super(matrix.ncolumns<matrix.nrows?matrix.ncolumns:matrix.nrows);
		this.bitset=new BitSet((nrows*(nrows-1))/2);
		for(int i=0;i<nrows;i++)
			for(int j=0;j<i;j++)
				if(matrix.get(i, j))
					this.set(i, j, true);
	}
	/*
	 * (non-Javadoc)
	 * @see matrix.BitMatrix#get(int, int)
	 */
	public boolean get(int i,int j)
	{
		if(!super.get(i, j)) return false;
		int r=i>j?i:j;
		int c=i+j-r;
		return bitset.get((r*(r+1))/2 //number of possible non-zero elements before r-th rows
					+c			//number of columns
					);
	}
	/*
	 * (non-Javadoc)
	 * @see matrix.ImplementedMatrix#set(int, int, boolean)
	 */
	public void set(int i,int j,boolean value){
		if(!(i<nrows&&j<ncolumns)) return;
		int r=i>j?i:j;
		int c=i+j-r;
		bitset.set((r*(r+1))/2 //number of possible non-zero elements before r-th rows
						+c,	//number of columns
					value); 
	}
	
}
