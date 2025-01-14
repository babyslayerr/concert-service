package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    void 콘서트스케줄_유저아이디가_좌석이_주어지면_해당좌석을_예약한다(){
        // given
        Long price = 5000L;
        ConcertSeat concertSeat = ConcertSeat.builder().build(); // 1번 콘서트
        User user = User.builder().build();

        // 첫번째 인자로 받은 객체를 반환
        BDDMockito.given(reservationRepository.save(any(Reservation.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        Reservation savedReservation = reservationService.makeReservation(user, concertSeat, price);

        // then
        Assertions.assertEquals("reserved", savedReservation.getStatus());
    }

    @Test
    void 결제전에_예약이_만료시_예외가_발생한다(){

        // given
        Long reservationId = 1L;
        Reservation mockReservation = Reservation.builder()
                .id(reservationId)
                .status("expired")
                .build();
        BDDMockito.given(reservationRepository.findById(reservationId))
                .willReturn(Optional.of(mockReservation));

        // when / then
        Assertions.assertThrows(IllegalStateException.class, () -> {
            reservationService.completeReservation(reservationId);
        });
    }

    @Test
    void 예약의상태가_예약에서_만료로_변경된다(){
        // given
        List<Reservation> reservations = List.of(Reservation.builder()
                .concertSeat(new ConcertSeat()).status("reserved").build());
        given(reservationRepository.findByExpireAtBeforeAndStatus(any(),anyString()))
                .willReturn(reservations);

        // when
        reservationService.expireReservation();

        // then
        reservations.forEach(reservation
                -> Assertions.assertEquals("expired", reservation.getStatus()));

    }
    @Test
    void 예약상태가_만료될때_연관된_좌석상태도_만료로_변경된다(){

        // given
        List<Reservation> reservations = List.of(Reservation.builder()
                .concertSeat(new ConcertSeat()).status("reserved").build());
        given(reservationRepository.findByExpireAtBeforeAndStatus(any(),anyString()))
                .willReturn(reservations);

        // when
        reservationService.expireReservation();

        // then
        reservations.forEach(reservation
                -> Assertions.assertEquals("available", reservation.getConcertSeat().getStatus()));

    }

    @Test
    void 결제완료시_예약이_완료상태로_변경된다(){
        // given
        Reservation reservedReservation = Reservation
                .builder()
                .status("reserved")
                .build();

        // when
        reservedReservation.completeReservation();

        // then
        Assertions.assertEquals("completed", reservedReservation.getStatus());
    }
}
