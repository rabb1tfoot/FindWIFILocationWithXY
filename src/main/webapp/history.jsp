<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<style>
table, td, th {
  border: 1px solid;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th{
	background-color: #b1d6ff ;
}

td{
	background-color: #e8f2fd ;
}
</style>

<title>와이파이 정보 구하기 - 서준선</title>
</head>
<body>
<h1>히스토리 목록</h1>
<a href = "http://localhost:8080/JSPStudy/index.jsp">홈</a> | <a href = "http://localhost:8080/JSPStudy/history.jsp">위치 히스토리 목록</a>

  <%
List<javaCode.History> arrHis = javaCode.DBManager.GetHistoryDB();
  %>

<p></p>
<table>
<thead>
  <tr>
    <th>ID</th>
    <th>위도</th>
    <th>경도</th>
    <th>조회일자</th>
    <th>비고</th>
  </tr>
  </thead>
  <tbody>
<% 
if(arrHis != null)
{
	for(int i = 0; i < arrHis.size(); ++i)
	{
		%>
		<tr>
		<%
		for(int j = 0; j < 5; ++j)
		{
			%>
			<th>
			<%
			if(j == 4)
			{
				%>
				<button><a href="delete.jsp?id=<%= arrHis.get(i).GetParam(0)%>">삭제하기</a></button>
				<%
			}
			else
			{
				String str = arrHis.get(i).GetParam(j);
				%>
				<%=str%>
				<%
			}
			 %>
			</th>
			<%
		}
		 %>
		</tr>
		<%
	}
}
%>
</tbody>
</table>
</body>
</html>