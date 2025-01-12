package kr.hhplus.be.server.domain.concert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertScheduleRepository extends JpaRepository<ConcertSchedule, Long> {

    Page<ConcertSchedule> findByConcertIdOrderByConcertDateAsc(Long concertId, Pageable pageable);

    Page<ConcertSchedule> findAllOrderByConcertDateAsc(Pageable pageable);
}
