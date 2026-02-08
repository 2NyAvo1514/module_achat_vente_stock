<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bons de Commande - Gestion Entreprise</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../layout/header.jsp" %>
    <div class="container-fluid">
        <div class="row">
            <%@ include file="../../layout/sidebar.jsp" %>
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">

<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Bons de commande fournisseurs</h1>
    <div class="btn-toolbar mb-2 mb-md-0">
        <a href="<c:url value='/achats/commandes/creer' />" class="btn btn-sm btn-primary">
            <i class="fas fa-plus"></i> Nouvelle commande
        </a>
    </div>
</div>

<div class="table-responsive">
    <table class="table table-striped table-hover align-middle">
        <thead>
            <tr>
                <th>Référence</th>
                <th>Fournisseur</th>
                <th>Statut</th>
                <th class="text-end">Montant</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${commandes}" var="bc">
                <tr>
                    <td>${bc.reference}</td>
                    <td>${bc.fournisseur != null ? bc.fournisseur.nom : '-'}</td>
                    <td>
                        <span class="badge badge-status ${bc.statut == 'VALIDEE' ? 'bg-success text-white' : bc.statut == 'BROUILLON' ? 'bg-secondary text-white' : 'bg-warning text-dark'}">${bc.statut}</span>
                    </td>
                    <td class="text-end">${bc.montantTotal}</td>
                    <td>
                        <a href="<c:url value='/achats/commandes/${bc.id}'/>" class="btn btn-sm btn-info">Voir</a>
                        <c:if test="${bc.statut == 'BROUILLON'}">
                            <form style="display:inline-block" method="post" action="${pageContext.request.contextPath}/achats/commandes/${bc.id}/valider">
                                <button type="submit" class="btn btn-sm btn-success">Valider</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<a href="${pageContext.request.contextPath}/achats/demandes" class="btn btn-link">← Retour aux demandes</a>
            </main>
        </div>
    </div>
    <%@ include file="../../layout/footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>