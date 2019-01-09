package bitstructures;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
/**
 * Abstracts a representation of Vector of bits. It doesn't allocate memory space for storing bits of vector but only contains information about its size.<br>
 * Usually an object is instantiated by overriding the get method. If it is not redefined, it is as if we had a vector of all 1 
 * @author Gianluca
 *
 */
public class BitVector {
	protected int n;
	protected BitSet bitset;
	public BitVector()
	{
	}
	/**
	 *instantiate an object that represents a vector of bits
	 *@param n size of vector
	 */
	public BitVector(int  n)
	{
		this.n=n;
	}
	/**
	 * @return size of vector
	 */
	public int size()
	{
		return n;
	}
	/**
	 * @param i index
	 * @return true if index is within size of vector
	 */ 
	public boolean get(int i)
	{
		return i<n;
	}
	public void set(int i,boolean value)
	{
	}
	/**
	 * compute the scalar product of two vectors
	 * @param v the right factor of scalar product
	 * @return true if exist index <b>i</b> such that i-th element of caller vector and i-th element of <b>v</b> contains 1;false otherwise
	 */
	public boolean product(BitVector v)
	{
		if(n!=v.n)
			return false;
		else for(int k=0;k<n;k++)
			if(get(k)&&v.get(k))
				return true;
		return false;
	}
	/**
	 * compute the scalar product between a vector and a matrix, the result is a vector
	 * @param m matrix that is right factor of scalar product
	 * @return a vector whose <b>i-th</b> element is true if the scalar product between the <b>i-th</b> row vector of matrix and vector <b>v</b> is true
	 */
	public BitVector product(BitMatrix m)
	{
		return m.transpose().product(this);
	}
	/**
	 * compute the intersection between two vectors
	 * @param v vector with which you want to perform intersection
	 * @return vector whose i-th element is true if both i-th element of <b>caller vector</b> and i-th element of <b>v</b>  are true
	 */
	public BitVector and(BitVector v)
	{
		return new BitVector((n<v.n?n:v.n))
		{
			public boolean get(int i)
			{
				return BitVector.this.get(i)&&v.get(i);
			}
		};
	}
	/**
	 * compute the union between two vectors
	 * @param v vector with which you want to perform union
	 * @return vector whose i-th element is true if either i-th element of <b>caller vector</b> is true or i-th element of <b>v</b> is true
	 */
	public BitVector or(BitVector v)
	{
		return new BitVector(n>v.n?n:v.n)
			{
				public boolean get(int i)
				{
					return BitVector.this.get(i)||v.get(i);
				}
			};
	}
	/**
	 *compute the negation of a vector
	 *@return a vector whose i-th element is true if i-th element of <b>caller vector</b> is false, and viceversa 
	 */
	public BitVector not()
	{
		return new BitVector(n)
		{
			public boolean get(int i)
			{
				return !BitVector.this.get(i);
			}
		};
	}
	/**
	 * compute the cartesian product between two vectors
	 * @param v vector that is right factor of cartesian product
	 * @return a matrix where (i,j)-th is true only if both i-th element of <b>caller vector</b> and j-th element of <b>v</b> are true
	 */
	public BitMatrix cartesianProduct(BitVector v)
	{
		if(v==this)
			return new SymmetricBitMatrix(v.n){
				public boolean get(int i,int j)
				{
					return v.get(i)&&v.get(j);
				}
				public SymmetricBitMatrix transitive_closure() 
				{
					return this;
				}
		};
		return new BitMatrix(n,v.n){
			public boolean get(int i,int j){
				return BitVector.this.get(i)&&v.get(j);
			}
		};
	}
	/**
	 * create a vector of bit by taking as input a vector of input
	 * @param array vector of input
	 * @return vector of bit whose <b>i</b>-th element is true only if array of integer contains the integer <b>i</b>
	 */
	public static BitVector createVectorfromArray(ArrayList<Integer> array)
	{
		return new BitVector(array.get(array.size()-1)){
			public boolean get(int i)
			{
				return array.contains(i);
			}
		};
	}
	/**
	 * transform a Vector of bits in a set of Integer
	 * @return a set of integer that contains all the indexes in which the bitvector is true
	 */
	public Set<Integer> toSet()
	{
		Set<Integer> set=new HashSet<Integer>();
		for(int i=0;i<n;i++)
			if(get(i))
				set.add(i);
		return set;
		
	}
}
