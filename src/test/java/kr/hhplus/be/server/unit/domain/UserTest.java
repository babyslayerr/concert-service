package kr.hhplus.be.server.unit.domain;

import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    void 잔액이_저장된다(){
        User user = User.builder().balance(0L).build();

        // when
        user.chargeAmount(50000L);

        // then
        assertEquals(50000L,user.getBalance());

    }
    @Test
    void 최대잔액은_10000000을_넘을수_없다(){
        // given
        User user = User.builder().balance(0L).build();

        // when, then
        assertThrows(IllegalArgumentException.class,()->{
            user.chargeAmount(10000001L);
        });

    }

    @Test
    void 잔액이_차감된다(){
        // given
        User user = User.builder().balance(5000L).build();

        // when
        user.debitBalance(3000L);

        // then
        assertEquals(2000L,user.getBalance());

    }

    @Test
    void 사용금액은_잔액보다_작아야한다(){
        // given
        User user = User.builder().balance(500L).build();

        // when, then
        assertThrows(IllegalArgumentException.class,()->
                user.debitBalance(1000L)
                );
    }
}
