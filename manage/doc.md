
# PROMPT POUR REPRENDRE LE PROJET - APPLICATION WEB ACHATS/VENTES/STOCK

## CONTEXTE DU PROJET
Je développe une application web intégrée de gestion Achats/Ventes/Stock/Inventaires pour une grande entreprise avec Spring Boot (backend) et JSP (frontend).

### Problématiques à résoudre :
- Processus dispersés (Excel, emails, ERP partiel)
- Traçabilité insuffisante (qui a fait quoi/quand/pourquoi)
- Risques : ruptures, surstock, pertes, erreurs de valorisation, fraude interne

### Objectifs principaux :
- Centralisation des flux : achats → réception → stockage → sortie → vente → facturation
- Standardisation des méthodes de gestion
- Renforcement contrôle interne (séparation des tâches, validations)
- Pilotage par KPI par rôle/service/site
- Réduction délais, erreurs et écarts d'inventaire

## STRUCTURE DE DOSSIERS ADOPTÉE (Option 1 - Interface unifiée)
```
achat-vente-stock-app/
├── 📁 src/main/
│ ├── 📁 java/com/entreprise/manage/
│ │ ├── 📁 config/ # Configurations
│ │ ├── 📁 core/ # Fonctionnalités transverses
│ │ │ ├── 📁 auth/ # RBAC + ABAC
│ │ │ ├── 📁 audit/ # Journalisation
│ │ │ └── 📁 exception/ # Gestion erreurs
│ │ │
│ │ ├── 📁 referentiels/ # Module Référentiels
│ │ │ ├── 📁 controller/
│ │ │ ├── 📁 service/
│ │ │ ├── 📁 repository/
│ │ │ └── 📁 model/
│ │ │
│ │ ├── 📁 achats/ # Module Achats
│ │ │ ├── 📁 demande-achat/
│ │ │ ├── 📁 commandes/
│ │ │ ├── 📁 reception/
│ │ │ └── 📁 factures-fournisseur/
│ │ │
│ │ ├── 📁 ventes/ # Module Ventes
│ │ │ ├── 📁 devis/
│ │ │ ├── 📁 commandes-client/
│ │ │ ├── 📁 livraison/
│ │ │ └── 📁 facturation-client/
│ │ │
│ │ ├── 📁 stock/ # Module Stock
│ │ │ ├── 📁 mouvements/
│ │ │ ├── 📁 gestion-lots/
│ │ │ ├── 📁 emplacements/
│ │ │ └── 📁 valorisation/
│ │ │
│ │ ├── 📁 inventaire/ # Module Inventaire
│ │ ├── 📁 reporting/ # Module Reporting/KPI
│ │ ├── 📁 administration/ # Module Administration
│ │ └── Application.java
│ │
│ ├── 📁 resources/
│ │ ├── 📁 static/ # CSS, JS, images
│ │ ├── 📁 templates/ # Pages JSP
│ │ │ └── 📁 WEB-INF/views/
│ │ │ ├── 📁 layout/ # Layouts communs
│ │ │ ├── 📁 shared/ # Composants réutilisables
│ │ │ ├── 📁 dashboard/ # Tableaux de bord
│ │ │ ├── 📁 referentiels/ # Pages référentiels
│ │ │ ├── 📁 achats/ # Module Achats
│ │ │ ├── 📁 ventes/ # Module Ventes
│ │ │ ├── 📁 stock/ # Module Stock
│ │ │ └── 📁 administration/ # Administration
│ │ │
│ │ ├── application.properties
│ │ └── 📁 db/migration/
│ │
│ └── 📁 webapp/ # Racine web (si différent)
│
├── 📁 tests/ # Tests unitaires/intégration
├── 📁 docs/ # Documentation
├── 📁 scripts/ # Scripts déploiement
└── pom.xml/docker-compose.yml
```

## FONCTIONNALITÉS PAR MODULE DÉTAILLÉES

### 1. MODULE CORE (Transverse)
- Authentification RBAC + ABAC (rôles: Acheteur, Magasinier, Commercial, Contrôleur, DAF)
- Restrictions par attributs: site, dépôt, famille articles, montant, département
- Principe du moindre privilège
- Journalisation complète non modifiable
- Accès temporaire avec délégation
- Double validation sur opérations sensibles

### 2. MODULE RÉFÉRENTIELS
- Articles (catégories, familles, unités, tarifs, lots/séries DLUO/DLC)
- Tiers (fournisseurs, clients avec classification)
- Sites/Dépôts/Emplacements (multi-sites support)
- Taxes, devises, paramètres généraux

