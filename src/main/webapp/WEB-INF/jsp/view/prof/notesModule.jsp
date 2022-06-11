<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/userheader.jsp" />
<div class="container">

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">



            <jsp:include page="../fragments/profMenu.jsp" />

        </div>
    </nav>






    <div>
        <h3>Note Module import page</h3>
    </div>

    <c:if test = "${error != null}">
    <div>
        <div class="alert alert-danger d-flex align-items-center col-6" role="alert">
            <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img"
                 aria-label="Danger:">
                <use xlink:href="#exclamation-triangle-fill" /></svg>
            <div> ${error}</div>
        </div>
    </div>
    </c:if>

    <form action="/prof/module" method="post" enctype="multipart/form-data">

        <div class="input-group mb-3">
            <label class="input-group-text" for="file">Upload</label>
            <input required type="file" class="form-control" id="file" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" name="file">
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
    </form>



    <jsp:include page="../fragments/userfooter.jsp" />

