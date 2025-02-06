package kr.hhplus.be.server.infrastructure.queue;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class QueueRedisRepositoryImpl implements QueueRepository {

    private final RedissonClient redissonClient;

    private final String REDIS_KEY_ACTIVE_TOKEN = "active-token";

    private final String REDIS_KEY_WAITING_TOKEN = "waiting-token";

    @Override
    public Queue save(Queue queue) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_WAITING_TOKEN);
        // timestamp 를 score 로 설정(값이 높을수록 나중에 들어온 값)
        scoredSortedSet.add(System.currentTimeMillis(),queue.getUuid());
        return queue;
    }

    @Override
    public Optional<Queue> findByUuid(String uuid) {
        // 도메인 모델 생성
        Queue queue = new Queue(uuid);

        // active-token 리스트 가져오기
        RScoredSortedSet<String> activeTokenSet = redissonClient.getScoredSortedSet(REDIS_KEY_ACTIVE_TOKEN);
        // active-token 에 포함되어 있으면
        if(activeTokenSet.contains(uuid)) queue.setStatusActive();
        else {
            RScoredSortedSet<String> waitingTokenSet = redissonClient.getScoredSortedSet(REDIS_KEY_WAITING_TOKEN);
            // waiting 토큰에 포함되어 있으면
            if(waitingTokenSet.contains(uuid)) queue.setStatusWait();
            // 없으면 null
            else queue = null;
        }

        return Optional.ofNullable(queue);
    }


    @Override
    public void delete(Queue queue) {
        RScoredSortedSet<String> activeTokenSet = redissonClient.getScoredSortedSet(REDIS_KEY_ACTIVE_TOKEN);
        activeTokenSet.remove(queue.getUuid());
    }

    @Override
    public long countByIsActive(String active) {
        return redissonClient.getScoredSortedSet(REDIS_KEY_ACTIVE_TOKEN).size();
    }

    @Override
    public List<Queue> findByIsActiveOrderByCreatedDateAsc(String isActive, Pageable pageable) {
        return List.of();
    }

    @Override
    public long countBeforeWaitingToken(String uuid) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_WAITING_TOKEN);
        // 0-based-index
        return sortedSet.rank(uuid);
    }
    @Override
    public long deleteExpiredTokens(int standardMinute){
        // queue + standardMinute < currentTime == 만료
        long expiredAt = System.currentTimeMillis()-standardMinute;

        // 30분이 지나면 만료된것으로 정책을 잡음
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(REDIS_KEY_ACTIVE_TOKEN);

        long deleteCount = scoredSortedSet.removeRangeByScore(0, true, expiredAt, true);
        return deleteCount;
    }

    @Override
    public long addActiveTokens(long count) {
        // Stream || 멀티스레드 환경에서 사용가능한 count 변수
        long successCount = 0;

        // 데이터 정합성 해결을 위한 작업단위 지정
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());
        RScoredSortedSet<String> waitingSet = redissonClient.getScoredSortedSet(REDIS_KEY_WAITING_TOKEN);
        RScoredSortedSet<String> activeSet = redissonClient.getScoredSortedSet(REDIS_KEY_ACTIVE_TOKEN);

        // 먼저 들어온 순서대로 토큰 활성화
        Collection<String> poppedValues = waitingSet.pollFirst((int) count);

        for (String uuid : poppedValues) {
            boolean successStatus = activeSet.add(System.currentTimeMillis(), uuid);
            if (successStatus) {
                successCount++;
            }
        }
        // 작업단위 한번에 실행
        transaction.commit();
        return successCount;
    }

    @Override
    public long countByIsActive() {
        return redissonClient.getScoredSortedSet(REDIS_KEY_ACTIVE_TOKEN).size();
    }
}
