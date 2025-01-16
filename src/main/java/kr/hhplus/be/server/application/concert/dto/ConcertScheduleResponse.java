package kr.hhplus.be.server.application.concert.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class ConcertScheduleResponse {
    private Long id;
    private LocalDate createdDate;
}
