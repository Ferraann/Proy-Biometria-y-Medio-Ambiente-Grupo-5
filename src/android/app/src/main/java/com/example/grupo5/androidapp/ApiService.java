package com.example.grupo5.androidapp;
import com.example.grupo5.androidapp.PojoRespuestaServidor;

import retrofit2.Call;
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
    @FormUrlEncoded
    @POST("index.php")
    Call<PojoRespuestaServidor> datosRegistro(
            @Field("Nombre") String nombre,
            @Field("Apellidos") String apellidos,
            @Field("Email") String email,
            @Field("Contrasenya") String contrasenya
    );

    @FormUrlEncoded
    @POST ("postGuardarMediciones.php")
    Call<Void> enviarDatos(
            @Field("CO2") float co2,
            @Field("Temperatura") float temperatura
    );

    @FormUrlEncoded
    @POST("login.php") // Cambia al endpoint real en tu servidor
    Call<Void> loginUsuario(
            @Field("email") String email,
            @Field("contrasenya") String contrasenya
    );
}
