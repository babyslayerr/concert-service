package kr.hhplus.be.server.application.concert.dto;

import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class ConcertScheduleResponse {
    private Long id;
    private LocalDate concertDate;

    public static ConcertScheduleResponse fromEntity(ConcertSchedule concertSchedule) {
        return ConcertScheduleResponse.builder()
                .id(concertSchedule.getId())
                .concertDate(concertSchedule.getConcertDate())
                .build();

    }
}
