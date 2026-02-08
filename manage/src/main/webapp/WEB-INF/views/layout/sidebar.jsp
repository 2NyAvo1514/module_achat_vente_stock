<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div
  class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse"
  id="sidebarMenu"
>
  <div class="position-sticky pt-3">
    <ul class="nav flex-column">
      <c:set var="path" value="${pageContext.request.requestURI}" />
      <li class="nav-item">
        <a
          class="nav-link d-flex justify-content-between align-items-center ${fn:contains(path,'/achats') ? 'active' : ''}"
          data-bs-toggle="collapse"
          href="#achatsSubmenu"
          role="button"
          aria-expanded="false"
          aria-controls="achatsSubmenu"
        >
          <span><span data-feather="shopping-cart"></span> Achats</span>
          <span class="small">▼</span>
        </a>
        <div
          class="collapse ${fn:contains(path,'/achats') ? 'show' : ''}"
          id="achatsSubmenu"
        >
          <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
            <li>
              <a href="/achats/demandes" class="link-dark nav-link"
                >Demandes d'Achat (DA)</a
              >
            </li>
            <li>
              <a href="/achats/demandes/nouvelle" class="link-dark nav-link"
                >Nouvelle DA</a
              >
            </li>
            <li>
              <a href="/achats/commandes" class="link-dark nav-link"
                >Bons de Commande (BC)</a
              >
            </li>
            <li>
              <a href="/achats/commandes/creer" class="link-dark nav-link"
                >Créer BC</a
              >
            </li>
            <li>
              <a href="/achats/demandes/attente" class="link-dark nav-link"
                >Validation</a
              >
            </li>
          </ul>
        </div>
      </li>
      <li class="nav-item">
        <a
          class="nav-link ${fn:contains(path,'/ventes') ? 'active' : ''}"
          href="/ventes"
        >
          <span data-feather="file-text"></span>
          Ventes
        </a>
      </li>
      <li class="nav-item">
        <a
          class="nav-link ${fn:contains(path,'/stock') ? 'active' : ''}"
          href="/stock"
        >
          <span data-feather="layers"></span>
          Stock
        </a>
      </li>
      <li class="nav-item">
        <a
          class="nav-link ${fn:contains(path,'/inventaire') ? 'active' : ''}"
          href="/inventaire"
        >
          <span data-feather="clipboard"></span>
          Inventaire
        </a>
      </li>
    </ul>
  </div>
</div>
