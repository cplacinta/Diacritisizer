<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body style="font-family:Verdana,serif;width:1000px;margin: auto auto">
<div><h1 style="text-align:center; padding-top: 50px">The Diacritisizer</h1></div>
<div>

 <div style="float:left;clear:left; width: 420px">
  <form method="post" action="restore" style="float:left;">
   <textarea name="input" rows=15 cols=50></textarea>
   <input type="submit" value="Restore" style="float:left;">
  </form>
 </div>

 <div style="float:right; width: 450px">
  <form method="post" action="train" style="float:right;clear:right;">
   <textarea name="result" style="float:right;" rows=15 cols=50><c:out value="${output}"/></textarea>
   <input type="submit" value="Submit" style="float:right;">
  </form>
 </div>

 <div style="width: 420px; font-size:12px; float:right;">
  <c:choose>
   <c:when test="${empty thank_you}">
    <p>*By submitting the corrected text back to us you help us improve the performance and accuracy of the
     algorithm</p>
   </c:when>
   <c:otherwise>
    <p>Thank you for taking the time to press the button!</p>
   </c:otherwise>
  </c:choose>
 </div>
</div>

</body>
</html>
