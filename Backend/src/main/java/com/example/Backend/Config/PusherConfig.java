package com.example.Backend.Config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {
    private final Pusher pusher;
//    private boolean isConnected = false;
//
//    public PusherConfig() {
//        pusher = new Pusher("1775490", "62793b88f2071f8a4248", "fe7cfbf1d2ca204ba193");
//        pusher.setCluster("ap1");
//        pusher.setEncrypted(true);
//        try {
//            pusher.trigger("test_channel", "test_event", "test_data");
//            isConnected = true;
//        } catch (Exception e) {
//            isConnected = false;
//            System.err.println("Failed to connect to Pusher: " + e.getMessage());
//        }
//    }
//
//    public void triggerEvent(String channelName, String eventName, Object data) {
//        pusher.trigger(channelName, eventName, data);
//    }
//
//    public boolean isConnected() {
//        return isConnected;
//    }
//}

    public PusherConfig() {
        pusher = new Pusher("1775156", "044c1424302fc8d096c9", "1f4e6e0ae990248217ce");
        pusher.setCluster("ap1");
        pusher.setEncrypted(true);
//        pusher = new Pusher("1775490", "62793b88f2071f8a4248", "fe7cfbf1d2ca204ba193");
//        pusher.setCluster("ap1");
//        pusher.setEncrypted(true);

    }
    public void triggerEvent(String channelName, String eventName, Object data) {
        pusher.trigger(channelName, eventName, data);
    }
}


