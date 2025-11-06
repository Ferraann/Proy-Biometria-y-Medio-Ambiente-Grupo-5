/* 
===============================================================================
NOMBRE: login.js
DESCRIPCIÓN: Script de validación y envío del formulario de inicio de sesión 
             de la plataforma AITHER. Controla la verificación de campos, 
             conexión con el servidor PHP y redirección a la página principal.
COPYRIGHT: © 2025 AITHER. Todos los derechos reservados.
FECHA: 04/11/2025
AUTORES: Sergi y Manuel
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
// FUNCIÓN PRINCIPAL: Evento 'submit' del formulario
// ------------------------------------------------------------------
// Se ejecuta al enviar el formulario, previene el comportamiento 
// por defecto, valida los campos y realiza una petición asincrónica
// al backend usando fetch.
form.addEventListener("submit", async (e) => {
  e.preventDefault(); // Evita el comportamiento por defecto (recargar página)
  msg.textContent = ""; // Limpia cualquier mensaje previo

  // ----------------------------------------------------------------
  // CAPTURA DE DATOS DEL FORMULARIO
  // ----------------------------------------------------------------
  const email = document.getElementById("gmail").value.trim();
  const password = document.getElementById("password").value.trim();

  // ----------------------------------------------------------------
  // VALIDACIÓN BÁSICA
  // ----------------------------------------------------------------
  if (!email || !password) {
    msg.style.color = "#ffdddd";
    msg.textContent = "Por favor, rellena todos los campos.";
    return; // Detiene la ejecución si hay campos vacíos
  }

  // ----------------------------------------------------------------
  // PREPARACIÓN DE DATOS PARA EL BACKEND
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
    // PETICIÓN ASINCRÓNICA AL SERVIDOR
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

    // Si el servidor devuelve algo inesperado (por ejemplo HTML de error)
    if (!response.ok) {
      throw new Error(`Error HTTP: ${response.status}`);
    }

    const data = await response.json();

    // ----------------------------------------------------------------
    // PROCESAMIENTO DE LA RESPUESTA
    // ----------------------------------------------------------------
    if (data.status === "ok") {
      msg.style.color = "green";
      msg.textContent = data.message || "Inicio de sesión exitoso.";

      // Guarda los datos del usuario si el backend los devuelve
      if (data.user) {
        localStorage.setItem("user", JSON.stringify(data.user));
      }

      // Redirige al dashboard tras 1.5 segundos
      setTimeout(() => {
        window.location.href = "dashboard.html";
      }, 1500);

    } else {
      // Mostramos mensaje de error si credenciales son incorrectas
      msg.style.color = "#ffdddd";
      msg.textContent = data.message || "Usuario o contraseña incorrectos.";
    }
  } catch (error) {
    // ----------------------------------------------------------------
    // MANEJO DE ERRORES DE CONEXIÓN
    // ----------------------------------------------------------------
    console.error("Error de conexión o formato inválido:", error);
    msg.style.color = "#ff0000ff";
    msg.textContent = "Error de conexión con el servidor.";
  }
});
