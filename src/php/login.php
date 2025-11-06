<?php
/* 
===============================================================================
NOMBRE: login.php
DESCRIPCIÓN: Script del lado del servidor para autenticar usuarios en la 
             plataforma AITHER. Recibe los datos del formulario mediante POST, 
             valida las credenciales en la base de datos y devuelve una 
             respuesta en formato JSON.
COPYRIGHT: © 2025 AITHER. Todos los derechos reservados.
FECHA: 03/11/2025
AUTOR: [Tu nombre aquí]
APORTACIÓN: Implementación de la lógica de autenticación con conexión a base 
            de datos MySQL, manejo de sesiones y respuesta estructurada al cliente.
===============================================================================
*/

// -----------------------------------------------------------------------------
// BLOQUE: Inicio de sesión y configuración de cabecera
// DISEÑO LÓGICO: Establece el manejo de sesión y define el tipo de contenido
// que se devolverá al cliente.
// DESCRIPCIÓN: Inicia la sesión PHP y especifica que la respuesta será JSON.
session_start();
header('Content-Type: application/json');

// -----------------------------------------------------------------------------
// BLOQUE: Parámetros de conexión a la base de datos
// DISEÑO LÓGICO: Define las credenciales necesarias para conectar con MySQL.
// DESCRIPCIÓN: Configura el acceso al servidor de base de datos y la base 'aither'.
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "aither";

// -----------------------------------------------------------------------------
// BLOQUE: Establecimiento de conexión con la base de datos
// DISEÑO LÓGICO: Crea un objeto mysqli y comprueba la conexión.
// DESCRIPCIÓN: Si ocurre un error, se devuelve una respuesta JSON de fallo.
$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    echo json_encode(['success' => false, 'message' => 'Conexión fallida a la base de datos']);
    exit;
}

// -----------------------------------------------------------------------------
// BLOQUE: Captura de datos del formulario
// DISEÑO LÓGICO: Obtiene los valores enviados por el método POST.
// DESCRIPCIÓN: Recupera el correo electrónico y la contraseña enviados 
// desde el formulario de inicio de sesión.
$email = $_POST['gmail'] ?? '';
$pass = $_POST['password'] ?? '';

// -----------------------------------------------------------------------------
// BLOQUE: Validación de entrada
// DISEÑO LÓGICO: Comprueba que los campos requeridos no estén vacíos.
// DESCRIPCIÓN: Si los datos están presentes, continúa con la validación 
// en la base de datos; de lo contrario, devuelve un error.
if ($email && $pass) {

    // -------------------------------------------------------------------------
    // BLOQUE: Consulta SQL preparada
    // DISEÑO LÓGICO: Utiliza una sentencia preparada para prevenir inyección SQL.
    // DESCRIPCIÓN: Busca el usuario correspondiente al correo electrónico introducido.
    $stmt = $conn->prepare("SELECT id, nombre, apellidos, password FROM usuario WHERE gmail = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->store_result();

    // -------------------------------------------------------------------------
    // BLOQUE: Verificación de existencia de usuario
    // DISEÑO LÓGICO: Comprueba si el correo existe en la base de datos.
    // DESCRIPCIÓN: Si existe, obtiene los datos del usuario y compara la contraseña.
    if ($stmt->num_rows > 0) {
        $stmt->bind_result($id, $nombre, $apellidos, $hash);
        $stmt->fetch();

        // ---------------------------------------------------------------------
        // BLOQUE: Comparación de contraseñas
        // DISEÑO LÓGICO: Compara la contraseña ingresada con la almacenada.
        // DESCRIPCIÓN: Actualmente realiza comparación directa en texto plano. 
        // Puede sustituirse por password_verify() para mayor seguridad.
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
    // -------------------------------------------------------------------------
    // BLOQUE: Campos vacíos
    // DISEÑO LÓGICO: Envía una respuesta de error si los campos no fueron completados.
    // DESCRIPCIÓN: Garantiza que ambos valores (email y contraseña) sean requeridos.
    echo json_encode(['success' => false, 'message' => 'Rellena todos los campos']);
}

// -----------------------------------------------------------------------------
// BLOQUE: Cierre de conexión
// DISEÑO LÓGICO: Finaliza la conexión activa a la base de datos.
// DESCRIPCIÓN: Libera los recursos asociados con la conexión MySQL.
$conn->close();
?>
