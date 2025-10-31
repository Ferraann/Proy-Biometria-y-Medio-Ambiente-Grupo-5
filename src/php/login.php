<?php
session_start();
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "aither";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    echo json_encode(['success' => false, 'message' => 'Conexión fallida a la base de datos']);
    exit;
}

$email = $_POST['gmail'] ?? '';
$pass = $_POST['password'] ?? '';

if ($email && $pass) {
    $stmt = $conn->prepare("SELECT id, nombre, apellidos, password FROM usuario WHERE gmail = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($id, $nombre, $apellidos, $hash);
        $stmt->fetch();

        // Comparación de texto plano (puedes cambiar a password_verify si quieres seguridad)
        if ($pass === $hash) {
            $_SESSION['user_id'] = $id;
            echo json_encode([
                'success' => true,
                'user' => [
                    'id' => $id,
                    'nombre' => $nombre,
                    'apellidos' => $apellidos,
                    'gmail' => $email
                ]
            ]);
        } else {
            echo json_encode(['success' => false, 'message' => 'Contraseña incorrecta']);
        }
    } else {
        echo json_encode(['success' => false, 'message' => 'Usuario no encontrado']);
    }

    $stmt->close();
} else {
    echo json_encode(['success' => false, 'message' => 'Rellena todos los campos']);
}

$conn->close();
?>
