<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nouvelle Demande - Gestion Entreprise</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../layout/header.jsp" %>
    <div class="container-fluid">
        <div class="row">
            <%@ include file="../../layout/sidebar.jsp" %>
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">Nouvelle Demande d'Achat</div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/achats/demandes/creer" class="row g-3">
                        <div class="col-12">
                            <label class="form-label">Site</label>
                            <select name="site.id" class="form-select">
                                <c:forEach items="${sites}" var="s">
                                    <option value="${s.id}">${s.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-12 text-end">
                            <button type="submit" class="btn btn-primary">Créer</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
            </main>
        </div>
    </div>
    <%@ include file="../../layout/footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>