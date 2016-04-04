package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import storage.Database;
import storage.Storage;

@SuppressWarnings("serial")
public class DisplayPicture extends HttpServlet{
	//This servlet is to get the images in the storage
	//And give the user the right to select points on the image. 
	protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) {
		//Input = the coordinates of points
		//Output = the OCR text in XML
	}
	protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter();
        out.println("<html><title>HeavyWater OCR Text</title><body>");
		String OCRPage = request.getParameter("page");
		if(OCRPage == null || OCRPage.equals("0")) {
			//Get the image/image list			
			ArrayList<String> imageList = Database.getImageRealPathList();
	        out.println("<h1>This is the list of Pictures!!</h1></br>");
	        if(imageList != null && !imageList.isEmpty()) {
	        	int i = 1;
	        	for(String s: imageList) {
	            	out.println("<a href='"+s+"'>" + s.substring(s.lastIndexOf('/') + 1) + "</a>");
	            	out.println("<span style='width:50px;'></span>");
	            	out.println("<form action='OcrResult.jsp' method='get'>");
	            	out.println("<input type='submit' value='OCR Result'/>");
	            	out.println("<input type='hidden' name='page' value='" + Integer.toString(i) + "'/>");
	            	out.println("<input type='hidden' name='valX' value='-1'/>");
	            	out.println("<input type='hidden' name='valY' value='-1'/></form>");
	            	i++;
	            }
	        }
	        else {
	        	out.println("<h2>No files uploaded</h2></br>");
	        }
//	        /**TEST*/
//	        Storage stg = Database.getStorage(0);
//	        if(stg != null) {
//	        	for(String s: stg.getX0List()){
//	            	out.println(s + " ");
//	            } 
//	        }               
//	        /**TEST END*/
	        out.println("</br><a href='homepage'>Back</a>");
	        
		}
		else {
//			int pageNumber = Integer.parseInt(OCRPage) - 1;
//			/**TODO: get the coordinates of the image*/
//			//Set the width and height of the image
//			int width = 0;
//			int height = 0;
//			String[] corners = Database.getStorage(pageNumber).getCorners();
//			width = Integer.parseInt(corners[2]) -  Integer.parseInt(corners[0]);
//			height = Integer.parseInt(corners[3]) -  Integer.parseInt(corners[1]);
//			int x = -1,y = -1;
//			//Get the real image path
//			String imageLocalPahth = Database.getStorage(pageNumber).getImageLocalPath();
//			/**TEST*/
//			x = 1100;
//			y = 4960;
//			//The ocr value shoud be the Address
//			/**TEST END*/
//			int index = Database.getOcrIndexPrecise(pageNumber, x, y);
//			String ocrValue = "";
//			String ocrConfidence = "";
//			String coordinates = "";
//			if(index != -1) {
//				ocrValue = Database.getStorage(pageNumber).getOCRValueList().get(index);
//				ocrConfidence = Database.getStorage(pageNumber).getOCRConfidenceList().get(index);
//			}
//			if(x != -1 && y != -1) {
//				coordinates = "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
//			}
//			out.println("<h1>OCR Text Visualization</h1></br>");
//			out.println("<h2>Click on the word you want on the picture below</h2>");
//			out.println("</br>OCR Text Value: ");
//			out.println(ocrValue);
//			out.println("</br>OCR Text Confidence: ");
//			out.println(ocrConfidence);
//			out.println("</br>Coordinates: ");
//			out.println(coordinates);
//			out.println("</br><a href='display?page=0'>Back</a>");
//			out.println("</br><a href='homepage'>Back to Homepage</a></br>");
//			/**TODO: Display a picture that can be click*/
//			
//			out.println("<applet codebase=\"http://heavywater.m4hwig382u.us-west-2.elasticbeanstalk.com\" code=\"XX\" width=\"350\" height=\"350\">");
//			out.println("Java applet that draws animated bubbles.");
//			out.println("</applet> ");
			
		}
		out.println("</body></html>");
		out.flush();
		out.close();
		
		
	}
}
