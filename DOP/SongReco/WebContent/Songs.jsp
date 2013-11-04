<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Beans.*" %>
<%@ page import="newEntities.*" %>
<%@ page import="MongoEntities.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table id="myTable1" border="0" width="400" cellspacing="0" 
              cellpadding="0">
   <%for(int i=0;i<Users.listSongs.size();i++){
	   SongBean s=(SongBean)Users.listSongs.get(i);%>           
	<tr>
	<td><a href="<%=request.getContextPath()%>/FetchSongDataBySongServlet?id=<%out.println(s.getSongID()); %>"><%=s.getSongID() %></a><td>
	<td><a href="<%=request.getContextPath()%>/UpdateSongLikeServlet?id=<%out.println(s.getSongID()); %>">Like</a><td>
	
	</tr>
	<%} %>
</table>

</body>
</html>