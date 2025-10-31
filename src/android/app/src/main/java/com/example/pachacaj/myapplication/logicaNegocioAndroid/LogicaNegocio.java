package com.example.pachacaj.myapplication.logicaNegocioAndroid;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.pachacaj.myapplication.activitys.BTLEActivity;
import com.example.pachacaj.myapplication.configuracionApi.ApiCliente;
import com.example.pachacaj.myapplication.configuracionApi.ApiService;

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
    //     Nombre:txt, Apellidos:txt, email:txt, contraseña:txt --> postRegistro()
    //-------------------------------------------------------------------------------------------
    public static void PostRegistro(String Nombre, String Apellidos, String Email, String Contrasenya, Context contexto){

        ApiService api = ApiCliente.getApiService();
        Call<PojoRespuestaServidor> call = api.datosRegistro(Nombre,Apellidos,Email,Contrasenya);

        //Ejecutamos la llamada post de forma asincrona, con un callback.Lo primero que hacemos es cojer la respuesta
        //del servido, al recibirlo comparamos si ha fallado algo y si la respuesta en si tiene cuerpo. Si no se cumple
        //ninguna de estás dos cóndiciones significa que algo a ocurriod en la conexión. Si por el contrario es favorable
        //la respuesta lo metemos en una clase pojo para poder usarlo de forma facil. Si la respuesta es aceptada, en este
        //caso significa que la cuenta de la persona no está creado teniendo en cuenta su email y si no es así es lo contrario
        call.enqueue(new Callback<PojoRespuestaServidor>() {
            @Override
            public void onResponse(Call<PojoRespuestaServidor> call, Response<PojoRespuestaServidor> response) {

                if (response.isSuccessful() && response.body() != null) {
                    PojoRespuestaServidor respuesta = response.body();

                    if ("ok".equals(respuesta.getStatus())) {
                        Log.d("API", "funciona: " + respuesta.getMensaje());
                        Intent intent = new Intent(contexto, BTLEActivity.class);
                        contexto.startActivity(intent);

                    } else {
                        Log.w("API", "No funciona: " + respuesta.getMensaje());
                        Toast.makeText(contexto,"Mail ya registrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API", "Error HTTP: código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PojoRespuestaServidor> call, Throwable t) {
                Log.e("API", "Error de conexión: " + t.getMessage());
            }
        });

    }
}
