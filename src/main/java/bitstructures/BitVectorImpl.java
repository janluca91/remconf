package bitstructures;
import java.util.BitSet;
/**
 * Abstracts a representation of Vector of bits. It allocate memory space for storing bits of vector<br>
 * @author Gianluca
 *
 */
public class BitVectorImpl extends BitVector {

	/**
	 * Instantiate a vector of n bits by allocating memory space for storing it
	 * @param n size of vector
	 */
	public BitVectorImpl(int n)
	{
		this.n=n;
		bitset=new BitSet(n);
	}
	/**
	 * instantiate a vector of n bits and take values from another vector as its intial values
	 * @param v vector from which you take initial values
	 * @param n size of vector
	 */
	public BitVectorImpl(BitVector v,int n)
	{
		this.n=n;
		bitset=new BitSet(n);
		for(int k=0;k<v.n;k++)
			set(k, v.get(k));
	}
	/**
	 * copying constructor
	 * @param v vector of bit that you want to clone
	 */
	public BitVectorImpl(BitVector v)
	{
		this.n=v.n;
		bitset=new BitSet(n);
		for(int k=0;k<v.n;k++)
			set(k, v.get(k));
	}
	/*
	 * (non-Javadoc)
	 * @see matrix.BitVector#get(int)
	 */
	public boolean get(int i)
	{
		return super.get(i)&&bitset.get(i);
	}
	/**
	 * set the i-th bit of array according value of the parameter
	 * @param i index
	 * @param value if the bit that you want to insert is 1 then it must be <b>true</b>, otherwise it must be <b>false</b>
	 */
	public void set(int i,boolean value)
	{
		if(super.get(i))
			bitset.set(i, value);
	}
}
