package com.hazelcast.jetfuel.config;

import com.amatkivskiy.gitter.sdk.model.response.message.MessageResponse;
import com.amatkivskiy.gitter.sdk.rx.client.RxGitterStreamingApiClient;
import com.google.gson.Gson;
import com.hazelcast.core.IMap;
import com.squareup.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit.client.OkClient;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class GitterClientConfig {

    @Value("${gitter.room_id}")
    String roomId;

    @Bean
    OkHttpClient getOkHttpClient() {
        OkHttpClient okClient = new OkHttpClient();

        // Configure OkHttpClient not to drop connection rapidly.
        okClient.setReadTimeout(10, TimeUnit.MINUTES);
        return okClient;
    }

    @Bean
    RxGitterStreamingApiClient gitterStreamingApiClient(@Value("${gitter.token}") String token,
                                                        Action1<MessageResponse> messageResponseAction1,
                                                        OkHttpClient httpClient) {
        final RxGitterStreamingApiClient gitterClient = new RxGitterStreamingApiClient.Builder()
                .withAccountToken(token)
                .withClient(new OkClient(httpClient))
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
