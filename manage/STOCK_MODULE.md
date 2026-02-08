# Module Stock - Documentation Complète

## 📋 Vue d'ensemble

Le module Stock gère l'intégralité des mouvements de stock, les niveaux disponibles, les lots avec traçabilité et les réservations pour les commandes client.

## 🏗️ Architecture

### Entités JPA

#### 1. **MouvementStock**

Enregistre chaque mouvement de stock (entrée, sortie, transfert, ajustement).

```java
- typeMouvement: ENTREE, SORTIE, TRANSFERT, AJUSTEMENT
- statut: PLANIFIEE, EXECUTEE, ANNULEE
- Optimistic Locking (@Version)
```

**Points clés:**

- Traçabilité complète (qui, quand, quoi, pourquoi)
- Support des transferts multi-dépôts
- Ajustement d'inventaire avec motif

#### 2. **NiveauStock**

Agrégat des quantités disponibles par article/dépôt.

```java
- quantiteDisponible: Stock physique
- quantiteReservee: Stock réservé par commandes
- quantiteCommandee: Stock en attente de réception
- stockMinimum/Maximum: Seuils d'alerte
- coutMoyenPondere: CUMP pour valorisation
```

**Calculs:**

- `quantiteUtilisable = quantiteDisponible - quantiteReservee`
- `CUMP = ((Qty * CUMP_ancien) + (Qty_entree * Prix_entree)) / (Qty + Qty_entree)`

#### 3. **LotStock**

Gestion des lots/séries avec traçabilité DLUO/DLC.

```java
- numerolot: Identifiant unique du lot
- dateExpiration: DLUO/DLC
- dateProduction: Pour FIFO
- statut: ACTIF, ALERTE_PEREMPTION, EXPIRE, UTILISE
```

**Flux:**

- Création au moment de l'entrée (réception fournisseur)
- Alertes automatiques à J-30 avant expiration
- Prélèvement FIFO ou FEFO

#### 4. **Reservation**

Réservation de stock pour les commandes client.

```java
- quantiteReservee: Quantité réservée
- quantitePrelevee: Quantité déjà sortie du stock
- statut: ACTIVE, PRELEVEE, LIVREE, ANNULEE
- dateExpiration: Délai de prélèvement
```

**Workflow:**

```
ACTIVE → PRELEVEE → LIVREE
   ↘ (Expiration) → ANNULEE
```

## 📦 Services

### NiveauStockService

**Principales opérations:**

```java
// Gestion des niveaux
NiveauStock getOrCreateNiveauStock(Long articleId, Long depotId)
void increaseDisponible(Long articleId, Long depotId, BigDecimal quantite, BigDecimal prix)
void decreaseDisponible(Long articleId, Long depotId, BigDecimal quantite)

// Calcul CUMP
void updateCUMPOnEntree(Long articleId, Long depotId, BigDecimal quantite, BigDecimal prix)

// Réservations
void addReservation(Long articleId, Long depotId, BigDecimal quantite)
void removeReservation(Long articleId, Long depotId, BigDecimal quantite)

// Alertes
List<NiveauStock> getArticlesEnAlerte(Long depotId)      // Sous minimum
List<NiveauStock> getArticlesEnSurstock(Long depotId)    // Au-dessus maximum

// Contrôle
boolean isAvailable(Long articleId, Long depotId, BigDecimal quantite)
```

**Transactionnalité:**

- `@Transactional` sur toutes les écritures
- Optimistic locking avec `@Version` sur l'entité
- Gestion des exceptions métier via `BusinessException`

### MouvementStockService

**Opérations principales:**

```java
// Créations
MouvementStock creerEntree(Long articleId, Long depotId, BigDecimal quantite, BigDecimal prix, String ref)
MouvementStock creerSortie(Long articleId, Long depotId, BigDecimal quantite, String ref)
MouvementStock creerTransfert(Long articleId, Long depotDepart, Long depotDest, BigDecimal quantite, String ref)
MouvementStock creerAjustement(Long articleId, Long depotId, BigDecimal quantite, String motif, String obs)

// Exécution
void executerMouvement(Long mouvementId)
void annulerMouvement(Long mouvementId)

// Requêtes
Page<MouvementStock> getMouvementsByArticle(Long articleId, Pageable page)
Page<MouvementStock> getMouvementsByDepot(Long depotId, Pageable page)
List<MouvementStock> getMouvementsPlanifiees()

// Valorisation
BigDecimal calculateCoutTotal(Long mouvementId)
```

