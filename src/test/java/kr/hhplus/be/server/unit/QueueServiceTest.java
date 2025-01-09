package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.presentation.queue.QueueResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class QueueServiceTest {

    @InjectMocks
    QueueService queueService;
    @Mock
    QueueRepository queueRepository;

    @Test
    void 토큰발급_요청시_대기열토큰이_발급된다(){
        // given
        BDDMockito.given(queueRepository.save(any()))
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
        BDDMockito.given(queueRepository.findByUuid(uuid))
                        .willReturn(Optional.of(
                                Queue.builder()
                                        .id(1L)
                                        .uuid(uuid)
                                        .isActive("active")
                                        .build()
                        ));

        QueueResponse queueResponse = queueService.checkStatus(uuid);

        Assertions.assertEquals("active",queueResponse.getIsActive());
    }


}
