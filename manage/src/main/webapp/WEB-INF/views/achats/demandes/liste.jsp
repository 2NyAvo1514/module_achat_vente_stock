<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demandes d'Achat - Gestion Entreprise</title>
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
    <h1 class="h2">Liste des Demandes d'Achat</h1>
    <div class="btn-toolbar mb-2 mb-md-0">
        <a href="<c:url value='/achats/demandes/nouvelle' />" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nouvelle Demande
        </a>
    </div>
</div>

<div class="card mb-3">
    <div class="card-body">
        <form method="get" class="row g-3">
            <div class="col-md-4">
                <select name="statut" class="form-select">
                    <option value="">Tous les statuts</option>
                    <option value="BROUILLON" ${param.statut == 'BROUILLON' ? 'selected' : ''}>Brouillon</option>
                    <option value="SOUMISE" ${param.statut == 'SOUMISE' ? 'selected' : ''}>Soumise</option>
                    <option value="VALIDEE" ${param.statut == 'VALIDEE' ? 'selected' : ''}>Validée</option>
                    <option value="REJETEE" ${param.statut == 'REJETEE' ? 'selected' : ''}>Rejetée</option>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary">Filtrer</button>
            </div>
        </form>
    </div>
</div>

<div class="table-responsive">
    <table class="table table-striped table-hover">
        <thead>
            <tr>
                <th>Référence</th>
                <th>Demandeur</th>
                <th>Date</th>
                <th>Statut</th>
                <th>Montant</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="demande" items="${demandes}">
                <tr>
                    <td>${demande.reference}</td>
                    <td>${demande.demandeur.nomComplet}</td>
                    <td>${demande.dateCreation}</td>
                    <td>
                        <span class="badge bg-${demande.statut == 'VALIDEE' ? 'success' : 
                                              demande.statut == 'SOUMISE' ? 'warning' : 
                                              demande.statut == 'REJETEE' ? 'danger' : 'secondary'}">
                            ${demande.statut}
                        </span>
                    </td>
                    <td>${demande.montantTotal} €</td>
                    <td>
                        <a href="<c:url value='/achats/demandes/${demande.id}' />" 
                           class="btn btn-sm btn-info">Voir</a>
                        <c:if test="${demande.statut == 'VALIDEE'}">
                            <a href="<c:url value='/achats/commandes/creer/${demande.id}' />" 
                               class="btn btn-sm btn-success">Créer Commande</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <%-- Pagination --%>
    <nav>
        <ul class="pagination justify-content-center">
            <c:forEach begin="0" end="${totalPages - 1}" var="i">
                <li class="page-item ${currentPage == i ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}&size=${param.size}&statut=${param.statut}">
                        ${i + 1}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</div>

            </main>
        </div>
    </div>
    <%@ include file="../../layout/footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>