package com.fcm.notificationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fcm.notificationsample.model.NotificationData;
import com.fcm.notificationsample.model.NotificationRequestModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class SendNotification extends AppCompatActivity {

    private EditText etToken;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        initialiseViews();
        initialiseListener();

    }

    private void initialiseListener() {
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etToken.getText().toString().isEmpty()) {
                    Toast.makeText(SendNotification.this, "Token is Empty", Toast.LENGTH_SHORT).show();
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sendNotification();
                            } catch (Exception e) {
                                Log.d("exception", e.getMessage());
                            }
                        }
                    }).start();


                }
            }
        });
    }

    private void sendNotification() throws IOException, JSONException {

        NotificationData notificationData = new NotificationData();
        NotificationRequestModel notificationRequestModel = new NotificationRequestModel();
        notificationData.setDetail("this is firebase push notification from java client (server)");
        notificationData.setTitle("Hello Firebase Push Notification");
        notificationRequestModel.setData(notificationData);
        notificationRequestModel.setTo(etToken.getText().toString());


        Gson gson = new Gson();
        Type type = new TypeToken<NotificationRequestModel>() {
        }.getType();

        String json = gson.toJson(notificationRequestModel, type);
        StringEntity input = new StringEntity(json);
        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                .addJSONObjectBody(new JSONObject(json)) // posting json
                .addHeaders("content-type", "application/json")
                .addHeaders("Authorization", "key=AAAA3uda8_M:APA91bHO9BzfzDKaSQSuq33SIMrtVb1Q0B57sfwz5kwlLonYw2RPicaXqcOQEpoE9Ozubr1Y3Ie84-tUWsVb6NDhEPBx1tOl7JR3JYLEqTsVi3rEZGMXhlVAT7JIQpAOc3PfGIuIWstg")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.d("Notifivation Sent Response", response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Log.d("Notifivation error Response", error.getErrorDetail());
                    }
                });

//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpPost postRequest = new HttpPost(
//                "https://fcm.googleapis.com/fcm/send");
//
//        NotificationRequestModel notificationRequestModel = new NotificationRequestModel();
//        NotificationData notificationData = new NotificationData();
//
//        notificationData.setDetail("this is firebase push notification from java client (server)");
//        notificationData.setTitle("Hello Firebase Push Notification");
//        notificationRequestModel.setData(notificationData);
//        notificationRequestModel.setTo(etToken.getText().toString());
//
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<NotificationRequestModel>() {
//        }.getType();
//
//        String json = gson.toJson(notificationRequestModel, type);
//
//        StringEntity input = new StringEntity(json);
//        input.setContentType("application/json");
//        postRequest.addHeader("Authorization", "key=AAAA7-UgB34:APA91bFz75NiS8BUX9LJDeEk9kcCahpZ1-MYyFfQ3d2gof0vUAFKNcQCmmf_10aZkmZH6fVPvcF71kgHq5Ab4ol0mvhvVwgMn-HS_0i-2DipSxd2Jf2_NUTYtJMOVwUGP318ldDgBBvP");
//        postRequest.setEntity(input);
//
//
//        System.out.println("reques:" + json);
//
//
//        HttpResponse response = httpClient.execute(postRequest);
//
//        if (response.getStatusLine().getStatusCode() != 200) {
//            throw new RuntimeException("Failed : HTTP error code : "
//                    + response.getStatusLine().getStatusCode());
//        } else if (response.getStatusLine().getStatusCode() == 200) {
//            System.out.println("response:" + EntityUtils.toString(response.getEntity()));
//        }

    }

    private void initialiseViews() {
        etToken = findViewById(R.id.etToken);
        buttonSend = findViewById(R.id.send);
    }
}