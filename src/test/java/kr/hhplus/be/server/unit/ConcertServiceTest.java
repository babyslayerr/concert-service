package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.concert.*;
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
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
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
        given(concertScheduleRepository.findByConcertId(concertId, pageable))
                .willReturn(mockPage);


        // when
        List<ConcertSchedule> availableDates = concertService.getAvailableConcert(concertId,pageable).getContent();

        // then
        Assertions.assertEquals("2025-01-01", availableDates.get(0).getConcertDate().format(DateTimeFormatter.ofPattern("YYYY-MM-DD")));
    }




    @Test
    void 특정콘서트아이디가_주어졌을때_해당하는_콘서트스케줄이_내림차순으로_조회된다(){

        // given
        Long concertId = 1L;
        List<ConcertSchedule> scheduleList = List.of(
        ConcertSchedule.builder()
                .id(1L)
                .concertDate(LocalDate.of(2024,1,1))
                .concert(Concert.builder()
                        .id(concertId)
                        .build())
                .build(),
        ConcertSchedule.builder()
                .id(2L)
                .concertDate(LocalDate.of(2024,3,1))
                .concert(Concert.builder()
                        .id(concertId)
                        .build())
                .build()
        );
        PageImpl<ConcertSchedule> concertSchedulePage = new PageImpl<>(scheduleList, PageRequest.of(0, 10), scheduleList.size());
        given(concertScheduleRepository.findByConcertId(any(),any()))
                .willReturn(concertSchedulePage);

        // when
        Page<ConcertSchedule> availableConcert = concertService.getAvailableConcert(concertId, PageRequest.of(0, 10));

        // then
        List<ConcertSchedule> list = availableConcert.getContent();
        Assertions.assertEquals(list.get(0).getConcertDate(),LocalDate.of(2024,1,1));
    }

    @Test
    void 특정콘서트ID가_주어졌지만_해당하는_스케줄이_없는경우_NoSuchElementException이_반환된다(){
        // given
        Long concertId = 1000L;
        Pageable pageable = PageRequest.of(0, 10);
        given(concertScheduleRepository.findByConcertId(concertId,pageable))
                .willReturn(new PageImpl<>(new ArrayList<>(),pageable,0));

        // when, then

        Assertions.assertThrows(
                NoSuchElementException.class,
                ()->
                concertService.getAvailableConcert(concertId,pageable)
        );
    }


    @Test
    void 예약가능한_좌석이_없을경우_NoSuchElementException이_반환된다(){
        // given
        long concertScheduleId = 1L;
        given(concertSeatRepository.findByConcertScheduleIdAndStatus(concertScheduleId,"available"))
                .willReturn(new ArrayList<>());

        // when, then
        Assertions.assertThrows(NoSuchElementException.class,()->
                concertService.getAvailableSeats(concertScheduleId)
                );
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
        List<ConcertSeat> seats = concertService.getAvailableSeats(concertScheduleId);

        // then
        Assertions.assertEquals("available" ,seats.get(0).getStatus());
    }
}
