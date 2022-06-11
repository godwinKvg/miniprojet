<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 3/17/2022
  Time: 1:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
   <form action="/admin/inscription" method="post" enctype="multipart/form-data">
      <input type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" name="file">
      <button  type="submit" >Valider</button>
   </form>
</body>
</html>
<script>
  function  post(e){
      e.preventDefault();
      fetch("http://localhost:8080/admin/filiere", {
          method: "GET",
      }).then(res => {
          console.log("Request complete! response:", res);
      });
  }
</script>