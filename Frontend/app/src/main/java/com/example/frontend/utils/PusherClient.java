package com.example.frontend.utils;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;

public class PusherClient {
    public static Pusher init(){
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        options.setEncrypted(true);

        Pusher pusher = new Pusher("044c1424302fc8d096c9", options);
        return pusher;
    }
}
