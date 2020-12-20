package com.example.myservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    boolean mServiceBound = false;
    MyBoundService mBoundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartService = findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(v -> {
            Intent mStartServiceIntent = new Intent(MainActivity.this, MyService.class);
            startService(mStartServiceIntent);
        });

        Button btnStartJobIntentService = findViewById(R.id.btn_start_job_intent_service);
        btnStartJobIntentService.setOnClickListener(v -> {
            Intent mStartIntentService = new Intent(MainActivity.this, MyJobIntentService.class);
            mStartIntentService.putExtra(MyJobIntentService.EXTRA_DURATION, 5000L);
            MyJobIntentService.enqueueWork(this, mStartIntentService);
        });

        Button btnStartBoundService = findViewById(R.id.btn_start_bound_service);
        btnStartBoundService.setOnClickListener(v -> {
            Intent mBoundServiceIntent = new Intent(MainActivity.this, MyBoundService.class);
            bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        });

        Button btnStopBoundService = findViewById(R.id.btn_stop_bound_service);
        btnStopBoundService.setOnClickListener(v -> {
            unbindService(mServiceConnection);
        });
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBoundService.MyBinder myBinder = (MyBoundService.MyBinder) service;
            myBinder.getService();
            mServiceBound = true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceBound) {
            unbindService(mServiceConnection);
        }
    }
}