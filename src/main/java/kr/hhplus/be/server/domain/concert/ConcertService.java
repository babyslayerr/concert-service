package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
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

    public Page<ConcertSchedule> getAvailableConcert(Long concertId, Pageable RequestPageable) {
        Pageable pageable = PageRequest.of(RequestPageable.getPageNumber(), RequestPageable.getPageSize(), Sort.by("concertDate"));
        Page<ConcertSchedule> concertScheduleList = new PageImpl<>(new ArrayList<>(), PageRequest.of(0,10),0);

        // 콘서트ID를 파라미터로 받지 않은 경우
        if(concertId == null) {
            concertScheduleList = concertScheduleRepository.findAll(pageable);
        }
        // 콘서트ID를 받은 경우
        if(concertId != null){
            concertScheduleList = concertScheduleRepository.findByConcertId(concertId,pageable);
            if(concertScheduleList.isEmpty()){
                throw new NoSuchElementException("콘서트 스케줄을 찾을 수 없습니다.");
            }
        }



        return concertScheduleList;
    }

    public List<ConcertSeat> getAvailableSeats(Long concertScheduleId) {

        List<ConcertSeat> availableSeats = concertSeatRepository.findByConcertScheduleIdAndStatus(concertScheduleId,"available");
        if(availableSeats.size() == 0){
            throw new NoSuchElementException("예약가능한 좌석이 없습니다.");
        }
        return availableSeats;
    }

    // facade 호출
    public ConcertSeat reserveAvailableSeat(User user, Long concertScheduleId, Long seatNo) {
        ConcertSeat seat = concertSeatRepository.findConcertSeatByConcertScheduleIdAndSeatNoAndStatus(concertScheduleId,seatNo,"available").orElseThrow();
        // 콘서트 예약정보 업데이트

        seat.reserved(user);
        concertSeatRepository.save(seat);
        return seat;
    }

    // facade 호출
    public ConcertSeat findConcertSeatById(Long concertSeatId) {
        return concertSeatRepository.findById(concertSeatId).orElseThrow();
    }

    public ConcertSeat changeConcertSeatCompleted(ConcertSeat concertSeat) {
        concertSeat.setCompletedStatus();
        return concertSeatRepository.save(concertSeat);
    }
}
