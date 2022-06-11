<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<div class="collapse navbar-collapse navbar navbar-dark bg-dark" id="navbarNav">
    <ul class="navbar-nav">
        <li class="nav-item"><a class="nav-link"
                                aria-current="page"
                                href="${pageContext.request.contextPath}/user/showUserHome">Home</a></li>
        <li class="nav-item"><a class="nav-link"
                                aria-current="page"
                                href="${pageContext.request.contextPath}/prof/module">Importer Notes</a></li>

        <li class="nav-item">

            <f:form action="${pageContext.request.contextPath}/logout" method="POST">

                <button type="submit" class="btn btn-link">logout</button>

            </f:form></li>
    </ul>
</div>