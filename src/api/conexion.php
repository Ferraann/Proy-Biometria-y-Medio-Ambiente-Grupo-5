<?php
// ------------------------------------------------------------------
// Fichero: php
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// fichero php: Conexion
//
// Descripción:
//  Se definira la conexión de la base de datos con nuestra página
//  web
// ------------------------------------------------------------------


function abrirServidor(){
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "aither";


    $conn = mysqli_connect($servername, $username, $password, $dbname);
    
    return ($conn);
}
?>  