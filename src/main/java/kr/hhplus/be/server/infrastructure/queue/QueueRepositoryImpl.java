//package kr.hhplus.be.server.infrastructure.queue;
//
//
//import kr.hhplus.be.server.domain.queue.Queue;
//import kr.hhplus.be.server.domain.queue.QueueRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//// 추후 구현체 변경 DB -> Redis 을 위한 인터페이스,구현 분리
//@Repository
//@RequiredArgsConstructor
//public class QueueRepositoryImpl implements QueueRepository {
//
//    private final QueueJpaRepository queueJpaRepository;
//
//    @Override
//    public Queue save(Queue queue) {
//        return queueJpaRepository.save(queue);
//    }
//
//    @Override
//    public Optional<Queue> findByUuid(String uuid) {
//        return queueJpaRepository.findFirstByUuid(uuid);
//    }
//
//    @Override
//    public long countByCreatedDateBeforeAndIsActive(LocalDateTime createdDate, String isActive) {
//        return queueJpaRepository.countByCreatedDateBeforeAndIsActive(createdDate,isActive);
//    }
//
//    @Override
//    public void delete(Queue queue) {
//        queueJpaRepository.delete(queue);
//    }
//
//    @Override
//    public List<Queue> findByIsActiveAndExpireAtBefore(String active, LocalDateTime now) {
//        return queueJpaRepository.findByIsActiveAndExpireAtBefore(active,now);
//    }
//
//    @Override
//    public long countByIsActive(String active) {
//        return queueJpaRepository.countByIsActive(active);
//    }
//
//    @Override
//    public List<Queue> findByIsActiveOrderByCreatedDateAsc(String isActive, Pageable pageable) {
//        return queueJpaRepository.findByIsActiveOrderByCreatedDateAsc(isActive,pageable);
//    }
//
//}
