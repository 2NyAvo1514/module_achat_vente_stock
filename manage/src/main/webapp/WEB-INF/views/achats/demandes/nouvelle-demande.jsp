<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Nouvelle Demande d'Achat</title>
    <script>
        function ajouterLigne() {
            const table = document.getElementById("lignesTable");
            const row = table.insertRow(-1);

            row.innerHTML = `
                <td>
                    <select name="lignes[].article.id">
                        <c:forEach items="${articles}" var="a">
                            <option value="${a.id}">${a.libelle}</option>
                        </c:forEach>
                    </select>
                </td>
                <td><input type="number" name="lignes[].quantite" step="0.01" oninput="calculerTotaux()"/></td>
                <td><input type="number" name="lignes[].prixUnitaire" step="0.01" oninput="calculerTotaux()"/></td>
                <td><input type="number" name="lignes[].totalLigne" readonly/></td>
                <td><button type="button" onclick="supprimerLigne(this)">❌</button></td>
            `;
        }

        function supprimerLigne(btn) {
            btn.closest("tr").remove();
            calculerTotaux();
        }

        function calculerTotaux() {
            let total = 0;
            const rows = document.querySelectorAll("#lignesTable tr");
            rows.forEach((row, i) => {
                if (i === 0) return;
                const qte = row.querySelector("input[name='lignes[].quantite']").value || 0;
                const pu = row.querySelector("input[name='lignes[].prixUnitaire']").value || 0;
                const totalLigne = qte * pu;
                row.querySelector("input[name='lignes[].totalLigne']").value = totalLigne.toFixed(2);
                total += totalLigne;
            });
            document.getElementById("totalDemande").value = total.toFixed(2);
        }
    </script>
</head>
<body>

<h2>Nouvelle Demande d'Achat</h2>

<form method="post" action="${pageContext.request.contextPath}/achats/demandes/creer">

    <label>Site :</label>
    <select name="site.id" required>
        <c:forEach items="${sites}" var="s">
            <option value="${s.id}">${s.nom}</option>
        </c:forEach>
    </select>

    <br/><br/>

    <table border="1" id="lignesTable">
        <tr>
            <th>Article</th>
            <th>Quantité</th>
            <th>Prix Unitaire</th>
            <th>Total Ligne</th>
            <th>Action</th>
        </tr>
    </table>

    <br/>
    <button type="button" onclick="ajouterLigne()">➕ Ajouter une ligne</button>

    <br/><br/>

    Total Demande :
    <input type="number" id="totalDemande" name="montantTotal" readonly/>

    <br/><br/>

    <button type="submit" name="action" value="brouillon">💾 Enregistrer</button>
    <button type="submit" name="action" value="soumettre">📤 Soumettre</button>

</form>

</body>
</html>
