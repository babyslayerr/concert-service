package kr.hhplus.be.server.application.concert;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.application.concert.dto.ConcertScheduleResponse;
import kr.hhplus.be.server.application.concert.dto.ConcertSeatResponse;
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

    @Transactional
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

    @Transactional
    public List<ConcertSeatResponse> getAvailableSeats(Long concertScheduleId){
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

    @Transactional
    public ConcertScheduleResponse getConcertSchedule(Long concertScheduleId){
        ConcertSchedule concertSchedule = concertService.getConcertSchedule(concertScheduleId);
        ConcertScheduleResponse concertScheduleResponse = ConcertScheduleResponse.fromEntity(concertSchedule);
        return concertScheduleResponse;

    }
}
