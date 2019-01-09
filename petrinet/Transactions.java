package petrinet;
import java.util.HashSet;
import java.util.Iterator;

import bitstructures.BitMatrix;

public class Transactions extends HashSet<Transaction> {

	public Transactions(Transactions a) {
		super();
		for(Transaction t:a)
			add(new Transaction(t));
	}
	public Transactions(Transaction t)
	{
		super();
		add(new Transaction(t));
	}
	
	public Transactions() {
		super();
	}
	private static final long serialVersionUID = 7914902948925502368L;
	/**
	 *add a transaction in a set of transaction, the following cases may occur:
	 *<br>1.new transaction is a subset of some transaction then it is not added
	 *<br>2. new transaction include some transaction then new transaction substitute the latter
	 *@param trans transaction to add to the set of transactions 
	 */
	public boolean add(Transaction trans){
		Iterator<Transaction> iterator=this.iterator();
		while(iterator.hasNext()) {
			Transaction t=iterator.next();
			if(t.containsAll(trans))
				return false;
			else if(trans.containsAll(t)) 
				iterator.remove();
		}
		return super.add(trans);
	}
	/**
	 * product between set of transactions and a transaction: 
	 * for each transaction in the set, all the no-conflict transition in <b>trans</b> are added to it  
	 * @param trans transaction on which you want to apply the product
	 * @param mutual mutual relation
	 * @return the resulting set of transactions
	 */
	private Transactions product(Transaction trans,BitMatrix mutual) {
		Transactions res=new Transactions(this);
		for(Transaction t:res)
			t.add(trans,mutual);
		return res;
	}
	/**
	 * for each transaction of the set, add to it one by one all the transactions passed as arguments   
	 * add a transaction <b>a</b> to a transaction <b>b</b> means adding all the no-conflicting transitions of <b>b</b> in <b>a</b>
	 * @param transactions transactions on which you want to apply the product
	 * @param mutual mutual relation
	 * @return set of transactions that is the result of the product
	 */
	private Transactions product(Transactions transactions,BitMatrix mutual) {
		if(transactions.isEmpty())
			return new Transactions(this);
		Transactions result= new Transactions();
		for(Transaction t:transactions)
			result.addAll(this.product(t, mutual));
		
		return result;
	}
	/**
	 * concatenate two set of transactions means that for each transaction of a set , it can be merged with another transaction of other set because:<br>
	 * 1-a transaction follow the other one
	 * 2-the two transaction are potentially concurrent(some transition can be in conflict with some of another transaction).
	 * the adding is done by taking into account the conflict between transitions
	 * @param transactions the set of transactions
	 * @param mutual mutual relation
	 * @return set of the transactions
	 */
	public Transactions concatenate(Transactions transactions,BitMatrix mutual){
		if(transactions==null)
			return this;
		Transactions result=transactions.product(this,mutual);
		result.addAll(this.product(transactions, mutual));
		return result;
	}
}
