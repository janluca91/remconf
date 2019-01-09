package bitstructures;
import java.util.BitSet;
/**
 *  Abstracts a representation of Matrix of bits. It allocate memory space for storing bits of matrix without optimization
 * @author Gianluca 
 */
public class GeneralBitMatrixImpl extends BitMatrix implements ImplementedMatrix {

	private BitSet bitset;
	public GeneralBitMatrixImpl()
	{
		
	}
	/**
	 * instantiate a general Matrix by allocating n*m space
	 * @param m number of rows
	 * @param n number of columns
	 */
	public GeneralBitMatrixImpl(int m,int n) {
		super(m,n);
		this.bitset=new BitSet(nrows*ncolumns);
	}
	/**
	 * instantiate a general Matrix by allocating n^2 space
	 * @param n size of matrix
	 */
	public GeneralBitMatrixImpl(int n) {
		super(n,n);
		this.bitset=new BitSet(ncolumns*ncolumns);
	}
	/**
	 * copying constructor
	 * @param matrix source matrix that you want to clone
	 */
	public GeneralBitMatrixImpl(BitMatrix matrix)
	{
		super(matrix.nrows,matrix.ncolumns);
		this.bitset=new BitSet(nrows*ncolumns);
		for(int i=0;i<nrows;i++)
			for(int j=0;j<ncolumns;j++)
				this.set(i, j,matrix.get(i, j));
	}
	/*
	 * (non-Javadoc)
	 * @see matrix.ImplementedMatrix#set(int, int, boolean)
	 */
	public void set(int i,int j,boolean value)
	{
		if(i<nrows&&j<nrows)
			bitset.set(i*ncolumns+j,value);
	}
	/*
	 * (non-Javadoc)
	 * @see matrix.BitMatrix#get(int, int)
	 */
	public boolean get(int i,int j)
	{
			return super.get(i, j)&&bitset.get(i*ncolumns+j);
	}

}
