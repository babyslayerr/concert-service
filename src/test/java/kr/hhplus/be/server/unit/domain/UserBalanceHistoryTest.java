package kr.hhplus.be.server.unit.domain;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserBalanceHistory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserBalanceHistoryTest {

    @Test
    void 잔액내역타입이_충전으로_저장된다(){
        // given
        User user = new User();
        UserBalanceHistory history = new UserBalanceHistory();

        // when
        history.setChargeHistory(user,15000L,10000L);
        // then
        assertEquals("charge", history.getActionType());

    }

    @Test
    void 충전시_잔액내역의_잔액은_금액보다_작으면_예외가_발생한다(){
        // given
        User user = new User();
        UserBalanceHistory history = new UserBalanceHistory();

        // when, then
        assertThrows(IllegalArgumentException.class,()->{
            history.setChargeHistory(user,9999L,10000L);
        });

    }

    @Test
    void 잔액내역타입이_사용으로_저장된다(){
        // given
        User user = new User();
        UserBalanceHistory history = new UserBalanceHistory();

        // when
        history.setUseHistory(user,5000L,10000L);
        // then
        assertEquals("use", history.getActionType());
    }
}
