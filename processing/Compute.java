package processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.lang.Process;
import bitstructures.BitMatrix;
import bitstructures.BitVector;
import bitstructures.BitVectorImpl;
import dynamic_petrinet.DynamicNet;
import dynamic_petrinet.DynamicTransition;
import output.DotOutput;
import output.FileOutput;
import output.PnmlOutput;
import parser.PNMLParser;
import persistent_net.Arc;
import persistent_net.Bridge;
import petrinet.PetriNet;
import petrinet.SCell;
import representation.Translator;

public class Compute {
	public static String DOT_PATH_FILE="dot_path.txt";
	public static String SUFFIXGRAPHIC="graphics.dot";
	public static String formats[]= {"svg"};
	public Compute() {
		// TODO Auto-generated constructor stub
	}
	public static void compute(InputStreamReader args) throws Exception {
		
	}
	public static void main(String[] args) throws Exception {
	      //Get the file name
		if (args.length == 0){
			System.err.println("USAGE: java SAX2Namespace <filename>");
	        return;
	    }
		Instant startTime=Instant.now();
		String filename=args[0];
		//RETRIEVE GRAPH FROM PARSER--------------------
		PNMLParser parser=new PNMLParser(filename);
		parser.parse();
		BitMatrix F= parser.getF();
		Translator translator=parser.getTranslator(); 
	    //construct vectors T and P
		BitVector P=new BitVectorImpl(translator.size());
		for(int i=0;i<P.size();i++)
			P.set(i,translator.get(i).isPlace());
		BitVector T=P.not();
		
		//CHECKING IF THE NET RESPECTS OUR SPECIFICATION------------
	    //check if every place have at maximum one incoming arcs
	    ArrayList<Integer> n_inarcs=F.transpose().scalar_product(P.or(T));
	    for(int i=0;i<n_inarcs.size();i++)
	    	if(P.get(i)&&n_inarcs.get(i)>1)
	        	throw new Exception ("exist a place that have more than one incoming arcs");
	        else if(T.get(i)&&n_inarcs.get(i)<1)
	        	throw new Exception ("exist a transition that have no incoming arcs");
	    
	    
	    //COMPUTE PETRI NET
	    PetriNet pn=new PetriNet(F,P,T);
	    for(int i=0;i<pn.mutual.ncolumns();i++)
	    	if(pn.mutual.get(i, i))
	    		throw new Exception("self conflict");
	       
	       //OPTIMIZATION PART-----------------------------
	       //remove the places that they will be never marked
	    for(int i=0;i<n_inarcs.size();i++)
	    	if(n_inarcs.get(i)==0&&!translator.get(i).isMarked())
	    		pn=pn.minus(i);
	    
	    //INITIALIZATION VARIABLE FOR OUTPUT
	    BufferedReader buffer = new BufferedReader(new FileReader(new File(DOT_PATH_FILE))); 
 		String dot_path=buffer.readLine();
 		buffer.close();
 		File file_dot=new File(dot_path);
 		File file_input,file_output;
 		String cmd;
 		Process p;
 		String doublequotes=System.getProperty("os.name").contains("Windows")?"\"":"";
 		Map<Set<Integer>,SCell> mapscell=new HashMap<Set<Integer>,SCell>();
	    ArrayList<Integer> n_outarcs=pn.F.scalar_product(pn.T);
	    BitVectorImpl unavoidable_marked=new BitVectorImpl(pn.P);  
	  //if you find a place that have more than 1 arc, you remove the descendant of its from unavoidable marked places 
	    for(int i=0;i<unavoidable_marked.size();i++)
	    	if(unavoidable_marked.get(i)&&n_outarcs.get(i)>1) 
	    		for(int j=i+1;j<pn.lessorequals.ncolumns();j++)
	    			   if(pn.lessorequals.get(i, j))
	    				   unavoidable_marked.set(j, false);
	    //PRINT PETRI NET with scells
	    pn.BC(mapscell, translator);
	    DotOutput petrinetprint=new DotOutput(translator,filename);
	    petrinetprint.print(pn,mapscell);
	    file_input=new File("source/"+filename+"/"+filename+".dot");
		for(int i=0;i<formats.length;i++) {
	 		file_output=new File("source/"+filename+"/"+filename+"."+formats[i]);
	 		cmd= doublequotes+file_dot.getAbsolutePath()+doublequotes+" "+file_input.getAbsolutePath()+" -T"+formats[i]+" -o "+file_output.getAbsolutePath();
	 		p=Runtime.getRuntime().exec(cmd);
	 		p.waitFor();
	 	} 
	   
	    //COMPUTE
	    Map<Set<Integer>,DynamicNet> mapdynet=new HashMap<Set<Integer>,DynamicNet>();
	    DynamicNet dynamicnet=pn.toDynamicNet(mapscell,mapdynet,translator,unavoidable_marked);
	    Bridge bridge=new Bridge(translator);
	    Set<Arc> staticPetriNet=null;
	    if(dynamicnet!=null)
	    	staticPetriNet=dynamicnet.toStatic(bridge,null,new HashMap<DynamicTransition,Integer>());
	    Instant endTime = Instant.now();
	    System.out.println(Duration.between(startTime, endTime).toMillis()+"millisecs");   
	    if(staticPetriNet==null) {
	    	System.out.println("you cannot create a static Petri Net from the given original net: try to add tokens");
	    	return;
	    }
	    //OUTPUT PART------------------------
	    FileOutput fileoutput;
	    fileoutput=new DotOutput(bridge.target,filename);
	    fileoutput.print(staticPetriNet);
	 	file_input=new File("target/"+filename+"/"+filename+".dot");
	 	file_output=new File("target/"+filename+"/"+filename+SUFFIXGRAPHIC);
	 	cmd=doublequotes+file_dot.getAbsolutePath()+doublequotes+" "+file_input.getAbsolutePath()+" -Tdot  -Grankdir=BT -o "+file_output.getAbsolutePath();
	 	p=Runtime.getRuntime().exec(cmd);
	 	p.waitFor();
	 	for(int i=0;i<formats.length;i++) {
	 		file_output=new File("target/"+filename+"/"+filename+"."+formats[i]);
	 		cmd= doublequotes+file_dot.getAbsolutePath()+doublequotes+" "+file_input.getAbsolutePath()+" -T"+formats[i]+" -o "+file_output.getAbsolutePath();
	 		p=Runtime.getRuntime().exec(cmd);
	 		p.waitFor();
	 	} 
	 	bridge.setPositionsFrom(FileOutput.PATH+filename+"/"+filename+SUFFIXGRAPHIC);
		fileoutput=new PnmlOutput(bridge.target,filename);
		fileoutput.print(staticPetriNet);
	}
}
