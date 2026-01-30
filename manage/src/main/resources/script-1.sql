CREATE DATABASE gestion_database;
\c gestion_database;

-- ============================================================
-- CORE MODULE - ENTERPRISE SECURITY + AUDIT + WORKFLOW
-- PostgreSQL 12+
-- ============================================================

BEGIN;

-- ============================================================
-- 1. ORGANISATION STRUCTURE
-- ============================================================

CREATE TABLE organisation (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(50) NOT NULL UNIQUE,
    nom             VARCHAR(150) NOT NULL,
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE site (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(50) NOT NULL UNIQUE,
    nom             VARCHAR(150) NOT NULL,
    organisation_id BIGINT NOT NULL REFERENCES organisation(id),
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE departement (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(50) NOT NULL UNIQUE,
    nom             VARCHAR(150) NOT NULL,
    site_id         BIGINT NOT NULL REFERENCES site(id),
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

-- ============================================================
-- 2. UTILISATEURS & RBAC
-- ============================================================

CREATE TABLE utilisateur (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    nom_complet     VARCHAR(150) NOT NULL,
    email           VARCHAR(150),
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    site_id         BIGINT REFERENCES site(id),
    departement_id  BIGINT REFERENCES departement(id),
    created_at      TIMESTAMP NOT NULL DEFAULT now(),
    last_login_at   TIMESTAMP
);

CREATE TABLE role (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(50) NOT NULL UNIQUE,
    libelle         VARCHAR(150) NOT NULL,
    niveau          INTEGER NOT NULL, -- hiérarchie (1=Opérateur, 2=Superviseur...)
    actif           BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE permission (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(100) NOT NULL UNIQUE,
    libelle         VARCHAR(200) NOT NULL,
    module          VARCHAR(50) NOT NULL
);

CREATE TABLE role_permission (
    role_id         BIGINT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    permission_id   BIGINT NOT NULL REFERENCES permission(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE utilisateur_role (
    utilisateur_id BIGINT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    role_id        BIGINT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    actif          BOOLEAN NOT NULL DEFAULT TRUE,
    date_debut     DATE NOT NULL DEFAULT CURRENT_DATE,
    date_fin       DATE,
    PRIMARY KEY (utilisateur_id, role_id)
);

-- ============================================================
-- 3. ABAC - ATTRIBUTS & POLITIQUES D’ACCÈS
-- ============================================================

CREATE TABLE attribut_acces (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(100) NOT NULL UNIQUE,
    libelle         VARCHAR(200) NOT NULL,
    type_valeur     VARCHAR(30) NOT NULL, -- STRING, NUMBER, BOOLEAN, LIST
    actif           BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE politique_acces (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT NOT NULL,
    permission_code VARCHAR(100) NOT NULL,
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE politique_condition (
    id              BIGSERIAL PRIMARY KEY,
    politique_id    BIGINT NOT NULL REFERENCES politique_acces(id) ON DELETE CASCADE,
    attribut_code   VARCHAR(100) NOT NULL,
    operateur       VARCHAR(20) NOT NULL, -- =, !=, >, <, IN, NOT_IN
    valeur          VARCHAR(255) NOT NULL
);

CREATE INDEX idx_pol_cond_pol ON politique_condition(politique_id);

-- ============================================================
-- 4. DÉLÉGATION TEMPORAIRE DE DROITS
-- ============================================================

CREATE TABLE delegation_acces (
    id                  BIGSERIAL PRIMARY KEY,
    delegant_id         BIGINT NOT NULL REFERENCES utilisateur(id),
    delegataire_id      BIGINT NOT NULL REFERENCES utilisateur(id),
    permission_code     VARCHAR(100) NOT NULL,
    date_debut          TIMESTAMP NOT NULL,
    date_fin            TIMESTAMP NOT NULL,
    motif               TEXT,
    actif               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_delegation_period ON delegation_acces(date_debut, date_fin);

-- ============================================================
-- 5. WORKFLOW ENGINE GÉNÉRIQUE
-- ============================================================

CREATE TABLE workflow_definition (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(100) NOT NULL UNIQUE,
    libelle         VARCHAR(200) NOT NULL,
    module          VARCHAR(50) NOT NULL,
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE workflow_step (
    id                  BIGSERIAL PRIMARY KEY,
    workflow_id         BIGINT NOT NULL REFERENCES workflow_definition(id) ON DELETE CASCADE,
    code                VARCHAR(100) NOT NULL,
    libelle             VARCHAR(200) NOT NULL,
    ordre               INTEGER NOT NULL,
    role_requis         VARCHAR(50),
    seuil_min           NUMERIC(18,2),
    seuil_max           NUMERIC(18,2),
    terminal            BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (workflow_id, code)
);

CREATE TABLE workflow_transition (
    id                  BIGSERIAL PRIMARY KEY,
    workflow_id         BIGINT NOT NULL REFERENCES workflow_definition(id) ON DELETE CASCADE,
    step_source_code    VARCHAR(100) NOT NULL,
    step_cible_code     VARCHAR(100) NOT NULL,
    action_code         VARCHAR(50) NOT NULL, -- APPROUVER, REJETER, ANNULER
    UNIQUE (workflow_id, step_source_code, action_code)
);

CREATE TABLE workflow_instance (
    id              BIGSERIAL PRIMARY KEY,
    workflow_code   VARCHAR(100) NOT NULL,
    objet_type      VARCHAR(100) NOT NULL,
    objet_id        BIGINT NOT NULL,
    step_actuel     VARCHAR(100) NOT NULL,
    statut          VARCHAR(50) NOT NULL,
    created_by      BIGINT NOT NULL REFERENCES utilisateur(id),
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE workflow_task (
    id              BIGSERIAL PRIMARY KEY,
    instance_id     BIGINT NOT NULL REFERENCES workflow_instance(id) ON DELETE CASCADE,
    step_code       VARCHAR(100) NOT NULL,
    assignable_role VARCHAR(50),
    assigne_id      BIGINT REFERENCES utilisateur(id),
    statut          VARCHAR(50) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE workflow_approval_log (
    id              BIGSERIAL PRIMARY KEY,
    instance_id     BIGINT NOT NULL REFERENCES workflow_instance(id) ON DELETE CASCADE,
    step_code       VARCHAR(100) NOT NULL,
    action          VARCHAR(50) NOT NULL,
    decision_by     BIGINT NOT NULL REFERENCES utilisateur(id),
    commentaire     TEXT,
    decided_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_wf_instance_obj ON workflow_instance(objet_type, objet_id);
CREATE INDEX idx_wf_task_assigne ON workflow_task(assigne_id);

-- ============================================================
-- 6. AUDIT & JOURNALISATION IMMUTABLE
-- ============================================================

CREATE TABLE audit_log (
    id              BIGSERIAL PRIMARY KEY,
    utilisateur_id  BIGINT REFERENCES utilisateur(id),
    action          VARCHAR(100) NOT NULL,
    objet_type      VARCHAR(100),
    objet_id        VARCHAR(100),
    ancien_valeur   JSONB,
    nouvelle_valeur JSONB,
    justification   TEXT,
    ip_address      VARCHAR(45),
    user_agent      TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_user ON audit_log(utilisateur_id);
CREATE INDEX idx_audit_obj ON audit_log(objet_type, objet_id);
CREATE INDEX idx_audit_date ON audit_log(created_at);

-- ============================================================
-- 7. PARAMÉTRAGE SYSTÈME
-- ============================================================

CREATE TABLE system_parameter (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(100) NOT NULL UNIQUE,
    valeur          TEXT NOT NULL,
    description     TEXT,
    updated_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE notification_preference (
    id              BIGSERIAL PRIMARY KEY,
    utilisateur_id  BIGINT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    canal           VARCHAR(30) NOT NULL, -- EMAIL, SMS, APP
    evenement       VARCHAR(100) NOT NULL,
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (utilisateur_id, canal, evenement)
);

-- ============================================================
-- 8. DONNÉES INITIALES (BOOTSTRAP)
-- ============================================================

-- Organisations / Sites
INSERT INTO organisation(code, nom) VALUES ('ORG1', 'Organisation Principale');
INSERT INTO site(code, nom, organisation_id) VALUES ('HQ', 'Siège Central', 1);
INSERT INTO departement(code, nom, site_id) VALUES ('FIN', 'Finance', 1);
INSERT INTO departement(code, nom, site_id) VALUES ('ACH', 'Achats', 1);
INSERT INTO departement(code, nom, site_id) VALUES ('STK', 'Stock', 1);
INSERT INTO departement(code, nom, site_id) VALUES ('VNT', 'Ventes', 1);

-- Rôles hiérarchiques
INSERT INTO role(code, libelle, niveau) VALUES
('OPERATEUR', 'Opérateur', 1),
('SUPERVISEUR', 'Superviseur', 2),
('MANAGER', 'Manager', 3),
('DIRECTEUR', 'Directeur', 4),
('ADMIN', 'Administrateur Système', 99);

-- Permissions CORE
INSERT INTO permission(code, libelle, module) VALUES
('CORE_USER_READ', 'Consulter utilisateurs', 'CORE'),
('CORE_USER_WRITE', 'Gérer utilisateurs', 'CORE'),
('CORE_ROLE_READ', 'Consulter rôles', 'CORE'),
('CORE_ROLE_WRITE', 'Gérer rôles', 'CORE'),
('CORE_WORKFLOW_ADMIN', 'Administrer workflows', 'CORE'),
('CORE_AUDIT_READ', 'Consulter journaux d’audit', 'CORE');

-- Rôles → Permissions
INSERT INTO role_permission(role_id, permission_id)
SELECT r.id, p.id
FROM role r, permission p
WHERE
    (r.code = 'ADMIN')
 OR (r.code = 'DIRECTEUR' AND p.code IN ('CORE_AUDIT_READ'))
 OR (r.code = 'MANAGER' AND p.code IN ('CORE_USER_READ','CORE_ROLE_READ'))
 OR (r.code = 'SUPERVISEUR' AND p.code IN ('CORE_USER_READ'))
 OR (r.code = 'OPERATEUR' AND p.code IN ('CORE_USER_READ'));

-- Attributs ABAC
INSERT INTO attribut_acces(code, libelle, type_valeur) VALUES
('site', 'Site utilisateur', 'STRING'),
('departement', 'Département utilisateur', 'STRING'),
('montant', 'Montant opération', 'NUMBER'),
('categorie_article', 'Catégorie article', 'STRING'),
('famille_article', 'Famille article', 'STRING');

-- Paramètres système
INSERT INTO system_parameter(code, valeur, description) VALUES
('SECURITY_PASSWORD_MIN_LENGTH', '10', 'Longueur minimale mot de passe'),
('WORKFLOW_DEFAULT_TIMEOUT_DAYS', '7', 'Délai par défaut validation'),
('AUDIT_RETENTION_YEARS', '10', 'Durée conservation logs audit');

COMMIT;
