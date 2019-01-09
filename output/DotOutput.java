package output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bitstructures.BitMatrix;
import persistent_net.Arc;
import petrinet.PetriNet;
import petrinet.SCell;
import representation.NetElement;
import representation.Translator;

public class DotOutput extends FileOutput {
	private static final String PERSISTENT_PLACE = "shape=doublecircle";
	private static final String PERSISTENT_TRANSITION = "shape=box";
	private final static String ASSOCIATION="->";
	private static final String TRANSITION = "box";
   
	public DotOutput(List<NetElement> trans,String filename) throws IOException {
		super(trans,filename);
		suffix=".dot";	
		introduction="digraph G {\r\n" + 
		"	graph [fontname=\"Verdana\",fontsize=12];\r\n" + 
		"	edge [fontname=\"Verdana\",fontsize=9];\r\n" + 
		"	node [fontname=\"Verdana\",fontsize=9,shape=circle];";
		terminal="\n}";
	}
	@Override
	public void print(Integer i) throws IOException {
		bufferedwriter.write(PREFIXNODEID+i.toString());
	}

	@Override
	public void print(Collection<Integer> c) throws IOException {
		boolean first=true;
		bufferedwriter.write("{");
		for(Integer i:c) {
			if(!first)
				bufferedwriter.write(",");
			print(i);
			first=false;
		}
		bufferedwriter.write("}");
	}
	@Override
	public void print(Arc a) throws IOException {
		print(a.preset);
		bufferedwriter.write(ASSOCIATION);
		print(a.postset);
	}
	@Override
	public void print(NetElement e) throws IOException {
		bufferedwriter.write("\n"+PREFIXNODEID+e.getId()+" [label=\""+e.getName());
		if(e.isTransition()) {
			if(e.isPersistent())
				bufferedwriter.write("\","+PERSISTENT_TRANSITION);
			else 
				bufferedwriter.write("\\n"+e.getProbability()+"\",shape="+TRANSITION);
		}
		else {
			bufferedwriter.write("\"");
			if(e.isPersistent())
			bufferedwriter.write(","+PERSISTENT_PLACE);
		}
			bufferedwriter.write("];");
	}
	public void print(PetriNet p) throws IOException
	{
		Map<Set<Integer>,SCell>map_scell=new HashMap<Set<Integer>,SCell>();
		p.BC(map_scell,(Translator) translator);
		print(p,map_scell);
	}
	public void print(PetriNet p,Map<Set<Integer>,SCell> map_scell) throws IOException
	{
		String path2="source/";
		Path path = Paths.get(path2+filename+"/");
		Files.createDirectories(path);
		filewriter=new FileWriter(path2+filename+"/"+filename+suffix);
		bufferedwriter=new BufferedWriter (filewriter);
		bufferedwriter.write(introduction);
		for(int i=0;i<translator.size();i++) {
			bufferedwriter.write("\n"+PREFIXNODEID+i+" [label=\""+translator.get(i).getName()+"\"");
			if(translator.get(i).isTransition())
				bufferedwriter.write(",shape="+TRANSITION);
			bufferedwriter.write("];\n");
		}
		print(p.F);
		int count=0;
		for(SCell sc:map_scell.values())
		{
			bufferedwriter.write("\nsubgraph cluster"+count+"{\nlabel=<C<sub>"+count+"</sub>>;\nstyle=dotted;\n");
			print(sc.nodes);
			bufferedwriter.write("}");
			count++;
		}
		bufferedwriter.write(terminal);
		bufferedwriter.close();
		filewriter.close();
	}
	private void print(BitMatrix f) throws IOException {
		for(Integer i=0;i<f.nrows();i++)
			for(Integer j=0;j<f.ncolumns();j++) 
				if(f.get(i, j)){
				print(i);
				bufferedwriter.write("->");
				print(j);
				bufferedwriter.write(";\n");
			}
	}
}
