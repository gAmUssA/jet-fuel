package com.hazelcast.jetfuel.web;

import com.amatkivskiy.gitter.sdk.model.response.message.MessageResponse;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.stream.IStreamList;
import com.hazelcast.jet.stream.IStreamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.hazelcast.jet.stream.DistributedCollectors.toIList;
import static com.hazelcast.jet.stream.DistributedCollectors.toIMap;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * TODO
 *
 * @author Viktor Gamov on 3/15/17.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
@RestController
@RequestMapping("/jet")
public class JetRestController {

    @Autowired
    public void setJet(JetInstance jet) {
        this.jet = jet;
    }

    private JetInstance jet;

    @RequestMapping(method = GET, path = "/top/{number}")
    @ResponseBody
    public String topUsers(@PathVariable("number") Integer number) {
        final IStreamMap<String, MessageResponse> map = jet.getMap("response-cache");
        final IStreamMap<String, Long> userMessagesCountMap = map.stream()
                .flatMap(e -> Stream.of(e.getValue()))
                .map(messageResponse -> messageResponse.fromUser)
                .map(userResponse -> userResponse.displayName)
                .collect(toIMap(k -> k, v -> 1L, (l, r) -> l + r));


        final IStreamList<String> list = userMessagesCountMap.stream()
                .sorted((o1, o2) -> Long.compare(o2.getValue(), o1.getValue()))
                //.sorted((Distributed.Comparator<Map.Entry<String, Long>>) (e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .limit(number)
                .map(e -> e.getKey() + " : " + e.getValue())
                .collect(toIList());
        return Arrays.toString(list.toArray());
    }
}