**Validations:**

- Contrôle de disponibilité avant sortie/transfert
- Validation article et dépôt existent
- Dépôts différents pour transfert
- Statut PLANIFIEE avant exécution

### LotStockService

**Gestion des lots:**

```java
LotStock creerLot(Long articleId, Long depotId, String numerolot, BigDecimal quantite, BigDecimal prix, LocalDate dateExp)
void updateStatutLot(Long lotId, StatutLot newStatut)
void preleveFromLot(Long lotId, BigDecimal quantite)

// Requêtes
List<LotStock> getLotsEnAlerte(Long depotId)          // Expire à J-30
List<LotStock> getLotsExpires(Long depotId)           // Expirés
List<LotStock> getLotsDisponiblesFIFO(Long articleId, Long depotId)
List<LotStock> getLotsDisponiblesFEFO(Long articleId, Long depotId)

// Maintenance
void refreshStatuts(Long depotId)
long countExpirationsInRange(Long depotId, LocalDate debut, LocalDate fin)
```

**Stratégies d'allocation:**

- **FIFO** (First In First Out): Production ordre chronologique
- **FEFO** (First Expire First Out): Expiration ordre chronologique
- Choix selon nature produit (configuration article)

### ReservationService

**Workflow de réservation:**

```java
// Création avec vérification disponibilité
Reservation creerReservation(Long articleId, Long depotId, String refCommande, BigDecimal quantite, LocalDateTime delai)

// Prélèvements partiels
void preleveReservation(Long reservationId, BigDecimal quantite)
void marquerLivree(Long reservationId)

// Gestion libération
void libereReservation(Long reservationId)

// Contrôle expirations
List<Reservation> getReservationsExpirees()
void cancelExpired()

// Requêtes
List<Reservation> getReservationsByCommande(String referenceCommande)
List<Reservation> getReservationsActives(Long articleId, Long depotId)
Long getTotalReserve(Long articleId, Long depotId)
```

## 🎯 Contrôleurs

### StockDashboardController

**Route:** `/stock`

- Vue d'ensemble articles en alerte
- Statut lots péremption
- Actions rapides

### MouvementStockController

**Routes:**

- `GET /stock/mouvements` - Liste tous
- `GET /stock/mouvements/planifiees` - Mouvements non exécutés
- `POST /stock/mouvements/{id}/executer` - Exécuter
- `POST /stock/mouvements/{id}/annuler` - Annuler

### NiveauStockController

**Routes:**

- `GET /stock/niveaux` - Liste par dépôt
- `GET /stock/niveaux/alertes/{depotId}` - Articles sous minimum
- `GET /stock/niveaux/surstock/{depotId}` - Articles surstock

### LotStockController

**Routes:**

- `GET /stock/lots` - Liste par dépôt
- `GET /stock/lots/alertes/{depotId}` - Alertes péremption
- `GET /stock/lots/expires/{depotId}` - Lots expirés
- `POST /stock/lots/{id}/preleve` - Prélever quantité

### ReservationController

**Routes:**

- `GET /stock/reservations` - Liste
- `GET /stock/reservations/expirees` - Réservations expirées
- `POST /stock/reservations/{id}/preleve` - Prélever
- `POST /stock/reservations/{id}/liberer` - Libérer

## 🔄 Flux Métier

### Flux Réception (Entrée)

```
1. Bon de Réception créé dans Achats
2. MouvementStock.creerEntree() [PLANIFIEE]
3. LotStock.creerLot() avec numéro et DLUO
4. MouvementStock.executerMouvement()
   ├─ NiveauStock.increaseDisponible()
   ├─ CUMP recalculé
   └─ NiveauStock.setQuantiteCommandee(-quantite)
5. Facture fournisseur match la réception
```

