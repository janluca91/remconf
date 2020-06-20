package bitstructures;

import java.util.ArrayList;
import java.util.BitSet;
/**
 * Represents a set of Cliques, by optimizing space.<br>Cliques are represented as a matrix of bits where (i,j)-th is true if and only if (j,i)-th is true 
 * @author Gianluca
 */
public class CliquesImpl extends SymmetricBitMatrix implements ImplementedMatrix{
	private final ArrayList<BitSet> nodes;
	/**
	 * instantiate an object that represents a set of cliques 
	 * @param n the number of point in the space
	 */
	public CliquesImpl(int n) {
		nrows=ncolumns=n;
		nodes=new ArrayList<BitSet>(nrows);
		for(int i=0;i<nrows;i++)
			nodes.add(null);
	}
	/**
	 * set the bit in (i-j)-th position to 1 or 0 according value of <b>value parameter</b>
	 * @param i index of rows
	 * @param j index of columns
	 * @param value if the bit that you want to insert is 1 then value must be <b>true</b>, otherwise it must be <b>false</b>
	 */
	public void set(int i,int j,boolean value)
	{
			if(!(i<nrows&&j<ncolumns)) return;
			BitSet a=nodes.get(i);
			BitSet b=nodes.get(j);
			if(a!=null){
				a.set(j);
				nodes.set(j,a);
				if(b!=null)
					for(int k=0;k<b.size();k++)
						if(b.get(k)){
							a.set(k);
							nodes.set(k, a);
							
						}
			}
			else if(b!=null){
				b.set(i);
				nodes.set(i,b);
			}
			else{
				a=new BitSet(nrows);
				nodes.set(i,a);
				nodes.set(j,a);
				a.set(i);
				a.set(j);
			}
	}
	/**
	 * @param i index of rows
	 * @param j index of columns
	 * @return true if the place stated by i and j is within the range
	 */
	public boolean get(int i,int j)
	{
		return super.get(i, j)&&nodes.get(i)!=null&&nodes.get(j)!=null&&nodes.get(i)==nodes.get(j);
	}
	/* (non-Javadoc)
	 *it returns itself since fixpoint of a set of cliques is the same set of cliques
	 * @see matrix.BitMatrix#fixpoint()
	 */
	public CliquesImpl transitive_closure(){
		return this;
	}
	/* (non-Javadoc)
	 * @see matrix.BitMatrix#union()
	 */
	public BitMatrix or(BitMatrix right)
	{
		return
			new BitMatrix(Math.max(nrows, right.nrows), Math.max(ncolumns, right.ncolumns))
			{
				public boolean get(int i,int j)
				{
					return CliquesImpl.this.get(i,j)||right.get(i, j);
				}
				public BitMatrix transitive_closure(){
					CompressedBitMatrixImpl result=new CompressedBitMatrixImpl(CliquesImpl.this,right.nrows,right.ncolumns);
					for(int i=0;i<right.nrows;i++)
						for(int j=0;j<right.ncolumns;j++)
							if(right.get(i, j))
								result.set(i, j, true);
					return result.transitive_closure();
				}
			};
	}
	protected BitMatrix unionImpl(BitMatrix left)
	{
		return or(left);
	}
}
