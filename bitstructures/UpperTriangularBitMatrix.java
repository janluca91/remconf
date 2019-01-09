package bitstructures;

/**
 * Abstracts a representation of Upper triangular Matrix of bits. It doesn't allocate memory space for storing bits of matrix but only contains information about size of matrix.<br>
 * Usually an object is instantiated by overriding the get method. If it is not redefined, it is as if we had a matrix <b>m</b> where <b>m</b>[i,j]=true iff j>i
 * @author Gianluca
 *
 */
public class UpperTriangularBitMatrix extends BitMatrix{

	
	/**instantiate an upper triangular Matrix 
	 * @param m number of rows
	 * @param n number of columns
	 */
	public UpperTriangularBitMatrix(int m, int n) {
		super(m, n);
	}

	/**
	 * instantiate an  upper triangular square Matrix 
	 * @param n the number of rows and columns
	 */
	public UpperTriangularBitMatrix(int n) {
		super(n);
	}
	/**
	 *instantiate a zero matrix (all the cells are false)
	 */
	public UpperTriangularBitMatrix() {
	}
	/* (non-Javadoc)
	 * in this case it returns an upper triangular Matrix since the intersection between an upper triangular and any kind of matrix is an upper triangular 
	 * @see matrix.BitMatrix.and(matrix.BitMatrix)
	 */
	public UpperTriangularBitMatrix and(final BitMatrix right)
	{
		return new UpperTriangularBitMatrix(nrows<right.nrows?nrows:right.nrows,ncolumns<right.ncolumns?ncolumns:right.ncolumns)
		{
			public boolean get(int i,int j)
			{
				return UpperTriangularBitMatrix.this.get(i, j)&&right.get(i,j);
				
			}
		};
	}
	protected BitMatrix andImpl(final BitMatrix left)
	{
		return and(left);
	}
	/* (non-Javadoc)
	 *it is accept method of Visitor pattern, while unionImpl is the visit method.  
	 * @see matrix.BitMatrix.union(matrix.BitMatrix)
	 */
	public BitMatrix or(final BitMatrix right)
	{
		return right.unionImpl((UpperTriangularBitMatrix)this);
	}
	protected UpperTriangularBitMatrix unionImpl(final UpperTriangularBitMatrix right)
	{
		return new UpperTriangularBitMatrix(nrows>right.nrows?nrows:right.nrows,ncolumns>right.ncolumns?ncolumns:right.ncolumns)
				{
					public boolean get(int i,int j)
					{
						return UpperTriangularBitMatrix.this.get(i, j)||right.get(i, j);
					}
				};
	}
	/* (non-Javadoc)
	 *it is accept method of Visitor pattern, while leftproduct is the visit method.  
	 * @see matrix.BitMatrix.product(matrix.BitMatrix)
	 */
	public BitMatrix product(final BitMatrix right)
	{
		return right.leftproduct((UpperTriangularBitMatrix)this);
	}
	protected UpperTriangularBitMatrixImpl leftproduct(final UpperTriangularBitMatrix left)
	{
		UpperTriangularBitMatrixImpl result=new UpperTriangularBitMatrixImpl(left.nrows,ncolumns);
		int n=nrows>left.ncolumns?left.ncolumns:nrows;
		for(int i=0;i<result.nrows;i++)
			for(int j=i+1;j<n;j++)
				if(left.get(i, j))
					for(int k=j+1;k<result.ncolumns;k++)
						if(get(j,k))
							result.set(i, k, true);
		return result;
	}
	/* (non-Javadoc)
	 *it returns an upper triangular matrix since the fixpoint operation is closed under upper triangular matrix set  
	 * @see matrix.BitMatrix.fixpoint()
	 */
	public UpperTriangularBitMatrixImpl transitive_closure()
	{
		UpperTriangularBitMatrixImpl result=new UpperTriangularBitMatrixImpl(nrows,ncolumns);
		for(int i=nrows-1;i>=0;i--)
			for(int j=ncolumns-1;j>i;j--)
				if(get(i, j))
				{
					result.set(i, j, true);
					if(j<nrows)
						for(int k=ncolumns-1;k>j;k--)
							if(result.get(j, k))
								result.set(i, k, true);
				}
		return result;
	}
}
