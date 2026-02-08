<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détail Demande - Gestion Entreprise</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../layout/header.jsp" %>
    <div class="container-fluid">
        <div class="row">
            <%@ include file="../../layout/sidebar.jsp" %>
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">

<div class="container-fluid">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2>Demande <small class="text-muted">${demande.reference}</small></h2>
                <div>
                    <a href="<c:url value='/achats/demandes'/>" class="btn btn-link">← Retour</a>
                </div>
            </div>

            <div class="card mb-3">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4"><strong>Statut</strong><div>${demande.statut}</div></div>
                        <div class="col-md-4"><strong>Date</strong><div>${demande.dateCreation}</div></div>
                        <div class="col-md-4 text-end"><strong>Montant</strong><div>${demande.montantTotal} €</div></div>
                    </div>
                </div>
            </div>

            <div class="card mb-3">
                <div class="card-header">Lignes</div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Article</th>
                                    <th>Quantité</th>
                                    <th>Prix unitaire</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${demande.lignes}" var="l">
                                    <tr>
                                        <td>${l.article.libelle}</td>
                                        <td>${l.quantite}</td>
                                        <td>${l.prixUnitaire}</td>
                                        <td>${l.totalLigne}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">Ajouter une ligne</div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/achats/demandes/${demande.id}/ligne/ajouter" class="row g-3">
                        <div class="col-md-5">
                            <label class="form-label">Article</label>
                            <select name="article.id" class="form-select">
                                <c:forEach items="${articles}" var="a">
                                    <option value="${a.id}">${a.libelle}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Quantité</label>
                            <input type="number" step="0.01" name="quantite" class="form-control"/>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Prix unitaire</label>
                            <input type="number" step="0.01" name="prixUnitaire" class="form-control"/>
                        </div>
                        <div class="col-md-2 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary">Ajouter</button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>
</div>

            </main>
        </div>
    </div>
    <%@ include file="../../layout/footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
