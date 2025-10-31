package com.example.pachacaj.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pachacaj.myapplication.activitys.RegistrarseActivity;

// ------------------------------------------------------------------
// Fichero: MainActivity.java
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Descripción:
//  Clase que gestionara las demás clases y ejecutara las diferentes
//  funcionalidades
// ------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonRegistrarse( View v ) {
        Intent intent = new Intent(MainActivity.this, RegistrarseActivity.class);
        startActivity(intent);

    } // ()

} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------