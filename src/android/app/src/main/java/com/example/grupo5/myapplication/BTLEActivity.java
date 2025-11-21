package com.example.grupo5.myapplication;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.grupo5.myapplication.R;
import com.example.grupo5.myapplication.TramaIBeacon;
import com.example.grupo5.myapplication.Utilidades;
import com.example.grupo5.myapplication.ApiCliente;
import com.example.grupo5.myapplication.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class BTLEActivity extends AppCompatActivity {

    // ---- Gestión del estado del beacon ----
    private long ultimoVisto = 0;
    private static final long TIEMPO_MAX_SIN_SEÑAL = 5000;
    private boolean beaconPresente = false;

    private static final String ETIQUETA_LOG = ">>>>";
    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;
    private final List<ScanFilter> filtros = new ArrayList<>();

    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        Log.d(ETIQUETA_LOG, "onCreate()");

        inicializarBlueTooth();
        iniciarHiloDeSupervision();
    }

    // --------------------------------------------------------------
    private void iniciarHiloDeSupervision() {
        new Thread(() -> {
            while (true) {
                try { Thread.sleep(2000); } catch (Exception ignored) {}

                if (beaconPresente &&
                        System.currentTimeMillis() - ultimoVisto > TIEMPO_MAX_SIN_SEÑAL) {

                    beaconPresente = false;
                    mostrarNotificacionDesconexion();
                    Log.e(ETIQUETA_LOG, "⚠ Beacon perdido (desconectado)");
                }
            }
        }).start();
    }

    // --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, "Buscar todos los dispositivos BTLE");

        callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e(ETIQUETA_LOG, "Error en escaneo: " + errorCode);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) return;

        elEscanner.startScan(callbackDelEscaneo);
    }

    // --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {
        ultimoVisto = System.currentTimeMillis();
        if (!beaconPresente) {
            beaconPresente = true;
        }

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) return;

        Log.d(ETIQUETA_LOG, "-------------------------------------------------");
        Log.d(ETIQUETA_LOG, "Dispositivo detectado");
        Log.d(ETIQUETA_LOG, "Nombre: " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, "MAC: " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, "RSSI: " + rssi);

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, "UUID: " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, "Major: " + Utilidades.bytesToInt(tib.getMajor()));
        Log.d(ETIQUETA_LOG, "Minor: " + Utilidades.bytesToInt(tib.getMinor()));

        enviarDatosAlServidor(
                Utilidades.bytesToLong(tib.getMinor()),
                Utilidades.bytesToLong(tib.getMajor())
        );
    }

    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado(View v) {
        buscarTodosLosDispositivosBTLE();
    }

    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        buscarEsteDispositivoBTLE("Grupo 6");
    }

    // --------------------------------------------------------------
    private void buscarEsteDispositivoBTLE(String dispositivoBuscado) {
        callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                mostrarInformacionDispositivoBTLE(resultado);
            }
        };

        filtros.clear();
        filtros.add(new ScanFilter.Builder().setDeviceName(dispositivoBuscado).build());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) return;

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(SCAN_MODE_LOW_LATENCY)
                .build();

        elEscanner.startScan(filtros, settings, callbackDelEscaneo);
    }

    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {
        if (callbackDelEscaneo == null) return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) return;

        elEscanner.stopScan(callbackDelEscaneo);
        callbackDelEscaneo = null;
    }

    // --------------------------------------------------------------
    private void inicializarBlueTooth() {
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) return;

        bta.enable();
        elEscanner = bta.getBluetoothLeScanner();

        if (elEscanner == null) {
            Log.e(ETIQUETA_LOG, "ERROR: No se pudo obtener escáner BLE");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    CODIGO_PETICION_PERMISOS
            );
        }
    }

    // --------------------------------------------------------------
    private void enviarDatosAlServidor(float CO2, float Temperatura) {
        ApiService api = ApiCliente.getApiService();
        Call<Void> call = api.enviarDatos(CO2, Temperatura);

        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful())
                    Log.d(ETIQUETA_LOG, "Datos enviados correctamente");
                else
                    Log.e(ETIQUETA_LOG, "Error HTTP: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(ETIQUETA_LOG, "Error al enviar datos: " + t.getMessage());
            }
        });
    }

    // --------------------------------------------------------------
    private void mostrarNotificacionDesconexion() {
        String channelId = "canal_ble_alertas";

        // Crear canal de notificación (Android 8+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel canal =
                    new NotificationChannel(channelId, "Alertas BLE",
                            NotificationManager.IMPORTANCE_HIGH);
            canal.setLightColor(0xFF00FFFF); // color cyan/neón
            canal.enableLights(true);
            canal.enableVibration(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_alert) // icono más llamativo
                .setContentTitle("⚠ Dispositivo fuera de rango")
                .setContentText("El beacon ha dejado de emitir señal.")
                .setColor(0xFF00FFFF) // color del icono en la barra de notificaciones
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setLights(0xFF00FFFF, 500, 1000) // luz tipo neón
                .setVibrate(new long[]{0, 500, 200, 500}); // vibración rítmica

        NotificationManagerCompat.from(this).notify(1001, builder.build());
}

}