package com.entreprise.manage.achats.service;

import com.entreprise.manage.achats.dto.DemandeAchatForm;
import com.entreprise.manage.achats.model.DemandeAchat;
import java.util.List;

public interface DemandeAchatService {
    DemandeAchat creerBrouillon(DemandeAchatForm form);

    DemandeAchat creerEtSoumettre(DemandeAchatForm form);

    void valider(Long id);

    List<DemandeAchat> listerToutes();

    DemandeAchat trouverParId(Long id);

    // -------------------------
    // Méthode manquante
    // -------------------------
    default DemandeAchat getById(Long id) {
        return trouverParId(id); // simple alias
    }
}
