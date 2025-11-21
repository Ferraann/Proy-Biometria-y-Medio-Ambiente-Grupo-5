package com.example.grupo5.myapplication;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DistanceActivity extends AppCompatActivity {

    private static final int REQ = 100;
    private TextView tvDistance;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable refresher = new Runnable() {
        @Override
        public void run() {
            double meters = DistanceManager.get(DistanceActivity.this).getTodayDistance();

            String text = (meters > 1000)
                    ? String.format("%.2f km", meters / 1000)
                    : String.format("%.0f m", meters);

            tvDistance.setText("Distancia hoy: " + text);

            handler.postDelayed(this, 1000); // refresco cada 1 s
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        tvDistance = findViewById(R.id.tvDistance);

        // Pedimos permisos
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    REQ
            );
        } else {
            DistanceManager.get(this).start();
        }
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ) {
            boolean ok = true;

            for (int g : grantResults) {
                if (g != PackageManager.PERMISSION_GRANTED) ok = false;
            }

            if (ok) {
                DistanceManager.get(this).start();
            } else {
                Toast.makeText(this, "Permiso de ubicación necesario", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(refresher);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(refresher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DistanceManager.get(this).stop();
    }
}

