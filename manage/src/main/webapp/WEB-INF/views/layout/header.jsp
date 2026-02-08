<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container-fluid">
    <a class="navbar-brand" href="/">Gestion Entreprise</a>
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#mainNavbar"
      aria-controls="mainNavbar"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="mainNavbar">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <c:set var="path" value="${pageContext.request.requestURI}" />
        <li class="nav-item dropdown">
          <a
            class="nav-link dropdown-toggle ${fn:contains(path,'/achats') ? 'active' : ''}"
            href="#"
            id="achatsMenu"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            >Achats</a
          >
          <ul class="dropdown-menu" aria-labelledby="achatsMenu">
            <li>
              <a class="dropdown-item" href="/achats/demandes"
                >Demandes d'Achat (DA)</a
              >
            </li>
            <li>
              <a class="dropdown-item" href="/achats/commandes"
                >Bons de Commande (BC)</a
              >
            </li>
            <li>
              <a class="dropdown-item" href="/achats/demandes/nouvelle"
                >Créer DA</a
              >
            </li>
            <li>
              <a class="dropdown-item" href="/achats/commandes/creer"
                >Créer BC</a
              >
            </li>
            <li><hr class="dropdown-divider" /></li>
            <li>
              <a class="dropdown-item" href="/achats/demandes/attente"
                >Validation / Files d'attente</a
              >
            </li>
          </ul>
        </li>

        <li class="nav-item dropdown">
          <a
            class="nav-link dropdown-toggle ${fn:contains(path,'/ventes') ? 'active' : ''}"
            href="#"
            id="ventesMenu"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            >Ventes</a
          >
          <ul class="dropdown-menu" aria-labelledby="ventesMenu">
            <li><a class="dropdown-item" href="/ventes/devis">Devis</a></li>
            <li>
              <a class="dropdown-item" href="/ventes/commandes"
                >Commandes clients</a
              >
            </li>
            <li>
              <a class="dropdown-item" href="/ventes/factures">Factures</a>
            </li>
          </ul>
        </li>

        <li class="nav-item dropdown">
          <a
            class="nav-link dropdown-toggle ${fn:contains(path,'/stock') ? 'active' : ''}"
            href="#"
            id="stockMenu"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            >Stock</a
          >
          <ul class="dropdown-menu" aria-labelledby="stockMenu">
            <li>
              <a class="dropdown-item" href="/stock/mouvements">Mouvements</a>
            </li>
            <li>
              <a class="dropdown-item" href="/stock/transferts">Transferts</a>
            </li>
            <li>
              <a class="dropdown-item" href="/stock/valuation">Valorisation</a>
            </li>
          </ul>
        </li>

        <li class="nav-item">
          <a
            class="${fn:contains(path,'/inventaire') ? 'nav-link active' : 'nav-link'}"
            href="/inventaire"
            >Inventaire</a
          >
        </li>
      </ul>
      <ul class="navbar-nav">
        <li class="nav-item dropdown">
          <a
            class="nav-link dropdown-toggle"
            href="#"
            id="userMenu"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            <c:out
              value="${sessionScope.utilisateur != null ? sessionScope.utilisateur.username : 'Utilisateur'}"
            />
          </a>
          <ul
            class="dropdown-menu dropdown-menu-end"
            aria-labelledby="userMenu"
          >
            <li><a class="dropdown-item" href="/profile">Profil</a></li>
            <li><a class="dropdown-item" href="/logout">Déconnexion</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>
