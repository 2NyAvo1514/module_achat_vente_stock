<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nouvelle Réservation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container form-container">
        <div class="page-header">
            <h1>Créer une Réservation</h1>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="/stock/reservations/creer" class="form">
            <div class="form-section">
                <h3>Informations Réservation</h3>

                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="articleId">Article *</label>
                        <select id="articleId" name="articleId" class="form-control" required>
                            <option value="">-- Sélectionner --</option>
                            <c:forEach var="article" items="${articles}">
                                <option value="${article.id}">${article.code} - ${article.designation}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group col-6">
                        <label for="depotId">Dépôt *</label>
                        <select id="depotId" name="depotId" class="form-control" required>
                            <option value="">-- Sélectionner --</option>
                            <c:forEach var="depot" items="${depots}">
                                <option value="${depot.id}">${depot.nom}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="referenceCommande">Référence Commande Client *</label>
                        <input type="text" id="referenceCommande" name="referenceCommande" class="form-control" 
                               required placeholder="N° commande client">
                    </div>

                    <div class="form-group col-6">
                        <label for="quantite">Quantité à Réserver *</label>
                        <input type="number" id="quantite" name="quantite" class="form-control" 
                               step="0.01" required placeholder="Quantité">
                    </div>
                </div>

                <div class="form-group">
                    <label for="delaiPrelevement">Délai de Prélèvement</label>
                    <input type="datetime-local" id="delaiPrelevement" name="delaiPrelevement" 
                           class="form-control" placeholder="Laissez vide pour défaut (7 jours)">
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">Créer Réservation</button>
                <a href="/stock/reservations" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
