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

    @Transactional
    public Long getBalance(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getBalance();
    }

    @Transactional
    public UserBalanceHistoryResponse chargeAmount(Long userId, Long amount) {
        // user 내 잔액 저장
        User user = userRepository.findById(userId).orElseThrow();
        if(amount < 0 || amount > 1000000) {
            log.error("amount is under 0 or over 1000000");
            throw new IllegalArgumentException("잘못된 충전 금액입니다.");};
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
        return userRepository.findById(userId).orElseThrow(()-> {
            log.error("cannot found user");
            return new NoSuchElementException("유저를 찾을 수 없습니다.");
        });
    }

    public UserBalanceHistory makePayment(Long userId, Long price) {
        User user = userRepository.findById(userId).orElseThrow();
        if(user.getBalance() < price){
            log.error("not enough balance, balance: {}, price: {}",user.getBalance(),price);
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
