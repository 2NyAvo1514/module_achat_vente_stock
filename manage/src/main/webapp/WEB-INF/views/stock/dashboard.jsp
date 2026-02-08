<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord Stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin: 20px 0;
        }
        .dashboard-card {
            background: white;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .dashboard-card h3 {
            margin-top: 0;
            color: #333;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
        }
        .card-number {
            font-size: 2em;
            font-weight: bold;
            color: #007bff;
        }
        .alert-card {
            border-left: 5px solid #dc3545;
        }
        .warning-card {
            border-left: 5px solid #ffc107;
        }
        .success-card {
            border-left: 5px solid #28a745;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="content">
    <div class="container">
        <div class="page-header">
            <h1>Tableau de Bord Stock</h1>
        </div>

        <div class="filters">
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label for="depotId">Sélectionner un Dépôt:</label>
                    <select id="depotId" name="depotId" class="form-control" onchange="this.form.submit()">
                        <option value="">-- Tous les dépôts --</option>
                        <c:forEach var="depot" items="${depots}">
                            <option value="${depot.id}" <c:if test="${depot.id == depotId}">selected</c:if>>
                                    ${depot.nom}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </form>
        </div>

        <c:if test="${depotId != null}">
            <div class="dashboard-grid">
                <!-- Card: Articles en Alerte Min -->
                <div class="dashboard-card alert-card">
                    <h3>⚠️ Articles Minimum</h3>
                    <div class="card-number">${alertesMin.size()}</div>
                    <p>Articles en dessous du stock minimum</p>
                    <c:if test="${alertesMin.size() > 0}">
                        <a href="/stock/niveaux/alertes/${depotId}" class="btn btn-sm btn-danger">Voir</a>
                    </c:if>
                </div>

                <!-- Card: Surstock -->
                <div class="dashboard-card warning-card">
                    <h3>📦 Surstock</h3>
                    <div class="card-number">${surstock.size()}</div>
                    <p>Articles au-dessus du stock maximum</p>
                    <c:if test="${surstock.size() > 0}">
                        <a href="/stock/niveaux/surstock/${depotId}" class="btn btn-sm btn-warning">Voir</a>
                    </c:if>
                </div>

                <!-- Card: Lots Alerte Péremption -->
                <div class="dashboard-card warning-card">
                    <h3>⏰ Lots Péremption</h3>
                    <div class="card-number">${lotsAlerte.size()}</div>
                    <p>Lots proches de l'expiration</p>
                    <c:if test="${lotsAlerte.size() > 0}">
                        <a href="/stock/lots/alertes/${depotId}" class="btn btn-sm btn-warning">Voir</a>
                    </c:if>
                </div>

                <!-- Card: Lots Expirés -->
                <div class="dashboard-card alert-card">
                    <h3>🗑️ Lots Expirés</h3>
                    <div class="card-number">${lotsExpires.size()}</div>
                    <p>Lots à éliminer</p>
                    <c:if test="${lotsExpires.size() > 0}">
                        <a href="/stock/lots/expires/${depotId}" class="btn btn-sm btn-danger">Voir</a>
                    </c:if>
                </div>

                <!-- Card: Réservations Expirées -->
                <div class="dashboard-card alert-card">
                    <h3>📋 Réserv. Expirées</h3>
                    <div class="card-number">${reservationsExpirees.size()}</div>
                    <p>Réservations à annuler</p>
                    <c:if test="${reservationsExpirees.size() > 0}">
                        <form method="post" action="/stock/reservations/cancel-expired" style="display:inline;">
                            <button type="submit" class="btn btn-sm btn-danger">Annuler</button>
                        </form>
                    </c:if>
                </div>
            </div>

            <!-- Quick Actions -->
            <div class="dashboard-grid" style="grid-template-columns: 1fr;">
                <div class="dashboard-card success-card">
                    <h3>Actions Rapides</h3>
                    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 10px;">
                        <a href="/stock/mouvements/entrees/nouveau" class="btn btn-success">+ Entrée</a>
                        <a href="/stock/mouvements/sorties/nouveau" class="btn btn-danger">+ Sortie</a>
                        <a href="/stock/mouvements/transferts/nouveau" class="btn btn-info">+ Transfert</a>
                        <a href="/stock/lots/nouveau" class="btn btn-primary">+ Lot</a>
                        <a href="/stock/reservations/nouvelle" class="btn btn-secondary">+ Réservation</a>
                        <a href="/stock/niveaux?depotId=${depotId}" class="btn btn-outline">Niveaux Stock</a>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${depotId == null}">
            <div class="alert alert-info">
                Sélectionnez un dépôt pour voir le tableau de bord.
            </div>
        </c:if>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>
