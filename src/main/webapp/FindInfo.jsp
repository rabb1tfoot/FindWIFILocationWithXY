<%@page import="java.util.List"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.net.URL"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기 - 서준선</title>
</head>
<body>
<%
javaCode.DBManager.GetAndFillDB();
int allData = javaCode.DBManager.GetfinalPage();
%>
<h2>와이파이 WIFI정보 <%=allData%> 개를 정상적으로 저장했습니다.</h2>
<p></p><a href = "http://localhost:8080/JSPStudy/index.jsp">홈으로 이동

</body>
</html>