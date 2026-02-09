```
═════════════════════════════════════════════════════════
        FLUX INTÉGRÉ: Fournisseur → Client
═════════════════════════════════════════════════════════

FOURNISSEUR         FRESHDISTRIB                  CLIENT
   (Producteur)        (Grossiste)               (Hôtel Plaza)
════════════════   ══════════════════════   ═══════════════════

┌─────────────┐    ┌─────────────────────┐    ┌─────────────┐
│   Commande  │    │  DEMANDE ACHAT      │    │   Demande   │
│    (BC)     │◄───┤  (DA)               │◄───┤   Client    │
└─────────────┘    │  • Articles         │    └─────────────┘
        │          │  • Quantités        │            │
        ▼          │  • DLC requise      │            ▼
┌─────────────┐    └──────────┬──────────┘    ┌─────────────┐
│  Livraison  │               │               │   Devis/    │
│   Camion    │───────────────┼──────────────►│  Commande   │
│ +4°C 7h00   │               │               │   Client    │
└─────────────┘               ▼               └─────────────┘
        │          ┌─────────────────────────┐            │
        ▼          │   RÉCEPTION + QC        │            ▼
┌─────────────┐    │  • Contrôle température │    ┌─────────────┐
│  Facture    │    │  • Échantillon 5%       │    │  Livraison  │
│ Fournisseur │◄───┤  • Pesée ±2%            │    │  Client     │
└─────────────┘    │  • Lot créé             │    └─────────────┘
                   │    (DLC tracée)         │            │
                   └──────────┬──────────────┘            ▼
                              │                  ┌─────────────┐
                              ▼                  │  Facture    │
                   ┌─────────────────────┐       │   Client    │
                   │     STOCKAGE        │       │  + Paiement │
                   │  • Emplacement A2   │       └─────────────┘
                   │  • Valorisation FIFO│
                   │  • Réservation auto │
                   └──────────┬──────────┘
                              │
                              ▼
                   ┌─────────────────────┐
                   │    INVENTAIRE       │
                   │  • Tournant mensuel │
                   │  • Écarts tracés    │
                   └─────────────────────┘