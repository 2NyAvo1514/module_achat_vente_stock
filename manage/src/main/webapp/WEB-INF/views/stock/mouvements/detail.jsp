<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Détail Mouvement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Détail Mouvement de Stock</h1>
            <a href="/stock/mouvements" class="btn btn-secondary">← Retour</a>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <c:if test="${mouvement != null}">
            <div class="detail-card">
                <div class="detail-header">
                    <h3>Mouvement #${mouvement.id}</h3>
                    <span class="badge badge-${mouvement.typeMouvement == 'ENTREE' ? 'success' : mouvement.typeMouvement == 'SORTIE' ? 'danger' : mouvement.typeMouvement == 'TRANSFERT' ? 'info' : 'warning'}">
                        ${mouvement.typeMouvement.label}
                    </span>
                    <span class="badge badge-${mouvement.statut == 'EXECUTEE' ? 'success' : mouvement.statut == 'PLANIFIEE' ? 'warning' : 'danger'}">
                        ${mouvement.statut.label}
                    </span>
                </div>

                <div class="detail-body">
                    <div class="detail-row">
                        <label>Article:</label>
                        <span>${mouvement.article.code} - ${mouvement.article.designation}</span>
                    </div>
                    <div class="detail-row">
                        <label>Quantité:</label>
                        <span>${mouvement.quantite} ${mouvement.article.unite.libelle}</span>
                    </div>
                    <div class="detail-row">
                        <label>Prix Unitaire:</label>
                        <span><fmt:formatNumber value="${mouvement.prixUnitaire}" type="currency"/></span>
                    </div>
                    <div class="detail-row">
                        <label>Coût Total:</label>
                        <span><fmt:formatNumber value="${coutTotal}" type="currency"/></span>
                    </div>
                    <div class="detail-row">
                        <label>Référence:</label>
                        <span><code>${mouvement.numeroReference}</code></span>
                    </div>

                    <c:if test="${mouvement.typeMouvement == 'TRANSFERT'}">
                        <div class="detail-row">
                            <label>Dépôt Départ:</label>
                            <span>${mouvement.depotDepart.nom}</span>
                        </div>
                        <div class="detail-row">
                            <label>Dépôt Destination:</label>
                            <span>${mouvement.depotDestination.nom}</span>
                        </div>
                    </c:if>

                    <c:if test="${mouvement.typeMouvement != 'TRANSFERT'}">
                        <div class="detail-row">
                            <label>Dépôt:</label>
                            <span>${mouvement.depotDestination.nom}</span>
                        </div>
                    </c:if>

                    <div class="detail-row">
                        <label>Créateur:</label>
                        <span>${mouvement.utilisateurCreateur.nom} ${mouvement.utilisateurCreateur.prenom}</span>
                    </div>
                    <div class="detail-row">
                        <label>Date Création:</label>
                        <span><fmt:formatDate value="${mouvement.dateCreation}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                    </div>

                    <c:if test="${mouvement.dateExecution != null}">
                        <div class="detail-row">
                            <label>Date Exécution:</label>
                            <span><fmt:formatDate value="${mouvement.dateExecution}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                        </div>
                    </c:if>

                    <c:if test="${mouvement.typeMouvement == 'AJUSTEMENT' && mouvement.motifAjustement != null}">
                        <div class="detail-row">
                            <label>Motif Ajustement:</label>
                            <span>${mouvement.motifAjustement}</span>
                        </div>
                    </c:if>

                    <c:if test="${mouvement.observations != null}">
                        <div class="detail-row">
                            <label>Observations:</label>
                            <span>${mouvement.observations}</span>
                        </div>
                    </c:if>
                </div>

                <div class="detail-footer">
                    <c:if test="${mouvement.statut == 'PLANIFIEE'}">
                        <form method="post" action="/stock/mouvements/${mouvement.id}/executer" style="display:inline;">
                            <button type="submit" class="btn btn-success">Exécuter</button>
                        </form>
                        <form method="post" action="/stock/mouvements/${mouvement.id}/annuler" style="display:inline;">
                            <button type="submit" class="btn btn-danger" onclick="return confirm('Confirmer l\'annulation?')">Annuler</button>
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