### 3. MODULE ACHATS (Workflow complet)
- Demande d'Achat (DA) avec workflow validation N1/N2/N3 selon seuils
- Commande Fournisseur (BC) avec contrôle "même personne ne peut créer+valider"
- Réception avec contrôle qualité et réception partielle
- Facture fournisseur avec 3-way match (BC/Réception/Facture)
- Règles: séparation création/validation, blocage si litiges

### 4. MODULE VENTES
- Devis/Pro-forma avec simulation remises
- Commande client avec réservation stock automatique
- Livraison (picking list, scan, priorités)
- Facturation client, avoirs, encaissements
- Contrôle: pas de livraison sans stock réservé

### 5. MODULE STOCK
- Mouvements (entrées/sorties/transferts) tracés
- Gestion lots/séries avec alertes péremption
- Valorisation FIFO/CUMP avec clôture mensuelle
- Réservation stock à la commande
- Allocation FIFO/FEFO selon nature produit

### 6. MODULE INVENTAIRE
- Inventaire tournant avec planification cycles
- Inventaire annuel avec gel des mouvements
- Gestion écarts avec workflow d'ajustement
- Contrôle: même personne ne peut inventorier+valider ajustement

### 7. MODULE REPORTING/KPI
- Tableaux de bord par rôle (Direction, Achats, Ventes, Stock, Finance)
- KPI temps réel: rotation stock, précision inventaire, cycle times
- Exports Excel/PDF/API
- Alertes seuils dépassés

### 8. MODULE ADMINISTRATION
- Gestion utilisateurs, rôles, habilitations
- Paramétrage workflows, seuils, notifications
- Audit et conformité
- Maintenance système

## POINTS SPÉCIFIQUES À DÉVELOPPER EN PRIORITÉ

### POINT 1: ARCHITECTURE SPRING BOOT POUR LE MODULE ACHATS
Je veux l'implémentation complète du workflow "Demande d'Achat → Commande Fournisseur" avec:
1. Entités JPA pour DA, LigneDA, BC, LigneBC
2. Services avec logique métier et validation des règles
3. Contrôleurs REST/Spring MVC
4. Pages JSP avec formulaires dynamiques
5. Workflow de validation configurable (seuils par montant/catégorie)
6. Intégration RBAC/ABAC (ex: acheteur ne peut valider sa propre DA)

### POINT 2: SYSTÈME RBAC + ABAC SPRING SECURITY
Configuration complète avec:
1. Rôles hiérarchiques (Opérateur → Superviseur → Manager → Directeur)
2. Restrictions par attributs (Annotation custom @PreAuthorize)
3. Voters personnalisés pour ABAC
4. Menu dynamique JSP basé sur permissions
5. Journalisation automatique des accès

### POINT 3: GESTION STOCK AVEC TRANSACTIONS
Implémentation des mouvements de stock avec:
1. Service transactionnel pour entrées/sorties
2. Gestion des réservations (optimistic locking)
3. Calcul coût moyen pondéré (CUMP) en temps réel
4. API pour scan RFID/Code-barres
5. Synchronisation multi-dépôts

### POINT 4: INTERFACE JSP AVEC COMPOSANTS RÉUTILISABLES
Structure JSP/Taglibs avec:
1. Layout commun (header, sidebar dynamique, footer)
2. Composants: formulaire recherche avancée, table paginée, workflow visuel
3. Fragments pour formulaires récurrents (ligne commande, etc.)
4. Intégration JavaScript pour validations client
5. Responsive design pour terminaux mobiles en entrepôt

### POINT 5: KPI ET REPORTING PERFORMANT
Architecture pour indicateurs temps réel:
1. Entités dédiées pour stockage KPI historisés
2. Services de calcul batch et temps réel
3. API REST pour dashboard dynamique
4. Cache Redis pour performances
5. Export Excel avec template personnalisé

## CONTRAINTES TECHNIQUES
- Spring Boot 2.7+ avec Java 11+
- Base de données: PostgreSQL pour transactionnel, Redis pour cache
- JSP avec JSTL et Taglibs personnalisés
- Spring Security avec OAuth2 resource server
- Spring Data JPA + QueryDSL pour requêtes complexes
- Flyway/Liquibase pour migrations base
- Docker pour conteneurisation
- API REST pour intégrations futures

## DEMANDE SPÉCIFIQUE POUR CE CHAT
[À ADAPTER SELON LE POINT QUE VOUS VOULEZ TRAITER EN PRIORITÉ]

Exemple 1: "Donne-moi l'implémentation complète du Module Achats avec les entités, services, contrôleurs et pages JSP pour la Demande d'Achat et son workflow de validation."

Exemple 2: "Montre-moi la configuration Spring Security complète pour RBAC+ABAC avec des exemples concrets de restrictions par site/dépôt/montant."

Exemple 3: "Propose l'architecture des services de stock avec gestion transactionnelle, calcul CUMP, et API pour terminaux de scan."