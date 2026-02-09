<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Commandes clients</title>
</head>
<body>
<h1>Commandes clients</h1>
<a href="${pageContext.request.contextPath}/ventes/create">Créer une commande</a>
<table border="1" cellpadding="4" cellspacing="0">
    <tr><th>ID</th><th>Réf</th><th>Client</th><th>Total</th><th>Status</th><th>Action</th></tr>
    <c:forEach var="c" items="${commandes}">
        <tr>
            <td><c:out value="${c.id}"/></td>
            <td><c:out value="${c.reference}"/></td>
            <td><c:out value="${c.client}"/></td>
            <td><fmt:formatNumber value="${c.total}" type="number" minFractionDigits="2"/></td>
            <td><c:out value="${c.status}"/></td>
            <td><a href="${pageContext.request.contextPath}/ventes/${c.id}">Voir</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
