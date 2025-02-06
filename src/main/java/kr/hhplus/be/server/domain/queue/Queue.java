package kr.hhplus.be.server.domain.queue;

import jakarta.persistence.*;
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
    // waiting, active
    @Enumerated(EnumType.STRING)
    private QueueStatus isActive = QueueStatus.waiting;
    private LocalDateTime expireAt;

    public Queue(String uuid){
        this.uuid = uuid;
    }

    public void activate() {
        isActive = QueueStatus.waiting;
        expireAt = LocalDateTime.now().plusMinutes(30);
    }

    public void setStatusActive() {
        isActive = QueueStatus.active;
    }

    public void setStatusWait() {
        isActive = QueueStatus.waiting;
    }

    public boolean isWaiting() {

        return this.isActive.equals(QueueStatus.waiting);
    }

}
