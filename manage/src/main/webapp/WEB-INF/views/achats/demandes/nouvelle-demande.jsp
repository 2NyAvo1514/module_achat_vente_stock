<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nouvelle Demande - Gestion Entreprise</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="../../layout/header.jsp" %>
    <div class="container-fluid">
        <div class="row">
            <%@ include file="../../layout/sidebar.jsp" %>
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">

                <div class="container">
                    <div class="row justify-content-center">
                        <div class="col-lg-10">
                            <div class="card mb-3">
                                <div class="card-header">Nouvelle Demande d'Achat</div>
                                <div class="card-body">
                                    <!-- DEBUG: Afficher les données reçues -->
                                    <div class="alert alert-info d-none">
                                        <strong>Debug:</strong>
                                        Articles reçus: ${articles.size()}
                                        <br>
                                        Sites reçus: ${sites.size()}
                                    </div>
                                    
                                    <form method="post" action="${pageContext.request.contextPath}/achats/demandes/enregistrer">
                                        <div class="mb-3">
                                            <label class="form-label">Site</label>
                                            <select name="site.id" class="form-select" required>
                                                <option value="">-- Sélectionner un site --</option>
                                                <c:forEach items="${sites}" var="s">
                                                    <option value="${s.id}">${s.nom}</option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="table-responsive mb-3">
                                            <table class="table table-sm" id="lignesTable">
                                                <thead>
                                                    <tr>
                                                        <th>Article</th>
                                                        <th>Quantité</th>
                                                        <th>Prix Unitaire</th>
                                                        <th>Total Ligne</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <!-- Les lignes seront ajoutées dynamiquement -->
                                                </tbody>
                                                <tfoot>
                                                    <tr>
                                                        <td colspan="3" class="text-end"><strong>Total Demande:</strong></td>
                                                        <td><input type="number" id="totalDemande" name="montantTotal" readonly class="form-control form-control-sm" value="0.00"/></td>
                                                        <td></td>
                                                    </tr>
                                                </tfoot>
                                            </table>
                                        </div>

                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <div>
                                                <button type="button" class="btn btn-sm btn-outline-primary" onclick="ajouterLigne()">
                                                    <i class="fas fa-plus"></i> Ajouter une ligne
                                                </button>
                                            </div>
                                        </div>

                                        <div class="text-end">
                                            <button type="submit" name="action" value="brouillon" class="btn btn-secondary">
                                                <i class="fas fa-save"></i> Enregistrer brouillon
                                            </button>
                                            <button type="submit" name="action" value="soumettre" class="btn btn-primary">
                                                <i class="fas fa-paper-plane"></i> Soumettre
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </main>
        </div>
    </div>
    <%@ include file="../../layout/footer.jsp" %>
    
    <!-- Scripts JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    
    <!-- Script pour la gestion des lignes de demande -->
    <script>
        // Articles passés depuis le serveur
        const articlesData = [
            <c:forEach items="${articles}" var="article">
            {
                id: "${article.id}",
                libelle: "${article.libelle}"
            },
            </c:forEach>
        ];
        
        console.log('Articles chargés depuis le serveur:', articlesData);
        
        function creerOptionsArticles() {
            if (!articlesData || articlesData.length === 0) {
                console.warn('Aucun article disponible');
                return '<option value="">Aucun article disponible</option>';
            }
            
            let options = '<option value="">-- Sélectionner un article --</option>';
            articlesData.forEach(article => {
                options += `<option value="\${article.id}">\${article.libelle}</option>`;
            });
            return options;
        }
        
        function ajouterLigne() {
            const tbody = document.querySelector("#lignesTable tbody");
            const ligneCount = tbody.children.length;
            
            const ligneHtml = `
                <tr>
                    <td>
                        <select name="lignes[\${ligneCount}].article.id" class="form-select form-select-sm article-select" required>
                            \${creerOptionsArticles()}
                        </select>
                    </td>
                    <td>
                        <input type="number" name="lignes[\${ligneCount}].quantite" 
                               class="form-control form-control-sm quantite" 
                               min="0.01" step="0.01" value="1.00" 
                               onchange="calculerTotaux()" required>
                    </td>
                    <td>
                        <input type="number" name="lignes[\${ligneCount}].prixUnitaire" 
                               class="form-control form-control-sm prix" 
                               min="0" step="0.01" value="0.00" 
                               onchange="calculerTotaux()" required>
                    </td>
                    <td>
                        <input type="number" name="lignes[\${ligneCount}].totalLigne" 
                               class="form-control form-control-sm total-ligne" 
                               readonly value="0.00">
                    </td>
                    <td>
                        <button type="button" class="btn btn-sm btn-outline-danger" onclick="supprimerLigne(this)">
                            <i class="fas fa-times">✖ Supprimer</i>
                        </button>
                    </td>
                </tr>
            `;
            
            tbody.insertAdjacentHTML('beforeend', ligneHtml);
            calculerTotaux();
            console.log('Ligne ajoutée, total lignes:', ligneCount + 1);
        }
        
        function supprimerLigne(button) {
            const row = button.closest('tr');
            row.remove();
            recalculerIndexLignes();
            calculerTotaux();
        }
        
        function recalculerIndexLignes() {
            const rows = document.querySelectorAll('#lignesTable tbody tr');
            rows.forEach((row, index) => {
                // Mettre à jour les noms des champs
                row.querySelector('.article-select').name = `lignes[${index}].article.id`;
                row.querySelector('.quantite').name = `lignes[${index}].quantite`;
                row.querySelector('.prix').name = `lignes[${index}].prixUnitaire`;
                row.querySelector('.total-ligne').name = `lignes[${index}].totalLigne`;
            });
        }
        
        function calculerTotaux() {
            let totalGeneral = 0;
            const rows = document.querySelectorAll('#lignesTable tbody tr');
            
            rows.forEach(row => {
                const quantite = parseFloat(row.querySelector('.quantite').value) || 0;
                const prix = parseFloat(row.querySelector('.prix').value) || 0;
                const totalLigne = quantite * prix;
                
                row.querySelector('.total-ligne').value = totalLigne.toFixed(2);
                totalGeneral += totalLigne;
            });
            
            document.getElementById('totalDemande').value = totalGeneral.toFixed(2);
        }
        
        // Initialisation
        document.addEventListener('DOMContentLoaded', function() {
            console.log('Page chargée, initialisation...');
            console.log('Nombre d\'articles disponibles:', articlesData.length);
            
            // Ajouter une première ligne par défaut
            ajouterLigne();
            
            // Activer le mode debug si nécessaire
            const debugInfo = document.querySelector('.alert-info');
            if (debugInfo && articlesData.length === 0) {
                debugInfo.classList.remove('d-none');
            }
        });
    </script>
</body>
</html>