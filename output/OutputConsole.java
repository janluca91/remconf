package output;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import dynamic_petrinet.NegativeTransition;
import dynamic_petrinet.PositiveTransition;
import persistent_net.Arc;
import petrinet.SCell;
import petrinet.Transaction;
import petrinet.Transactions;
import representation.NetElement;

public class OutputConsole extends Output {

	public OutputConsole(List<NetElement> translator) {
		super(translator);
	}	
	@Override
	public void print(Integer i) {
		System.out.print(translator.get(i).getName());
		
	}
	@Override
	public void print(Collection<Integer> c) {
		boolean first=true;
		if(c==null||c.size()<=0)
			return ;
		System.out.print("{");
		for(Integer i:c) {
			if(!first)
				System.out.print(",");
			print(i);
			first=false;
		}
		System.out.print("}");
		
	}
	public void print(Transactions transactions) {
		for(Transaction t:transactions) {
			System.out.print("{");
			print(t);
			System.out.println("}");
		}
	}
	
	public void print(NegativeTransition t) {
		System.out.print("not(");
		print(t.preset);
		System.out.print(")-->[not(");
		print(t.negativepostset);
		System.out.print("),");
		if(t.net!=null)
			t.net.print(this);
		System.out.println("]");
	}
	public void print(PositiveTransition c) {
		print(c.preset);
		System.out.print("-");
		print(c.transaction);
		System.out.print("->[");
		print(c.positivepostset);
		System.out.print(",not(");
		print(c.negativepostset);
		System.out.print(")]");
		System.out.println("with probability: "+c.getProbability());
		
	}
	public void print(SCell sCell) {
		System.out.println("nodes:");
    	print(sCell.nodes);
    	System.out.println("\nPreset:");
    	print(sCell.preset);
    	System.out.println("\nmaximal Transactions:");
    	print(sCell.transactions);
    	}
	public void print(Arc a) {
		if(a==null)
			return;
		print(a.preset);
		System.out.println("-->[");
		print(a.postset);
	}
	@Override
	public void print(Set<Arc> arcs) {
		for(Arc a:arcs)
			print(a);
		
	}
	@Override
	public void print(NetElement e) throws IOException {
		// TODO Auto-generated method stub
		
	}
		
}
