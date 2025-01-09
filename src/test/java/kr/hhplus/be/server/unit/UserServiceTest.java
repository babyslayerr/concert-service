package kr.hhplus.be.server.unit;


import kr.hhplus.be.server.domain.user.*;
import kr.hhplus.be.server.presentation.user.dto.UserBalanceHistoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserBalanceHistoryRepository userBalanceHistoryRepository;

    @InjectMocks
    UserService userService;

    @Test
    void 사용자식별자가_주어지면_사용자의잔액을_조회한다(){
        
        // given
        Long userId = 1L;
        User mockUser = User.builder()
                        .username("username")
                        .balance(5000L)
                        .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        // when
        Long userBalance = userService.getBalance(userId);

        // then
        assertEquals(5000L,userBalance);

    }

    @Test
    void 사용자와금액이_주어지면_잔액이_충전되고_충전내역이_반환된다(){
        // given
        Long userId = 1L;
        Long amount = 5000L;
        User user = User.builder()
                .balance(5000L)
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userBalanceHistoryRepository.save(any())).willAnswer(invocation->
                invocation.getArgument(0) // 첫번째 인자값 반환하기
        );

        // when
        UserBalanceHistoryResponse response = userService.chargeAmount(userId,amount);

        // then
        // 저장 여부 확인
        assertEquals(10000,response.getBalance());
    }
}
