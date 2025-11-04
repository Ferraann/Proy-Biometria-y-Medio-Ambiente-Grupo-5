/* 
===============================================================================
NOMBRE: login.js
DESCRIPCIÓN: Script de validación y envío del formulario de inicio de sesión 
             de la plataforma AITHER. Controla la verificación de campos, 
             conexión con el servidor PHP y redirección a la página principal.
COPYRIGHT: © 2025 AITHER. Todos los derechos reservados.
FECHA: 03/11/2025
AUTOR: [Tu nombre aquí]
APORTACIÓN: Implementación del manejo de eventos del formulario de login, 
            validación de datos y comunicación asincrónica con el servidor.
===============================================================================
*/

// -----------------------------------------------------------------------------
// DECLARACIÓN DE VARIABLES
// DISEÑO LÓGICO: Referencias a los elementos del formulario y mensaje.
// DESCRIPCIÓN: Se obtienen los elementos necesarios para gestionar la 
// interacción con el usuario.
const form = document.getElementById("loginForm");
const msg = document.getElementById("message");

// -----------------------------------------------------------------------------
// FUNCIÓN: Evento 'submit' del formulario
// DISEÑO LÓGICO: Escucha el envío del formulario, previene el envío por defecto,
// valida los datos, y realiza una petición asincrónica al servidor.
// DESCRIPCIÓN: Comprueba los campos de correo y contraseña, envía los datos 
// al servidor mediante 'fetch' y gestiona la respuesta para autenticar al usuario.
form.addEventListener("submit", async (e) => {
  e.preventDefault(); // Previene recarga de la página
  msg.textContent = ""; // Limpia mensajes previos

  // Captura y limpieza de valores introducidos
  const email = document.getElementById("gmail").value.trim();
  const password = document.getElementById("password").value.trim();

  // Validación básica de campos vacíos
  if (!email || !password) {
    msg.style.color = "#ffdddd";
    msg.textContent = "Por favor, rellena todos los campos.";
    return;
  }

  // Creación del objeto FormData con los valores del formulario
  const formData = new FormData();
  formData.append("gmail", email);
  formData.append("password", password);

  try {
    // -------------------------------------------------------------------------
    // BLOQUE: Petición asincrónica al servidor
    // DISEÑO LÓGICO: Envío de datos al script PHP de login mediante método POST.
    // DESCRIPCIÓN: Se comunica con el backend para validar las credenciales
    // y recibe una respuesta en formato JSON.
    const response = await fetch("../php/login.php", {
      method: "POST",
      body: formData
    });

    const data = await response.json(); // Conversión de respuesta a JSON

    // -------------------------------------------------------------------------
    // BLOQUE: Gestión de respuesta del servidor
    // DISEÑO LÓGICO: Analiza el resultado del login y actúa según el caso.
    // DESCRIPCIÓN: Si las credenciales son válidas, almacena los datos del 
    // usuario en localStorage y redirige a la página principal.
    if (data.success) {
      // Guardar usuario en localStorage
      localStorage.setItem("user", JSON.stringify(data.user));

      // Redirigir inmediatamente sin mostrar mensaje
      window.location.href = "dashboard.html";
    } else {
      msg.style.color = "#ffdddd";
      msg.textContent = data.message || "Usuario o contraseña incorrectos.";
    }
  } catch (error) {
    // -------------------------------------------------------------------------
    // BLOQUE: Manejo de errores
    // DISEÑO LÓGICO: Captura excepciones de la petición fetch.
    // DESCRIPCIÓN: Informa al usuario sobre problemas de conexión con el servidor.
    console.error(error);
    msg.style.color = "#ffdddd";
    msg.textContent = "Error de conexión con el servidor.";
  }
});
