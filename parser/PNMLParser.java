package parser;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import bitstructures.BitMatrix;
import oracle.xml.parser.v2.SAXParser;
import representation.Translator;
public class PNMLParser {
	public static String EXTENSION=".pnml";
	public static String PATH="source/";
	private String sourcefile;
	private PNMLHandler pnmlHandler;
	SAXParser parser;
	public PNMLParser(String fname){
		sourcefile=PATH+fname+EXTENSION;  	 
	}
	
   public void parse(){
      try{
         // Create handlers for the parser
         // For all the other interface use the default provided by
         // Handler base
    	  pnmlHandler=new PNMLHandler(); 
         // Get an instance of the parser
         parser = new SAXParser();
         /*
         XSDBuilder builder = new XSDBuilder();       
         // Build XML Schema Object
         XMLSchema schemadoc = (XMLSchema)builder.build(DemoUtil.createURL("pnml1.xsd"));  
           parser.setXMLSchema(schemadoc);
           */
         // set validation mode
         ((SAXParser)parser).setValidationMode(SAXParser.AUTO_VALIDATION);
        
         parser.setContentHandler(pnmlHandler);
         parser.setErrorHandler(pnmlHandler);
         parser.setEntityResolver(pnmlHandler);
         parser.setDTDHandler(pnmlHandler);
         parser.parse(createURL(sourcefile).toString());
         pnmlHandler.optimizeF();
      }
      catch (Exception e) {
         System.err.println(e.toString());
      }
   
   }
   public BitMatrix getF(){
	   if(pnmlHandler!=null)
		   return (BitMatrix) pnmlHandler.F;
	   else return null;
   }
   public Translator getTranslator()
   {
	   return pnmlHandler.translator;
   }
   /**  This is a bunch of weird code that is required to
    * make a valid URL on the Windows platform, due
    * to inconsistencies in what getAbsolutePath returns
    * @param fileName
    * @return url of filename
    */
   public static URL createURL(String fileName)
   {
      URL url = null;
      try 
      {
         url = new URL(fileName);
      } 
      catch (MalformedURLException ex) 
      {
         File f = new File(fileName);
         try 
         {
            String path = f.getAbsolutePath();
            String fs = System.getProperty("file.separator");
            if (fs.length() == 1)
            {
               char sep = fs.charAt(0);
               if (sep != '/')
                  path = path.replace(sep, '/');
               if (path.charAt(0) != '/')
                  path = '/' + path;
            }
            path = "file://" + path;
            url = new URL(path);
         } 
         catch (MalformedURLException e) 
         {
            System.out.println("Cannot create url for: " + fileName);
            System.exit(0);
         }
      }
      return url;
   }
}


