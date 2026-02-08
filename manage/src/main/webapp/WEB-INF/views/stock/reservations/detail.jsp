<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Détail Réservation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Détail Réservation</h1>
            <a href="/stock/reservations" class="btn btn-secondary">← Retour</a>
        </div>

        <c:if test="${reservation != null}">
            <div class="detail-card">
                <div class="detail-header">
                    <h3>Réservation #${reservation.id}</h3>
                    <span class="badge badge-${reservation.statut == 'ACTIVE' ? 'success' : reservation.statut == 'PRELEVEE' ? 'info' : reservation.statut == 'LIVREE' ? 'primary' : 'danger'}">
                        ${reservation.statut.label}
                    </span>
                </div>

                <div class="detail-body">
                    <div class="detail-row">
                        <label>Commande Client:</label>
                        <span><code>${reservation.referenceCommande}</code></span>
                    </div>
                    <div class="detail-row">
                        <label>Article:</label>
                        <span>${reservation.article.code} - ${reservation.article.designation}</span>
                    </div>
                    <div class="detail-row">
                        <label>Dépôt:</label>
                        <span>${reservation.depot.nom}</span>
                    </div>
                    <div class="detail-row">
                        <label>Quantité Réservée:</label>
                        <span>${reservation.quantiteReservee}</span>
                    </div>
                    <div class="detail-row">
                        <label>Quantité Prélevée:</label>
                        <span>${reservation.quantitePrelevee}</span>
                    </div>
                    <div class="detail-row">
                        <label>À Prélever:</label>
                        <span><strong>${reservation.quantiteReservee - reservation.quantitePrelevee}</strong></span>
                    </div>
                    <div class="detail-row">
                        <label>Date Réservation:</label>
                        <span><fmt:formatDate value="${reservation.dateReservation}" pattern="dd/MM/yyyy HH:mm"/></span>
                    </div>
                    <c:if test="${reservation.dateExpiration != null}">
                        <div class="detail-row">
                            <label>Délai Prélèvement:</label>
                            <span><fmt:formatDate value="${reservation.dateExpiration}" pattern="dd/MM/yyyy HH:mm"/></span>
                        </div>
                    </c:if>
                </div>

                <div class="detail-footer">
                    <c:if test="${reservation.statut == 'ACTIVE'}">
                        <form method="post" action="/stock/reservations/${reservation.id}/preleve" class="form-inline" style="margin-bottom:10px;">
                            <div class="form-group">
                                <label for="quantite">Quantité à Prélever:</label>
                                <input type="number" id="quantite" name="quantite" class="form-control" 
                                       step="0.01" max="${reservation.quantiteReservee - reservation.quantitePrelevee}" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Prélever</button>
                        </form>

                        <form method="post" action="/stock/reservations/${reservation.id}/liberer" style="display:inline;">
                            <button type="submit" class="btn btn-danger" onclick="return confirm('Confirmer la libération?')">Libérer</button>
                        </form>
                    </c:if>

                    <c:if test="${reservation.statut == 'PRELEVEE'}">
                        <form method="post" action="/stock/reservations/${reservation.id}/livrer" style="display:inline;">
                            <button type="submit" class="btn btn-success">Marquer Livrée</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:if>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
