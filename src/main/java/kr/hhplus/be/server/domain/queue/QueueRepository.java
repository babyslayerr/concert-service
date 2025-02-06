package kr.hhplus.be.server.domain.queue;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueRepository {

    Queue save(Queue queue);

    Optional<Queue> findByUuid(String uuid);

    void delete(Queue queue);

    long countByIsActive(String active);

    List<Queue> findByIsActiveOrderByCreatedDateAsc(String isActive, Pageable pageable);

    long countBeforeWaitingToken(String uuid);

    long deleteExpiredTokens(int standardMinute);

    long addActiveTokens(long count);

    long countByIsActive();
}
