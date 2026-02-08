<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nouvelle Commande - Gestion Entreprise</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../layout/header.jsp" %>
    <div class="container-fluid">
        <div class="row">
            <%@ include file="../../layout/sidebar.jsp" %>
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-lg-10">
            <div class="card">
                <div class="card-header">Créer Bon de Commande depuis la DA <small class="text-muted">${demande.reference}</small></div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/achats/commandes/creer">
                        <input type="hidden" name="demandeId" value="${demande.id}"/>

                        <div class="mb-3">
                            <label class="form-label">Fournisseur</label>
                            <select name="fournisseurId" class="form-select" required>
                                <c:forEach items="${fournisseurs}" var="f">
                                    <option value="${f.id}">${f.nom}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="table-responsive mb-3">
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>Article</th>
                                        <th>Quantité</th>
                                        <th>Prix Unitaire</th>
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

            </main>
        </div>
    </div>
    <%@ include file="../../layout/footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <strong>Total commande : ${demande.montantTotal} €</strong>
                            <div>
                                <button type="submit" class="btn btn-primary">✅ Créer Bon de Commande</button>
                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
