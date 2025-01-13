package kr.hhplus.be.server.domain.queue;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Queue {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private LocalDateTime createdDate = LocalDateTime.now();
    private String isActive = "wait";
    private LocalDateTime expireAt;

    public void activate() {
        isActive = "active";
        expireAt = LocalDateTime.now().plusMinutes(30);
    }


}
