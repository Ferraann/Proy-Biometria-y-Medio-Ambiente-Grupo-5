package com.example.grupo5.myapplication;

import android.bluetooth.BluetoothGatt;

public class BeaconManager {

    private static BluetoothGatt gatt = null;

    public static Runnable onBeaconDesconectado = null;

    public static void setGatt(BluetoothGatt g) {
        gatt = g;
    }

    public static void desconectar() {
        if (gatt != null) {
            gatt.disconnect();
            gatt.close();
            gatt = null;
        }

        // Avisar a quien esté escuchando
        if (onBeaconDesconectado != null) {
            onBeaconDesconectado.run();
        }
    }
}
