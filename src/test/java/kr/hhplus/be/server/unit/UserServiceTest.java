package kr.hhplus.be.server.unit;


import kr.hhplus.be.server.domain.user.*;
import kr.hhplus.be.server.application.user.dto.UserBalanceHistoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
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
    void 사용자를_찾을수_없으면_NosuchElementException이발생한다(){
        // given
        long userId = 1L;
        given(userRepository.findById(userId)).willThrow(NoSuchElementException.class);

        // when, then
        Assertions.assertThrows(
                NoSuchElementException.class,
                ()->userService.findUserById(userId)
        );
    }

    @Test
    void 충전금액은_마이너스가_될수없다(){
        // given
        long userId = 1L;
        long amount = -10000L;
        given(userRepository.findById(userId)).willReturn(Optional.of(User.builder().build()));

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->
                userService.chargeAmount(userId, amount)
                );

    }

    @Test
    void 최대충전금액은_백만원을_넘을수없다(){
        // given
        long userId = 1L;
        long amount = 1000001L;
        given(userRepository.findById(userId)).willReturn(Optional.of(User.builder().build()));

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->
                        userService.chargeAmount(userId, amount)
        );
    }
    @Test
    void 잔액_최대량은_마이너스가_될수없다(){

        // given
        User user = User.builder().build();

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                ()-> {
                    user.setBalance(10000001L);
                });
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
