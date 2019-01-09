package representation;

public abstract class NetElement {
	protected String id, name;
	protected Double x,y;
	protected boolean persistent;
	public NetElement(String id,String name,boolean per)
	{
		super();
		this.id=id;
		this.name=name;
		persistent=per;
		x=y=null;
	}
	/**
	 * 
	 * @return identifier of net element
	 */
	public String getId() {
		return id;
	}
	/**
	 * change the indentifier of net element
	 * @param id the new identifier 
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the name of net element
	 */
	public String getName() {
		return name;
	}
	/**
	 * change the name of net element
	 * @param id the new identifier
	 */
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean equals(Object o)
	{
		if(super.equals(o))
			return true;
		if(!(o instanceof NetElement))
			return false;
		NetElement object=(NetElement) o;
		return object.id.compareTo(this.id)==0;
	}
	@Override
	public int hashCode()
	{
		return this.id.hashCode();
	}
	public boolean isTransition()
	{
		return false;
	}
	public boolean isPlace(){
		return false;
	}
	public boolean isPersistent() {
		return persistent;
	}
	public double getProbability() {
		return 0;
	}
	/**
	 * refresh the probability of executing net element by multiplying the actual probability with passed-as-argument probability
	 * @param prob probability to multiply
	 */
	public void addProbability(Double prob) {
		
	}
	public boolean isMarked() {
		return false;
	}
	public void setTokens(int t) {
		
	}
	public int getTokens() {
		return 0;
	}
	public void setPosition(double x, double y) {
		this.x=x;
		this.y=y;	
	}
	public int getX() {
		if(x==null)
			return 0;
		return x.intValue();
	}
	public int getY() {
		if(y==null)
			return 0;
		return y.intValue();
	}
}
