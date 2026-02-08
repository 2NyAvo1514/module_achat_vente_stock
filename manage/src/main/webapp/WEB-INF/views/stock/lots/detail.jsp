<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Détail Lot</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Détail Lot de Stock</h1>
            <a href="/stock/lots" class="btn btn-secondary">← Retour</a>
        </div>

        <c:if test="${lot != null}">
            <div class="detail-card">
                <div class="detail-header">
                    <h3>Lot: ${lot.numerolot}</h3>
                    <span class="badge badge-${lot.statut == 'ACTIF' ? 'success' : lot.statut == 'ALERTE_PEREMPTION' ? 'warning' : 'danger'}">
                        ${lot.statut.label}
                    </span>
                </div>

                <div class="detail-body">
                    <div class="detail-row">
                        <label>Article:</label>
                        <span>${lot.article.code} - ${lot.article.designation}</span>
                    </div>
                    <div class="detail-row">
                        <label>Dépôt:</label>
                        <span>${lot.depot.nom}</span>
                    </div>
                    <div class="detail-row">
                        <label>Quantité Initiale:</label>
                        <span>${lot.quantiteInitiale}</span>
                    </div>
                    <div class="detail-row">
                        <label>Quantité Disponible:</label>
                        <span><strong>${lot.quantiteDisponible}</strong></span>
                    </div>
                    <div class="detail-row">
                        <label>Prix Unitaire:</label>
                        <span><fmt:formatNumber value="${lot.prixUnitaire}" type="currency"/></span>
                    </div>
                    <div class="detail-row">
                        <label>Valeur Total:</label>
                        <span><fmt:formatNumber value="${lot.quantiteDisponible * lot.prixUnitaire}" type="currency"/></span>
                    </div>

                    <c:if test="${lot.dateExpiration != null}">
                        <div class="detail-row">
                            <label>Date Expiration:</label>
                            <span class="${lot.estExpire() ? 'text-danger' : lot.estProchePeremption() ? 'text-warning' : ''}">
                                <fmt:formatDate value="${lot.dateExpiration}" pattern="dd/MM/yyyy"/>
                            </span>
                        </div>
                    </c:if>

                    <div class="detail-row">
                        <label>Date Production:</label>
                        <span><fmt:formatDate value="${lot.dateProduction}" pattern="dd/MM/yyyy"/></span>
                    </div>
                    <div class="detail-row">
                        <label>Date Création:</label>
                        <span><fmt:formatDate value="${lot.dateCreation}" pattern="dd/MM/yyyy HH:mm"/></span>
                    </div>

                    <c:if test="${lot.observations != null}">
                        <div class="detail-row">
                            <label>Observations:</label>
                            <span>${lot.observations}</span>
                        </div>
                    </c:if>
                </div>

                <c:if test="${lot.quantiteDisponible > 0 && lot.statut != 'EXPIRE'}">
                    <div class="detail-footer">
                        <form method="post" action="/stock/lots/${lot.id}/preleve" class="form-inline">
                            <div class="form-group">
                                <label for="quantite">Quantité à Prélever:</label>
                                <input type="number" id="quantite" name="quantite" class="form-control" 
                                       step="0.01" max="${lot.quantiteDisponible}" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Prélever</button>
                        </form>
                    </div>
                </c:if>
            </div>
        </c:if>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
