package kr.hhplus.be.server.domain.queue;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueRepository {

    Queue save(Queue queue);

    Optional<Queue> findByUuid(String uuid);

    long countByCreatedDateBeforeAndIsActive(LocalDateTime createdDate,String isActive);

    void delete(Queue queue);

    List<Queue> findByIsActiveAndExpireAtBefore(String active, LocalDateTime now);

    long countByIsActive(String active);


    List<Queue> findByIsActiveOrderByCreatedDateAsc(String isActive, Pageable pageable);
}
