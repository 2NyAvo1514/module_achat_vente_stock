<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Commande</title>
</head>
<body>
<h1>Commande <c:out value="${commande.reference}"/></h1>
<p>Client: <c:out value="${commande.client}"/></p>
<p>Status: <c:out value="${commande.status}"/></p>
<p>Total: <c:out value="${commande.total}"/></p>
<h2>Lignes</h2>
<table border="1" cellpadding="4">
    <tr><th>Produit</th><th>Quantité</th><th>Prix unitaire</th></tr>
    <c:forEach var="l" items="${commande.lignes}">
        <tr>
            <td><c:out value="${l.produit}"/></td>
            <td><c:out value="${l.quantite}"/></td>
            <td><c:out value="${l.prixUnitaire}"/></td>
        </tr>
    </c:forEach>
</table>
<p><a href="${pageContext.request.contextPath}/ventes">Retour</a></p>
</body>
</html>
