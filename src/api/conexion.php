<?php
/**
 * @file conexion.php
 * @brief Establece la conexión con la base de datos MySQL.
 * @author Sergi Puig Biosca
 * @date 2025-10-29
 * @copyright Todos los derechos reservados
 */

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
        'message' => 'Error de conexión a la base de datos: ' . $conn->connect_error
    ]));
}
?>
