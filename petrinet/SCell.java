package petrinet;

import java.util.HashSet;
import java.util.Set;

import bitstructures.BitMatrix;
import bitstructures.BitVector;
import output.OutputConsole;
import representation.Translator;
public class SCell extends BitVector{
	public Set<Integer> nodes;
	public Set<Integer> preset;
	public Transactions transactions;
	public SCell(int n)
	{
		this.n=n;
		nodes=new HashSet<Integer>();
		preset=new HashSet<Integer>();	
		transactions=null;
	}
	/**
	 * add a node to the set of nodes that compose s-cell
	 * @param node integer to add
	 */
	public void add(int node)
	{
		nodes.add(node);
	}
	@Override
	public boolean get(int i) {
		return nodes.contains(i);
	}
	/**
	 * compute maximal transactions starting from <b>index</b>
	 * @return the set of maximal transactions from <b>index</b>
	 */
	private Transactions compute(int index,BitMatrix F,BitMatrix mutual,boolean transition)
	{
		if(!nodes.contains(index)) 
			return null;
		Transactions result =new Transactions();
		if(transition){
			Transaction t=new Transaction();
			t.add(index);
			result.add(t);
			for(int i=index+1;i<F.ncolumns();i++)
				if(F.get(index, i)) //then i is a place 
					//all the transactions coming from places i can be concatenate together in a transaction generated from transition i 
					result=result.concatenate(compute(i,F,mutual,false),mutual);
		}
		else 
			for(int i=index+1;i<F.ncolumns();i++)
				if(F.get(index, i)) //then i is a transition
					//each transaction coming from a transition i is added to the set of available transactions of place index
					result.addAll(compute(i,F,mutual,true));
		return result;
	}
	/**
	 * @param F adjacency matrix
	 * @param mutual mutual relation
	 * compute maximal transactions in SCell
	 * @return the set of maximal transactions in SCell
	 */
	public void computeMaximalTransations(BitMatrix F,BitMatrix mutual)
	{
		transactions=new Transactions();
		for(Integer p:preset) 
			transactions=transactions.concatenate(compute(p,F,mutual,false), mutual);
	}
	/**
	 * Compute the probability of each transaction by normalizing respect to total probability of transactions of SCell
	 * @param translator matrix that contains all the probabilities of each arc
	 */
	public void computeProbability(Translator translator)
	{
		double total=0;
		for(Transaction t:transactions)
			total+=t.computeProbability(translator);
		for(Transaction t:transactions)
			t.probability/=total;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(super.equals(obj))
			return true;
		if(!(obj instanceof SCell))
			return false;
		SCell s=(SCell) obj;
		return nodes.equals(s.nodes);
	}
	@Override
	public int hashCode()
	{
		return nodes.hashCode();
	}
	public void print(OutputConsole output) {
    		output.print(this);
    	}
}
