<%@ page import="com.ensah.core.bo.InscriptionAnnuelle" %>
<%@ page import="java.util.List" %><%--
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
<c:if test="${sessionScope.dejaInscrits.isEmpty()==false}">
<div class="p" style="display: flex;width: 100%;justify-content:space-around">
    <p>ANCIENS ETUDIANTS</p>

    <button onclick="reinscrire()"> Reinscrire tous ces etudiants</button>

</div>

<div class="wrapper">

    <table class="styled-table">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Prenom</th>
            <th>Niveau dans la base</th>
            <th>Niveau suivant</th>
            <th>Filiere</th>
            <th>Action</th>


        </tr>
        </thead>
        <tbody>

  <c:forEach var="i" begin="0" end="${dejaInscrits.size()-1}" step="1" >
          <tr>
              <c:set var="inscriptions" value="${dejaInscrits.get(i).getInscriptions()}"/>
              <c:set var="derniereInscriptionAnnuelle" value="${inscriptions.get(inscriptions.size()-1)}"/>
              <td><c:out value="${dejaInscrits.get(i).getNom()}" /></td>
              <td><c:out value="${dejaInscrits.get(i).getPrenom()}" /></td>
              <td><c:out value="${derniereInscriptionAnnuelle.getNiveau().getAlias()}" /></td>
              <td><c:out value="${derniereInscriptionAnnuelle.getNiveau().getNiveauSuivant().getAlias()}" /></td>
              <td><c:out value="${derniereInscriptionAnnuelle.getNiveau().getFiliere().getTitreFiliere()}" /></td>
              <td class="reinscription" ><button   id_user="${dejaInscrits.get(i).getIdUtilisateur()}">Reinscrire</button></td>
          </tr>
  </c:forEach>




        </tbody>
    </table>
</div>
</c:if>

<c:if test="${sessionScope.pasInscrits.isEmpty()==false}">

<div class="p" style="display: flex;width: 100%;justify-content:space-around">
    <p>NOUVELS ETUDIANTS</p>
    <button onclick="inscrire()">Inscrire tous ces etudiants</button>
</div>

<div class="wrapper">

    <table class="styled-table">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Prenom</th>
            <th>Niveau d'inscription</th>
            <th>Type</th>
            <th>Action</th>
        </tr>
        </thead>
    <tbody>

<c:forEach var="i" begin="0" end="${pasInscrits.size()-1}" step="1" >
            <tr>
                <td><c:out value="${pasInscrits.get(i).getNom()}" /></td>
                <td><c:out value="${pasInscrits.get(i).getPrenom()}" /></td>
                <td><c:out value="${pasInscrits.get(i).getId_niveau()}" /></td>
                <td><c:out value="${pasInscrits.get(i).getType()}" /></td>
                <td class="inscription"><button   id_user="${pasInscrits.get(i).getId_etudiant()}">Inscrire</button></td>
            </tr>
        </c:forEach>




        </tbody>
    </table>
</div>
</c:if>
<p style="color: red" id="messageId"></p>
</body>

</html>

<script>

    function  reinscrire(){

        let elements=document.querySelectorAll(".reinscription > button")

        for(el of elements){



                fetch("http://localhost:8080/admin/validerInscriptions/"+el.getAttribute("id_user"), {
                    method: "GET",
                }).then(res => {
                    console.log("Request complete! response:", res);
                });
            }


    }
    function  inscrire(){

        let elements=document.querySelectorAll(".inscription > button")
        console.log(elements)
        for(el of elements){

            fetch("http://localhost:8080/admin/InscrireNouvel/"+el.getAttribute("id_user"), {
                method: "GET",
            }).then(res=> {
                let result=res.json();
                var message=""
               result.then(response=>{
                    message=response.message;
                     if(message!="success"){
                        messageId.innerHTML=messageId.textContent+"<br>"+message;
                     }
                })
                console.log(message);

            });
        }


    }
    function  update(){
        let checkboxes=document.getElementsByTagName('input');

        for(checkbox of checkboxes){
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
