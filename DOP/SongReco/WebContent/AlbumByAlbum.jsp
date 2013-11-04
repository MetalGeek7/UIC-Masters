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
<% List<AlbumBean> l=(List)request.getAttribute("list_album"); %>
<body>
<table id="myTable" cellspacing="10" 
              cellpadding="0">

<tr>
<th width="250">AlbumID</th>
<th width="250">AlbumName</th>
<th width="250">AlbumArtist</th>
<th width="250">ReleaseYear</th>
<th width="250">AlbumCost</th>
<th width="250">AlbumGenre</th>
<th width="250">LikedCount</th>
<th width="250">SharedCount</th>
<th width="250">PurchaseCount</th>
<th width="250">AvgRating</th>
</tr>
<%for(int i=0;i<l.size();i++)
{%>
<tr id="row1">
<td width="250"><%=l.get(i).getAlbumId()%></td>
<td width="250"><%=l.get(i).getAlbumName()%></td>
<td width="250"><%=l.get(i).getAlbumArtist()%></td>
<td width="250"><%=l.get(i).getReleaseYear()%></td>
<td width="250"><%=l.get(i).getAlbumCost()%></td>
<td width="250"><%=l.get(i).getAlbumGenre()%></td>
<td width="250"><%=l.get(i).getLikedCount()%></td>
<td width="250"><%=l.get(i).getSharedCount()%></td>
<td width="250"><%=l.get(i).getPurchaseCount()%></td>
<td width="250"><%=l.get(i).getAvgRating()%></td>
</tr>
<%} %>
</table>
</body>
</html>