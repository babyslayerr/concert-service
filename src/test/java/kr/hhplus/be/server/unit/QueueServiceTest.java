package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.application.queue.dto.QueueResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueueServiceTest {

    @InjectMocks
    QueueService queueService;
    @Mock
    QueueRepository queueRepository;

    @Test
    void 토큰발급_요청시_대기열토큰이_발급된다(){
        // given
        given(queueRepository.save(any()))
                .willAnswer(invocation->
                    invocation.getArgument(0) // 첫번째 인자값 반환하기
                );

        // when
        String uuid = queueService.getToken();
        // then
        Assertions.assertNotNull(uuid);
    }

    @Test
    void 토큰확인_요청시_활성화된토큰의_가능여부를_확인한다(){

        // given
        String uuid = UUID.randomUUID().toString();
        given(queueRepository.findByUuid(uuid))
                        .willReturn(Optional.of(
                                Queue.builder()
                                        .id(1L)
                                        .uuid(uuid)
                                        .isActive("active")
                                        .build()
                        ));

        Queue queue = queueService.checkStatus(uuid);

        Assertions.assertEquals("active",queue.getIsActive());
    }

    @Test
    void 토큰_만료시간이_지났으면_토큰이_만료된다(){

        // given
        List<Queue> mockList = List.of(
                Queue.builder().build(),
                Queue.builder().build()
        );
        given(queueRepository.findByIsActiveAndExpireAtBefore(any(), any()))
                .willReturn(mockList);
        // when
        queueService.expireTokens();

        // then
        // 최소 한번 호출
        verify(queueRepository, atLeastOnce()).delete(any());
    }

    @Test
    void 최대활성화토큰은_100개이상이_될수없다(){
        // given
        // 활성화된 토큰갯수가 100개
        given(queueRepository.countByIsActive("active"))
                .willReturn(100L);

        // when
        queueService.activateTokens();

        // then
        // 활성화된 토큰 갯수가 100개라면 추가적인 활성화 로직이 실행되지 않아야 한다.
        verify(queueRepository, never()).findByIsActiveOrderByCreatedDateAsc(any(), any());
        verify(queueRepository, never()).save(any());
    }
    @Test
    void 활성화할수있는_토큰이_있는경우_최대100개까지_활성화된다() {
        // given
        // 활성화된 토큰 갯수가 95개
        given(queueRepository.countByIsActive("active"))
                .willReturn(95L);

        // 활성화할 수 있는 대기중 토큰 5개 준비
        List<Queue> waitingTokens = IntStream.range(0, 5)
                .mapToObj(i -> new Queue())
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(0, 5);
        given(queueRepository.findByIsActiveOrderByCreatedDateAsc("wait", pageable))
                .willReturn(waitingTokens);

        // when
        queueService.activateTokens();

        // then
        // 대기 중인 토큰 5개가 활성화 되었는지 확인
        verify(queueRepository).findByIsActiveOrderByCreatedDateAsc("wait", pageable);
        verify(queueRepository, times(5)).save(any());
        waitingTokens.forEach(token -> Assertions.assertEquals("active", token.getIsActive()));
    }

}
