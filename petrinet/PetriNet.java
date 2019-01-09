package petrinet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bitstructures.BitMatrix;
import bitstructures.BitVector;
import bitstructures.BitVectorImpl;
import bitstructures.SymmetricBitMatrix;
import bitstructures.SymmetricBitMatrixImpl;
import bitstructures.UpperTriangularBitMatrixImpl;
import dynamic_petrinet.DynamicNet;
import dynamic_petrinet.NegativeTransition;
import dynamic_petrinet.PositiveTransition;
import representation.Translator;
public class PetriNet extends BitVector{
	/**
	 * adjacency matrix
	 */
	public BitMatrix F;	
	/**
	 * places vector
	 */
	public BitVector P;
	/**
	 * transitions vector
	 */
	public BitVector T;
	/**
	 * relational order established by arcs: reflexive closure of {@link #F}
	 */
	public BitMatrix lessorequals;
	/**
	 * Incoming places of each transitions
	 */
	public BitMatrix Pre;
	/**
	 * (t1,t2) belongs to it if intersection between presets of <b>t1</b> and <b>t2</b> is not empty
	 */
	public BitMatrix strongmutual;
	/**
	 * transitive closure of  {@link #strongmutual}
	 */
	public BitMatrix weakmutual;
	/**
	 * hereditary {@link #strongmutual}
	 */
	public BitMatrix mutual;
	/**
	 * postset:places that doesn't precede anyone
	 */
	public BitVector post;
	public PetriNet(BitMatrix f,BitVector p,BitVector t)
	{
		n=t.size();
		F=f;
		P=p;
		T=t;
		Pre=F.and(P.cartesianProduct(T));
		post=P.and(T.product(F.transpose()).not());
		lessorequals=F.transitive_closure().or(SymmetricBitMatrixImpl.identity(T.size()));
		strongmutual=SymmetricBitMatrixImpl.identity(T.size()).not().and((Pre.transpose().product(Pre)));
		weakmutual=strongmutual.transitive_closure();
		mutual=lessorequals.transpose().product(strongmutual.product(lessorequals));
	}
	private PetriNet(PetriNet pn,SCell scell)
	{
		n=scell.size();
		T=scell.and(pn.T);
		post=T.product(pn.F).and(scell.not());
		P=pn.P.and(scell).or(post);
		//BitVector d=P.union(T);
		BitMatrix domain=this.cartesianProduct(this);
		F=pn.F.and(domain);
		Pre=pn.Pre.and(domain);
		lessorequals=pn.lessorequals.and(domain);
		strongmutual=pn.strongmutual.and(domain);
		weakmutual=pn.weakmutual.and(domain);
		mutual=pn.mutual.and(domain);
	}
	private PetriNet(BitMatrix f, BitVector p,BitVector t,BitVector pset,BitMatrix pre,BitMatrix le,BitMatrix strong)
	{
		n=t.size();
		F=f;
		P=p;
		T=t;
		post=pset;
		Pre=pre;
		Set<Integer> set=T.toSet();
		if(set.isEmpty())
			lessorequals=strongmutual=weakmutual=mutual=new BitMatrix();
		else {
		lessorequals=le;
		//strongmutual=SymmetricMatrix.identity(T.size()).not().and(Pre.transpose().product(Pre));
		strongmutual=strong;
		weakmutual=strongmutual.transitive_closure();
		mutual=lessorequals.transpose().product(strongmutual.product(lessorequals));
		}
	}
	@Override
	public boolean get(int i) {
		return P.get(i)||T.get(i);
	}
	@Override
	public boolean equals(Object object)
	{
		if(super.equals(object))
				return true;
		if(!(object instanceof PetriNet))
			return false;
		PetriNet pn=(PetriNet) object;
		return this.T.equals(pn.T);
	}
	/**
	 * construct a new Petri Net without a place and its descendants
	 * @param place that you want to remove together its descendants
	 * @return transformed Petri Net after reducing
	 */
	public PetriNet minus(int place)
	{
		BitVector d=lessorequals.getRow(place).not();
		BitMatrix domain=d.cartesianProduct(d);
		BitMatrix less=new UpperTriangularBitMatrixImpl(lessorequals.and(domain));
		return new PetriNet(F.and(domain),P.and(d),T.and(d),post.and(d),Pre.and(domain),less.or(SymmetricBitMatrix.identity(less.ncolumns())),new SymmetricBitMatrixImpl(strongmutual.and(domain)));
	}
	/**
	 * generate all the SCells of a Petri Net
	 * @param map_scell contains all the SCells already computed
	 * @param translator contains the information about net elements(example the probability)
	 * @return the set of SCells of a Petri Net
	 */
	public Set<SCell> BC(Map<Set<Integer>,SCell> map_scell,Translator translator)
	{
		if(map_scell==null)
			map_scell=new HashMap<Set<Integer>,SCell>();
		HashSet<SCell> scells=new HashSet<SCell>();
		BitMatrix precedenceorequals=null;
	    BitMatrix equivalence=null;
        BitVector notvisited=new BitVectorImpl(T);	
        for(int i=0;i<notvisited.size();i++)
        	if(notvisited.get(i))
        	{
        		if(precedenceorequals==null) {
        			precedenceorequals=weakmutual.or(lessorequals.or(Pre.transpose())).transitive_closure();
        	    	equivalence=precedenceorequals.and(precedenceorequals.transpose());
        		}
        		Set<Integer> nodes=equivalence.getRow(i).toSet();
        		SCell scell=map_scell.get(nodes);
        		if(scell==null) {
        			scell=new SCell(T.size());
        			scell.nodes=nodes;
        		//forward contains all the incoming nodes(all the nodes that have at least one incoming arcs)
        			scell.preset=scell.and(scell.product(F).not()).toSet();
        			scell.computeMaximalTransations(F, mutual);
        			scell.computeProbability(translator);
        			map_scell.put(nodes, scell);
        		}
        		scells.add(scell);
        		notvisited=notvisited.and(scell.not());
        	}
        return scells;
	}
	/**
	 * generate the corresponding DynamicNet of a Petri Net
	 * @param map_scell SCells already computed
	 * @param map_dynets DynamicNet already computed
	 * @param marked the set of places unavoidable marked
	 * @param translator contains the information about net elements(example the probability)
	 * @return Dynamic net corresponding to the Petri Net
	 * @throws Exception 
	 */
	public DynamicNet toDynamicNet(Map<Set<Integer>,SCell> map_scell,Map<Set<Integer>,DynamicNet> map_dynet,Translator translator,BitVector marked) throws Exception {
		if(map_dynet==null) 
			map_dynet=new HashMap<Set<Integer>,DynamicNet>();
		if(map_scell==null)
			map_scell=new HashMap<Set<Integer>,SCell>();
		Set<Integer> key=T.or(post).toSet();
		if(key.isEmpty())
			return null;
		if(map_dynet.containsKey(key))
			return map_dynet.get(key);
		DynamicNet dynamicnet=new DynamicNet();
		Set<SCell> scells=BC(map_scell,translator);
	    for(SCell c:scells)
	    {	
	    	PetriNet pnet=new PetriNet(this,c);
	    	//arconlypostset contains all the arcs entering one of the scell postset nodes 
	    	BitMatrix arconlytopostset=F.and(new BitVector(F.ncolumns()).cartesianProduct(pnet.post));
	    	for(Transaction t:c.transactions)
	    	{
	    		//t_pos:postset of transaction t
	    		BitVector tpos=t.toBitVector().product(arconlytopostset);
	    		Set<Integer> complement=pnet.post.and(tpos.not()).toSet();
	    		dynamicnet.add(new PositiveTransition(c.preset,tpos.toSet(),complement,t));
	    	}
	    	for(Integer p:c.preset)
	    		if(!marked.get(p)) {
	    			PetriNet pnet_p=pnet.minus(p);
	    			DynamicNet t=pnet_p.toDynamicNet(map_scell,map_dynet,translator,marked);
	    			Set<Integer> complement=pnet.post.and(pnet_p.post.not()).toSet();
	    			dynamicnet.add(new NegativeTransition(p,t,complement));
	    		}
	    }
	    map_dynet.put(key, dynamicnet);
	    return dynamicnet;
	}
}
