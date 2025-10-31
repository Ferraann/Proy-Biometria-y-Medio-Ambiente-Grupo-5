<?php
// ------------------------------------------------------------------
// Fichero: index.php
// Autor: Pablo Chasi
// Fecha: 27/10/2025
// ------------------------------------------------------------------
// Descripción:
//  Punto de entrada principal de la API REST. 
//  Este router analiza la ruta y el método HTTP, 
//  y delega el procesamiento al controlador adecuado.
// ------------------------------------------------------------------

require_once ('../api/conexion.php');
require_once ('../logicaNegocio/index2.php');

$servirdor = abrirServidor();

$requestUri = $_SERVER['REQUEST_URI'];
$method = $_SERVER['REQUEST_METHOD'];

switch (true){
    case $method == "POST":
        $nombre = $_POST['Nombre'];
        $apellidos = $_POST['Apellidos'];
        $email = $_POST['Email'];
        $contrasenya = $_POST['Contrasenya'];
        registrarUsuari($nombre,$apellidos,$email,$contrasenya,$servirdor);

        break;
}

$servirdor -> close();
?>