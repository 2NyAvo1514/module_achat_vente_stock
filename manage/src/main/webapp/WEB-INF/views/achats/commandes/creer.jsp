<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Créer Bon de Commande</title>
</head>
<body>
<h2>Créer BC depuis DA ${demande.reference}</h2>

<form method="post" action="${pageContext.request.contextPath}/achats/commandes/creer">
    <input type="hidden" name="daId" value="${demande.id}"/>

    Fournisseur :
    <select name="fournisseurId">
        <c:forEach items="${fournisseurs}" var="f">
            <option value="${f.id}">${f.nom}</option>
        </c:forEach>
    </select>

    <br/><br/>
    <button type="submit">Créer Bon de Commande</button>
</form>

</body>
</html>
