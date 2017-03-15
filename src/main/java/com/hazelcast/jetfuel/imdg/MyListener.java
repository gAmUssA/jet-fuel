package com.hazelcast.jetfuel.imdg;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyListener implements
        EntryAddedListener,
        EntryUpdatedListener,
        EntryRemovedListener {

    @Override
    public void entryAdded(EntryEvent event) {
        log.info("Entry added [{} : {}]", event.getKey(), event.getValue());
    }

    @Override
    public void entryUpdated(EntryEvent event) {
        log.info("Entry updated [{} : {}]. Old value {}", event.getKey(), event.getValue(), event.getOldValue());
    }

    @Override
    public void entryRemoved(EntryEvent event) {
        log.info("Entry removed [{} : {}]", event.getKey(), event.getOldValue());
    }
}