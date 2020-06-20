package bitstructures;
import java.util.ArrayList;

/**
 * Abstracts a representation of Matrix of bits. It doesn't allocate memory space for storing bits of matrix but only contains information about size of matrix.<br>
 * Usually an object is instantiated by overriding the get method. If it is not redefined, it is as if we had a matrix of all 1
 * @author Gianluca
 */
public class BitMatrix implements IMatrix{

	protected int nrows,ncolumns;
	
	/**
	 * @param n size of matrix 
	 * @return a new matrix <b>m</b> where m[i,j]=true iff i=j
	 */
	public static BitMatrix identity(int n)
	{
		return new SymmetricBitMatrix(n){
			public boolean get(int i,int j)
			{
				return super.get(i, j)&&(i==j);
			}	
		};
	}
	/**
	 * instantiate a general Matrix 
	 * @param m number of rows 
	 * @param n number of columns 
	 */
	public BitMatrix(int m,int n)
	{
		nrows=m;
		ncolumns=n;
	}
	/**
	 *instantiate a general matrix
	 *@param n number of rows and columns of matrix
	 */
	public BitMatrix(int n) {
		//bitset=matrix.bitset;
		nrows=n;
		ncolumns=n;
	}
	/**
	 *instantiate a zero matrix (all the cells are false)
	 */
	public BitMatrix()
	{
		//instantiate a zero Matrix
	}
	/**
	 * Construct a BitMatrix from an Integer Array where (i,j)-th element of matrix is 1 if and only if i-th position of integer array is j
	 * @param intarray an ArrayList Integer from which you want to build the matrix
	 * @return a new general BitMatrix
	 */
	public static BitMatrix matrixFromIntArray(ArrayList<Integer> intarray)
	{
		return new BitMatrix(intarray.size())
		{
			public boolean get(int i,int j)
			{
				return (i<intarray.size()&&j<intarray.size()&&intarray.get(i)==j);
			}
		};	
	}
	/**
	 * @param i index of rows
	 * @param j index of columns
	 * @return true if the place stated by i and j is within the range
	 */
	public boolean get(int i,int j)
	{
		return i<nrows&&j<ncolumns;
	}
	/**
	 *@return number of rows of the matrix
	 */
	public int nrows()
	{
		return nrows;
	}
	/**
	 *@return number of columns of the matrix
	 */
	public int ncolumns()
	{
		return ncolumns;
	}
	/**
	 *@return a new BitVector that represent the <b>r-th</b> row of the caller BitMatrix
	 */
	public BitVector getRow(int r)
	{
		return new BitVector(ncolumns)
		{
			public boolean get(int i)
			{
				return BitMatrix.this.get(r, i);
			}
		};
	}
	/**
	 *@return a new BitVector that represent the <b>c-th</b> column of the caller BitMatrix
	 */
	public BitVector getColumn(int c)
	{
		return new BitVector(nrows)
		{
			public boolean get(int i)
			{
				return BitMatrix.this.get(i, c);
			}
		};
	}
	/**
	 *@return a new BitMatrix whose (i,j)-th element is the (j,i)-th element of caller BitMatrix
	 */
	public BitMatrix transpose()
	{
		return new BitMatrix(ncolumns,nrows){
			public boolean get(int i,int j)
			{
				return BitMatrix.this.get(j, i);
			}
			public BitMatrix transpose()
			{
				return BitMatrix.this;
			}
		};	
	}
	/**if the caller is a transpose of passed argument matrix, then it returns a SymmetricMatrix otherwise returns other kind of BitMatrix
	 * @param right the right factor of scalar product
	 *@return a new Bit Matrix whose (i,j)-th element is the (j-h)-th element of caller BitMatrix
	 */
	public BitMatrix product(final BitMatrix right)
	{
		if((right.transpose()==this)||(transpose()==right)){
			SymmetricBitMatrixImpl	result=new SymmetricBitMatrixImpl(nrows);
			for(int i=0;i<nrows;i++)
				for(int j=0;j<result.ncolumns;j++)
					if(get(i,j))
						for(int k=i+1;k<ncolumns;k++)
							if(right.get(j, k))
								result.set(i, k, true);
			return result;
		}
		else 
			return right.leftproduct(this);
	}
	
