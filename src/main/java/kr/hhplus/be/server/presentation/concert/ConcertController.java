package kr.hhplus.be.server.presentation.concert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ConcertController {
    // 예약 가능 날짜 조회 API
    @GetMapping("/concert/available-dates")
    public ResponseEntity<Map<String, List<String>>> getAvailableDates() {
        return ResponseEntity.ok(Map.of(
                "availableDates", Arrays.asList("2024-01-01", "2024-01-02", "2024-01-03")
        ));
    }

    // 예약 가능한 좌석 조회 API
    @GetMapping("/concert/available-seats")
    public ResponseEntity<Map<String, List<Integer>>> getAvailableSeats(@RequestParam int concertId) {
        return ResponseEntity.ok(Map.of(
                "availableSeats", Arrays.asList(1, 2, 3, 10, 20)
        ));
    }
}
