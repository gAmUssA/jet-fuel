package com.hazelcast.jetfuel.config;

import com.amatkivskiy.gitter.sdk.model.response.message.MessageResponse;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.EntryListenerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.map.listener.MapListener;
import info.jerrinot.subzero.SubZero;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author Viktor Gamov on 3/14/17.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
@Configuration
public class HazelcastJetConfig {

    @Bean
    public Config config(MapListener listener) {
        final ClasspathXmlConfig classpathXmlConfig = new ClasspathXmlConfig("hazelcast.xml");

        // listener config
        EntryListenerConfig lc = new EntryListenerConfig();
        lc.setIncludeValue(true).setImplementation(listener);
        classpathXmlConfig.getMapConfig("response-cache").addEntryListenerConfig(lc);

        // serialization config
        SubZero.useForClasses(classpathXmlConfig, MessageResponse.class);

        return classpathXmlConfig;
    }

    @Bean
    public JetInstance jetInstance(Config config) {
        JetConfig jetConfig = new JetConfig();
        jetConfig.setHazelcastConfig(config);
        return Jet.newJetInstance(jetConfig);
    }

    @Bean
    public HazelcastInstance hazelcastInstance(JetInstance jetInstance) {
        return jetInstance.getHazelcastInstance();
    }

    @Bean
    public IMap<String, MessageResponse> getResponseCacheMap(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("response-cache");
    }
}
