package kr.hhplus.be.server.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationOutboxRepository extends JpaRepository<ReservationOutbox, Long> {

    List<ReservationOutbox> findAllByStatusAndEventName(OutboxStatus outboxStatus,String eventName);

    Optional<ReservationOutbox> findByReservationIdAndEventName(Long reservationId, String eventName);
}
