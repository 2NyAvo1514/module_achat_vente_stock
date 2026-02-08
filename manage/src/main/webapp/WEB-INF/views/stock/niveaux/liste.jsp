<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Niveaux de Stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Gestion des Niveaux de Stock</h1>
            <nav class="breadcrumb">
                <a href="/dashboard">Accueil</a> / <span>Stock</span> / <span>Niveaux</span>
            </nav>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

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
                <a href="/stock/niveaux/alertes/${depotId}" class="btn btn-warning">Alertes (Min)</a>
                <a href="/stock/niveaux/surstock/${depotId}" class="btn btn-info">Surstock (Max)</a>
            </form>
        </div>

        <div class="table-container">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Article</th>
                        <th>Code</th>
                        <th>Dépôt</th>
                        <th>Disponible</th>
                        <th>Réservée</th>
                        <th>Utilisable</th>
                        <th>Commandée</th>
                        <th>CUMP</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="niveau" items="${niveaux.content}">
                        <tr>
                            <td>${niveau.article.designation}</td>
                            <td><code>${niveau.article.code}</code></td>
                            <td>${niveau.depot.nom}</td>
                            <td>${niveau.quantiteDisponible}</td>
                            <td>${niveau.quantiteReservee}</td>
                            <td>
                                <strong>${niveau.quantiteUtilisable}</strong>
                                <c:if test="${niveau.quantiteUtilisable < niveau.stockMinimum}">
                                    <span class="badge badge-danger">ALERTE</span>
                                </c:if>
                                <c:if test="${niveau.quantiteDisponible > niveau.stockMaximum}">
                                    <span class="badge badge-info">SURSTOCK</span>
                                </c:if>
                            </td>
                            <td>${niveau.quantiteCommandee}</td>
                            <td><fmt:formatNumber value="${niveau.coutMoyenPondere}" type="currency"/></td>
                            <td>
                                <span class="badge">Min: ${niveau.stockMinimum} | Max: ${niveau.stockMaximum}</span>
                            </td>
                            <td>
                                <a href="/stock/niveaux/${niveau.article.id}/${niveau.depot.id}" class="btn btn-sm btn-primary">Détail</a>
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
