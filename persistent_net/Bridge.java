package persistent_net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import petrinet.Transaction;
import representation.NetElement;
import representation.Place;
import representation.Transition;

/**
 * Useful when in the new net you create some elements and retain someone from source net
 * Link between the old dictionary of net elements and the new dictionary
 * @author Gianluca
 *
 */
public class Bridge {
	/**
	 * Net Elements in source petri net
	 */
	public List<NetElement> source;
	/**
	 * Net Elements in target petri net
	 */
	public List<NetElement> target;
	
	/**
	 * map the translation between source and target
	 * mapping[X]=Y means that X is mapped to Y in the new Dictionary
	 */
	private Map<Object,Integer> mapping; 
	
	/**
	 * for each id p of a net element. auxiliaries[p] gives the auxiliary id of net element related to p.  
	 */
	private Map<Integer,Integer> auxiliaries;
	/**
	 * @param source the old dictionary
	 * @throws Exception 
	 */
	public  Bridge(List<NetElement> source) throws Exception {
		if(source==null)
			throw new Exception("the source cannot be null");
		this.source=source;
		target=new ArrayList<NetElement>();
		mapping=new HashMap<Object,Integer>();
		auxiliaries=new HashMap<Integer,Integer>();
	}
	/**
	 * @param element net element that you want to add
	 * @return id of element
	 */
	private Integer add(NetElement element)
	{
		boolean ok=target.add(element);
		if(ok) {
			Integer id=target.size()-1;
			element.setId(id.toString());
			return id;
		}
		return null;
	}
	/**
	 * When you want to retain a element of old net with a certain id and to insert it into the new one
	 * a changing of id is necessary
	 * @param i id of old element that you want to retain and to insert  
	 * @return id of inserted element
	 */
	public Integer imports(Integer i) {
		if(mapping.containsKey(i))
			return mapping.get(i);
		Integer r=add(source.get(i));
		if(r!=null)
			mapping.put(i,r);
		return r;
	}
	/**
	 * When you want to retain a set of elements from old net with a certain ids and to insert them into the new one
	 * @param indexes the set of indexes of elements that you want to retain and to insert
	 * @return set of id of inserted elements
	 */
	public Set<Integer> importsAll(Set<Integer> indexes)
	{
		Set<Integer> res=new HashSet<Integer>();
		for(Integer i:indexes) {
			Integer p=imports(i);
			if(p!=null)
				res.add(p);
		}
		return res;
	}
	/**
	 * generate a persistent place that is counterpart of net element of old net with a certain id
	 * @param index the id of the net element of old net from which you want to create its counterpart
	 * @return
	 */
	public Integer persistentPlaceFrom(Integer index) {
		Integer i=imports(index);
		if(i==null)
			return null;
		if(auxiliaries.containsKey(i))
			return auxiliaries.get(i);
		Integer r=add(new Place("",source.get(index).getName(),true));
		if(r!=null)
			auxiliaries.put(i,r);
		return r;
	}
	/**
	 * generate persistent places that are counterpart of net elements of old net with id contained in a set of integer
	 * @param indexes the ids of the net elements of old net from which you want to create their counterpart
	 * @return
	 */
	public Set<Integer> persistentPlacesFrom(Set<Integer> indexes) {
		Set<Integer> places=new HashSet<Integer>();
		for(Integer i:indexes)
		{
			Integer p=persistentPlaceFrom(i);
			if(p!=null)
				places.add(p);
		}
		return places;
	}
	/**
	 * generate transition that include some transitions of old net 
	 * @param preset set of incoming places of that transaction
	 * @param transitions the ids of transitions of old net that you want to include in the new transition
	 * @return id of the new transition in new net
	 */
	public Integer generateTransitionFrom(Set<Integer> preset,Transaction transitions) {	
		if(transitions==null||transitions.isEmpty())
			return null;
		Set<Integer> key=new HashSet<Integer>(transitions);
		key.addAll(preset);
		if(mapping.containsKey(key))
			return mapping.get(key);
		Transition t=new Transition("","",false);
		for(Integer i:transitions) {
			t.setName(t.getName()+source.get(i).getName());
		}
		t.addProbability(transitions.probability);
		Integer result=add(t);
		if(result!=null)
			mapping.put(key,result);
		return result;
	}
	/**
	 * generate a new place that is related to a transition in new net
	 * @param index id of transition of new net from which the new place will be related to 
	 * @return id of the new place
	 */
	public Integer generateAuxiliaryPlaceFrom(Integer index) {
		if(index==null) 
			return null;
		if(auxiliaries.containsKey(index))
			return auxiliaries.get(index);
		Integer r=add(new Place("","p"+target.get(index).getName(),true));
		if(r!=null)
			auxiliaries.put(index, r);
		return r;
	}
	/**
	 * generate a new transition that is related to a place in new net
	 * @param index id of place of new net from which the new transition will be related to 
	 * @return index of new transition
	 */
	public Integer generateAuxiliaryTransitionFrom(Integer index)
	{
		if(index==null||index>=target.size())
			return null;
		String name;
		Integer aux=auxiliaries.get(index);
		if(aux==null)
			name="t"+target.get(index).getName();
		else 
			name=target.get(aux).getName().substring(0, 1)+"'"+target.get(aux).getName().substring(1);
		Integer i=add(new Transition("",name,true));
		if(i!=null)
			auxiliaries.put(index,i);
		return i;
	}
	public void setPositionsFrom(String filename)
	{
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(filename);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            Pattern p = Pattern.compile("c[0-9]+\\t\\t \\[");
            Pattern p2=Pattern.compile("pos=\"[0-9]*\\.?[0-9]*\\,[0-9]*\\.?[0-9]*\"");
            Pattern p3=Pattern.compile("c[0-9]+");
            Matcher m;
            while((line = bufferedReader.readLine()) != null) {
            	m=p.matcher(line);
            	if(m.find()) {
            		m=p3.matcher(line);
            		m.find();
            		Integer id=Integer.parseInt(line.substring(m.start()+1,m.end()));
            		do {
            			m=p2.matcher(line);
            		}while(!m.find()&&(line=bufferedReader.readLine())!=null);
            		int sep=line.indexOf(',', m.start());
            		double x=Double.parseDouble(line.substring(m.start()+5,sep));
            		double y=Double.parseDouble(line.substring(sep+1,m.end()-1));
            		target.get(id).setPosition(x,y);
            	}
            }
            bufferedReader.close();
            fileReader.close();
	} catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + filename + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
		}
	}

}
