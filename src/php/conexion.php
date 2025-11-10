<?php
// ============================
// AITHER - conexion.php
// Archivo de conexión a la base de datos
// ============================

// Datos de conexión
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "aither";

// Crear conexión
$conn = new mysqli($servername, $username, $password, $dbname);

// Verificar conexión
if ($conn->connect_error) {
    die(json_encode([
        'success' => false,
        'message' => 'Error de conexión con la base de datos: ' . $conn->connect_error
    ]));
}

// Establecer codificación UTF-8
$conn->set_charset("utf8");
?>
