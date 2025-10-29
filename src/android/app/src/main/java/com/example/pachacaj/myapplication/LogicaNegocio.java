package com.example.pachacaj.myapplication;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//------------------------------------------------------------------
// Fichero: ApiCliente
// Autor: Pablo Chasi
// Fecha: 24/10/2025
//------------------------------------------------------------------
// Clase LogicaNegocio
//
// Descripción:
//  Esta clase se encargar de hacer toda la logica de negocio de
//  la app movil al servidor, se declara metodos post,get,insert...
//------------------------------------------------------------------

public class LogicaNegocio {

    //-------------------------------------------------------------------------------------------
    //     Nombre:txt, Apellidos:txt, email:txt, contraseña:txt --> postRegistro() --> retrofit
    //-------------------------------------------------------------------------------------------
    public static void PostRegistro(String Nombre, String Apellidos, String Email, String Contrasenya){


        ApiService api = ApiCliente.getApiService();
        Call<Void> call = api.datosRegistro(Nombre,Apellidos,Email,Contrasenya);

        //Ejecutamos el post de la llamada de forma asincrona, si se establece la conexión
        //en el log cat nos aparecera mensaje que dependiendo si el servidor ha respondido
        //nos dice si si o si no, y si dice que no se establecio la conexion avisa que no.
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "✅ Registro exitoso: código " + response.code());
                } else {
                    Log.e("API", "⚠️ Error del servidor: código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API", "❌ Error de conexión: " + t.getMessage());
            }
        });
    }
}
