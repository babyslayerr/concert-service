package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Concert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String concertname;
}
