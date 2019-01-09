package petrinet;

import java.util.HashSet;

import bitstructures.BitMatrix;
import bitstructures.BitVector;
import representation.Translator;
public class Transaction extends HashSet<Integer>{
	private static final long serialVersionUID = -3009631609931577871L;
	public double probability;
	private int max;
	public Transaction(Transaction a) {
		super(a);
		max=a.max;
		probability=a.probability;
	}
	public Transaction() {
		super();
		max=0;
		probability=0;
	}
	@Override
	public boolean add(Integer e) {
		boolean r= super.add(e);
		if(r&&max<e)
			max=e;
		return r;
	}
	/**
	 * add transition t if it is not in conflict with some transition included in caller transaction
	 * @param t transition to add to the transaction 
	 * @param mutual mutual relation
	 * @return true  if the entry was successful
	 */
	public boolean add(Integer t,BitMatrix mutual) {
		for(Integer iter:this)
			if(mutual.get(iter,t))
				return false;
		return add(t);
	}
	/**
	 * add all the transition t of a transaction in caller transaction, 
	 * only the transition that are not in conflict with some transition in caller transaction are added
	 * @param transaction set of transition that you want to add
	 * @param mutual mutual relation
	 * @return true if at least a transition is added.
	 */
	public boolean add(Transaction transaction,BitMatrix mutual)
	{
		//check if at least one element is inserted
		boolean insert=false;
		for(Integer t:transaction)
			insert|=add(t,mutual);
		return insert;
	}
	/**
	 * Translate a set of integer in a vector of bits
	 * @return a bit vector where i-th element is true if the set contains integer i
	 */
	public BitVector toBitVector()
	{
		return new BitVector(max+1) {
			public boolean get(int i)
			{
				return Transaction.this.contains(i);
			}
		};
	}
	protected double computeProbability(Translator translator) {
		probability=1;
		for(Integer t:this)
					probability*=translator.get(t).getProbability();
		return probability;
	}
}
