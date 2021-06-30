package com.hingorani.safechat.Fragments;

import com.hingorani.safechat.Notifications.MyResponse;
import com.hingorani.safechat.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=Add your own Server Key for Cloud Messaging"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
