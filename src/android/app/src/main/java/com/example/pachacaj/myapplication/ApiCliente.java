package com.example.pachacaj.myapplication;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// ------------------------------------------------------------------
// Clase ApiCliente
// ------------------------------------------------------------------
// Descripción:
//   Clase que se encarga de configurar y crear una estancia
//  de Retrofit cliente que permite conectar tu aplicación
//  Android con un servidor web o Api
// ------------------------------------------------------------------

public class ApiCliente {
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
}
