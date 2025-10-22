// ------------------------------------------------------------------
// Fichero: LogicaFake.java
// Autor: Ferran Sansaloni Prats
// Fecha: 03/10/2025
// ------------------------------------------------------------------
// Descripción:
//   Clase auxiliar utilizada en modo offline para simular el envío de
//   mediciones a la base de datos. Genera un objeto JSON con los datos
//   recibidos y devuelve una respuesta simulada sin conexión real.
// ------------------------------------------------------------------

package com.example.pachacaj.myapplication;

import android.util.Log;

import org.json.JSONObject;

// ------------------------------------------------------------------
// Clase LogicaFake
// ------------------------------------------------------------------
// Descripción:
//   Implementa una lógica simulada para pruebas sin conexión. Crea y
//   devuelve un objeto JSON con los datos recibidos como si hubieran
//   sido correctamente guardados en la base de datos.
// ------------------------------------------------------------------
public class LogicaFake {

    // --------------------------------------------------------------
    // Metodo: guardarMedicionFake
    // Descripción:
    //   Crea un objeto JSON simulando una respuesta del servidor con
    //   los datos recibidos. Se utiliza cuando la app está en modo
    //   sin conexión (offline).
    // Diseño:
    //   String: nombre, String: uuid, int: rssi, int: major, int: minor -->
    //                                   guardarMedicionFake()
    //                                                      JSON <--
    // --------------------------------------------------------------
    public JSONObject guardarMedicionFake(String nombre, String uuid, int rssi, int major, int minor) {
        try {
            // json a enviar
            JSONObject respuesta = new JSONObject();

            // Campos simulados
            respuesta.put("success", true);
            respuesta.put("mensaje", "Medición simulada (modo sin conexión)");

            // Datos recibidos
            respuesta.put("id", 999);
            respuesta.put("nombre", nombre);
            respuesta.put("uuid", uuid);
            respuesta.put("rssi", rssi);
            respuesta.put("major", major);
            respuesta.put("minor", minor);
            return respuesta;

        } catch (Exception e) {
            // Error
            Log.e(">>>>>>", "Erro al enviar el JSON sin conexión " + e.getMessage(), e);
            return null;
        }
    } // ()

} // class LogicaFake
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
