package com.entreprise.manage.stock.service.impl;

import com.entreprise.manage.stock.service.ReservationService;
import com.entreprise.manage.stock.model.Reservation;
import com.entreprise.manage.stock.model.StatutReservation;
import com.entreprise.manage.stock.repository.ReservationRepository;
import com.entreprise.manage.referentiels.repository.ArticleRepository;
import com.entreprise.manage.referentiels.repository.DepotRepository;
import com.entreprise.manage.core.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ArticleRepository articleRepository;
    private final DepotRepository depotRepository;
    private final NiveauStockServiceImpl niveauStockService;

    @Override
    public Reservation creerReservation(Long articleId, Long depotId, String referenceCommande,
            BigDecimal quantite, LocalDateTime delaiPrelevement) {
        // Vérifier que le stock est disponible
        if (!niveauStockService.isAvailable(articleId, depotId, quantite)) {
            throw new BusinessException("Stock insuffisant pour la réservation. Article: " + articleId);
        }

        Reservation reservation = new Reservation();
        reservation.setArticle(articleRepository.getReferenceById(articleId));
        reservation.setDepot(depotRepository.getReferenceById(depotId));
        reservation.setReferenceCommande(referenceCommande);
        reservation.setQuantiteReservee(quantite);
        reservation.setQuantitePrelevee(BigDecimal.ZERO);
        reservation.setStatut(StatutReservation.ACTIVE);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setDateExpiration(delaiPrelevement);
        reservation.setDateCreation(LocalDateTime.now());

        // Ajouter la réservation au niveau de stock
        niveauStockService.addReservation(articleId, depotId, quantite);

        return reservationRepository.save(reservation);
    }

    @Override
    public void preleveReservation(Long reservationId, BigDecimal quantite) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("Réservation non trouvée"));

        if (!reservation.getStatut().equals(StatutReservation.ACTIVE)) {
            throw new BusinessException("La réservation n'est pas à l'état ACTIVE");
        }

        BigDecimal quantitePreleable = reservation.getQuantiteReservee()
                .subtract(reservation.getQuantitePrelevee());

        if (quantite.compareTo(quantitePreleable) > 0) {
            throw new BusinessException("Quantité à prélever dépasse la réservation");
        }

        reservation.setQuantitePrelevee(reservation.getQuantitePrelevee().add(quantite));

        // Si tout est prélevé
        if (reservation.getQuantitePrelevee().equals(reservation.getQuantiteReservee())) {
            reservation.setStatut(StatutReservation.PRELEVEE);
        }

        reservationRepository.save(reservation);
    }

    @Override
    public void libereReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("Réservation non trouvée"));

        if (reservation.getStatut().equals(StatutReservation.ANNULEE)) {
            throw new BusinessException("La réservation est déjà annulée");
        }

        // Libérer du stock réservé
        BigDecimal aLiberer = reservation.getQuantiteReservee()
                .subtract(reservation.getQuantitePrelevee());
        if (aLiberer.compareTo(BigDecimal.ZERO) > 0) {
            niveauStockService.removeReservation(
                    reservation.getArticle().getId(),
                    reservation.getDepot().getId(),
                    aLiberer);
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        reservationRepository.save(reservation);
    }

    @Override
    public void marquerLivree(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("Réservation non trouvée"));

        if (!reservation.getStatut().equals(StatutReservation.PRELEVEE)) {
            throw new BusinessException("La réservation doit être en état PRELEVEE");
        }

        reservation.setStatut(StatutReservation.LIVREE);
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByCommande(String referenceCommande) {
        return reservationRepository.findByReferenceCommande(referenceCommande);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsActives(Long articleId, Long depotId) {
        return reservationRepository.findReservationsActives(articleId, depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsExpirees() {
        return reservationRepository.findReservationsExpirees();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalReserve(Long articleId, Long depotId) {
        Long total = reservationRepository.getTotalReservationsActives(articleId, depotId);
        return total != null ? total : 0L;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservation(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Reservation> getReservationsByStatut(StatutReservation statut, Pageable pageable) {
        return reservationRepository.findByStatut(statut, pageable);
    }

    @Override
    public void cancelExpired() {
        List<Reservation> expiries = getReservationsExpirees();
        for (Reservation res : expiries) {
            libereReservation(res.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canReserve(Long articleId, Long depotId, BigDecimal quantite) {
        return niveauStockService.isAvailable(articleId, depotId, quantite);
    }
}
