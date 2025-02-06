package kr.hhplus.be.server.presentation.concert;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.concert.ConcertFacade;
import kr.hhplus.be.server.application.concert.dto.ConcertScheduleResponse;
import kr.hhplus.be.server.application.concert.dto.ConcertSeatResponse;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    // 예약 가능 날짜 조회 API
    @Operation(summary = "예약 가능 날짜 조회 API")
    @GetMapping("/concert/available-dates/{concertId}")
    public ResponseEntity<Page<ConcertScheduleResponse>> getAvailableDates(
            @PathVariable("concertId") Long concertId, Pageable pageable
            ) {
        Page<ConcertScheduleResponse> availableConcertDates = concertFacade.getAvailableConcertDates(concertId, pageable);

        return new ResponseEntity<>(availableConcertDates, HttpStatus.OK);
    }

    // 예약 가능한 좌석 조회 API
    @Operation(summary = "예약 가능한 좌석 조회 API")
    @GetMapping("/concert/available-seats/{concertScheduleId}")
    public ResponseEntity<List<ConcertSeatResponse>> getAvailableSeats(
            @PathVariable(name = "concertScheduleId") Long concertScheduleId) {
        return ResponseEntity.ok(concertFacade.getAvailableSeats(concertScheduleId));
    }

    // 캐싱 기법 적용
    @Operation(summary = "콘서트 스케줄 상세조회 API")
    @GetMapping("/concert/{concertScheduleId}")
    public ResponseEntity<ConcertScheduleResponse> ge(@PathVariable("concertScheduleId") Long concertScheduleId){
        ConcertScheduleResponse concertScheduleResponse = concertFacade.getConcertSchedule(concertScheduleId);
        return ResponseEntity.ok(concertScheduleResponse);
    }
}
