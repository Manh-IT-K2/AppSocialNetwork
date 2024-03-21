package com.example.Backend.Config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {
    private final Pusher pusher;

    public PusherConfig() {
        pusher = new Pusher("1775156", "044c1424302fc8d096c9", "1f4e6e0ae990248217ce");
        pusher.setCluster("ap1");
        pusher.setEncrypted(true);
    }

    public void triggerEvent(String channelName, String eventName, Object data) {
        pusher.trigger(channelName, eventName, data);
    }
}
