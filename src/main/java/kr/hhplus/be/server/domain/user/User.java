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

    @Version
    private int version;

    // 잔액 충전
    public void chargeAmount(Long amount) {
        if(amount < 0 || amount > 1000000) {
            throw new IllegalArgumentException("충전금액이 0보다 작거나 최대충전보다 큰 금액입니다.");
        }
        Long newBalance = this.balance + amount;

        if(newBalance > 10000000) throw new IllegalArgumentException("최대잔액은 1000만원을 넘을 수 없습니다.");
        this.balance = newBalance;

    }

    // 잔액 차감
    public void debitBalance(Long price) {
        if(this.balance < price){
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.balance -= price;
    }
}
