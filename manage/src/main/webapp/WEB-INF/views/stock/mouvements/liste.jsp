<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mouvements de Stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Mouvements de Stock</h1>
            <div class="header-actions">
                <a href="/stock/mouvements/entrees/nouveau" class="btn btn-success">+ Entrée</a>
                <a href="/stock/mouvements/sorties/nouveau" class="btn btn-danger">+ Sortie</a>
                <a href="/stock/mouvements/transferts/nouveau" class="btn btn-info">+ Transfert</a>
                <a href="/stock/mouvements/planifiees" class="btn btn-warning">Planifiées</a>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="filters">
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label for="depotId">Dépôt:</label>
                    <select id="depotId" name="depotId" class="form-control">
                        <option value="">-- Tous --</option>
                        <c:forEach var="depot" items="${depots}">
                            <option value="${depot.id}" <c:if test="${depot.id == depotId}">selected</c:if>>
                                    ${depot.nom}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Filtrer</button>
            </form>
        </div>

        <div class="table-container">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Type</th>
                        <th>Article</th>
                        <th>Dépôt</th>
                        <th>Quantité</th>
                        <th>P.U.</th>
                        <th>Coût</th>
                        <th>Référence</th>
                        <th>Statut</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="mouvement" items="${mouvements.content}">
                        <tr>
                            <td><span class="badge badge-${mouvement.typeMouvement == 'ENTREE' ? 'success' : mouvement.typeMouvement == 'SORTIE' ? 'danger' : 'info'}">
                                    ${mouvement.typeMouvement.label}
                            </span></td>
                            <td>${mouvement.article.designation}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${mouvement.typeMouvement == 'TRANSFERT'}">
                                        ${mouvement.depotDepart.nom} → ${mouvement.depotDestination.nom}
                                    </c:when>
                                    <c:otherwise>
                                        ${mouvement.depotDestination.nom}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${mouvement.quantite}</td>
                            <td><fmt:formatNumber value="${mouvement.prixUnitaire}" type="currency"/></td>
                            <td><fmt:formatNumber value="${mouvement.quantite * mouvement.prixUnitaire}" type="currency"/></td>
                            <td><code>${mouvement.numeroReference}</code></td>
                            <td>
                                <span class="badge badge-${mouvement.statut == 'EXECUTEE' ? 'success' : mouvement.statut == 'PLANIFIEE' ? 'warning' : 'danger'}">
                                    ${mouvement.statut.label}
                                </span>
                            </td>
                            <td><fmt:formatDate value="${mouvement.dateCreation}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>
                                <a href="/stock/mouvements/${mouvement.id}" class="btn btn-sm btn-primary">Voir</a>
                                <c:if test="${mouvement.statut == 'PLANIFIEE'}">
                                    <form method="post" action="/stock/mouvements/${mouvement.id}/executer" style="display:inline;">
                                        <button type="submit" class="btn btn-sm btn-success">Exécuter</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <nav class="pagination">
            <c:if test="${currentPage > 0}">
                <a href="?page=${currentPage - 1}&depotId=${depotId}" class="btn btn-sm">Précédent</a>
            </c:if>
            <span>Page ${currentPage + 1} sur ${totalPages}</span>
            <c:if test="${currentPage < totalPages - 1}">
                <a href="?page=${currentPage + 1}&depotId=${depotId}" class="btn btn-sm">Suivant</a>
            </c:if>
        </nav>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
