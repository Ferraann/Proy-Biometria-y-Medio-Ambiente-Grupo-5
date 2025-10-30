package com.example.pachacaj.myapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
    Call<Void> datosRegistro(
        @Field("Nombre") String nombre,
        @Field("Apellidos") String apellidos,
        @Field("Email") String email,
        @Field("Contrasenya") String contrasenya
    );

}
