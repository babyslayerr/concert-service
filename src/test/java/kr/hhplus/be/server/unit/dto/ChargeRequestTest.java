package kr.hhplus.be.server.unit.dto;

import kr.hhplus.be.server.presentation.user.dto.ChargeRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class ChargeRequestTest {
    @Test
    void 요청시_userId가_누락되면_예외발생(){
        Assertions.assertThrows(RuntimeException.class,()->{
            new ChargeRequest(null,1000L);
        });
    }

    @Test
    void 요청시_충전금액이_누락되면_예외발생(){
        Assertions.assertThrows(RuntimeException.class,()->{
            new ChargeRequest(1L,null);
        });

    }
}
