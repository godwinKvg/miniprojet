<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 4/24/2022
  Time: 11:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
 <div class="p" style="display: flex">
     <p>Les donnees des etudiants suivants dans la base de donnees sont contradictoires avec les
         donnees dans le fichier excell.</p>
 </div>
<div class="wrapper">
<c:if test="${!empty sessionScope.badInfos}">
  <c:choose>
      <c:when test="${sessionScope.badInfos.size()>=1}">
          <table class="styled-table">
              <thead>
              <tr>
                  <th>Donnees du fichier</th>
                  <th>Donnees en base de donnees</th>
                  <th>Mettre a jour?</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="i" begin="0" end="${badInfos.size()-1}" step="1" >
                  <tr>

                      <td><c:out value="${badInfoExcell.get(i).getNom()}" /> ,<c:out value="${badInfoExcell.get(i).getPrenom()}" /> ,<c:out value="${badInfoExcell.get(i).getCne()}" /></td>
                      <td><c:out value="${badInfos.get(i).getNom()}" /> ,<c:out value="${badInfos.get(i).getPrenom()}" /> ,<c:out value="${badInfos.get(i).getCne()}" /></td>
                      <td><input type="checkbox" value="${badInfos.get(i).getEmail()}" onclick="showButton()" /></td>
                  </tr>

              </c:forEach>


              </tbody>
          </table>
      </c:when>
      <c:otherwise>
             <p>Aucune info</p>
      </c:otherwise>
  </c:choose>
</c:if>


</div>
<div style="width: 100%;display: flex;justify-content:center;display: none" id="button" > <button style="background: green" onclick="update()">Valider</button></div>
 <div style="width: 100%;display: flex;justify-content:center;margin-top: 20px" > <a href="/admin/validerInscriptions"> Continuer</a></div>
</body>

</html>

<script>


    function  showButton(){
        let el=document.getElementById("button");

        el.style.display="block"

    }
     function  update(){




         let checkboxes=document.getElementsByTagName('input');

          for(checkbox of checkboxes){
              console.log("Hi dans")
              if(checkbox.checked){


                  fetch("http://localhost:8080/admin/rest/updateInfos/"+checkbox.value, {
                      method: "GET",
                  }).then(res => {
                      console.log("Request complete! response:", res);
                  });
              }
          }
     }
</script>



<style>
    .wrapper {
        display: flex;
        justify-content: center;
    }

    .styled-table {
        border-collapse: collapse;
        margin: 25px 0;
        font-size: 0.9em;
        font-family: sans-serif;
        min-width: 400px;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    }

    .styled-table thead tr {
        background-color: #009879;
        color: #ffffff;
        text-align: left;
    }

    .styled-table th,
    .styled-table td {
        padding: 12px 15px;
    }

    .styled-table tbody tr {
        border-bottom: 1px solid #dddddd;
    }

    .styled-table tbody tr:nth-of-type(even) {
        background-color: #f3f3f3;
    }

    .styled-table tbody tr:last-of-type {
        border-bottom: 2px solid #009879;
    }

    .styled-table tbody tr.active-row {
        font-weight: bold;
        color: #009879;
    }
</style>
