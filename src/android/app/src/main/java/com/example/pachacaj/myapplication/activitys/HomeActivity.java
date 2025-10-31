package com.example.pachacaj.myapplication.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pachacaj.myapplication.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView bienvenida = findViewById(android.R.id.text1); // o usa el id real del TextView si lo cambias

        String emailUsuario = getIntent().getStringExtra("email_usuario");
        if (emailUsuario != null) {
            bienvenida.setText("¡Bienvenido, " + emailUsuario + "!");
        }
    }
}
