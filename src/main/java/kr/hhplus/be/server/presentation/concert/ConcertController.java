package kr.hhplus.be.server.presentation.concert;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ConcertController {
    // 예약 가능 날짜 조회 API
    @Operation(summary = "예약 가능 날짜 조회 API")
    @GetMapping("/concert/available-dates/{concertId}")
    public ResponseEntity<Map<String, List<String>>> getAvailableDates(
            @PathVariable("concertId") Long concertId

    ) {
        // TODO 페이징 처리
        return ResponseEntity.ok(Map.of(
                "availableDates", Arrays.asList("2024-01-01", "2024-01-02", "2024-01-03")
        ));
    }

    // 예약 가능한 좌석 조회 API
    @Operation(summary = "예약 가능한 좌석 조회 API")
    @GetMapping("/concert/available-seats/{concertScheduleId}")
    public ResponseEntity<Map<String, List<Integer>>> getAvailableSeats(
            @PathVariable(name = "concertScheduleId") Long concertScheduleId) {
        return ResponseEntity.ok(Map.of(
                "availableSeats", Arrays.asList(1, 2, 3, 10, 20)
        ));
    }
}
