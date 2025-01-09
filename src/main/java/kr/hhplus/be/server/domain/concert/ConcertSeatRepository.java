package kr.hhplus.be.server.domain.concert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {
    Optional<ConcertSeat > findConcertSeatByConcertScheduleIdAndSeatNo(Long concertScheduleId, Long seatNo);
    List<ConcertSeat> findByConcertScheduleIdAndStatus(Long concertScheduleId, String status);
}
