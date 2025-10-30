<?php
// ------------------------------------------------------------------
// Fichero: index.php
// Autor: Pablo
// Coautor: Manuel
// Fecha: 30/10/2025
// ------------------------------------------------------------------
// Descripción:
//  Este archivo actúa como punto de entrada principal de la API.
//  Según el método HTTP (GET o POST) y los parámetros enviados,
//  decide qué función de logicaNegocio ejecutar.
// ------------------------------------------------------------------

header('Content-Type: application/json');

// Incluimos los archivos de conexión y lógica de negocio
require_once('conexion.php');
require_once('logicaNegocio.php');

// Abrimos conexión a la base de datos
$conn = abrirServidor();

// Detectamos el método HTTP (GET, POST, etc.)
$method = $_SERVER['REQUEST_METHOD'];

// Procesamos la petición según el método
switch ($method) {

    // ---------------------------------------------------------
    // MÉTODO POST → Registro, login o guardar mediciones
    // ---------------------------------------------------------
    case "POST":
        // Leemos el cuerpo del request (puede venir en JSON o form-data)
        $input = json_decode(file_get_contents("php://input"), true);
        if (!$input) $input = $_POST;

        // Comprobamos si la acción viene definida
        $accion = $input['accion'] ?? null;

        switch ($accion) {
            case "registrarUsuario":
                echo json_encode(registrarUsuario($conn, $input));
                break;

            case "login":
                echo json_encode(loginUsuario($conn, $input['gmail'], $input['password']));
                break;

            case "guardarMedicion":
                echo json_encode(guardarMedicion($conn, $input));
                break;

            default:
                echo json_encode(["status" => "error", "message" => "Acción POST no reconocida."]);
                break;
        }
        break;


    // ---------------------------------------------------------
    // MÉTODO GET → Consultar datos (por ejemplo, mediciones)
    // ---------------------------------------------------------
    case "GET":
        // Se puede usar un parámetro 'accion' en la URL (ej: ?accion=getMediciones)
        $accion = $_GET['accion'] ?? null;

        switch ($accion) {
            case "getMediciones":
                echo json_encode(obtenerMediciones($conn));
                break;

            default:
                echo json_encode(["status" => "error", "message" => "Acción GET no reconocida."]);
                break;
        }
        break;

    // ---------------------------------------------------------
    // OTROS MÉTODOS (PUT, DELETE, etc.)
    // ---------------------------------------------------------
    default:
        echo json_encode(["status" => "error", "message" => "Método HTTP no soportado."]);
        break;
}

// Cerramos la conexión
$conn->close();
?>
