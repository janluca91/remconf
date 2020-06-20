package output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import persistent_net.Arc;
import representation.NetElement;

public abstract class FileOutput extends Output {
	public final static String PATH="target/";
	protected final static String PREFIXNODEID="c";
	protected FileWriter filewriter;
	protected BufferedWriter bufferedwriter;
	protected final String filename;
	protected String suffix;
	protected  String introduction;
	protected  String terminal;
	public FileOutput(List<NetElement> trans,String fname)
	{
		super(trans);
		filename=fname;
		
	}
	@Override
	public void print(Integer i) throws IOException {
	}

	@Override
	public void print(Collection<Integer> c) throws IOException {
	}
	@Override
	public void print(Set<Arc> arcs) throws IOException {
		if(arcs==null)
			return;
		Path path = Paths.get(PATH+filename+"/");
		Files.createDirectories(path);
		filewriter=new FileWriter(PATH+filename+"/"+filename+suffix);
		bufferedwriter=new BufferedWriter (filewriter);
		bufferedwriter.write(introduction);
		for(NetElement netElement: translator) {
			print(netElement);
			bufferedwriter.write("\n");
		}
		for(Arc a:arcs){
			print(a);
			bufferedwriter.write("\n");
		}
		bufferedwriter.write(terminal);
		bufferedwriter.close();
		filewriter.close();
	}
}

