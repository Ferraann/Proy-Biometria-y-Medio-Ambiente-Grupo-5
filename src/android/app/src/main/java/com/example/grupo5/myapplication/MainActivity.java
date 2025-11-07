package com.example.grupo5.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import static com.example.grupo5.myapplication.LogicaNegocio.PostLogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo5.myapplication.RegistroActivity;

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

    EditText Email, Contrasenya;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Email = findViewById(R.id.EmailUsuarioLogin);
        Contrasenya = findViewById(R.id.ContrasenyaUsuarioLogin);
    }


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonRegistrarse( View v ) {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);

    } // ()

    // --------------------------------------------------------------
    // Intento de login
    // --------------------------------------------------------------
    public void botonLogin(View v) {
        String email = Email.getText().toString();
        String pass = Contrasenya.getText().toString();

        //Con una biblioteca podemos comprobar la estructura basica de un email
        //si está detecta que no es semejante a un email, entonces devuelve false.
        //si es verdad entonces un true.
        if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText()).matches()){
            Toast.makeText(this, "Por favor, introduce un email valido", Toast.LENGTH_SHORT).show();
            return;
        }

        PostLogin(email, pass, this);
    }
} // class

// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
