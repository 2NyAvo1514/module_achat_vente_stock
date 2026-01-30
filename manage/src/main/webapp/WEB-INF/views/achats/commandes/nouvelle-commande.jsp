<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Créer Bon de Commande</title>
</head>
<body>

<h2>Créer Bon de Commande depuis la DA ${demande.reference}</h2>

<form method="post" action="${pageContext.request.contextPath}/achats/commandes/creer">

    <input type="hidden" name="demandeId" value="${demande.id}"/>

    <label>Fournisseur :</label>
    <select name="fournisseurId" required>
        <c:forEach items="${fournisseurs}" var="f">
            <option value="${f.id}">${f.nom}</option>
        </c:forEach>
    </select>

    <br/><br/>

    <table border="1">
        <tr>
            <th>Article</th>
            <th>Quantité</th>
            <th>Prix Unitaire</th>
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

    <br/>

    <strong>Total commande : ${demande.montantTotal}</strong>

    <br/><br/>

    <button type="submit">✅ Créer Bon de Commande</button>

</form>

</body>
</html>
