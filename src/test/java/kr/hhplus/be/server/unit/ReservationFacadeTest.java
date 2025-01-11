package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.presentation.reservation.dto.ReservationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ReservationFacadeTest {

    @InjectMocks
    ReservationFacade reservationFacade;
    @Mock
    ConcertService concertService;
    @Mock
    ReservationService reservationService;

    @Mock
    UserService userService;

    @Mock
    QueueService queueService;

    @Test
    void 콘서트_좌석예약시_좌석에약과_내역이_생성된다() {

        // given
        Long userId = 1L;
        Long concertScheduleId = 1L;
        Long seatNo = 1L;
        given(concertService.reserveAvailableSeat(any(), any(), any()))
                .willReturn(ConcertSeat.builder()
                        .id(1L)
                        .price(10000L)
                        .build());

        given(reservationService.makeReservation(any(),any(),any()))
                .willReturn(Reservation.builder().build());

        InOrder inOrder = inOrder(concertService, reservationService);

        // when
        ReservationResponse response = reservationFacade.reserveSeat(userId, concertScheduleId, seatNo);

        // then
        // 호출 순서 확인
        inOrder.verify(concertService).reserveAvailableSeat(any(), any(), any());
        inOrder.verify(reservationService).makeReservation(any(), any(), any());
    }


    @Test
    void 좌석_결제시_좌석결제_프로세스순서가_보장된다() {
        // given
        Long userId = 1L;
        Long concertSeatId = 1L;
        Long reservationId = 1L;
        String tokenUuid = "testTokenUuid";

        ConcertSeat mockConcertSeat = ConcertSeat.builder()
                .price(10000L)
                .build();
        User mockUser = User.builder()
                .build();

        given(concertService.findConcertSeatById(concertSeatId)).willReturn(mockConcertSeat);
        given(userService.findUserById(userId)).willReturn(mockUser);


        // when
        reservationFacade.makeSeatPayment(userId, concertSeatId, reservationId, tokenUuid);

        // then
        InOrder inOrder = inOrder(concertService, userService, reservationService, queueService);
        // 1. 좌석 조회
        inOrder.verify(concertService).findConcertSeatById(any());

        // 2. 사용자 조회
        inOrder.verify(userService).findUserById(any());

        // 3. 결제 요청
        inOrder.verify(userService).makePayment(any(), any());

        // 4. 좌석 상태 변경
        inOrder.verify(concertService).saveConcertSeat(any());

        // 5. 예약 상태 변경
        inOrder.verify(reservationService).completeReservation(any());

        // 6. 토큰 삭제
        inOrder.verify(queueService).removeToken(any());

    }
}