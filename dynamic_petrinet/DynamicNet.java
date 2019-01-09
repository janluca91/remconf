package dynamic_petrinet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import output.OutputConsole;
import persistent_net.Arc;
import persistent_net.Bridge;
/**
 * Represent dynamic Net
 * @author Gianluca
 *
 */
public class DynamicNet extends HashSet<DynamicTransition>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826796772854824526L;
	private static int counter=0;
	private int id;
	/**
	 * set of dynamic transitions
	 */
	public DynamicNet() {
		super();
		id=counter++;
	}
	public void print(OutputConsole printer)
	{
		for(DynamicTransition t:this){
			System.out.println();
			t.print(printer);
		}
	}
	/**
	 * translate from dynamic net to static net
	 * @param bridge for more information see {@link Bridge}
	 * @param start arc from which this dynamic net is generated, then each transition is triggered form this arc 
	 * @param map_dt mappig between a dynamic transition and its corresponding id in the new net.
	 * @return set of arcs that represent the static Net
	 */
	public Set<Arc> toStatic(Bridge bridge,Arc start,Map<DynamicTransition,Integer> map_dt){
		Set<Arc> arcs=new HashSet<Arc>();
		for(DynamicTransition t:this) 
			arcs.addAll(t.toStatic(bridge,start,map_dt));
		return arcs;
		
	}
	@Override
	public boolean equals(Object obj) {
		return this==obj||(obj instanceof DynamicNet&&((DynamicNet) obj).id==id);
	}
	@Override
	public int hashCode()
	{
		return id;
	}
}
