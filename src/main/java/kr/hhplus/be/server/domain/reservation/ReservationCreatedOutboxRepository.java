package kr.hhplus.be.server.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationCreatedOutboxRepository extends JpaRepository<ReservationCreatedOutbox,Long> {
    Optional<ReservationCreatedOutbox> findByReservationId(Long reservationId);

    List<ReservationCreatedOutbox> findAllByStatus(OutboxStatus outboxStatus);
}
