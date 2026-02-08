<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Réservations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Réservations de Stock</h1>
            <div class="header-actions">
                <a href="/stock/reservations/nouvelle" class="btn btn-success">+ Nouvelle Réservation</a>
                <a href="/stock/reservations/expirees" class="btn btn-danger">Réservations Expirées</a>
            </div>
        </div>

        <div class="table-container">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Commande</th>
                        <th>Article</th>
                        <th>Dépôt</th>
                        <th>Quantité Réservée</th>
                        <th>Quantité Prélevée</th>
                        <th>À Prélever</th>
                        <th>Statut</th>
                        <th>Date Réservation</th>
                        <th>Délai Prélèvement</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="res" items="${reservations.content}">
                        <tr>
                            <td><code>${res.referenceCommande}</code></td>
                            <td>${res.article.designation}</td>
                            <td>${res.depot.nom}</td>
                            <td>${res.quantiteReservee}</td>
                            <td>${res.quantitePrelevee}</td>
                            <td><strong>${res.quantiteReservee - res.quantitePrelevee}</strong></td>
                            <td>
                                <span class="badge badge-${res.statut == 'ACTIVE' ? 'success' : res.statut == 'PRELEVEE' ? 'info' : res.statut == 'LIVREE' ? 'primary' : 'danger'}">
                                    ${res.statut.label}
                                </span>
                            </td>
                            <td>${res.dateReservation}</td>
                            <td>${res.dateExpiration}</td>
                            <td>
                                <a href="/stock/reservations/${res.id}" class="btn btn-sm btn-primary">Voir</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <nav class="pagination">
            <c:if test="${currentPage > 0}">
                <a href="?page=${currentPage - 1}" class="btn btn-sm">Précédent</a>
            </c:if>
            <span>Page ${currentPage + 1} sur ${totalPages}</span>
            <c:if test="${currentPage < totalPages - 1}">
                <a href="?page=${currentPage + 1}" class="btn btn-sm">Suivant</a>
            </c:if>
        </nav>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
