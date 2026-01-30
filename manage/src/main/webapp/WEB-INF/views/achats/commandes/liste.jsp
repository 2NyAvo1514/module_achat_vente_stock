<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Bons de commande</title>
</head>
<body>
<h2>Bons de commande fournisseurs</h2>

<table border="1">
    <tr>
        <th>Référence</th>
        <th>Fournisseur</th>
        <th>Statut</th>
        <th>Montant</th>
        <th>Actions</th>
    </tr>
    <c:forEach items="${commandes}" var="bc">
        <tr>
            <td>${bc.reference}</td>
            <td>${bc.fournisseur.nom}</td>
            <td>${bc.statut}</td>
            <td>${bc.montantTotal}</td>
            <td>
                <c:if test="${bc.statut == 'BROUILLON'}">
                    <form method="post" action="${pageContext.request.contextPath}/achats/commandes/${bc.id}/valider">
                        <button type="submit">Valider</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="${pageContext.request.contextPath}/achats/demandes">Retour aux demandes</a>

</body>
</html>
