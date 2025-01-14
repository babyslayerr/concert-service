package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertScheduleRepository concertScheduleRepository;

    private final ConcertSeatRepository concertSeatRepository;

    private static final Logger log = LoggerFactory.getLogger(ConcertService.class);

    public Page<ConcertSchedule> getAvailableConcert(Long concertId, Pageable requestPageable) {
        log.info("concertId: {}, pageNumber: {},pageSize: {}",concertId,requestPageable.getPageNumber(),requestPageable.getPageSize());
        Pageable pageable = PageRequest.of(requestPageable.getPageNumber(), requestPageable.getPageSize(), Sort.by("concertDate"));
        Page<ConcertSchedule> concertScheduleList = new PageImpl<>(new ArrayList<>(), PageRequest.of(0,10),0);

        // 콘서트ID를 파라미터로 받지 않은 경우
        if(concertId == null) {
            log.info("findAll without concertId");
            concertScheduleList = concertScheduleRepository.findAll(pageable);
        }
        // 콘서트ID를 받은 경우
        if(concertId != null){
            log.info("findAll with concertId");
            concertScheduleList = concertScheduleRepository.findByConcertId(concertId,pageable);
            if(concertScheduleList.isEmpty()){
                log.error("No ConcertSchedules");
                throw new NoSuchElementException("콘서트 스케줄을 찾을 수 없습니다.");
            }
        }
        return concertScheduleList;
    }

    public List<ConcertSeat> getAvailableSeats(Long concertScheduleId) {
        log.info("concertScheduleId: {}",concertScheduleId);
        List<ConcertSeat> availableSeats = concertSeatRepository.findByConcertScheduleIdAndStatus(concertScheduleId,"available");
        if(availableSeats.size() == 0){
            log.error("No AvailableSeats");
            throw new NoSuchElementException("예약가능한 좌석이 없습니다.");
        }
        return availableSeats;
    }

    // facade 호출
    // 좌석 예약
    public ConcertSeat reserveAvailableSeat(User user, Long concertScheduleId, Long seatNo) {
        log.info("userId: {}, concertScheduleId: {}, seatNo: {}",user.getId(),concertScheduleId,seatNo);
        ConcertSeat seat = concertSeatRepository.findConcertSeatByConcertScheduleIdAndSeatNoAndStatus(concertScheduleId,seatNo,"available").orElseThrow();
        // 콘서트 예약정보 업데이트
        seat.reserved(user);
        concertSeatRepository.save(seat);
        log.info("seatStatus is {}",seat.getStatus());
        return seat;
    }

    // facade 호출
    public ConcertSeat findConcertSeatById(Long concertSeatId) {
        log.info("concertSeatId: {}",concertSeatId);
        ConcertSeat seat = concertSeatRepository.findById(concertSeatId).orElseThrow();

        return seat;
    }

    public ConcertSeat changeConcertSeatCompleted(ConcertSeat concertSeat) {
        log.info("ConcertSeatId: {}",concertSeat.getId());
        concertSeat.setCompletedStatus();
        ConcertSeat reservedConcertSeat = concertSeatRepository.save(concertSeat);
        return reservedConcertSeat;
    }
}
