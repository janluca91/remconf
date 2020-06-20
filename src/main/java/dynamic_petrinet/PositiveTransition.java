package dynamic_petrinet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import output.OutputConsole;
import persistent_net.Arc;
import persistent_net.Bridge;
import petrinet.Transaction;
public class PositiveTransition extends DynamicTransition {
	/**
	 * set of places that are preset of positive transition
	 */
	public Set<Integer> preset;
	/**
	 * the set of not persistent place in postset
	 */
	public Set<Integer> positivepostset;
	/**
	 * transaction from which this dynamic transaction derives
	 */
	public Transaction transaction;
	public PositiveTransition(Set<Integer> preset,Set<Integer> positivepostset,Set<Integer> negativepostset,Transaction labels) throws Exception
	{
		super(negativepostset);
		this.preset=preset;
		this.positivepostset=positivepostset;
		this.transaction=labels;
		if(preset==null||preset.isEmpty()||positivepostset==null)
		throw new Exception("preset and positivepostset cannot be null and preset must contain at least an element");
	}
	@Override
	public void print(OutputConsole output) {
		output.print(this);
	}
	

	@Override
	public Set<Arc> toStatic(Bridge bridge,Arc start,Map<DynamicTransition,Integer> map_dt) {
		Set<Arc> arcs= new HashSet<>();
		Integer pt=map_dt.get(this);
		if(pt!=null) {
			if(start!=null)
				start.postset.add(pt);
			return arcs;
		}
		Integer t=bridge.generateTransitionFrom(preset,transaction);
		Arc b=new Arc(t,bridge.importsAll(positivepostset));
		b.postset.addAll(bridge.persistentPlacesFrom(negativepostset));
		Arc a=new Arc(bridge.importsAll(preset),b.preset);
		if(start!=null) {
			pt=bridge.generateAuxiliaryPlaceFrom(t);
			start.postset.add(pt);
			a.preset.add(pt);
		}
		arcs.add(a);
		arcs.add(b);
		return arcs;
	}
	public double getProbability()
	{
		return transaction.probability;
	}
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj)||(obj instanceof PositiveTransition)&&
				((PositiveTransition)obj).transaction.equals(transaction);
	}
	@Override
	public int hashCode()
	{
		if(transaction!=null)
		return transaction.hashCode();
		else return super.hashCode();
		
	}
	
}
