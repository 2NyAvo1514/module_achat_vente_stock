<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lots de Stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Gestion des Lots de Stock</h1>
            <div class="header-actions">
                <a href="/stock/lots/nouveau" class="btn btn-success">+ Nouveau Lot</a>
                <a href="/stock/lots/alertes/${depotId}" class="btn btn-warning">⚠ Alertes</a>
                <a href="/stock/lots/expires/${depotId}" class="btn btn-danger">🗑 Expirés</a>
            </div>
        </div>

        <div class="filters">
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label for="depotId">Dépôt:</label>
                    <select id="depotId" name="depotId" class="form-control">
                        <option value="">-- Sélectionner --</option>
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
                        <th>Numéro Lot</th>
                        <th>Article</th>
                        <th>Dépôt</th>
                        <th>Quantité Initial</th>
                        <th>Quantité Disponible</th>
                        <th>P.U.</th>
                        <th>Date Expiration</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="lot" items="${lots.content}">
                        <tr class="${lot.estExpire() ? 'row-danger' : lot.estProchePeremption() ? 'row-warning' : ''}">
                            <td><code>${lot.numerolot}</code></td>
                            <td>${lot.article.designation}</td>
                            <td>${lot.depot.nom}</td>
                            <td>${lot.quantiteInitiale}</td>
                            <td><strong>${lot.quantiteDisponible}</strong></td>
                            <td><fmt:formatNumber value="${lot.prixUnitaire}" type="currency"/></td>
                            <td>
                                <c:if test="${lot.dateExpiration != null}">
                                    <fmt:formatDate value="${lot.dateExpiration}" pattern="dd/MM/yyyy"/>
                                </c:if>
                            </td>
                            <td>
                                <span class="badge badge-${lot.statut == 'ACTIF' ? 'success' : lot.statut == 'ALERTE_PEREMPTION' ? 'warning' : 'danger'}">
                                    ${lot.statut.label}
                                </span>
                            </td>
                            <td>
                                <a href="/stock/lots/${lot.id}" class="btn btn-sm btn-primary">Voir</a>
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
