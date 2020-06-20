package dynamic_petrinet;

import java.util.Map;
import java.util.Set;

import output.OutputConsole;
import persistent_net.Arc;
import persistent_net.Bridge;

public abstract class DynamicTransition {
	/**
	 * every dynamic transition can have persistent places in postset
	 */
	public Set<Integer> negativepostset;
	public DynamicTransition() {
	}
	public DynamicTransition(Set<Integer> negpost) throws Exception {
		this();
		if(negpost==null) throw new Exception("negativepostset cannot be null");
		negativepostset=negpost;
	}
	public  abstract void print(OutputConsole printer);
	/**
	 * translate from dynamic net to static net
	 * @param bridge for more information see {@link Bridge}
	 * @param start arc that follows this transition
	 * @return arc the translation in arc of this dynamic transition
	 */
	public abstract Set<Arc> toStatic( Bridge bridge,Arc start,Map<DynamicTransition,Integer> map_dt );
}
