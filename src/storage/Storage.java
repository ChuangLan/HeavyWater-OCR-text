package storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList; 
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException; 

public class Storage {
	private ArrayList<String> x0List;
	private ArrayList<String> x1List;
	private ArrayList<String> y0List;
	private ArrayList<String> y1List;
	private ArrayList<String> OCRValueList;
	private ArrayList<String> OCRConfidenceList;
	private int number;
	private boolean isValid;
	private String imageRealPath;
	private String imageLocalPath;
	private String corX0;
	private String corY0;
	private String corX1;
	private String corY1;
	
	public Storage() {}
	public void init(){
		x0List = new ArrayList<String>();
		x1List = new ArrayList<String>();
		y0List = new ArrayList<String>();
		y1List = new ArrayList<String>();
		OCRValueList = new ArrayList<String>();
		OCRConfidenceList = new ArrayList<String>();
		number = 0;
		isValid = false;
		imageRealPath = "";
		imageLocalPath = "";
	}
	
	public void setupImageRealPath(String realPath){
		imageRealPath = realPath;
	}
	
	public String getImageRealPath(){
		return imageRealPath;
	}
	
	public void setupImageLocalPath(String localPath){
		imageLocalPath = localPath;
	}
	
	public String getImageLocalPath(){
		return imageLocalPath;
	}
	
	public void setupXMLPath(String xmlFilePath){
		/**TODO: parse the XML, get coordinates*/
		try{
		File  xmlFile = new File(xmlFilePath); 
		if(!xmlFile.exists()) {
			isValid = false;
			return;
		}
        
        DocumentBuilderFactory  builderFactory =  DocumentBuilderFactory.newInstance(); 
        DocumentBuilder builder;
		builder = builderFactory.newDocumentBuilder();          
        Document doc;
		doc = builder.parse(xmlFile);  
          
        doc.getDocumentElement().normalize();
        //get the corners
        NodeList corners = doc.getElementsByTagName("Corner");
        NodeList co_x0_y0 = corners.item(0).getChildNodes();
        NodeList co_x1_y1 = corners.item(2).getChildNodes();
        corX0 = co_x0_y0.item(0).getTextContent();
        corY0 = co_x0_y0.item(1).getTextContent();
        corX1 = co_x1_y1.item(0).getTextContent();
        corY1 = co_x1_y1.item(1).getTextContent();
        //get the coordinates
        NodeList  nList = doc.getElementsByTagName("Span"); 
        //this is for the binary search
        this.number = nList.getLength();
        for(int  i = 0 ; i<nList.getLength();i++){ 
        	Node  node = nList.item(i);
        	NodeList children = node.getChildNodes();
        	//text
        	String value = children.item(0).getTextContent();
        	//coordinates
        	NodeList coordinates = children.item(1).getChildNodes();
        	String x0 = coordinates.item(0).getTextContent();
        	String y0 = coordinates.item(1).getTextContent();
        	String x1 = coordinates.item(2).getTextContent();
        	String y1 = coordinates.item(3).getTextContent();
        	//confidence
        	String confidence = children.item(2).getTextContent();
        	//add
        	x0List.add(x0);
        	x1List.add(x1);
        	y0List.add(y0);
        	y1List.add(y1);        	
        	OCRValueList.add(value);        	
        	OCRConfidenceList.add(confidence);
        }
        isValid = true;
	   }catch(Exception  e){ 
		   isValid = false;
	       e.printStackTrace();  	         
	   }  
		
	}
	public boolean isXMLValid(){
		return isValid;
	}
	
	//utility
	public ArrayList<String> getX0List() {
		return x0List;
	}
	public ArrayList<String> getX1List() {
		return x1List;
	}
	public ArrayList<String> getY0List() {
		return y0List;
	}
	public ArrayList<String> getY1List() {
		return y1List;
	}
	public ArrayList<String> getOCRValueList() {
		return OCRValueList;
	}
	public ArrayList<String> getOCRConfidenceList() {
		return OCRConfidenceList;
	}
	public int getNumber(){
		return number;
	}
	public String[] getCorners() {
		String[] result = {corX0, corY0, corX1, corY1};
		return result;
	}
	
}
