package com.example.frontend.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.frontend.response.GroupChat.GroupChatWithMessagesResponse;
import com.example.frontend.utils.PushNotificationHelper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.lang.reflect.Type;
import java.util.List;

public class PusherNotificationService extends Service {

    private Pusher pusher;
    private Channel channel;

    @Override
    public void onCreate() {
        super.onCreate();


        // Kết nối đến Pusher
        pusher.connect();

        // Đăng ký kênh để nhận thông báo từ Pusher
        channel = pusher.subscribe("ListGroupChat"); // Thay your_channel_name bằng tên kênh của bạn

        // Đặt lắng nghe sự kiện
        channel.bind("lastmess", new SubscriptionEventListener() { // Thay your_event_name bằng tên sự kiện của bạn
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                Log.d("Pusher", "Received new message: " + data);
                // Hiển thị thông báo khi nhận được sự kiện từ Pusher
                PushNotificationHelper notificationHelper = new PushNotificationHelper(getApplicationContext());
                notificationHelper.showNotification("Groupchat", "test");
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Không cần onBind trong trường hợp này
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Ngắt kết nối và hủy đăng ký kênh khi dịch vụ bị hủy
        pusher.unsubscribe("your_channel_name"); // Thay your_channel_name bằng tên kênh của bạn
        pusher.disconnect();
    }
}
