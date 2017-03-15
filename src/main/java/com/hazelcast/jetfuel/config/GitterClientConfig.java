package com.hazelcast.jetfuel.config;

import com.amatkivskiy.gitter.sdk.model.response.message.MessageResponse;
import com.amatkivskiy.gitter.sdk.rx.client.RxGitterStreamingApiClient;
import com.google.gson.Gson;
import com.hazelcast.core.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rx.functions.Action1;

@Configuration
@Slf4j
public class GitterClientConfig {

    @Value("${gitter.room_id}")
    String roomId;

    @Bean
    RxGitterStreamingApiClient gitterStreamingApiClient(@Value("${gitter.token}") String token,
                                                        Action1<MessageResponse> messageResponseAction1) {
        final RxGitterStreamingApiClient gitterClient = new RxGitterStreamingApiClient.Builder()
                .withAccountToken(token)
                .build();

        gitterClient.getRoomMessagesStream(roomId).subscribe(messageResponseAction1);
        return gitterClient;
    }

    @Bean
    Action1<MessageResponse> getMessageResponseAction(IMap<String, MessageResponse> responseCache) {
        return messageResponse -> {
            // write to Hazelcast now
            // TODO: push this to Kafka
            responseCache.putIfAbsent(messageResponse.id, messageResponse);
            log.info("messageResponse: {} ", new Gson().toJson(messageResponse));
        };
    }
}
