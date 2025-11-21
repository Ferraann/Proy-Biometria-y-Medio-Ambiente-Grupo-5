package com.example.grupo5.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.location.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DistanceManager {

    private static DistanceManager instance;
    private final Context ctx;

    private SharedPreferences prefs;
    private FusedLocationProviderClient client;
    private LocationCallback callback;

    private static final String PREF = "DIST_PREFS";
    private static final String KEY_DIST = "DIST_TODAY";
    private static final String KEY_LAST_LAT = "LAST_LAT";
    private static final String KEY_LAST_LON = "LAST_LON";
    private static final String KEY_DATE = "LAST_DATE";
    private static final String KEY_LAST_ACC = "LAST_ACC";

    private DistanceManager(Context c) {
        ctx = c.getApplicationContext();
        prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        client = LocationServices.getFusedLocationProviderClient(ctx);
    }

    public static DistanceManager get(Context c) {
        if (instance == null) instance = new DistanceManager(c);
        return instance;
    }

    public double getTodayDistance() {
        resetIfNeeded();
        return Double.longBitsToDouble(
                prefs.getLong(KEY_DIST, Double.doubleToLongBits(0.0))
        );
    }

    private void saveTodayDistance(double m) {
        prefs.edit().putLong(KEY_DIST, Double.doubleToLongBits(m)).apply();
    }

    private void saveLastLocation(double lat, double lon, float acc) {
        prefs.edit()
                .putFloat(KEY_LAST_LAT, (float) lat)
                .putFloat(KEY_LAST_LON, (float) lon)
                .putFloat(KEY_LAST_ACC, acc)
                .apply();
    }

    private boolean hasLastLocation() {
        return prefs.contains(KEY_LAST_LAT) && prefs.contains(KEY_LAST_LON);
    }

    private Location getLastLocation() {
        if (!hasLastLocation()) return null;
        Location l = new Location("prefs");
        l.setLatitude(prefs.getFloat(KEY_LAST_LAT, 0));
        l.setLongitude(prefs.getFloat(KEY_LAST_LON, 0));
        l.setAccuracy(prefs.getFloat(KEY_LAST_ACC, 100));
        return l;
    }

    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private void resetIfNeeded() {
        String last = prefs.getString(KEY_DATE, null);
        String now = today();
        if (!now.equals(last)) {
            prefs.edit()
                    .putString(KEY_DATE, now)
                    .remove(KEY_LAST_LAT)
                    .remove(KEY_LAST_LON)
                    .remove(KEY_LAST_ACC)
                    .putLong(KEY_DIST, Double.doubleToLongBits(0.0))
                    .apply();
        }
    }

    @SuppressLint("MissingPermission")
    public void start() {
        resetIfNeeded();

        if (callback != null) return;

        LocationRequest req = LocationRequest.create();
        req.setInterval(2000); // actualizar cada 2 s
        req.setFastestInterval(1500); // mínima cada 1,5 s
        req.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY); // menos batería, mejor en interiores
        req.setSmallestDisplacement(1f); // solo si te mueves ≥1 m

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                for (Location loc : result.getLocations()) {

                    // Ignorar lecturas con precisión mala (>20 m)
                    if (loc.hasAccuracy() && loc.getAccuracy() > 20) continue;

                    if (!hasLastLocation()) {
                        saveLastLocation(loc.getLatitude(), loc.getLongitude(), loc.getAccuracy());
                        prefs.edit().putString(KEY_DATE, today()).apply();
                        return;
                    }

                    Location last = getLastLocation();
                    float d = last.distanceTo(loc);

                    // Filtrar movimientos irreales (<0.5 m o >50 m)
                    if (d >= 0.5 && d <= 50) {
                        double acc = getTodayDistance();
                        acc += d;
                        saveTodayDistance(acc);
                    }

                    saveLastLocation(loc.getLatitude(), loc.getLongitude(), loc.getAccuracy());
                }
            }
        };

        client.requestLocationUpdates(req, callback, Looper.getMainLooper());
    }

    public void stop() {
        if (callback != null) {
            client.removeLocationUpdates(callback);
            callback = null;
        }
    }


}
