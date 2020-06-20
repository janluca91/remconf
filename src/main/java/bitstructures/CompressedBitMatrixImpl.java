package bitstructures;

import java.util.ArrayList;
/**
 /**
 * Abstract a representation of a compressed general matrix. Then it consists of a surjective but not-injective function that maps element from a large domain into a smaller domain<br>
 * Then the size of the adjacency matrix is smaller than expected by the domain size.
 * @author Gianluca
 */
public class CompressedBitMatrixImpl extends CompressedBitMatrix implements ImplementedMatrix {

	protected GeneralBitMatrixImpl matrix;
	public CompressedBitMatrixImpl(){	
	}
	/**
	 * Instantiate a compressed matrix by starting from a set of cliques. 
	 * @param c set of cliques
	 * @param nr number of rows of the matrix
	 * @param nc number of columns of matrix
	 */
	public CompressedBitMatrixImpl(CliquesImpl c,int nr,int nc)
	{
		int n,count,reward;
		nrows= Math.max(nr, c.nrows);
		ncolumns= Math.max(nc, c.ncolumns);
		n= Math.max(nrows, ncolumns);
		map= new ArrayList<>(n);
		count=0;
		for(int i=0;i<n;i++)
			map.add(null);
		for(int i=0;i<c.nrows;i++)
			if(map.get(i)==null){
				map.set(i,count);
				if(c.get(i, i))
					for(int j=i+1;j<c.ncolumns;j++)
						if(c.get(i,j))
							map.set(j, count);
				count++;
			}
		reward=c.nrows-count;
		for(int i=c.nrows;i<n;i++)
			map.set(i,count++);
		adjacency_matrix=matrix=new GeneralBitMatrixImpl(nrows-reward,ncolumns-reward);
		for(int i=0;i<c.nrows;i++)
			if(c.get(i, i))
				set(i,i,true);
				
	}
	/*
	 * (non-Javadoc)
	 * @see matrix.ImplementedMatrix#set(int, int, boolean)
	 */
	public void set(int i,int j,boolean value)
	{
		if(i<nrows&&j<ncolumns)
			matrix.set(map.get(i), map.get(j),value);
	}
}
