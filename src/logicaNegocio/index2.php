<?php
// ------------------------------------------------------------------
// Fichero: index.php
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Ficher: LogicaNegocio.php
//  En este archivo .php lo que pretendemos es poner toda la logica
// del negocio con sus diversas funciones.
// ------------------------------------------------------------------

function registrarUsuari($Nombre,$Apellidos,$Email,$Contrasenya,$bbdd){

    //Sentencia sql para introducir datos, en este caso los del usuario.
    $query = "INSERT INTO usuario(nombre, apellidos, gmail, password) VALUES ('$Nombre ','$Apellidos','$Email','$Contrasenya')";

    //Confirmo que la sentencia sql funciona y devuelvo una respuesta dependieno si si o si no
    if ($bbdd->query($query)) {
        return true;
    } else {
        return false;
    }
}
?>