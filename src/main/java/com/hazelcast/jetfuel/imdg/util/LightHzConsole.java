package com.hazelcast.jetfuel.imdg.util;

import com.hazelcast.config.Config;
import com.hazelcast.console.ConsoleApp;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jetfuel.config.HazelcastJetConfig;
import com.hazelcast.jetfuel.imdg.MyListener;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

/**
 * light member console allows to debug hz cluster
 * uses {@link ConsoleApp}
 *
 * @author Viktor Gamov on 3/15/17.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class LightHzConsole {
    public static void main(String[] args) throws Exception {
        final Config config = new HazelcastJetConfig().config(new MyListener());
        config.setLiteMember(true);
        final HazelcastInstance hazelcastInstance = newHazelcastInstance(config);
        new ConsoleApp(hazelcastInstance).start();
    }
}
