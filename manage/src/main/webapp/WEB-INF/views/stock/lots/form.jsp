<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nouveau Lot</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container form-container">
        <div class="page-header">
            <h1>Créer un Nouveau Lot</h1>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="/stock/lots/creer" class="form">
            <div class="form-section">
                <h3>Informations Lot</h3>

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
                        <label for="numerolot">Numéro Lot *</label>
                        <input type="text" id="numerolot" name="numerolot" class="form-control" 
                               required placeholder="Numéro de lot unique">
                    </div>

                    <div class="form-group col-6">
                        <label for="quantite">Quantité *</label>
                        <input type="number" id="quantite" name="quantite" class="form-control" 
                               step="0.01" required placeholder="Quantité">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="prixUnitaire">Prix Unitaire *</label>
                        <input type="number" id="prixUnitaire" name="prixUnitaire" class="form-control" 
                               step="0.01" required placeholder="P.U.">
                    </div>

                    <div class="form-group col-6">
                        <label for="dateExpiration">Date Expiration (DLUO/DLC)</label>
                        <input type="date" id="dateExpiration" name="dateExpiration" class="form-control">
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">Créer Lot</button>
                <a href="/stock/lots" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
