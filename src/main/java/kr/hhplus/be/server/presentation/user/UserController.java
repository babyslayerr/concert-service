package kr.hhplus.be.server.presentation.user;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.application.user.dto.UserBalanceHistoryResponse;
import kr.hhplus.be.server.presentation.user.dto.ChargeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    // 잔액 충전 API
    @Operation(summary = "잔액 충전 API")
    @PostMapping("/payment/charge")
    public ResponseEntity<UserBalanceHistoryResponse> chargeBalance(@RequestBody ChargeRequest chargeRequest) {
        UserBalanceHistoryResponse userBalanceHistoryResponse = userFacade.chargeAmount(chargeRequest.userId(), chargeRequest.amount());
        return ResponseEntity.ok(userBalanceHistoryResponse);
    }

    // 잔액 조회 API
    @Operation(summary = "잔액 조회 API")
    @GetMapping("/payment/balance/{userId}")
    public ResponseEntity<Long> getBalance(
            @PathVariable(name = "userId") Long userId) {

        Long balance = userFacade.getBalance(userId);
        return ResponseEntity.ok(balance);
    }

}
