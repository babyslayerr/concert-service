package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.presentation.concert.dto.ConcertScheduleResponse;
import kr.hhplus.be.server.presentation.concert.dto.ConcertSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    public Page<ConcertScheduleResponse> getAvailableConcertDates(Long concertId, Pageable pageable){
        Page<ConcertSchedule> concertSchedulePage = concertService.getAvailableConcert(concertId, pageable);
        // 날짜만 추출
        List<ConcertScheduleResponse> concertScheduleResponseList = concertSchedulePage.stream()
                .map((concertSchedule) -> {
                    return ConcertScheduleResponse.builder()
                            .id(concertSchedule.getId())
                            .createdDate(concertSchedule.getConcertDate())
                            .build();
                }).toList();
        Page<ConcertScheduleResponse> response = new PageImpl<>(concertScheduleResponseList, concertSchedulePage.getPageable(), concertSchedulePage.getTotalElements());

        return response;
    }

    List<ConcertSeatResponse> getAvailableSeats(Long concertScheduleId){
        List<ConcertSeat> availableSeats = concertService.getAvailableSeats(concertScheduleId);
        List<ConcertSeatResponse> response  = availableSeats.stream()
                .map((concertSeat) ->
                        ConcertSeatResponse.builder()
                                .id(concertSeat.getId())
                                .seatNo(concertSeat.getSeatNo())
                                .price(concertSeat.getPrice())
                                .status(concertSeat.getStatus())
                                .build()
                ).toList();
        return response;
    }
}
