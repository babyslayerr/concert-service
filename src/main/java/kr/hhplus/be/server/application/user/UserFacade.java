package kr.hhplus.be.server.application.user;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.application.user.dto.UserBalanceHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;

    @Transactional
    public Long getBalance(Long userId){
        return userService.getBalance(userId);
    }

    @Transactional
    public UserBalanceHistoryResponse chargeAmount(Long userId, Long amount){

        UserBalanceHistoryResponse response = userService.chargeAmount(userId, amount);
        return response;
    }
}
