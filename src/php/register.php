<?php
header('Content-Type: application/json');

// Incluir conexión
require_once "conexion.php";

$nombre = $_POST['nombre'] ?? '';
$apellidos = $_POST['apellidos'] ?? '';
$correo = $_POST['correo'] ?? '';
$pass = $_POST['password'] ?? '';

if (!$nombre || !$apellidos || !$correo || !$pass) {
    echo json_encode(['success' => false, 'message' => 'Por favor, completa todos los campos.']);
    exit;
}

// Verificar si el usuario ya existe
$check = $conn->prepare("SELECT id FROM usuario WHERE gmail = ?");
$check->bind_param("s", $correo);
$check->execute();
$check->store_result();

if ($check->num_rows > 0) {
    echo json_encode(['success' => false, 'message' => 'El correo ya está registrado.']);
    $check->close();
    $conn->close();
    exit;
}
$check->close();

// ⚠️ Contraseña en texto plano (igual que tu login actual)
$hash = $pass;

// Insertar nuevo usuario
$stmt = $conn->prepare("INSERT INTO usuario (nombre, apellidos, gmail, password) VALUES (?, ?, ?, ?)");
$stmt->bind_param("ssss", $nombre, $apellidos, $correo, $hash);

if ($stmt->execute()) {
    echo json_encode(['success' => true, 'message' => 'Registro exitoso.']);
} else {
    echo json_encode(['success' => false, 'message' => 'Error al registrar el usuario.']);
}

$stmt->close();
$conn->close();
?>
