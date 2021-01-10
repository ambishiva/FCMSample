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
                        etToken.setText("");
                        Toast.makeText(SendNotification.this,"Notification sent successfully",Toast.LENGTH_SHORT).show();
                        Log.d("Notification Sent Response", response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("Notification error Response", error.getErrorDetail());
                    }
                });

    }

    private void initialiseViews() {
        etToken = findViewById(R.id.etToken);
        buttonSend = findViewById(R.id.send);
    }
}