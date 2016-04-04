package servlets;

import java.io.*;
import java.util.*;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

import javax.media.jai.JAI;  
import javax.media.jai.RenderedOp;  
  
import com.sun.media.jai.codec.BMPEncodeParam;  
import com.sun.media.jai.codec.ImageCodec;  
import com.sun.media.jai.codec.ImageEncoder;  
import com.sun.media.jai.codec.JPEGEncodeParam;

import storage.Database;
import storage.Storage;  

@SuppressWarnings("serial")
public class UploadPicture extends HttpServlet{
	
	//private boolean isMultipart;
	private String filePath;
	private String xmlPath;	
	//private String fileName;
	//private int maxFileSize = 50 * 1024;
	//private int maxMemSize = 4 * 1024;
	//private File file ;
	
	//private static final String UPLOAD_PATH = "F:\\Work\\Eclipse\\BasicJavaWebApplication\\data\\images";
	
//    public void init( ){
//	      // Get the file location where it would be stored.
//	      filePath = 
//	             getServletContext().getInitParameter("file-upload"); 
//	}
	protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
		//This is just for test!
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("File Uploaded Successfully!");
        out.println("<a href='homepage'>Back</a>");
	}
	protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");  //set encoding 
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter();
		boolean success = false;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
        	out.println("<html><title>HeavyWater OCR Text</title><body>");
            out.println("Files Uploading Failed!");
            out.println("<a href='homepage'>Back</a>");
            out.println("</body></html>");
            out.flush();
            return;
        }
        //clear the path
        filePath = "";
        xmlPath = "";
		//upload part
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext sc = this.getServletContext();		 
        String path = sc.getRealPath("/upload");
        File dir  = new File(path);
        if(!dir.exists()) {
        	dir.mkdirs();
        }
        factory.setRepository(dir);  
        //set cache 
        factory.setSizeThreshold(1024*1024) ; 
		ServletFileUpload upload = new ServletFileUpload(factory);	

		
		//To store the image path and xml info
		Storage storage = new Storage();
		storage.init();
		Database.init();
		int test = 0;
		
		 try {  
	            //multiple files uploaded available
	            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
	              
	            for(FileItem item : list)  
	            {  
	                String name = item.getFieldName();  
	                  
	                //if string
	                if(item.isFormField())  
	                {                     
	                    //to get the string from the form
	                    String value = item.getString() ;  
	                      
	                    request.setAttribute(name, value);  
	                }  
	                //to process the none text file
	                else  
	                {  
	                    /** 
	                     * to get the name of uploaded file
	                     */  
	                    //get the path's name  
	                    String value = item.getName() ;  
	                    int start = value.lastIndexOf("\\");    
	                    String fileName = value.substring(start+1);  
	                      
	                    request.setAttribute(name, fileName);  
	                      
	                    //save to disk 
                                        	
                    	
                    	if(fileName.endsWith(".tif") || fileName.endsWith(".tiff")) {
              
    	                    //tif -> bmp
	                    	String inputFile = dir.getAbsolutePath() + "/" + fileName;
	                    	String outputFile = inputFile.substring(0, inputFile.lastIndexOf('.')) + ".bmp"; 

	                    	if(Database.containImage(outputFile)) {
	                        	sendMessage("The image's name exists! " , false, out);
	                        	return;
	                        }
	                    	
	                    	item.write( new File(dir.getAbsolutePath(),fileName) );
	                    	
	                        RenderedOp src = JAI.create("fileload", inputFile);  
	                        //new File(outputFile);
	                        OutputStream os = new FileOutputStream(outputFile);  
	                        BMPEncodeParam param = new BMPEncodeParam();  
	                        ImageEncoder enc = ImageCodec.createImageEncoder("BMP", os,param);  
	                        enc.encode(src);  
	                        os.close();//close the stream 	
                    		filePath = request.getRequestURL().toString();
    	                    filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1) + "upload/" + fileName;
    	                    filePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".bmp";
    	                    storage.setupImageLocalPath(outputFile);
    	                    storage.setupImageRealPath(filePath);		                        
                    	}
                    	else {
                    		//xml
                    		item.write( new File(dir.getAbsolutePath(),fileName) );
                    		String xmlLocalPath = dir.getAbsolutePath() + "/" + fileName;
                			xmlPath = request.getRequestURL().toString();
                			xmlPath = xmlPath.substring(0, xmlPath.lastIndexOf("/") + 1) + "upload/" + fileName;
                			//store the xml info
                			storage.setupXMLPath(xmlLocalPath);
                			test = storage.getNumber();
                			if(!storage.isXMLValid()) {
                				sendMessage("XML is not Valid! ", false, out);
                				out.close();
                				return;
                			}
                			
                    	}
                    	success = true;
                    }                    
	                
	            }              
	    		
	              
	        } catch (FileUploadException e) {  
	            // TODO Auto-generated catch block 
	        	success = false;
	            e.printStackTrace();  
	        }  
	        catch (Exception e) {  
	            // TODO Auto-generated catch block 
	        	success = false;
	            //e.printStackTrace();  
	        }
		//if the image's format is not tif, success = false
		if(filePath.equals("") || xmlPath.equals(""))
				success = false;
		/**TODO: If upload successfully, send a page with back button to home*/		
        if(success) {
        	Database.putStorage(storage); 
			sendMessage("TEST: " + Integer.toString(test), true, out);
		} 
		
    	/**TODO: If upload failed, send a page with back button to home.*/
		
	    else{
        	sendMessage("Cannot upload files. Please check the format of your input(tif, xml)", false, out);
        } 
          	
	}
	public void sendMessage(String msg, boolean success, PrintWriter out){
		if(success) {
			out.println("<html><title>HeavyWater OCR Text</title><body>");
	        out.println("<h1>Files Uploaded Successfully!!</h1><br/>");
	        //out.println(filePath);
	        //out.println(xmlPath);
	        //out.println("<img alt=\"go\" src=\"upload/<%=(String)request.getAttribute('pic')%> \" />");
	        //out.println("<h1>Test: " + msg + "</h1><br/>");
	        //out.println("<a href=\""+ filePath +" \">Display Image</a></br>");
	        //out.println("<a href=\""+ xmlPath +" \">Display xml</a></br>");
	        out.println("<a href='display'>Display All</a></br>");
	        out.println("</br><a href='homepage'>Back to Home Page</a>");
	        out.println("</body></html>");
		}
		else
		{
			out.println("<html><title>HeavyWater OCR Text</title><body>");
            out.println("<h1>Failed!</h1></br>");
            out.println("<h2>Message: " + msg + "</h2></br>");
            out.println("</br><a href='homepage'>Back to Home Page</a>");
            out.println("</body></html>");
		}
        out.flush();
        out.close();
	}
	
}
