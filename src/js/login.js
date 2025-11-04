/* 
===============================================================================
NOMBRE: login.js
DESCRIPCIÓN: Script de validación y envío del formulario de inicio de sesión 
             de la plataforma AITHER. Controla la verificación de campos, 
             conexión con el servidor PHP y redirección a la página principal.
COPYRIGHT: © 2025 AITHER. Todos los derechos reservados.
FECHA: 04/11/2025
AUTOR: Sergi y Manuel
APORTACIÓN: Implementación del manejo de eventos del formulario de login, 
            validación de datos y comunicación asincrónica con el servidor.
===============================================================================
*/
// ------------------------------------------------------------------
// DECLARACIÓN DE VARIABLES
// ------------------------------------------------------------------
// Captura de referencias a los elementos HTML del formulario y 
// área donde se mostrarán los mensajes de error o éxito.
const form = document.getElementById("loginForm");
const msg = document.getElementById("message");

// ------------------------------------------------------------------
// FUNCIÓN: Evento 'submit' del formulario
// ------------------------------------------------------------------
// Se ejecuta al enviar el formulario, previene el comportamiento 
// por defecto, valida los campos y realiza una petición asincrónica
// al backend usando fetch.
form.addEventListener("submit", async (e) => {
  e.preventDefault(); // Evita que el formulario recargue la página
  msg.textContent = ""; // Limpia cualquier mensaje previo

  // ----------------------------------------------------------------
  // Captura y limpieza de valores ingresados por el usuario
  // ----------------------------------------------------------------
  const email = document.getElementById("gmail").value.trim();
  const password = document.getElementById("password").value.trim();

  // ----------------------------------------------------------------
  // Validación básica de campos vacíos
  // ----------------------------------------------------------------
  if (!email || !password) {
    msg.style.color = "#ffdddd";
    msg.textContent = "Por favor, rellena todos los campos.";
    return; // Detiene la ejecución si hay campos vacíos
  }

  // ----------------------------------------------------------------
  // Preparación de datos para enviar al backend
  // ----------------------------------------------------------------
  // Se crea un objeto JSON que incluye la acción 'login' que
  // identifica la petición en el backend, y los datos del usuario.
  const payload = {
    accion: "login",
    gmail: email,
    password: password
  };

  try {
    // ----------------------------------------------------------------
    // Petición asincrónica al servidor
    // ----------------------------------------------------------------
    // Se envía la información mediante POST como JSON, y se
    // espera la respuesta en formato JSON.
    const response = await fetch("../api/index.php", {
      method: "POST",
      headers: {
        "Content-Type": "application/json" // Especifica que enviamos JSON
      },
      body: JSON.stringify(payload) // Convertimos el objeto a JSON
    });

    // ----------------------------------------------------------------
    // Procesamiento de la respuesta del servidor
    // ----------------------------------------------------------------
    const data = await response.json(); // Convertimos la respuesta a JSON

    // Verificamos si la petición fue exitosa según la API
    if (data.status === "ok") {
      // Guardamos información del usuario en localStorage si existe
      if (data.user) localStorage.setItem("user", JSON.stringify(data.user));

      // Redirigimos a la página principal del dashboard
      window.location.href = "dashboard.html";
    } else {
      // Mostramos mensaje de error si credenciales son incorrectas
      msg.style.color = "#ffdddd";
      msg.textContent = data.message || "Usuario o contraseña incorrectos.";
    }
  } catch (error) {
    // ----------------------------------------------------------------
    // Manejo de errores de conexión
    // ----------------------------------------------------------------
    console.error(error); // Loguea el error en consola
    msg.style.color = "#ffdddd";
    msg.textContent = "Error de conexión con el servidor.";
  }
});
