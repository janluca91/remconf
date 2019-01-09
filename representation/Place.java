package representation;

public class Place extends NetElement{
	private Integer tokens;
	public Place(String id,String name,boolean per)
	{
		super(id,name,per);
		tokens=null;
	}
	public boolean isPlace(){
		return true;
	}
	@Override
	public boolean isMarked()
	{
		return tokens!=null&&tokens>0;
	}
	public void setTokens(int t)
	{
		tokens=t;
	}
	@Override
	public int getTokens()
	{
		if(tokens==null)
			return 0;
		else return tokens;
	}
	public String getName()
	{
		return super.getName();
	}
}
