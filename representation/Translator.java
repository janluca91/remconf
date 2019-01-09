package representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Translator extends ArrayList<NetElement>{
	private static final long serialVersionUID = -4088561085120305505L;
	protected Map<String,Integer> idToInteger;
	public Translator()
	{
		super();
		idToInteger=new HashMap<String,Integer>();
	}
	@Override
	public boolean add(NetElement e)
	{
		if(idToInteger.get(e.getId())!=null)
			return false;
		boolean ok=super.add(e);
		if(ok)
			idToInteger.put(e.getId(),size()-1);
		return ok;
	}
	@Override
	public NetElement remove(int index)
	{
		NetElement e=super.remove(index);
		idToInteger.remove(e.getId());
		return e;
	}
	@Override
	public boolean remove(Object obj)
	{
		if(!(obj instanceof NetElement))
			return false;
		NetElement e= (NetElement) obj;
		Integer index=idToInteger.remove(e.getId());
		if(index!=null) {
			super.remove(index.intValue());
		 	return true;
		 }
		 return false;
	}
	/**
	 * 
	 * @param s id of net element
	 * @return position in translator array of net element with id <b>s</b>
	 */
	public int get(String s)
	{
		Integer i=idToInteger.get(s);
		if(i==null)
			return -1;
		return i;
	}
	@Override
	public int indexOf(Object obj)
	{
		if(!(obj instanceof NetElement))
			return -1;
		NetElement e= (NetElement) obj;
		return get(e.getId());
	}
	/**
	 * do permutation according a permutation vector v<br>
	 * array[v[i]]=array[i]
	 * @param permutation at i-th position contains the index where the element in position i must go
	 */
	public void doPermutation(List<Integer> permutation)
	{
		List<NetElement> old=new ArrayList<NetElement>(this);
		for(int i=0;i<size();i++)
			set(permutation.get(i),old.get(i));
		Iterator<Entry<String, Integer>> iterator=idToInteger.entrySet().iterator();
		while(iterator.hasNext())
		{
			   Entry<String,Integer> entry=iterator.next();
			   entry.setValue(permutation.get(entry.getValue()));
		}
	}
	
}
