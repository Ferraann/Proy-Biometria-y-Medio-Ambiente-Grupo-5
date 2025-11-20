    package com.example.grupo5.myapplication;

    import static com.example.grupo5.myapplication.LogicaNegocio.putModificarDatos;

    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
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
        Button botonModificarNombre,botonModificarApellido,botonModificarCorreo,botonModificarContrasenya,botonGuardarDatos;
        boolean modificaNombre,modificaApellido,modificaCorreo,modificaContrasenya;
        PojoUsuario usuario = new PojoUsuario();

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

            botonModificarNombre = findViewById(R.id.modificarNombre);
            botonModificarApellido = findViewById(R.id.modificarApellidos);
            botonModificarCorreo = findViewById(R.id.modificarCorreo);
            botonModificarContrasenya = findViewById(R.id.modificarContrasenyaAntigua);
            botonGuardarDatos = findViewById(R.id.guardarDatos);

            modificaNombre = false;
            modificaContrasenya = false;
            modificaCorreo = false;
            modificaApellido = false;
            //Recuperar datos del usuario desde SharedPreferences
            SharedPreferences prefs = getSharedPreferences("SesionUsuario", MODE_PRIVATE);
            String nombreUsuario = prefs.getString("nombre", "");
            String apellidosUsuario = prefs.getString("apellidos", "");

            // Mostrar los datos en los EditText
            nombre.setText(nombreUsuario);
            apellidos.setText(apellidosUsuario);
        }

        public void botonActivarModificarNombre(View v){

            nombre.setEnabled(!nombre.isEnabled());

            botonModificarContrasenya.setEnabled(!botonModificarContrasenya.isEnabled());
            botonModificarApellido.setEnabled(!botonModificarApellido.isEnabled());
            botonModificarCorreo.setEnabled(!botonModificarCorreo.isEnabled());
            botonGuardarDatos.setEnabled(!botonGuardarDatos.isEnabled());


        }

        public void botonActivarModificarApellidos(View v){

            apellidos.setEnabled(!apellidos.isEnabled());
        }

        public void botonActivarModificarContrasenya(View v){
            contrasenyaAntigua.setEnabled(!contrasenyaAntigua.isEnabled());
            contrasenyaNueva.setEnabled(!contrasenyaNueva.isEnabled());
            repetirContrasenyaNueva.setEnabled(!repetirContrasenyaNueva.isEnabled());
        }

        public void botonActivarModificarCorreo(View v){
            correoNuevo.setEnabled(!correoNuevo.isEnabled());
            repetirCorreoNuevo.setEnabled(!repetirCorreoNuevo.isEnabled());
        }

        public void botonGuardarModificaciones(View v){
            if(modificaNombre){
                usuario.setNombre(nombre.getText().toString());
                putModificarDatos(usuario);
            }else if(modificaApellido){

            }else if(modificaCorreo){

            }else if(modificaContrasenya){

            }
        }
    }
