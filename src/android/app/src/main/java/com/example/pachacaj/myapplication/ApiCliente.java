package com.example.pachacaj.myapplication;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
// ------------------------------------------------------------------
// Fichero: ApiCliente
// Autor: Pablo Chasi
// Fecha: 24/10/2025
// ------------------------------------------------------------------
// Clase ApiCliente
//
// Descripción:
//   Clase que se encarga de configurar y crear una estancia
//  de Retrofit cliente que permite conectar tu aplicación
//  Android con un servidor web o Api. Ademas, se declararan los metodos
//  de las apps.
// ------------------------------------------------------------------

public class ApiCliente {
    //Declaro mi ip
    private static final String urlLocal = "http://10.141.177.250/Proy-Biometria-y-Medio-Ambiente-Grupo-5/src/api/";

    //Pongo Retrofit en null para después comprobar si está o no funcional
    private static Retrofit retrofit = null;

    //-------------------------------------------------
    //      getApiService() --> retrofit
    //-------------------------------------------------
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

}
