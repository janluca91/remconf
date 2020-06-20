package output;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import persistent_net.Arc;
import representation.NetElement;

public abstract class Output {

	protected List<NetElement> translator;
	/**
	 * 
	 * @param trans relate integer id with correponding net element 
	 */
	public  Output(List<NetElement> trans)
	{
		translator=trans;
	}
	public abstract void print(Integer i) throws IOException;
	public abstract void print(NetElement e) throws IOException;
	public abstract void print(Collection<Integer> c) throws IOException;
	//public abstract void print(PositiveTransition c);
	//public abstract void print(NegativeTransition c);
	//public abstract void print(SCell sCell);
	//public abstract void print(Transactions trans);
	public abstract void print(Arc a) throws IOException;
	public abstract void print(Set<Arc> arcs) throws IOException;
}
