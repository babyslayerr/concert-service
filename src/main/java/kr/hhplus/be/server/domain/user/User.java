package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 식별자
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 유저명
    @Column
    private String username;
    // 계좌 잔액
    @Column
    private Long balance;

    public void setBalance(Long balance) {
        if(balance > 10000000) throw new IllegalArgumentException("최대잔액은 1000만원을 넘을 수 없습니다.");
        this.balance = balance;
    }

}
