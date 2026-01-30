<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Demandes d'achat</title>
</head>
<body>
<h2>Demandes d'achat</h2>

<a href="${pageContext.request.contextPath}/achats/demandes/nouvelle">Nouvelle demande</a>

<table border="1">
    <tr>
        <th>Référence</th>
        <th>Statut</th>
        <th>Montant</th>
        <th>Actions</th>
    </tr>
    <c:forEach items="${demandes}" var="da">
        <tr>
            <td>${da.reference}</td>
            <td>${da.statut}</td>
            <td>${da.montantTotal}</td>
            <td>
                <a href="${pageContext.request.contextPath}/achats/demandes/${da.id}">Ouvrir</a>
                <c:if test="${da.statut == 'BROUILLON'}">
                    <form method="post" action="${pageContext.request.contextPath}/achats/demandes/${da.id}/soumettre" style="display:inline;">
                        <button type="submit">Soumettre</button>
                    </form>
                </c:if>
                <c:if test="${da.statut == 'SOUMISE'}">
                    <form method="post" action="${pageContext.request.contextPath}/achats/demandes/${da.id}/valider" style="display:inline;">
                        <button type="submit">Valider</button>
                    </form>
                </c:if>
                <c:if test="${da.statut == 'VALIDEE'}">
                    <a href="${pageContext.request.contextPath}/achats/commandes/creer/${da.id}">Créer BC</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
