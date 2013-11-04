<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<FORM NAME="userform" method="post" >
<table id="myTable1" border="0" cellspacing="0" 
              cellpadding="0">
<tr id="row1"  >
<td height="25" width="500" > User Name </td>
<td><input type=text id="0" name="0" value="<%=Users.user.getUserName() %>" ></td>
</tr> 
<tr id="row2"  >
<td height="25" width="500" > User Date of Birth </td>
<td width="500"> <%=Users.user.getUserDob() %> </td>
<td><input type=text id="1" name="1" value="<%=Users.user.getUserDob() %>" ></td>
</tr> 
<tr id="row3"  >
<td height="25" width="500" > User Location </td>
<td width="500"> <%=Users.user.getUserLocation() %> </td>
<td><input type=text id="2" name="2" value="<%=Users.user.getUserLocation() %>" ></td>
</tr>  
<tr id="row4"  >
<td height="25" width="500" > User Preferred Genre </td>
<td width="500"> <%=Users.user.getUserPreferredGenre() %> </td>
<td><input type=text id="3" name="3" value="<%=Users.user.getUserPreferredGenre() %>" ></td>
</tr>
<tr id="row5"  >
<td height="25" width="500" > User Preferred Artist </td>
<td width="500"> <%=Users.user.getUserPreferredArtist() %> </td>
<td><input type=text id="4" name="4" value="<%=Users.user.getUserPreferredArtist() %>" ></td>
</tr>
<tr>
<td><input type="submit" name="submit" value="Submit"></td>
</tr>
</table>
</FORM>
</body>
</html>