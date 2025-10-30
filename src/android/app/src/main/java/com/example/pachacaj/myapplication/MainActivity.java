package com.example.pachacaj.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// ------------------------------------------------------------------
// Fichero: MainActivity.java
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Descripción:
//  Clase principal que gestiona el login y redirige a HomeActivity
// ------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {

    private EditText emailET, passET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.editTextTextEmailAddress2);
        passET = findViewById(R.id.editTextTextPassword);
    }

    // --------------------------------------------------------------
    // Navegar a la pantalla de registro
    // --------------------------------------------------------------
    public void botonRegistrarse(View v) {
        Intent intent = new Intent(MainActivity.this, RegistrarseActivity.class);
        startActivity(intent);
    }

    // --------------------------------------------------------------
    // Intento de login
    // --------------------------------------------------------------
    public void botonLogin(View v) {
        String email = emailET.getText().toString().trim();
        String pass = passET.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiCliente.getApiService();
        Call<Void> call = apiService.loginUsuario(email, pass);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();

                    // ✅ Enviar el email a la siguiente pantalla
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("email_usuario", email);
                    startActivity(intent);
                    finish(); // Evita volver al login al presionar "atrás"
                } else if (response.code() == 401) {
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
