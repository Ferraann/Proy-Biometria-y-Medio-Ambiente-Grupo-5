package com.example.grupo5.myapplication;
import com.example.grupo5.myapplication.PojoRespuestaServidor;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
// ------------------------------------------------------------------
// Fichero: ApiCliente
// Autor: Pablo Chasi
// Fecha: 24/10/2025
// ------------------------------------------------------------------
// Interfaz ApiService
//
// Descripción:
//  Interfaz donde se declara las peticiones HTTP y las variables que
//  se usaran y enviaran.
// ------------------------------------------------------------------
public interface ApiService {
    //Metodo post para hacer el login
    //lo que se pretende es enviar y no
    //recibir al tener void.

    @POST("index.php")
    Call<PojoRespuestaServidor> datosRegistro(
            @Body PojoUsuario usuario
    );


    @POST ("index.php")
    Call<Void> enviarDatos(
            @Field("CO2") float co2,
            @Field("Temperatura") float temperatura
    );


    @POST("index.php")
    Call<PojoRespuestaServidor> loginUsuario(@Body PojoUsuario usuario);


    @POST("index.php") // Cambia al endpoint real en tu servidor
    Call<PojoRespuestaServidor> modificarDatos(@Body PojoUsuario usuario);

    @POST("index.php")
    Call<PojoRespuestaServidor> vincularSensor(@Body PojoSensor sensor);

    @POST("index.php")
    Call<JsonObject> crearSensorYRelacion(
            @Query("accion") String accion,
            @Body JsonObject body);
}
