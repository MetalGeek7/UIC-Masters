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
<% List<SongBean> l=(List)request.getAttribute("list_song"); %>
<body>
<table id="myTable" cellspacing="10" 
              cellpadding="0">

<tr>
<th width="250">SongID</th>
<th width="250">AlbumID</th>
<th width="250">ArtistID</th>
<th width="250">GenreID</th>
<th width="250">ReleaseYear</th>
<th width="250">LikedCount</th>
<th width="250">SharedCount</th>
<th width="250">PurchaseCount</th>
<th width="250">AverageRating</th>
<th width="250">SongHotness</th>
<th width="250">SongEnergy</th>
<th width="250">SongDanceability</th>
<th width="250">SongTempo</th>
</tr>
<%for(int i=0;i<l.size();i++)
{%>
<tr id="row1">
<td width="250"><%=l.get(i).getSongID()%></td>
<td width="250"><a href="<%=request.getContextPath()%>/FetchAlbumDataByAlbumServlet?id=<%out.println(l.get(i).getAlbumID()); %>"><%=l.get(i).getAlbumID() %></a></td>
<td width="250"><a href="<%=request.getContextPath()%>/FetchArtistDataByArtistServlet?id=<%out.println(l.get(i).getArtistID()); %>"><%=l.get(i).getArtistID() %></a></td>
<td width="250"><%=l.get(i).getArtistID()%></td>
<td width="250"><%=l.get(i).getGenreID()%></td>
<td width="250"><%=l.get(i).getReleaseYear()%></td>
<td width="250"><%=l.get(i).getLikedCount()%></td>
<td width="250"><%=l.get(i).getSharedCount()%></td>
<td width="250"><%=l.get(i).getPurchaseCount()%></td>
<td width="250"><%=l.get(i).getAverageRating()%></td>
<td width="250"><%=l.get(i).getSongHotness()%></td>
<td width="250"><%=l.get(i).getSongEnergy()%></td>
<td width="250"><%=l.get(i).getSongDanceability()%></td>
<td width="250"><%=l.get(i).getSongTempo()%></td>
</tr>
<%} %>
</table>
</body>
</html>