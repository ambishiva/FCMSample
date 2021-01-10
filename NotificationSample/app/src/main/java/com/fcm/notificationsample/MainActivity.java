package com.fcm.notificationsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    private String TAG = "fcm";

    private TextView tvToken;
    private Button sendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchFireBaseToken();
        tvToken = findViewById(R.id.token);
        sendNotification = findViewById(R.id.sendNotification);
        intialiseListener();
    }

    private void intialiseListener() {
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendNotification = new Intent(MainActivity.this, SendNotification.class);
                startActivity(sendNotification);
            }
        });
    }

    private void fetchFireBaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Toast.makeText(MainActivity.this, "Token is " + token, Toast.LENGTH_SHORT).show();
                        tvToken.setText(token);
                    }
                });
    }
}