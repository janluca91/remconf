package representation;

public class Transition extends NetElement {
	protected double probability;
	public double getProbability() {
		return probability;
	}	
	public Transition(String id,String name,boolean per)
	{
		super(id,name,per);
		probability=1;
	}
	public boolean isTransition()
	{
		return true;
	}
	public void addProbability(Double prob) {
		if(prob!=null)
			probability*=prob;
	}
}
