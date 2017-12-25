package com.ifeins.nearbymediashare.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.Task;
import com.ifeins.nearbymediashare.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String SERVICE_ID = "com.ifeins.nearbymediashare";

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    @BindView(R.id.btn_advertise)
    Button mAdvertiseButton;

    private ConnectionLifecycleCallback mConnectionLifecycleCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(String s, ConnectionInfo connectionInfo) {

            }

            @Override
            public void onConnectionResult(String s, ConnectionResolution connectionResolution) {

            }

            @Override
            public void onDisconnected(String s) {

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "You must approve permissions to use the app", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        advertise();
    }

    @OnClick(R.id.btn_advertise)
    void advertise() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = { Manifest.permission.ACCESS_COARSE_LOCATION };
            requestPermissions(permissions, REQUEST_CODE_REQUIRED_PERMISSIONS);
            return;
        }

        Task<Void> task = Nearby.getConnectionsClient(this).startAdvertising(
                "john",
                SERVICE_ID,
                mConnectionLifecycleCallback,
                new AdvertisingOptions(Strategy.P2P_STAR));

        task.addOnSuccessListener((result) -> Log.d(TAG, "advertise: success"));
        task.addOnFailureListener((exception) -> Log.d(TAG, "advertise: failure" + exception.getMessage()));
    }
}
