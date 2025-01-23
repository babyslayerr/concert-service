package kr.hhplus.be.server.domain.user;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.user.dto.UserBalanceHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserBalanceHistoryRepository userBalanceHistoryRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public Long getBalance(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getBalance();
    }

    public UserBalanceHistoryResponse chargeAmount(Long userId, Long amount) {
        // user 내 잔액 저장
        User user = userRepository.findById(userId).orElseThrow();

        user.chargeAmount(amount);
        userRepository.flush();

        // user 잔액 히스토리 저장
        UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
        userBalanceHistory.setChargeHistory(user, user.getBalance(),amount);
        UserBalanceHistory saved = userBalanceHistoryRepository.save(userBalanceHistory);

        // response 객체 생성
        UserBalanceHistoryResponse response = UserBalanceHistoryResponse.builder()
                .amount(saved.getAmount())
                .balance(saved.getBalance())
                .actionType(saved.getActionType())
                .build();
        return response;
    }

    public User findUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> {
            log.warn("cannot found user");
            return new NoSuchElementException("유저를 찾을 수 없습니다.");
        });
    }

    public UserBalanceHistory makePayment(Long userId, Long price) {
        User user = userRepository.findById(userId).orElseThrow();

        // 잔액 차감 versioning 을 통한 동시성 제어중
        user.debitBalance(price);
        userRepository.flush();

        // 사용내역 저장
        UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
        userBalanceHistory.setUseHistory(user,user.getBalance(),price);
        return userBalanceHistoryRepository.save(userBalanceHistory);
    }
}
