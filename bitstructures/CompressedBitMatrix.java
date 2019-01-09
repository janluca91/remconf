package bitstructures;

import java.util.ArrayList;
/**
 * Abstract a representation of a compressed general matrix. Then it consists of a surjective but not-injective function that maps element from a large domain into a smaller domain<br>
 * Then the size of the adjacency matrix is smaller than expected by the domain size.
 * @author Gianluca
 *
 */
public class CompressedBitMatrix extends BitMatrix{
	protected ArrayList<Integer> map;
	protected BitMatrix adjacency_matrix;
	public CompressedBitMatrix() {
		
	}
	/**
	 * instantiate an object that represents a general matrix of bit but it permits to reduce space in storing by compressing the matrix of bit 
	 * @param m map array where i-th element contains the index of j-th element that include its in reduced matrix. 
	 * @param matrix reduced matrix of bit
	 * @param nr number of rows of the matrix that you want
	 * @param nc number of column of the matrix that you want
	 */
	public CompressedBitMatrix(ArrayList<Integer> m,BitMatrix matrix,int nr,int nc)
	{
		super(nr>m.size()?nr:m.size(),nc>m.size()?nc:m.size());
		map=m;
		adjacency_matrix=matrix;
	}
	/**
	 * @param m number of rows of the matrix that you want
	 * @param n number of column of the matrix that you want
	 */
	public CompressedBitMatrix(int m,int n)
	{
		nrows=m;
		ncolumns=n;
	}
	/**
	 * @param i index of rows
	 * @param j index of columns
	 * @return <b>true</b> if the (i,j)-th position in the bit matrix contains 1,<b>false</b> otherwise
	 */
	public boolean get(int i,int j)
	{
		return super.get(i, j)&&adjacency_matrix.get(map.get(i),map.get(j));
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#transpose()
	 */
	public  CompressedBitMatrix transpose()
	{
		return new CompressedBitMatrix(map,adjacency_matrix.transpose(),ncolumns,nrows);
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#not()
	 */
	public CompressedBitMatrix not()
	{
		return new CompressedBitMatrix(map,adjacency_matrix.not(),nrows,ncolumns);
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#fixpoint()
	 */
	public CompressedBitMatrix transitive_closure()
	{
		return new CompressedBitMatrix(map,adjacency_matrix.transitive_closure(),nrows,ncolumns);
	}
}
