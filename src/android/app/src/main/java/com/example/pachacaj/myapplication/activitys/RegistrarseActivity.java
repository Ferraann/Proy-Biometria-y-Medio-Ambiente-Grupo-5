package com.example.pachacaj.myapplication.activitys;

import static com.example.pachacaj.myapplication.logicaNegocioAndroid.LogicaNegocio.PostRegistro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pachacaj.myapplication.R;
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
    EditText Usuario,Apellidos,Email,Contrasenya;

    //Metodo onCreate donde se ejecuta lo principal
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse);

        //Obtengo los edit text por su id que yo le he piesto
        Usuario = findViewById(R.id.NombreUsuario);
        Apellidos = findViewById(R.id.ApellidosUsuario);
        Email = findViewById(R.id.EmailUsuario);
        Contrasenya = findViewById(R.id.ContrasenyaUsuario);
    }

    //Boton para enviar los datos al servidor
    public void botonEnviarDatos(View v){
        Log.d("MainActivity", "Mensaje de depuración");
        Log.d("Usuario", Usuario.getText().toString());
        Log.d("apellido ", Apellidos.getText().toString());
        Log.d("email", Email.getText().toString());
        Log.d("contrasenya", Contrasenya.getText().toString());

        // validación que comprueba que si hay alguno que está vacio no se ejecuta la sentencia sql.
        if (Usuario.getText().toString().isEmpty() || Apellidos.getText().toString().isEmpty() || Email.getText().toString().isEmpty() || Contrasenya.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Con una biblioteca podemos comprobar la estructura basica de un email
        //si está detecta que no es semejante a un email, entonces devuelve false.
        //si es verdad entonces un true.
        if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText()).matches()){
            Toast.makeText(this, "Por favor, introduce un email valido", Toast.LENGTH_SHORT).show();
            return;
        }

        PostRegistro(Usuario.getText().toString(),Apellidos.getText().toString(),Email.getText().toString(),Contrasenya.getText().toString(),this);


    }
}
