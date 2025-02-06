package kr.hhplus.be.server.domain.concert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ConcertCacheRepository {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    private static final int CONCERT_SCHEDULE_TTL = 3600;  // 기본 TTL: 1시간

    public ConcertSchedule getCachedConcertSchedule(Long concertScheduleId) {
        String concertScheduleKey = "concertSchedule:"  + concertScheduleId;
        RBucket<String> bucket = redissonClient.getBucket(concertScheduleKey);

        String concertScheduleJson = bucket.get();


        if(concertScheduleJson == null) return null;

        // 조회될 때마다 TTL 연장
        bucket.expire(CONCERT_SCHEDULE_TTL, TimeUnit.SECONDS);
        try {
            // JSON 문자열 → 객체 변환
            return objectMapper.readValue(concertScheduleJson, ConcertSchedule.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 캐싱 데이터 변환 실패", e);
        }
    }

    public void saveConcertScheduleToCache(Long concertScheduleId, ConcertSchedule schedule) {
        String cacheKey = "concertSchedule:" + concertScheduleId;
        RBucket<String> cache = redissonClient.getBucket(cacheKey);

        try {
            // 객체 → JSON 문자열 변환 후 저장
            String jsonData = objectMapper.writeValueAsString(schedule);
            cache.set(jsonData, CONCERT_SCHEDULE_TTL, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 캐싱 데이터 저장 실패", e);
        }
    }

    // 추후 스케줄 변경 API 가 나오면 사용할 만료 메서드
    public void evictConcertScheduleCache(Long concertScheduleId) {
        String cacheKey = "concertSchedule:" + concertScheduleId;
        redissonClient.getBucket(cacheKey).delete();
    }
}