	/**
	 * compute the product between the caller matrix and a passed as argument bit vector and return the result
	 * @param right the right factor of scalar product
	 * @return a bitvector v where v[i]==true iff exist an integer k such that <b>caller matrix</b>[i,k]==true  and <b>right</b>[k] ==true 
	 * */
	public BitVector product(final BitVector right) {
		return new BitVector(nrows)
		{
			public boolean get(int i)
			{
				for(int k=0;k<ncolumns;k++)
					if(BitMatrix.this.get(i,k)&&right.get(k))
						return true;
				return false;
			}
		};
	}
	/**
	 * compute the result of intersection between the caller matrix and passed as argument matrix.
	 * @param right the right factor of intersection
	 * @return a bitmatrix m where m[i,j]==true iff <b>caller matrix</b>[i,j]==true and <b>right</b>==true
	 */
	public BitMatrix and(final BitMatrix right)
	{
		return right.andImpl(this);
	}
	protected BitMatrix andImpl(final BitMatrix right)
	{
		return new BitMatrix(Math.min(nrows, right.nrows), Math.min(ncolumns, right.ncolumns))
		{
			public boolean get(int i,int j)
			{
				return BitMatrix.this.get(i, j)&&right.get(i,j);
				
			}
		};
	}
	protected BitMatrix andImpl(final SymmetricBitMatrix right){
		return andImpl((BitMatrix)right);
	}
	/**
	 * compute the result of union between the caller matrix and passed as argument matrix.
	 * @param right the right factor of intersection
	 * @return a bitmatrix m where m[i,j]==true iff <b>caller matrix</b>[i,j]==true or <b>right</b>==true
	 */
	public BitMatrix or(final BitMatrix right)
	{
		return right.unionImpl(this);
	}
	protected BitMatrix unionImpl(final UpperTriangularBitMatrix right)
	{
		return unionImpl( (BitMatrix) right);
	}
	protected BitMatrix unionImpl(final SymmetricBitMatrix right)
	{
		return unionImpl((BitMatrix) right);
	}
	protected BitMatrix unionImpl(final BitMatrix right)
	{
		return new BitMatrix(Math.max(nrows, right.nrows), Math.max(ncolumns, right.ncolumns))
		{
			public boolean get(int i,int j)
			{
				return BitMatrix.this.get(i, j)||right.get(i,j);
				
			}
		};
	}
	/**
	 * compute the negation of caller matrix
	 * @return a bitmatrix m where m[i,j]==true iff <b>caller matrix</b>[i,j]==false
	 */
	public BitMatrix not()
	{
		return new BitMatrix(nrows,ncolumns)
		{
			public boolean get(int i,int j)
			{
				return super.get(i, j)&&!BitMatrix.this.get(i, j);
				
			}
		};
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object obj)
	{
		if(super.equals(obj)) 
			return true;
		if(!(obj instanceof BitMatrix))
			return false;
		BitMatrix bitmatrix=(BitMatrix) obj;
		for(int i=0;i<nrows;i++)
			for(int j=0;j<ncolumns;j++)
				if(get(i, j)!=bitmatrix.get(i, j))
					return false;
		return true;
	}
	/**
	 * compute the scalar product between the caller matrix and passed as argument Bit Vector
	 * @param right the right factor of scalar product
	 * @return a ArrayList of Integer <b>vett</b> where <b>vett</b>[i]=j iff number of true cells in i-th rows of caller matrix is j
	 */
	
	public  ArrayList<Integer> scalar_product(BitVector right)
	{
		ArrayList<Integer> result= new ArrayList<>(nrows);
		for(int i=0;i<nrows;i++)
		{
			int count=0;
			for(int j=0;j<ncolumns;j++)
				if(get(i,j)&&right.get(j))
					count++;
			result.add(count);
		}
		return result;
	}
	
	
	/**
	 * compute the transitive closure of caller matrix. the transitive closure is the union of all power of caller_matrix that is transitive_closure(A)=A<sup>1</sup>U..U..U A<sup>&infin;</sup>
	 * @return a new Matrix <b>m</b> where <b>m</b>[i,j]==true if exists a path from i to j in the graph represented by <b>caller matrix</b>
	 */
	public BitMatrix transitive_closure()
	{
		//warshall algorithm O(n^3)
		GeneralBitMatrixImpl closure=new GeneralBitMatrixImpl(this);
		for(int k=0;k<nrows;k++)
			for(int j=0;j<ncolumns;j++)
				if(closure.get(k,j))
					for(int i=0;i<nrows;i++)
						if(closure.get(i, k))
							closure.set(i, j, true);
		return closure;
	}
	protected BitMatrix leftproduct(final BitMatrix left){
		GeneralBitMatrixImpl result=new GeneralBitMatrixImpl(left.nrows,ncolumns);
		int n= Math.min(nrows, left.ncolumns);
		for(int i=0;i<left.nrows;i++)
			for(int j=0;j<n;j++)
				if(left.get(i,j))
					for(int k=0;k<result.ncolumns;k++)
						if(get(j, k))
							result.set(i, k, true);
		return result;
	}
	protected BitMatrix leftproduct(final UpperTriangularBitMatrix left)
	{
		GeneralBitMatrixImpl result=new GeneralBitMatrixImpl(left.nrows,ncolumns);
		int n= Math.min(nrows, left.ncolumns);
		for(int i=0;i<result.nrows;i++)
			for(int j=i+1;j<n;j++)
				if(left.get(i,j))
					for(int k=0;k<result.ncolumns;k++)
						if(get(j, k))
							result.set(i, k, true);
		return result;
	}
}
