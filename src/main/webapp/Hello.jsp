<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>회원목록</h1>
<table>
	<thead>
		<tr>
		<th>이름</th>
		<th>이메일</th>
		<th>비밀번호</th>
		<th>날짜</th>
		</tr>
	</thead>
		<tbody>
		<%
		List<String> strs = javaCode.Test.DBSelectWeb();
		for(int i = 0; i < 4; ++i)
		{
			out.write("<tr>");
			for(int j = 0; j < strs.size() / 4; ++j)
			{
		%>
		<%--<a href = "detail.jsp?memberType=" <%=파라미터1%> &userId=<%=파라미터2%>>  --%>
				<td><%=strs.get(i * 4 + j)%></td>
		<%
			}
			out.write("</tr>");
		}
		%>
		</tbody>
</table>
<%--
<%
	List<String> str = javaCode.Test.DBSelectWeb();
	System.out.println(str.toString());
	out.write(str.toString());
%>
--%>
</body>
</html>