### Flux Commande Client

```
1. Commande créée dans Ventes
2. Reservation.creerReservation() [ACTIVE]
   └─ NiveauStock.addReservation()
3. Picking: Lot FIFO/FEFO sélectionné
4. Sortie: MouvementStock.creerSortie() [PLANIFIEE]
5. Prélèvement: Reservation.preleveReservation() [PRELEVEE]
6. Expédition: MouvementStock.executerMouvement()
   └─ NiveauStock.decreaseDisponible()
7. Facturation: Reservation.marquerLivree() [LIVREE]
```

### Flux Transfert Interne

```
1. MouvementStock.creerTransfert() [PLANIFIEE]
2. MouvementStock.executerMouvement()
   ├─ NiveauStock[depot_depart].decreaseDisponible()
   └─ NiveauStock[depot_destination].increaseDisponible()
```

### Flux Inventaire/Ajustement

```
1. Inventaire tournant/annuel déclenché
2. MouvementStock.creerAjustement() [PLANIFIEE]
   └─ Motif: "Inventaire 2026-02", "Casse", etc.
3. Validation N1 (Magasinier ≠ Inventoriste)
4. MouvementStock.executerMouvement()
   ├─ Si quantite > 0: augmenter stock
   └─ Si quantite < 0: diminuer stock
```

## 🔐 Sécurité & Validations

### Permissions RBAC

```
Magasinier:
- CREATE: Mouvements, Lots, Réservations
- UPDATE: Mouvements (statut)
- READ: Tous

Superviseur Stock:
- All Magasinier perms
- DELETE: Mouvements (annulation)
- VALIDATE: Transferts > montant

DAF (Finance):
- READ: Valorisation, CUMP, Coûts

Responsable Inventaire:
- CREATE: Ajustements
- VALIDATE: Ajustements (autre personne)
```

### Validations Métier

**À l'exécution d'un mouvement:**

1. ✅ Statut = PLANIFIEE
2. ✅ Article existe et dépôt existe
3. ✅ Pour sortie/transfert: Stock disponible ≥ quantité demandée
4. ✅ Utilisateur a permission
5. ✅ Pas de raison métier de bloquer (litiges, etc.)

**À la réservation:**

1. ✅ Quantité disponible ≥ Quantité réservée
2. ✅ Dépôt accessible par utilisateur
3. ✅ Délai prélèvement raisonnable

**À l'ajustement:**

1. ✅ Motif documenté
2. ✅ Personne différente qui valide

## 📊 Modèle de Données

```sql
-- Niveaux
CREATE TABLE niveaux_stock (
  id BIGINT PRIMARY KEY,
  article_id BIGINT NOT NULL,
  depot_id BIGINT NOT NULL,
  quantite_disponible DECIMAL(15,2),
  quantite_reservee DECIMAL(15,2),
  quantite_commandee DECIMAL(15,2),
  stock_minimum DECIMAL(15,2),
  stock_maximum DECIMAL(15,2),
  cout_moyen_pondere DECIMAL(15,2),
  date_maj TIMESTAMP,
  version BIGINT,
  UNIQUE(article_id, depot_id)
);

-- Mouvements
CREATE TABLE mouvements_stock (
  id BIGINT PRIMARY KEY,
  type_mouvement VARCHAR(20),
  statut_mouvement VARCHAR(20),
  article_id BIGINT NOT NULL,
  depot_depart_id BIGINT,
  depot_destination_id BIGINT NOT NULL,
  quantite DECIMAL(15,2),
  prix_unitaire DECIMAL(15,2),
  numero_reference VARCHAR(50),
  utilisateur_createur_id BIGINT,
  date_creation TIMESTAMP,
  date_execution TIMESTAMP,
  motif_ajustement VARCHAR(255),
  observations TEXT,
  version BIGINT,
  INDEX(type_mouvement, statut_mouvement),
  INDEX(date_creation)
);

-- Lots
CREATE TABLE lots_stock (
  id BIGINT PRIMARY KEY,
  numerolot VARCHAR(50) UNIQUE NOT NULL,
  article_id BIGINT NOT NULL,
  depot_id BIGINT NOT NULL,
  quantite_initiale DECIMAL(15,2),
  quantite_disponible DECIMAL(15,2),
  prix_unitaire DECIMAL(15,2),
  date_expiration DATE,
  date_production DATE,
  date_creation TIMESTAMP,
  statut_lot VARCHAR(20),
  observations TEXT,
  version BIGINT,
  INDEX(date_expiration),
  INDEX(statut_lot)
);

-- Réservations
CREATE TABLE reservations_stock (
  id BIGINT PRIMARY KEY,
  article_id BIGINT NOT NULL,
  depot_id BIGINT NOT NULL,
  reference_commande VARCHAR(50) NOT NULL,
  quantite_reservee DECIMAL(15,2),
  quantite_prelevee DECIMAL(15,2),
  statut_reservation VARCHAR(20),
  date_reservation TIMESTAMP,
  date_expiration TIMESTAMP,
  date_creation TIMESTAMP,
  observations TEXT,
  version BIGINT,
  INDEX(reference_commande, statut_reservation),
  INDEX(date_expiration)
);
```

