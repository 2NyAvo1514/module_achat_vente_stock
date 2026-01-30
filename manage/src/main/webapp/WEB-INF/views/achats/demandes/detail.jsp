<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Détail Demande d'Achat</title>
</head>
<body>
<h2>Demande ${demande.reference}</h2>
<p>Statut : ${demande.statut}</p>
<p>Montant total : ${demande.montantTotal}</p>

<h3>Lignes</h3>
<table border="1">
    <tr>
        <th>Article</th>
        <th>Quantité</th>
        <th>Prix unitaire</th>
        <th>Total</th>
    </tr>
    <c:forEach items="${demande.lignes}" var="l">
        <tr>
            <td>${l.article.libelle}</td>
            <td>${l.quantite}</td>
            <td>${l.prixUnitaire}</td>
            <td>${l.totalLigne}</td>
        </tr>
    </c:forEach>
</table>

<h3>Ajouter une ligne</h3>
<form method="post" action="${pageContext.request.contextPath}/achats/demandes/${demande.id}/ligne/ajouter">
    Article :
    <select name="article.id">
        <c:forEach items="${articles}" var="a">
            <option value="${a.id}">${a.libelle}</option>
        </c:forEach>
    </select>
    Quantité : <input type="number" step="0.01" name="quantite"/>
    Prix unitaire : <input type="number" step="0.01" name="prixUnitaire"/>
    <button type="submit">Ajouter</button>
</form>

<br/>
<a href="${pageContext.request.contextPath}/achats/demandes">Retour</a>

</body>
</html>
