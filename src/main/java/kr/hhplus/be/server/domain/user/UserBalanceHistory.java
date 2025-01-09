package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
public class UserBalanceHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 참조하는 양이 적을거라 생각하여 LAZY 타입으로 설정
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime createdDate = LocalDateTime.now();
    private Long amount;
    private String actionType;
    private Long balance;

    public void setChargeHistory(User user,Long balance ,Long amount) {
        this.user = user;
        this.amount = amount;
        this.actionType = "charge";
        this.balance = balance;
    }

    public void setUseHistory(User user, Long balance, Long amount){
        this.user = user;
        this.amount = amount;
        this.actionType = "use";
        this.balance = balance;
    }
}
