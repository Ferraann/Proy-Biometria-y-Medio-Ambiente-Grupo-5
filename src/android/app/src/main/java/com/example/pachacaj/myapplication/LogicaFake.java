/*// ------------------------------------------------------------------
// Fichero: LogicaFake.java
// Autor: Ferran Sansaloni Prats
// Fecha: 24/10/2025
// ------------------------------------------------------------------
// Clase LogicaFake
// ------------------------------------------------------------------
// Descripción:
//   Implementa una lógica simulada para pruebas sin conexión. Crea y
//   devuelve un objeto JSON con los datos recibidos como si hubieran
//   sido correctamente guardados en la base de datos.
// ------------------------------------------------------------------

package com.example.pachacaj.myapplication;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogicaFake {
    //Declaro mi ip
    private static final String urlLocal = "http://10.15.138.250/Proyecto-Aplicaciones-De-Biometr-a-Y-Medio-Ambiente/src/Api/";

    //Pongo Retrofit en null para después comprobar si está o no funcional
    private static Retrofit retrofit = null;

    public static ApiService getApiService(){
        if (retrofit == null) {
            //interceptar metodo php
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //Consigo el cliente para enviarlo
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            //Declaro mi estructura para después saber donde envio los datos
            retrofit = new Retrofit.Builder()
                    .baseUrl(urlLocal)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
} // class LogicaFake
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
*/
