<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nouvelle Entrée de Stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container form-container">
        <div class="page-header">
            <h1>Créer une Entrée de Stock</h1>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="/stock/mouvements/entrees/creer" class="form">
            <div class="form-section">
                <h3>Informations Entrée</h3>

                <div class="form-group">
                    <label for="articleId">Article *</label>
                    <select id="articleId" name="articleId" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="article" items="${articles}">
                            <option value="${article.id}">${article.code} - ${article.designation}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="depotId">Dépôt Destination *</label>
                    <select id="depotId" name="depotId" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="depot" items="${depots}">
                            <option value="${depot.id}">${depot.nom}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="quantite">Quantité *</label>
                        <input type="number" id="quantite" name="quantite" class="form-control" 
                               step="0.01" required placeholder="Quantité">
                    </div>
                    <div class="form-group col-6">
                        <label for="prixUnitaire">Prix Unitaire *</label>
                        <input type="number" id="prixUnitaire" name="prixUnitaire" class="form-control" 
                               step="0.01" required placeholder="P.U.">
                    </div>
                </div>

                <div class="form-group">
                    <label for="numeroReference">Numéro Référence (BC/Réception) *</label>
                    <input type="text" id="numeroReference" name="numeroReference" class="form-control" 
                           required placeholder="Bon de commande ou réception">
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">Créer Entrée</button>
                <a href="/stock/mouvements" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
