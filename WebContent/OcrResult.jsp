<%@ page language="java" import="storage.Database" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%
    String OCRPage = request.getParameter("page");
    int pageNumber = Integer.parseInt(OCRPage) - 1;
	/**TODO: get the coordinates of the image*/
	//Set the width and height of the image
	int width = 0;
	int height = 0;
	String[] corners = Database.getStorage(pageNumber).getCorners();
	width = Integer.parseInt(corners[2]) -  Integer.parseInt(corners[0]);
	height = Integer.parseInt(corners[3]) -  Integer.parseInt(corners[1]);
	int x = -1,y = -1;
	//Get the local image path
	String imageLocalPath = Database.getStorage(pageNumber).getImageLocalPath();
	String imageRealPath = Database.getStorage(pageNumber).getImageRealPath();
	/**TEST*/
	//x = 1100;
	//y = 4960;
	//The ocr value shoud be the Address
	/**TEST END*/	
	x = Integer.parseInt(request.getParameter("valX"))*4;		//ZOOMED!!!
	y = Integer.parseInt(request.getParameter("valY"))*4;		//ZOOMED!!!
	int index = Database.getOcrIndexPrecise(pageNumber, x, y);
	String ocrValue = "";
	String ocrConfidence = "";
	String coordinates = "";
	if(index != -1) {
		ocrValue = Database.getStorage(pageNumber).getOCRValueList().get(index);
		ocrConfidence = Database.getStorage(pageNumber).getOCRConfidenceList().get(index);
	}
	if(x > 0 && y > 0) {
		coordinates = "(" + Integer.toString(x/4) + ", " + Integer.toString(y/4) + ")"; // ZOOMED!!!
	}
%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>OCR Result Page</title>
<style type="text/css">
.tip {
width:200px;
border:2px solid #ddd;
padding:8px;
background:#f1f1f1;
color:#666;
}
</style>
<script type="text/javascript"> 
function mousePos(e){
	var x,y;
	var e = e||window.event;
	return {
		x:Math.round(e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft -8),		//Calibration!!!
		y:Math.round(e.clientY+document.body.scrollTop+document.documentElement.scrollTop -276) 		//Calibration!!!
	};
};
function test(e){
	//document.getElementById("mjs").innerHTML = mousePos(e).x+','+mousePos(e).y;	
	window.location.href="OcrResult.jsp?page="+<%=OCRPage%>+"&valX="+mousePos(e).x+"&valY="+mousePos(e).y	
};
</script>
</head>
<body>	
	<h1>OCR Text Visualization</h1><br/>
	<h2>Click on the word on the picture below</h2>
	</br>OCR Text Value: 
	<%=ocrValue %>
	</br>OCR Text Confidence: 
	<%=ocrConfidence %>
	</br>Coordinates: 
	<%=coordinates%>
	</br><a href="display?page=0">Back</a></br>
	<a href="homepage">Back to HomePage</a></br>	
	<div id="test" onclick="test(event)">
		<img height="<%=height/4%>" width="<%=width/4%>" src="<%=imageRealPath%>"/>
	</div>	
	
</body>
</html>