package com.example.grupo5.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo5.androidapp.R;

import static com.example.grupo5.myapplication.LogicaNegocio.PostLogin;

public class MainActivity extends AppCompatActivity {

    EditText Email, Contrasenya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Email = findViewById(R.id.EmailUsuarioLogin);
        Contrasenya = findViewById(R.id.ContrasenyaUsuarioLogin);
    }

    public void botonRegistrarse(View v) {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    public void botonLogin(View v) {
        String email = Email.getText().toString();
        String pass = Contrasenya.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText()).matches()) {
            Toast.makeText(this, "Por favor, introduce un email valido", Toast.LENGTH_SHORT).show();
            return;
        }
        
        PostLogin(email, pass, this);
    }
}
