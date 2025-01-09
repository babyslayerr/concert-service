package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSchedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate concertDate;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;
}
