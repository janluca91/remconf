package bitstructures;
/**
 * Abstracts a representation of a Symmetric Matrix of bits. It doesn't allocate memory space for storing bits of matrix but only contains information about size of matrix.<br>
 * Usually an object is instantiated by overriding the get method. If it is not redefined, it is as if we had a matrix of all 1
 * @author Gianluca
 *
 */
public class SymmetricBitMatrix extends BitMatrix{

	/**
	 * instantiate a symmetric bit matrix
	 * @param n umber of rows and columns of bit matrix
	 */
	public SymmetricBitMatrix(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}
	/**
	 * instantiate a zero matrix
	 */
	public SymmetricBitMatrix() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#transpose()
	 */
	public SymmetricBitMatrix transpose()
	{
		return this;
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#and(matrix.BitMatrix)
	 */
	public BitMatrix and(final BitMatrix right)
	{
		return right.andImpl((SymmetricBitMatrix) this);
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#union(matrix.BitMatrix)
	 */
	public BitMatrix or(final BitMatrix right)
	{
		return right.unionImpl((SymmetricBitMatrix) this);
	}
	protected SymmetricBitMatrix andImpl(SymmetricBitMatrix right)
	{ 
		return new SymmetricBitMatrix(nrows>right.nrows?right.nrows:nrows){
			public boolean get(int i,int j){
				return SymmetricBitMatrix.this.get(i, j)&&right.get(i, j);
				}
			};
	}
	protected SymmetricBitMatrix unionImpl(SymmetricBitMatrix right)
	{
		 return new SymmetricBitMatrix(nrows>right.nrows?nrows:right.nrows){
			public boolean get(int i,int j)
			{
				return SymmetricBitMatrix.this.get(i, j)||right.get(i, j);
			}
		};
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#not()
	 */
	public SymmetricBitMatrix not()
	{
		return new SymmetricBitMatrix(nrows){
			public boolean get(int i,int j)
			{
				return super.get(i, j)&&!SymmetricBitMatrix.this.get(i, j);
			}
		};
				
	}
	/* (non-Javadoc)
	 * in this case return a Cliques since the power of a Symmetric will represent a graph where i is connected to j and j to k  then i is connected to k
	 * @see matrix.BitMatrix#fixpoint()
	 */
	public SymmetricBitMatrix transitive_closure()
	{
		CliquesImpl result=new CliquesImpl(nrows);
		for(int i=0;i<nrows;i++)
			if(!result.get(i, i))
				depthvisit(i,result);
		
		return result;		
	}
	private void depthvisit(int i,CliquesImpl result)
	{
			for(int j=0;j<nrows;j++)
				if(!result.get(j, j)&&get(i,j)){
					result.set(i, j, true);
					depthvisit(j,result);
				}
	}
}
