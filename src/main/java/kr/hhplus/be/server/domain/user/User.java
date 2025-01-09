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
        this.balance = balance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
