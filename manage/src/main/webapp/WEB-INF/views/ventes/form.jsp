<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Créer commande</title>
</head>
<body>
<h1>Créer une commande client</h1>
<form method="post" action="${pageContext.request.contextPath}/ventes/create">
    <label>Client: <input type="text" name="client" required/></label><br/>
    <label>Référence: <input type="text" name="reference"/></label><br/>
    <label>Produit: <input type="text" name="produit" required/></label><br/>
    <label>Quantité: <input type="number" name="quantite" value="1" required/></label><br/>
    <label>Prix unitaire: <input type="number" step="0.01" name="prixUnitaire" value="0.0" required/></label><br/>
    <button type="submit">Créer</button>
</form>
<p><a href="${pageContext.request.contextPath}/ventes">Retour à la liste</a></p>
</body>
</html>
