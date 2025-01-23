package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.application.reservation.dto.ReservationResponse;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertScheduleRepository;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ConcertSeatRepository;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class ReservationFacadeConcurrencyTest {

    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    QueueService queueService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConcertScheduleRepository concertScheduleRepository;

    @Autowired
    ConcertSeatRepository concertSeatRepository;


    @Test
    void 동시예약시_5명중_1명만_성공한다() throws InterruptedException, ExecutionException {
        // given - 필요 데이터 삽입
        // 유저 저장
        User user = userRepository.save(User.builder()
                .balance(10000L)
                .username("testUser")
                .build());
        // 콘서트 스케줄 저장
        ConcertSchedule concertSchedule = concertScheduleRepository.save(ConcertSchedule.builder()
                .concertDate(LocalDate.now())
                .build());
        // 콘서트 좌석 저장
        ConcertSeat concertSeat = concertSeatRepository.save(ConcertSeat.builder()
                .seatNo(1L)
                .price(5000L)
                .concertSchedule(concertSchedule)
                        .status("available")
                .build());

        int threadCount = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<Boolean>> results = new ArrayList<>();

        for(int i =0; i<threadCount;i++){
            results.add(executorService.submit(()->{

                try {
                    reservationFacade.reserveSeat(user.getId(),concertSchedule.getId(),concertSeat.getSeatNo());
                    return true;
                } catch (Exception e){
                    return false;
                } finally {
                    latch.countDown();
                }
            }));
        }
        // 대기
        latch.await();
        // 작업종료
        executorService.shutdown();

        // 성공 카운팅
        int successCount = 0;
        for(Future<Boolean>  result:results){
            if(result.get()){
                successCount++;
            }
        }
        // 한명만 성공한다
        Assertions.assertEquals(1,successCount);
    }

    @Test
    void 동시_결제시_1번만_성공한다() throws ExecutionException, InterruptedException {
        // given - 필요 데이터 삽입
        // 토큰 발급
        String uuid = queueService.getToken();
        // 강제 액티브
        queueService.activateTokens();
        // 유저 저장
        User user = userRepository.save(User.builder()
                .balance(10000L)
                .username("testUser")
                .build());
        // 콘서트 스케줄 저장
        ConcertSchedule concertSchedule = concertScheduleRepository.save(ConcertSchedule.builder()
                .concertDate(LocalDate.now())
                .build());
        // 콘서트 좌석 저장
        ConcertSeat concertSeat = concertSeatRepository.save(ConcertSeat.builder()
                .seatNo(1L)
                .price(5000L)
                .concertSchedule(concertSchedule)
                .status("available")
                .build());

        // 예약 처리
        ReservationResponse response = reservationFacade.reserveSeat(user.getId(), concertSchedule.getId(), concertSeat.getSeatNo());

        Long userId = user.getId();
        Long concertSeatId = concertSeat.getId();
        Long reservationId = response.getId();
        String tokenUuid = uuid;

        int threadCount = 10;

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<Boolean>> results = new ArrayList<>();

        for(int i =0; i<threadCount;i++){
            results.add(executorService.submit(()->{

                try {
                    reservationFacade.makeSeatPayment(userId,concertSeatId,reservationId,tokenUuid);
                    return true;
                } catch (Exception e){
                    return false;
                } finally {
                    latch.countDown();
                }
            }));
        }
        // 대기
        latch.await();
        // 작업종료
        executorService.shutdown();

        // 성공 카운팅
        int successCount = 0;
        for(Future<Boolean>  result:results){
            if(result.get()){
                successCount++;
            }
        }
        // then
        // 한명만 성공한다
        Assertions.assertEquals(1,successCount);
    }

}
