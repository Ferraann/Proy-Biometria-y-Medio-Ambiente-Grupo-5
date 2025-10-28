package com.example.pachacaj.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;
// ------------------------------------------------------------------
// Fichero: MainActivity.java
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Descripción:
//  Clase donde a traves del formulario mostrado en el layout,
//  se enviara los datos a traves de Retrofit al servidor web.
//  Donde se guardara y se usara posteriormente.
// ------------------------------------------------------------------

public class RegistrarseActivity extends AppCompatActivity {

    //Formulario del layout donde se trabaja
    TextView Usuario = findViewById(R.id.NombreUsuario);
    TextView Apellidso = findViewById(R.id.ApellidosUsuario);
    TextView Email = findViewById(R.id.EmailUsuario);
    TextView Contrasenya = findViewById(R.id.ContrasenyaUsuario);

    //Metodo onCreate donde se ejecuta lo principal
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse);
    }

    //Boton para enviar los datos al servidor
    public void botonEnviarDatos(View v){
        Log.d("MainActivity", "Mensaje de depuración");
    }
}
