/*package com.example.pachacaj.myapplication.activitys;


import static com.example.pachacaj.myapplication.LogicaNegocio.PostLogin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pachacaj.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    EditText Email, Contrasenya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Email = findViewById(R.id.EmailUsuarioLogin);
        Contrasenya = findViewById(R.id.ContrasenyaUsuarioLogin);
    }

    public void botonLogin(View v) {
        String email = Email.getText().toString();
        String pass = Contrasenya.getText().toString();

        Log.d("LoginActivity", "Intentando login con:");
        Log.d("Email", email);
        Log.d("Contraseña", pass);

        PostLogin(email, pass);
    }
}*/
