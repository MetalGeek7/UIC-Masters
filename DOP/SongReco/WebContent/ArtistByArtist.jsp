<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="MongoEntities.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<% List<ArtistDataBean> l=(List)request.getAttribute("list_artist"); %>
<body>
<table id="myTable" cellspacing="10" 
              cellpadding="0">

<tr>
<th width="250">ArtistID</th>
<th width="250">ArtistName</th>
<th width="250">ArtistDob</th>
<th width="250">ArtistLocation</th>
<th width="250">ArtistGenreId</th>
<th width="250">LikedCount</th>
<th width="250">AvgRating</th>
</tr>
<%for(int i=0;i<l.size();i++)
{%>
<tr id="row1">
<td width="250"><%=l.get(i).getArtistId()%></td>
<td width="250"><%=l.get(i).getArtistName()%></td>
<td width="250"><%=l.get(i).getArtistDob()%></td>
<td width="250"><%=l.get(i).getArtistLocation()%></td>
<td width="250"><%=l.get(i).getArtistGenreId()%></td>
<td width="250"><%=l.get(i).getLikedCount()%></td>
<td width="250"><%=l.get(i).getAvgRating()%></td>
<td width="250"><a href="<%=request.getContextPath()%>/FetchSongDataByArtistServlet?id=<%out.println(l.get(i).getArtistId()); %>">Get Songs of Artist</a></td>
</tr>
<%} %>
</table>
</body>
</html>