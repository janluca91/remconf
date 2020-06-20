package persistent_net;

import java.util.HashSet;
import java.util.Set;

/**
 * represent an Arc from a set of nodes to another set of nodes in static Petri Net 
 * @author Gianluca
 */
public class Arc{
	/**
	 * set of source nodes
	 */
	public Set<Integer> preset;
	/**
	 * set of target nodes
	 */
	public Set<Integer> postset;
	/**
	 * set of arcs following it
	 */
	public void add(Integer a)
	{
		postset.add(a);
	}
	private void check()
	{
		if(preset==null)
			preset=new HashSet<Integer>();
		if(postset==null)
			postset=new HashSet<Integer>();
	}
	public Arc(Set<Integer> preset,Set<Integer> postset) {
		this.preset=preset;
		this.postset=postset;
		check();
	}
	public Arc(Integer preset,Set<Integer> postset) {
		if(preset!=null) {
		this.preset=new HashSet<Integer>();
		this.preset.add(preset);
		}
		this.postset=postset;
		check();
	}
	public Arc(Set<Integer> preset,Integer postset) {
		this.preset=preset;
		if(postset!=null) {
		this.postset=new HashSet<Integer>();
		this.postset.add(postset);
		}
		check();
	}
	public Arc(Integer preset,Integer postset) {
		if(preset!=null) {
			this.preset=new HashSet<Integer>();
			this.preset.add(preset);
		}
		if(postset!=null) {
			this.postset=new HashSet<Integer>();
			this.postset.add(postset);
		}
		check();
	}
	@Override
	public boolean equals(Object o) {
		if(super.equals(o)) return true;
		if(!(o instanceof Arc)) return false;
		Arc a=(Arc) o;
		return postset.equals(a.postset)&&preset.equals(a.preset);
	}
	@Override
	public int hashCode()
	{
			int code=postset.hashCode();
			code*=31;
			code+=preset.hashCode();
			return code;
	}
}
