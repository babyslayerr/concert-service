package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertScheduleRepository concertScheduleRepository;

    private final ConcertSeatRepository concertSeatRepository;

    public List<LocalDate> getAvailableConcertDates(Long concertId, Pageable pageable) {

        Page<ConcertSchedule> concertScheduleList = concertScheduleRepository.findByConcertIdOrderByConcertDateAsc(concertId,pageable);
        // 날짜만 추출
        List<LocalDate> dates = concertScheduleList.stream()
                .map((concertSchedule) -> {
                    return concertSchedule.getConcertDate();
                }).toList();

        return dates;
    }

    public List<ConcertSeat> getAvailableSeats(Long concertScheduleId) {

        List<ConcertSeat> availableSeats = concertSeatRepository.findByConcertScheduleIdAndStatus(concertScheduleId,"available");

        return availableSeats;
    }

    // facade 호출
    public ConcertSeat reserveAvailableSeat(Long userId, Long concertScheduleId, Long seatNo) {
        ConcertSeat seat = concertSeatRepository.findConcertSeatByConcertScheduleIdAndSeatNo(concertScheduleId,seatNo).orElseThrow();
        // 콘서트 예약정보 업데이트

        seat.reserve(User.builder().id(userId).build());
        concertSeatRepository.save(seat);
        return seat;
    }

    // facade 호출
    public ConcertSeat findConcertSeatById(Long concertSeatId) {
        return concertSeatRepository.findById(concertSeatId).orElseThrow();
    }

    public ConcertSeat saveConcertSeat(ConcertSeat concertSeat) {
        return concertSeatRepository.save(concertSeat);
    }
}
