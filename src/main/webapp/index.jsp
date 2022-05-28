<%@page import="java.util.List"%>
<%@page import="javaCode.DBManager"%>
<%@page import="javaCode.WifiInfo"%>
<%@page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="CSS/table.css" rel="stylesheet" type="text/css"/> 
<title>와이파이 정보 구하기 - 서준선</title>
</head>
<body>
<script src="JS/func.js" type="text/javascript"></script>
<h1>와이파이 정보 구하기</h1>
<a href = "http://localhost:8080/JSPStudy/index.jsp">홈</a> | <a href = "http://localhost:8080/JSPStudy/history.jsp">위치 히스토리 목록</a> 
| <a href = "http://localhost:8080/JSPStudy/findInfo.jsp">Open API 와이파이 정보 가져오기</a>
<p></p>
<form action="insertWifiInfo.jsp">
LAT : <input id="locationX" name="locationX"  required value="0.0"/>
LNT : <input id="locationY" name="locationY" required value="0.0"/>
<button id="btnFindloc" type="button" onclick="GetLoc()">내 위치 가져오기</button>
<input required type="submit" value="근처 wifi 정보 찾기"/>
</form>


<table>
  <tr>
    <th>거리(KM)</th>
    <th>관리번호</th>
    <th>자치구</th>
    <th>와이파이명</th>
    <th>도로명주소</th>
    <th>설치위치(층)</th>
    <th>상세주소</th>
    <th>설치유형</th>
    <th>설치기관</th>
    <th>서비스</th>
    <th>망종류</th>
    <th>설치년도</th>
    <th>실내외구분</th>
    <th>WIFI접속환경</th>
    <th>Y좌표</th>
    <th>X좌표</th>
    <th>작업</th>
  </tr>
  <%
  //if(!javaCode.DBManager.IsSaved())
  //{
	//  out.write("<td colspan=\"15\">우선 와이파이 정보를 불러와주세요</td>");
  //}
  //else
  //{
	  if(DBManager.IsSetDistance())
	  {
		  List<WifiInfo> infos = DBManager.GetwifiInfos();
		  
		  for(int i = 0; i < infos.size(); ++i)
		  {
			  %>
			  <tr>
			  <%
			  for(int j = 0; j < infos.get(i).ParamSize(); ++j)
			  {
				  %>
				  <th>
				  <%=infos.get(i).GetParam(j)%>
				  </th>
				  <%
			  }
			  %>
			  </tr>
			  <%
		  }
	  }
	  
  //}
  %>
</table>

<%--
거리(KM) 관리번호 자치구 와이파이명 도로명주소 상세주소 설치유형 설치기관 서비스 구분 망종류 실내외구분 WIFI접속환경 X좌표 Y좌표 작업일자


<%
		List<String> strs = javaCode.Test.DBSelectWeb();
		for(int i = 0; i < 4; ++i)
		{
			out.write("<tr>");
			for(int j = 0; j < strs.size() / 4; ++j)
			{
		%>
		<%--<a href = "detail.jsp?memberType=" <%=파라미터1%> &userId=<%=파라미터2%>>
				<td><%=strs.get(i * 4 + j)%></td>
		<%
			}
			out.write("</tr>");
		}
		%>
<%
	List<String> str = javaCode.Test.DBSelectWeb();
	System.out.println(str.toString());
	out.write(str.toString());
%>
--%>
</body>
</html>