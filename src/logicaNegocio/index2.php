<?php
// ------------------------------------------------------------------
// Fichero: logicaNegocio.php
// Autor: Pablo
// Coautor: Manuel
// Fecha: 30/10/2025
// ------------------------------------------------------------------
// Descripción:
//  Aquí se definen todas las funciones lógicas que maneja la API.
//  Cada función se encarga de interactuar con la base de datos y
//  devolver los resultados al archivo index.php.
// ------------------------------------------------------------------

// -------------------------------------------------------------
// FUNCIÓN 1: Registrar un nuevo usuario
// -------------------------------------------------------------
function registrarUsuario($conn, $data) {
    // Preparamos la sentencia SQL segura (para evitar inyecciones)
    $sql = "INSERT INTO usuario (nombre, apellidos, gmail, password, credencial_id)
            VALUES (?, ?, ?, ?, ?)";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssssi", 
        $data['nombre'], 
        $data['apellidos'], 
        $data['gmail'], 
        $data['password'], 
        $data['credencial_id']
    );

    // Ejecutamos y devolvemos el resultado
    if ($stmt->execute()) {
        return ["status" => "ok", "message" => "Usuario registrado correctamente."];
    } else {
        return ["status" => "error", "message" => "No se pudo registrar el usuario: " . $conn->error];
    }
}


// -------------------------------------------------------------
// FUNCIÓN 2: Iniciar sesión (login)
// -------------------------------------------------------------
function loginUsuario($conn, $gmail, $password) {
    $sql = "SELECT id, nombre, apellidos, gmail, password, credencial_id 
            FROM usuario WHERE gmail = ?";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $gmail);
    $stmt->execute();
    $result = $stmt->get_result();

    // Si existe el usuario, comprobamos la contraseña
    if ($row = $result->fetch_assoc()) {
        // En producción, deberías usar password_hash() y password_verify()
        if ($row['password'] === $password) {
            return ["status" => "ok", "usuario" => $row];
        } else {
            return ["status" => "error", "message" => "Contraseña incorrecta."];
        }
    }

    return ["status" => "error", "message" => "Usuario no encontrado."];
}


// -------------------------------------------------------------
// FUNCIÓN 3: Obtener todas las mediciones
// -------------------------------------------------------------
function obtenerMediciones($conn) {
    $sql = "SELECT m.id, tm.medida, tm.unidad, m.valor, m.hora, m.localizacion, s.mac
            FROM medicion m
            INNER JOIN tipo_medicion tm ON m.tipo_medicion_id = tm.id
            INNER JOIN sensor s ON m.sensor_id = s.id
            ORDER BY m.hora DESC";

    $result = $conn->query($sql);
    $datos = [];

    if ($result && $result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $datos[] = $row;
        }
    }

    return $datos;
}


// -------------------------------------------------------------
// FUNCIÓN 4: Guardar una nueva medición
// -------------------------------------------------------------
function guardarMedicion($conn, $data) {
    $sql = "INSERT INTO medicion (tipo_medicion_id, valor, sensor_id, localizacion)
            VALUES (?, ?, ?, ?)";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("idis",
        $data['tipo_medicion_id'],
        $data['valor'],
        $data['sensor_id'],
        $data['localizacion']
    );

    if ($stmt->execute()) {
        return ["status" => "ok", "message" => "Medición guardada correctamente."];
    } else {
        return ["status" => "error", "message" => "Error al guardar medición: " . $conn->error];
    }
}
?>
