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
require_once __DIR__ . '/../logicaNegocio/index2.php';


// Abrimos conexión a la base de datos
$conn = abrirServidor();

// Capturar body crudo json
$rawBody = '';

// Detectamos el método HTTP (GET, POST, etc.)
$method = $_SERVER['REQUEST_METHOD'];

if ($method === 'POST' || $method === 'PUT') {
    // Leemos el contenido crudo del body
    $rawBody = file_get_contents("php://input");

    // Mostramos en consola (solo visible en terminal o log)
    error_log("BODY CRUDO recibido: " . $rawBody);

    // Intentamos decodificar el JSON recibido
    $input = json_decode($rawBody, true);

    // Si no es un JSON válido, respondemos con error
    if (json_last_error() !== JSON_ERROR_NONE) {
        error_log("JSON inválido: " . json_last_error_msg());
        http_response_code(400);
        echo json_encode(["status" => "error", "message" => "JSON inválido"]);
        exit;
    }

    // Guardar copia del JSON recibido (para debug)
    file_put_contents("logs/last_request.json", $rawBody);

} else {
    // Para GET y otros métodos, usamos los parámetros normales
    $input = $_GET;
}


// Procesamos la petición según el método
switch ($method) {

    // ---------------------------------------------------------
    // MÉTODO POST → Registro, login o guardar mediciones
    // ---------------------------------------------------------
    case "POST":
        
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
