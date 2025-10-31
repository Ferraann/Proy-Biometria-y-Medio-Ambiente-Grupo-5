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
    // 1 Verificar parámetros obligatorios
    if (!isset($data['tipo_medicion_id'], $data['valor'], $data['sensor_id'], $data['localizacion'])) {
        return ["status" => "error", "message" => "Faltan parámetros obligatorios."];
    }

    $sensor_id = $data['sensor_id'];

    // 2 Comprobar si existe una relación activa usuario-sensor con ese sensor
    $sqlCheck = "SELECT COUNT(*) as count FROM usuario_sensor WHERE sensor_id = ? AND actual = 1";
    $stmtCheck = $conn->prepare($sqlCheck);
    $stmtCheck->bind_param("i", $sensor_id);
    $stmtCheck->execute();
    $result = $stmtCheck->get_result();
    $row = $result->fetch_assoc();
    $existeRelacion = $row['count'];
    $stmtCheck->close();

    // 3 Si no hay relación activa, no permitir guardar la medición
    if ($existeRelacion == 0) {
        return [
            "status" => "error",
            "message" => "No existe una relación activa entre el sensor y un usuario."
        ];
    }

    // 4 Insertar la medición si la relación es válida si existe la relacion usuario-sensor
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


// -------------------------------------------------------------
// FUNCIÓN 5: Crear un nuevo tipo de medición
// -------------------------------------------------------------
function crearTipoMedicion($conn, $data) {
    if (!isset($data['medida']) || !isset($data['unidad'])) {
        return ["status" => "error", "message" => "Faltan parámetros: medida y unidad son obligatorios."];
    }

    $sql = "INSERT INTO tipo_medicion (medida, unidad, txt) VALUES (?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sss", $data['medida'], $data['unidad'], $data['txt']);

    if ($stmt->execute()) {
        return ["status" => "ok", "message" => "Tipo de medición creado correctamente.", "id" => $conn->insert_id];
    } else {
        return ["status" => "error", "message" => "Error al crear tipo de medición: " . $conn->error];
    }
}

// -------------------------------------------------------------
// FUNCIÓN 6: Asignar un sensor a un usuario, creando el sensor si no existe o modificar la relación si ya existe
// -------------------------------------------------------------
function crearSensorYRelacion($conn, $data) {
    if (!isset($data['mac']) || !isset($data['usuario_id'])) {
        return ["status" => "error", "message" => "Faltan parámetros: mac y usuario_id son obligatorios."];
    }

    $mac = $data['mac'];
    $usuario_id = $data['usuario_id'];

    // 1. Comprobar si el sensor ya existe
    $sqlBuscarSensor = "SELECT id FROM sensor WHERE mac = ?";
    $stmtBuscar = $conn->prepare($sqlBuscarSensor);
    $stmtBuscar->bind_param("s", $mac);
    $stmtBuscar->execute();
    $result = $stmtBuscar->get_result();

    if ($row = $result->fetch_assoc()) {
        $sensor_id = $row['id'];
    } else {
        // 2. Si no existe, lo creamos
        $sqlInsertarSensor = "INSERT INTO sensor (mac, problema) VALUES (?, 0)";
        $stmtInsertar = $conn->prepare($sqlInsertarSensor);
        $stmtInsertar->bind_param("s", $mac);

        if ($stmtInsertar->execute()) {
            $sensor_id = $conn->insert_id;
        } else {
            return ["status" => "error", "message" => "Error al crear el sensor: " . $conn->error];
        }
    }

    // 3. Cerrar relaciones anteriores activas de ese sensor
    $sqlCerrarRelaciones = "UPDATE usuario_sensor 
                            SET actual = 0, fin_relacion = NOW() 
                            WHERE sensor_id = ? AND actual = 1";
    $stmtCerrar = $conn->prepare($sqlCerrarRelaciones);
    $stmtCerrar->bind_param("i", $sensor_id);
    $stmtCerrar->execute();

    // 4. Crear nueva relación usuario-sensor
    $sqlNuevaRelacion = "INSERT INTO usuario_sensor (usuario_id, sensor_id, actual, inicio_relacion)
                         VALUES (?, ?, 1, NOW())";
    $stmtRelacion = $conn->prepare($sqlNuevaRelacion);
    $stmtRelacion->bind_param("ii", $usuario_id, $sensor_id);

    if ($stmtRelacion->execute()) {
        return [
            "status" => "ok",
            "message" => "Sensor asignado correctamente.",
            "sensor_id" => $sensor_id,
            "id_relacion" => $conn->insert_id
        ];
    } else {
        return ["status" => "error", "message" => "Error al crear la relación usuario-sensor: " . $conn->error];
    }
}

// -------------------------------------------------------------
// FUNCIÓN 7: Terminar relación de un sensor y marcarlo con problema
// -------------------------------------------------------------
function marcarSensorConProblemas($conn, $data) {
    if (!isset($data['sensor_id'])) {
        return ["status" => "error", "message" => "Falta el parámetro sensor_id."];
    }

    $sensor_id = $data['sensor_id'];

    // 1. Finalizar relaciones activas del sensor
    $sqlFinalizar = "UPDATE usuario_sensor 
                     SET actual = 0, fin_relacion = NOW() 
                     WHERE sensor_id = ? AND actual = 1";
    $stmtFin = $conn->prepare($sqlFinalizar);
    $stmtFin->bind_param("i", $sensor_id);
    $stmtFin->execute();

    // 2. Marcar el sensor como con problema
    $sqlProblema = "UPDATE sensor SET problema = 1 WHERE id = ?";
    $stmtProb = $conn->prepare($sqlProblema);
    $stmtProb->bind_param("i", $sensor_id);

    if ($stmtProb->execute()) {
        return [
            "status" => "ok",
            "message" => "Sensor marcado con problema y relación finalizada.",
            "filas_relaciones_finalizadas" => $stmtFin->affected_rows
        ];
    } else {
        return ["status" => "error", "message" => "Error al actualizar sensor: " . $conn->error];
    }
}

?>
