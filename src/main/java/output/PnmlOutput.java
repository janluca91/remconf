package output;

import java.io.IOException;
import java.util.List;

import persistent_net.Arc;
import representation.NetElement;

public class PnmlOutput extends FileOutput{
	int counter_arcs;
	public PnmlOutput(List<NetElement> trans,String filename) throws IOException {
		super(trans,filename);
		counter_arcs=0;
		suffix=".pnml";
		introduction="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<pnml>\r\n" + 
				"  <net type=\"http://www.informatik.hu-berlin.de/top/pntd/ptNetb\" id=\"noID\">";
		terminal="<toolspecific tool=\"WoPeD\" version=\"1.0\">\r\n" + 
				"    </toolspecific>\r\n" + 
				"  </net>\r\n" + 
				"</pnml>";
	}
	@Override
	public void print(NetElement e) throws IOException {
		if(e==null) return;
		int size_x=40,size_y=40;
		if(e.isPersistent())
			size_x=size_y=20;
		String label=e.isTransition()?"transition":"place";
			bufferedwriter.write("<"+label+" ");
		bufferedwriter.write("id=\""+PREFIXNODEID+e.getId()+"\">\n<name>\n<text>"+e.getName());
		if(e.isTransition())
			bufferedwriter.write(" ("+e.getProbability()+")");
		bufferedwriter.write("</text>\n</name>");
		bufferedwriter.write("<graphics>\r\n" + 
				"        <position x=\""+e.getX()+"\" y=\""+e.getY()+"\"/>\r\n"+
				"<dimension x=\""+size_x+"\" y=\""+size_y+"\"/>\r\n"+
				"      </graphics>");
		if(e.isMarked())
			bufferedwriter.write("\n<initialMarking>\n<text>"+e.getTokens()+"</text>\n</initialMarking>");
		bufferedwriter.write("\n</"+label+">");
	}
	@Override
	public void print(Arc a) throws IOException {
		if(a.preset==null||a.postset==null)
			return;
		for(Integer i:a.preset)
			for(Integer j:a.postset) {
				bufferedwriter.write("<arc id=\""+(counter_arcs++)+"\" source=\""+PREFIXNODEID+i+"\""
						+ " target=\""+PREFIXNODEID+j+"\"></arc>\n");
				if(translator.get(i).isPersistent()&&translator.get(i).isPlace())
					bufferedwriter.write("<arc id=\""+(counter_arcs++)+"\" source=\""+PREFIXNODEID+j+"\""
							+ " target=\""+PREFIXNODEID+i+"\"></arc>\n");
			}
		
	}
}
