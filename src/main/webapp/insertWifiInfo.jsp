<%@page import="java.util.List"%>
<%@page import="javaCode.DBManager"%>
<%@page import="javaCode.WifiInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%

	String x = request.getParameter("locationX");
    String y = request.getParameter("locationY");        
    
    Double dx = Double.valueOf(x);
    Double dy = Double.valueOf(y);
    DBManager.InsertHistoryDB(dx, dy);
    DBManager.FindaroundWifi(dx, dy);
%>
	<script>
	location.href="index.jsp";
	</script>

</body>
</html>