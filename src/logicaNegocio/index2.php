<?php
// ------------------------------------------------------------------
// Fichero: index2.php
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Descripción:
//  Lógica de negocio para el registro de usuarios. Se encarga de
//  validar duplicados, encriptar la contraseña y devolver respuestas
//  estructuradas en formato JSON.
// ------------------------------------------------------------------

function registrarUsuario($Nombre, $Apellidos, $Email, $Contrasenya, $bbdd) {

    $sql_check = "SELECT * FROM usuario WHERE gmail = ?";
    $stmt = $bbdd->prepare($sql_check);
    $stmt->bind_param("s", $Email);
    $stmt->execute();
    $resultado = $stmt->get_result();

    if ($resultado->num_rows > 0) {
        return [
            "status" => "error",
            "message" => "El correo electrónico ya está registrado."
        ];
    }


    $sql_insert = "INSERT INTO usuario (nombre, apellidos, gmail, password) VALUES (?, ?, ?, ?)";
    $stmt_insert = $bbdd->prepare($sql_insert);
    $stmt_insert->bind_param("ssss", $Nombre, $Apellidos, $Email, $Contrasenya);

    if ($stmt_insert->execute()) {
        return [
            "status" => "ok",
            "message" => "Usuario registrado correctamente."
        ];
    } else {
        return [
            "status" => "error",
            "message" => "Error al registrar el usuario: " . $stmt_insert->error
        ];
    }
}
?>