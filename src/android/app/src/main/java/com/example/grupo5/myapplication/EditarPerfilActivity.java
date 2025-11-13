package com.example.grupo5.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

// ------------------------------------------------------------------
// Fichero: MainActivity.java
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Descripción:
// Clase que se hara la función de la página  editar perfil
// su función principal es que mediante un boton puedas seleccionar
// uno de los datos que quieres modificar, cuando edita es información
// y quiere guardalor lo que se hara es modificar las base de datos
// cambiando aquellos que hemos cambiado.
// ------------------------------------------------------------------
public class EditarPerfilActivity extends AppCompatActivity {

    EditText nombre, apellidos, contrasenyaAntigua,contrasenyaNueva,repetirContrasenyaNueva,correoNuevo,repetirCorreoNuevo;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //Obtengo los edit text.
        nombre = findViewById(R.id.editarNombre);
        apellidos=findViewById(R.id.editarApellidos);
        contrasenyaAntigua = findViewById(R.id.editarContrasenyaActual);
        contrasenyaNueva = findViewById(R.id.editarContrasenyaNueva);
        repetirContrasenyaNueva = findViewById(R.id.editarRepetirContrasenyaNueva);
        correoNuevo = findViewById(R.id.editarCorreoNuevo);
        repetirCorreoNuevo = findViewById(R.id.editarRepetirCorreoNuevo);

    }

    public void botonActivarModificarNombre(View v){
        nombre.isEnabled();
    }

    public void botonActivarModificarApellidos(View v){
        apellidos.isEnabled();
    }

    public void botonActivarModificarContrasenya(View v){
        contrasenyaAntigua.isEnabled();
        contrasenyaNueva.isEnabled();
        repetirContrasenyaNueva.isEnabled();
    }

    public void botonActivarModificarCorreo(View v){
        correoNuevo.isEnabled();
        repetirCorreoNuevo.isEnabled();
    }

    public void botonGuardarModificaciones(View v){

    }
}
