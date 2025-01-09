package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.concert.*;
import kr.hhplus.be.server.presentation.concert.dto.ConcertScheduleResponse;
import kr.hhplus.be.server.presentation.concert.dto.ConcertSeatResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {

    @Mock
    ConcertScheduleRepository concertScheduleRepository;

    @Mock
    ConcertSeatRepository concertSeatRepository;
    @InjectMocks
    ConcertService concertService;


    @Test
    void 콘서트식별자가_주어질경우_콘서트스케줄리스트가_반환된다(){
        // given
        Long concertId = 1L;
        Concert concert = Concert.builder()
                .id(concertId)
                .build();

        ConcertSchedule concertSchedule = ConcertSchedule.builder()
                .id(1L)
                .concertDate(LocalDate.of(2025, 1, 1))
                .concert(concert)
                .build();
        List<ConcertSchedule> mockList = new ArrayList<>();
        mockList.add(concertSchedule);
        Pageable pageable = PageRequest.of(1, 10);
        Page<ConcertSchedule> mockPage = new PageImpl<>(mockList, pageable, mockList.size());
        given(concertScheduleRepository.findByConcertIdOrderByConcertDateAsc(concertId, pageable))
                .willReturn(mockPage);


        // when
        List<ConcertScheduleResponse> availableDates = concertService.getAvailableConcertDates(concertId,pageable);

        // then
        Assertions.assertEquals("2025-01-01", availableDates.get(0).getCreatedDate().format(DateTimeFormatter.ofPattern("YYYY-MM-DD")));
    }


    @Test
    void 콘서트스케줄이_주어질경우_예약가능한_좌석이_반환된다(){

        // given
        Long concertScheduleId = 1L;
        List<ConcertSeat> mockList = new ArrayList<>();
        mockList.add(ConcertSeat.builder()
                .status("available")
                .build());
        given(concertSeatRepository.findByConcertScheduleIdAndStatus(concertScheduleId,"available")).willReturn(mockList);

        // when
        List<ConcertSeatResponse> seats = concertService.getAvailableSeats(concertScheduleId);

        // then
        Assertions.assertEquals("available" ,seats.get(0).getStatus());
    }

}
