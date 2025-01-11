package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.presentation.concert.dto.ConcertScheduleResponse;
import kr.hhplus.be.server.presentation.concert.dto.ConcertSeatResponse;
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

    public List<ConcertScheduleResponse> getAvailableConcertDates(Long concertId, Pageable pageable) {

        Page<ConcertSchedule> concertScheduleList = concertScheduleRepository.findByConcertIdOrderByConcertDateAsc(concertId,pageable);
        // 날짜만 추출
        List<ConcertScheduleResponse> concertScheduleResponseList = concertScheduleList.stream()
                .map((concertSchedule) -> {
                    return ConcertScheduleResponse.builder()
                            .id(concertSchedule.getId())
                            .createdDate(concertSchedule.getConcertDate())
                            .build();
                }).toList();

        return concertScheduleResponseList;
    }

    public List<ConcertSeatResponse> getAvailableSeats(Long concertScheduleId) {

        List<ConcertSeat> availableSeats = concertSeatRepository.findByConcertScheduleIdAndStatus(concertScheduleId,"available");
        List<ConcertSeatResponse> responsesList = availableSeats.stream()
                .map((concertSeat) ->
                        ConcertSeatResponse.builder()
                                .id(concertSeat.getId())
                                .seatNo(concertSeat.getSeatNo())
                                .price(concertSeat.getPrice())
                                .status(concertSeat.getStatus())
                                .build()
                ).toList();
        return responsesList;
    }

    // facade 호출
    public ConcertSeat reserveAvailableSeat(Long userId, Long concertScheduleId, Long seatNo) {
        ConcertSeat seat = concertSeatRepository.findConcertSeatByConcertScheduleIdAndSeatNoAndStatus(concertScheduleId,seatNo,"available").orElseThrow();
        // 콘서트 예약정보 업데이트

        seat.reserve(User.builder().id(userId).build());
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
