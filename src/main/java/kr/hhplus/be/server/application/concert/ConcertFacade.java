package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private String test;


}
