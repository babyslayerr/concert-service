package kr.hhplus.be.server.domain.reservation;

import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCreatedOutboxRepository extends JpaRepository<PaymentCreatedOutbox, Long> {
    Optional<PaymentCreatedOutbox> findByReservationId(Long reservationId);

    List<PaymentCreatedOutbox> findAllByStatus(OutboxStatus outboxStatus);
}
