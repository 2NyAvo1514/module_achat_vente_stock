<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion - FreshDistrib</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .logo {
            text-align: center;
            margin-bottom: 30px;
            color: #2c3e50;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="logo">
                <h2>FreshDistrib</h2>
                <p class="text-muted">Gestion Achats/Stock</p>
            </div>
            
            <c:if test="${param.error != null}">
                <div class="alert alert-danger">
                    Identifiants incorrects
                </div>
            </c:if>
            
            <c:if test="${param.logout != null}">
                <div class="alert alert-success">
                    Vous avez été déconnecté avec succès
                </div>
            </c:if>
            
            <form method="post" action="/login">
                <div class="mb-3">
                    <label for="username" class="form-label">Nom d'utilisateur</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                
                <div class="mb-3">
                    <label for="password" class="form-label">Mot de passe</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                
                <button type="submit" class="btn btn-primary w-100">Se connecter</button>
                
                <div class="mt-3 text-center">
                    <small class="text-muted">
                        Comptes de test :<br>
                        acheteur1 / pass<br>
                        manager1 / pass
                    </small>
                </div>
            </form>
        </div>
    </div>
</body>
</html>