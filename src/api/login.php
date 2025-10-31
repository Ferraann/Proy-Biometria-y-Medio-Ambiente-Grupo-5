<?php
/**
 * @file login.php
 * @brief Endpoint para autenticar usuarios mediante correo y contraseña.
 * @details Este archivo recibe peticiones POST, valida credenciales y devuelve
 *          un objeto JSON con el resultado del login.
 * @author Sergi Puig Biosca
 * @date 2025-10-29
 * @copyright Todos los derechos reservados
 */

session_start();
header('Content-Type: application/json');
require_once __DIR__ . '/conexion.php'; // Usa la conexión compartida

// Obtener datos del formulario
$email = $_POST['gmail'] ?? '';
$pass = $_POST['password'] ?? '';

// Validar campos vacíos
if (empty($email) || empty($pass)) {
    echo json_encode(['success' => false, 'message' => 'Rellena todos los campos']);
    exit;
}

// Preparar la consulta
$stmt = $conn->prepare("SELECT id, nombre, apellidos, password FROM usuario WHERE gmail = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$stmt->store_result();

// Verificar si existe el usuario
if ($stmt->num_rows > 0) {
    $stmt->bind_result($id, $nombre, $apellidos, $hash);
    $stmt->fetch();

    // Comparación simple (puedes cambiar a password_verify si las contraseñas están encriptadas)
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
$conn->close();
?>