## 🚀 Utilisation

### Exemple 1: Créer une Entrée

```java
@PostMapping("/entrees")
public String creerEntree(@RequestParam Long articleId, ...) {
    MouvementStock mouvement = mouvementStockService.creerEntree(
        articleId, depotId, quantite, prixUnitaire, numeroBC
    );
    // Utilisateur vérifie puis exécute
    return "redirect:/stock/mouvements/" + mouvement.getId();
}

// Puis après vérification:
@PostMapping("/mouvements/{id}/executer")
public String executer(@PathVariable Long id) {
    mouvementStockService.executerMouvement(id);  // ✅ Stock + CUMP mis à jour
    return "redirect:/stock/mouvements/" + id;
}
```

### Exemple 2: Réserver pour Commande Client

```java
@PostMapping("/reservations")
public String reserver(@RequestParam Long articleId, ...) {
    // Vérifie disponibilité et crée réservation
    Reservation res = reservationService.creerReservation(
        articleId, depotId, refCommande, quantite, LocalDateTime.now().plusDays(7)
    );
    return "redirect:/stock/reservations/" + res.getId();
}

// Plus tard: Prélèvement
reservationService.preleveReservation(reservationId, quantitePrelevee);

// À la livraison
reservationService.marquerLivree(reservationId);
```

## 🔧 Configuration

### application.properties

```properties
# Stock
stock.cump.precision=2
stock.lot.alerte_peremption_jours=30
stock.reservation.delai_defaut_jours=7
stock.transfert.inter_depot=true
stock.ajustement.require_validation=true
```

## 📈 KPI & Reporting

### Indicateurs disponibles

- **Rotation Stock** = Coût COGS / Valeur stock moyen
- **Précision Inventaire** = Articles corrects / Total articles
- **Taux Rupture** = Articles en alerte minimum
- **Taux Surstock** = Articles au-dessus maximum
- **Turnover** = Sorties / Stock moyen
- **Jours d'appréciation** = Stock / Consommation jour

## 🐛 Troubleshooting

### Problème: Quantité insuffisante à la sortie

**Cause probable:** Quantité réservée non libérée avant
**Solution:** Vérifier et libérer réservations expirées

```java
reservationService.cancelExpired();
```

### Problème: CUMP incohérent

**Cause:** Entrées sans prix ou transferts sans CUMP initial
**Solution:** Vérifier application du prix aux transferts

```java
// Dans creerTransfert, utiliser prix du lot source
```

### Problème: Doublon de lot

**Cause:** Création lot avec même numéro
**Solution:** Numéro lot doit être unique par fournisseur/date

```java
// Ajouter préfixe fournisseur: "FON001-20260207-001"
```

## 📝 Migration DB

Flyway scripts pour module:

```sql
-- V1__create_stock_tables.sql
-- V2__add_lot_management.sql
-- V3__add_reservation_status.sql
```

---

**Version:** 1.0  
**Dernière mise à jour:** 2026-02-07  
**Responsable:** Équipe Stock
