// ------------------------------------------------------------------
// Fichero: BluetoothDataSender.java
// Autor: Ferran Sansaloni Prats
// Fecha: 04/10/2025
// ------------------------------------------------------------------
// Descripción:
//   Esta clase se encarga de enviar los datos de los dispositivos
//   BTLE detectados al servidor (Plesk) mediante peticiones HTTP POST
//   usando la librería OkHttp. Permite también trabajar en modo offline
//   para simular las respuestas del servidor sin conexión, con la Logica Fake.
// ------------------------------------------------------------------

package com.example.pachacaj.myapplication;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// ------------------------------------------------------------------
// Clase: BluetoothDataSender
// ------------------------------------------------------------------
// Descripción:
//   Contiene los datos de un dispositivo BTLE detectado (nombre, UUID,
//   RSSI, major y minor) y los envía a la base de datos del servidor
//   mediante una petición HTTP POST en formato JSON.
// ------------------------------------------------------------------
public class BluetoothDataSender {

    // Atributos de la medición
    private String nombre;
    private String uuidString;
    private int rssi;
    private int major;
    private int minor;

    // Constantes de configuración
    private static final boolean MODO_OFFLINE = false; // ← cambia a true para usar la lógica fake

    // URL del fichero php donde enviar los datos
    private static final String URL_GUARDAR_MEDICION = "http://172.20.10.2/Sprint_0_Ferran_Sansaloni_Prats/src/servidor/api/api_post.php";

    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------
    // Descripción:
    //   Inicializa el objeto con los datos de un dispositivo BTLE detectado.
    // Diseño:
    //   String: nombre, String: uuidString, int: rssi, int: major, int: minor -->
    //                      BluetoothDataSender()
    //                                      <--
    // --------------------------------------------------------------
    public BluetoothDataSender(String nombre, String uuidString, int rssi, int major, int minor) {
        this.nombre = nombre;
        this.uuidString = uuidString;
        this.rssi = rssi;
        this.major = major;
        this.minor = minor;
    } // ()

    // --------------------------------------------------------------
    // Metodo: toString()
    // Descripción:
    //   Devuelve una representación en texto del objeto BluetoothDataSender.
    // --------------------------------------------------------------
    @Override
    public String toString() {
        return "BluetoothDataSender{" +
                "nombre='" + nombre + '\'' +
                ", uuidString='" + uuidString + '\'' +
                ", rssi=" + rssi +
                ", major=" + major +
                ", minor=" + minor +
                '}';
    } // ()

    // --------------------------------------------------------------
    // Metodo: guardarMedida()
    // Descripción:
    //   Envía la medición al servidor mediante una petición HTTP POST.
    //   Si el modo offline está activado, usa una respuesta simulada sin
    //   realizar la conexión real.
    // Diseño:
    //              guardarMedida()
    //                    <--
    // --------------------------------------------------------------
    public void guardarMedida() {

        // Si el modo es offline recibe el json que devuelve guardarMedicionFake y lo muestra en el Logcat
        // (no guarda nada en la base de datos)
        if (MODO_OFFLINE) {
            LogicaFake fake = new LogicaFake();
            JSONObject respuesta = fake.guardarMedicionFake(nombre, uuidString, rssi, major, minor);
            Log.d(">>>>>>", "Respuesta fake: " + respuesta.toString());
            return;
        }

        // Creamos el cliente OkHttp
        OkHttpClient client = new OkHttpClient();

        try {
            // Construir JSON para enviar
            JSONObject json = new JSONObject();
            json.put("nombre", nombre);
            json.put("id_sensor", 1);
            json.put("uuid", uuidString);
            json.put("rssi", rssi);
            json.put("major", major);
            json.put("minor", minor);
            json.put("latitud", 40.123456);
            json.put("longitud", -3.123456);
            json.put("medicionCo2", 1234);

            // Cuerpo de la peticion
            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            // Construcción de la petición POST
            Request request = new Request.Builder()
                    .url(URL_GUARDAR_MEDICION)
                    .post(body)
                    .build();

            // Mensajes:
            client.newCall(request).enqueue(new Callback() {
                // Se ejecuta si ocurre un error en la conexión o cualquier fallo de envío
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(">>>>>>", "Error al enviar medida: " + e.getMessage(), e);
                } // ()

                // Se ejecuta si el servidor responde correctamente.
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e(">>>>>>", "Respuesta error: " + response.body().string());
                    } else {
                        Log.d(">>>>>>", "Medida enviada correctamente: " + response.body().string());
                    }
                } // ()
            });

        } catch (Exception e) {
            // Error
            Log.e(">>>>>>", "Excepción al construir/enviar medida: " + e.getMessage(), e);
        }
    }
} // class BluetoothDataSender
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------

