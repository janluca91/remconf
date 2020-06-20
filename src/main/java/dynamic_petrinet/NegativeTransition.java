package dynamic_petrinet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import output.OutputConsole;
import persistent_net.Arc;
import persistent_net.Bridge;
public class NegativeTransition extends DynamicTransition {
	/**
	 * negative place is the preset of a negative transition
	 */
	public Integer preset;
	/**
	 * dynamic net as a postset of negative transition
	 */
	public DynamicNet net;
	public NegativeTransition(Integer preset,DynamicNet net,Set<Integer> negativepostset) throws Exception {
		super(negativepostset);
		this.preset=preset;
		this.net=net;
		if(preset==null)
			throw new Exception("preset  cannot be null");
	}
	@Override
	public void print(OutputConsole output) {
		output.print(this);
	}
	@Override
	public Set<Arc> toStatic( Bridge bridge,Arc start,Map<DynamicTransition,Integer> map_dt) {
		Set<Arc> arcs= new HashSet<>();
		Integer pt=map_dt.get(this);
		if(pt!=null)
		{
			if(start!=null)
				start.postset.add(pt);
			return arcs;
		}
		if(negativepostset.isEmpty()&&net==null)
			return arcs;
		Integer p=bridge.persistentPlaceFrom(preset);
		Integer t=bridge.generateAuxiliaryTransitionFrom(p);
		Arc	b=new Arc(t,bridge.persistentPlacesFrom(negativepostset));
		Arc a=new Arc(p,b.preset);
		if(start!=null) {
			pt=bridge.generateAuxiliaryPlaceFrom(t);
			a.preset.add(pt);
			start.postset.add(pt);
			map_dt.put(this, pt);
		}
		arcs.add(b);
		arcs.add(a);
		if(net!=null)
			arcs.addAll(net.toStatic(bridge,b,map_dt));
		return arcs;
	}
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)) return true;
		if(!(obj instanceof NegativeTransition))
			return false;
		NegativeTransition t=(NegativeTransition) obj;
		if(!(preset.equals(t.preset)&&negativepostset.equals(t.negativepostset)))
			return false;
		if(net!=null)
			return net.equals(t.net);
		return t.net==null;
	}
	@Override
	public int hashCode()
	{
			int code=(preset.hashCode()*31+negativepostset.hashCode());
			if(net!=null)
				code=code*31+net.hashCode();
			return code;
	}
	
}
