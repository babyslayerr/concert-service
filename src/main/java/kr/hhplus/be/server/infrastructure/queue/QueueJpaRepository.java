package kr.hhplus.be.server.infrastructure.queue;

import kr.hhplus.be.server.domain.queue.Queue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<Queue, Long> {

    Optional<Queue> findFirstByUuid(String uuid);

    // 항상 대기열 만료 및 활성화 스케줄러가 돌아간다 -> 상태가 wait 만 검색
    @Query("SELECT COUNT(q) FROM Queue q WHERE q.createdDate < :createdDate AND q.isActive = 'wait'")
    long countByCreatedDateBeforeAndIsActiveWait(@Param("createdDate") LocalDateTime createdDate);

    List<Queue> findByIsActiveAndExpireAtBefore(String isActive, LocalDateTime expireAt);


    long countByIsActive(String active);

    List<Queue> findByIsActiveOrderByCreatedDateAsc(String isActive, Pageable pageable);

    long countByCreatedDateBeforeAndIsActive(LocalDateTime createdDate,String isActive);
}
