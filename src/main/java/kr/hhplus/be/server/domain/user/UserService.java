package kr.hhplus.be.server.domain.user;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.presentation.user.dto.UserBalanceHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserBalanceHistoryRepository userBalanceHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long getBalance(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getBalance();
    }

    @Transactional
    public UserBalanceHistoryResponse chargeAmount(Long userId, Long amount) {
        // user 내 잔액 저장
        User user = userRepository.findById(userId).orElseThrow();
        user.setBalance(user.getBalance()+amount);
        userRepository.save(user);

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
        return userRepository.findById(userId).orElseThrow();
    }

    public UserBalanceHistory makePayment(User user, Long price) {
        if(user.getBalance() < price){
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        // 잔액이 충분하면 결제
        user.setBalance(user.getBalance()-price);
        userRepository.save(user);

        // 사용내역 저장
        UserBalanceHistory userBalanceHistory = new UserBalanceHistory();
        userBalanceHistory.setUseHistory(user,user.getBalance(),price);
        return userBalanceHistoryRepository.save(userBalanceHistory);
    }
}
