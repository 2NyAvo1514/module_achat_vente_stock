-- ================================
-- SCHEMA : ACHATS / STOCK - V1 (FR)
-- PostgreSQL
-- ================================

DROP DATABASE IF EXISTS achat_stock;
CREATE DATABASE achat_stock;
\c achat_stock;

-- ================================
-- CORE / SECURITE
-- ================================

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    libelle VARCHAR(100) NOT NULL,
    niveau INTEGER NOT NULL
);

CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom_utilisateur VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom_complet VARCHAR(150),
    actif BOOLEAN DEFAULT TRUE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE utilisateur_role (
    utilisateur_id INTEGER REFERENCES utilisateur(id),
    role_id INTEGER REFERENCES role(id),
    PRIMARY KEY (utilisateur_id, role_id)
);

CREATE TABLE site (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE depot (
    id SERIAL PRIMARY KEY,
    site_id INTEGER REFERENCES site(id),
    code VARCHAR(20) NOT NULL,
    nom VARCHAR(100) NOT NULL
);

-- ================================
-- REFERENTIELS
-- ================================

CREATE TABLE unite (
    id SERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE categorie (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE article (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    libelle VARCHAR(150) NOT NULL,
    categorie_id INTEGER REFERENCES categorie(id),
    unite_id INTEGER REFERENCES unite(id),
    gestion_lot BOOLEAN DEFAULT TRUE,
    duree_conservation_jours INTEGER,
    actif BOOLEAN DEFAULT TRUE
);

CREATE TABLE fournisseur (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    nom VARCHAR(150) NOT NULL,
    telephone VARCHAR(50),
    email VARCHAR(100),
    actif BOOLEAN DEFAULT TRUE
);

-- ================================
-- MODULE ACHATS
-- ================================

CREATE TABLE demande_achat (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(50) UNIQUE NOT NULL,
    demandeur_id INTEGER REFERENCES utilisateur(id),
    site_id INTEGER REFERENCES site(id),
    statut VARCHAR(30) NOT NULL,
    montant_total NUMERIC(15,2) DEFAULT 0,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_validation TIMESTAMP,
    valide_par INTEGER REFERENCES utilisateur(id)
);

CREATE TABLE ligne_demande_achat (
    id SERIAL PRIMARY KEY,
    demande_achat_id INTEGER REFERENCES demande_achat(id) ON DELETE CASCADE,
    article_id INTEGER REFERENCES article(id),
    quantite NUMERIC(12,2) NOT NULL,
    prix_unitaire NUMERIC(12,2) NOT NULL,
    total_ligne NUMERIC(15,2) NOT NULL
);

CREATE TABLE bon_commande (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(50) UNIQUE NOT NULL,
    demande_achat_id INTEGER REFERENCES demande_achat(id),
    fournisseur_id INTEGER REFERENCES fournisseur(id),
    statut VARCHAR(30) NOT NULL,
    montant_total NUMERIC(15,2),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_validation TIMESTAMP,
    valide_par INTEGER REFERENCES utilisateur(id)
);

CREATE TABLE ligne_bon_commande (
    id SERIAL PRIMARY KEY,
    bon_commande_id INTEGER REFERENCES bon_commande(id) ON DELETE CASCADE,
    article_id INTEGER REFERENCES article(id),
    quantite NUMERIC(12,2) NOT NULL,
    prix_unitaire NUMERIC(12,2) NOT NULL,
    total_ligne NUMERIC(15,2) NOT NULL
);

-- ================================
-- MODULE STOCK
-- ================================

CREATE TABLE lot_stock (
    id SERIAL PRIMARY KEY,
    article_id INTEGER REFERENCES article(id),
    depot_id INTEGER REFERENCES depot(id),
    numero_lot VARCHAR(100),
    date_peremption DATE,
    quantite_disponible NUMERIC(12,2) DEFAULT 0,
    cout_unitaire NUMERIC(12,2),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE mouvement_stock (
    id SERIAL PRIMARY KEY,
    article_id INTEGER REFERENCES article(id),
    depot_id INTEGER REFERENCES depot(id),
    lot_stock_id INTEGER REFERENCES lot_stock(id),
    type_mouvement VARCHAR(20) NOT NULL,
    quantite NUMERIC(12,2) NOT NULL,
    cout_unitaire NUMERIC(12,2),
    reference VARCHAR(100),
    date_mouvement TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================
-- INDEXES
-- ================================

CREATE INDEX idx_demande_achat_statut ON demande_achat(statut);
CREATE INDEX idx_bon_commande_statut ON bon_commande(statut);
CREATE INDEX idx_lot_stock_article ON lot_stock(article_id);
CREATE INDEX idx_mouvement_stock_article ON mouvement_stock(article_id);

-- ================================
-- DONNEES D'EXEMPLE
-- ================================

-- ROLES
INSERT INTO role (code, libelle, niveau) VALUES
('OPERATEUR', 'Opérateur', 1),
('SUPERVISEUR', 'Superviseur', 2),
('MANAGER', 'Manager', 3),
('DIRECTEUR', 'Directeur', 4);

-- UTILISATEURS
INSERT INTO utilisateur (nom_utilisateur, mot_de_passe, nom_complet) VALUES
('acheteur1', 'pass', 'Acheteur Principal'),
('magasinier1', 'pass', 'Magasinier Central'),
('manager1', 'pass', 'Manager Achats');

INSERT INTO utilisateur_role VALUES
(1, 1),
(2, 1),
(3, 3);

-- SITES / DEPOTS
INSERT INTO site (code, nom) VALUES
('TNR', 'Antananarivo'),
('NOS', 'Nosy Be');

INSERT INTO depot (site_id, code, nom) VALUES
(1, 'DEP-TNR-01', 'Dépôt Central Tana'),
(2, 'DEP-NOS-01', 'Dépôt Nosy Be');

-- UNITES
INSERT INTO unite (code, libelle) VALUES
('KG', 'Kilogramme'),
('PC', 'Pièce'),
('CT', 'Carton');

-- CATEGORIES
INSERT INTO categorie (code, libelle) VALUES
('LEG', 'Légumes'),
('FRU', 'Fruits'),
('SUR', 'Produits surgelés');

-- ARTICLES
INSERT INTO article (code, libelle, categorie_id, unite_id, gestion_lot, duree_conservation_jours) VALUES
('ART-TOM', 'Tomates fraîches', 1, 1, TRUE, 7),
('ART-POM', 'Pommes de terre', 1, 1, TRUE, 30),
('ART-BAN', 'Bananes', 2, 1, TRUE, 10),
('ART-POI', 'Poisson surgelé', 3, 1, TRUE, 180);

-- FOURNISSEURS
INSERT INTO fournisseur (code, nom, telephone, email) VALUES
('FOUR-AGR-01', 'Coopérative Agricole Mada', '0340000001', 'contact@coopmada.mg'),
('FOUR-MER-01', 'Pêcheurs de l-Océan Indien', '0340000002', 'contact@poisson.mg');

-- ================================
-- ACHATS
-- ================================

INSERT INTO demande_achat (reference, demandeur_id, site_id, statut, montant_total)
VALUES ('DA-2026-0001', 1, 1, 'SOUMISE', 1500000);

INSERT INTO ligne_demande_achat (demande_achat_id, article_id, quantite, prix_unitaire, total_ligne)
VALUES
(1, 1, 100, 5000, 500000),
(1, 2, 200, 5000, 1000000);

INSERT INTO bon_commande (reference, demande_achat_id, fournisseur_id, statut, montant_total)
VALUES ('BC-2026-0001', 1, 1, 'VALIDEE', 1500000);

INSERT INTO ligne_bon_commande (bon_commande_id, article_id, quantite, prix_unitaire, total_ligne)
VALUES
(1, 1, 100, 5000, 500000),
(1, 2, 200, 5000, 1000000);

-- ================================
-- STOCK
-- ================================

INSERT INTO lot_stock (article_id, depot_id, numero_lot, date_peremption, quantite_disponible, cout_unitaire)
VALUES
(1, 1, 'LOT-TOM-001', CURRENT_DATE + INTERVAL '7 days', 100, 5000),
(2, 1, 'LOT-POM-001', CURRENT_DATE + INTERVAL '30 days', 200, 5000);

INSERT INTO mouvement_stock (article_id, depot_id, lot_stock_id, type_mouvement, quantite, cout_unitaire, reference)
VALUES
(1, 1, 1, 'ENTREE', 100, 5000, 'RECEPTION BC-2026-0001'),
(2, 1, 2, 'ENTREE', 200, 5000, 'RECEPTION BC-2026-0001');
