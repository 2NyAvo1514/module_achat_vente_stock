<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Nouvelle Demande d'Achat</title>
</head>
<body>
<h2>Nouvelle Demande d'Achat</h2>

<form method="post" action="${pageContext.request.contextPath}/achats/demandes/creer">
    Site :
    <select name="site.id">
        <c:forEach items="${sites}" var="s">
            <option value="${s.id}">${s.nom}</option>
        </c:forEach>
    </select>
    <br/><br/>
    <button type="submit">Créer</button>
</form>

</body>
</html>
