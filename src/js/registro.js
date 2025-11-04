/* 
===============================================================================
NOMBRE: registro.js
DESCRIPCIÓN: Script para gestionar el registro de nuevos usuarios en la 
             plataforma AITHER. Valida los datos del formulario y 
             envía la información al servidor mediante una petición asíncrona.
COPYRIGHT: © 2025 AITHER. Todos los derechos reservados.
FECHA: 03/11/2025
AUTOR: [Tu nombre aquí]
APORTACIÓN: Implementación de la lógica de validación de campos y comunicación 
            con el backend PHP para registrar usuarios desde el formulario.
===============================================================================
*/

// -----------------------------------------------------------------------------
// DECLARACIÓN DE VARIABLES
// DISEÑO LÓGICO: Referencias a los elementos del formulario y del área de mensajes.
// DESCRIPCIÓN: Permiten manipular los datos ingresados por el usuario y mostrar 
// respuestas visuales durante el proceso de registro.
const form = document.getElementById("registreForm");
const msg = document.getElementById("message");

// -----------------------------------------------------------------------------
// FUNCIÓN: Evento 'submit' del formulario
// DISEÑO LÓGICO: Controla el envío del formulario, validando los campos, 
// evitando el comportamiento por defecto y gestionando la petición al servidor.
// DESCRIPCIÓN: Comprueba que todos los campos estén completos, prepara la 
// información en un objeto FormData y la envía de forma asíncrona al backend.
form.addEventListener("submit", async (e) => {
  e.preventDefault(); // Evita recargar la página al enviar el formulario
  msg.textContent = ""; // Limpia mensajes anteriores

  // Captura y limpieza de los valores introducidos por el usuario
  const nombre = document.getElementById("nombre").value.trim();
  const apellidos = document.getElementById("apellido").value.trim();
  const email = document.getElementById("gmail").value.trim();
  const password = document.getElementById("password").value.trim();

  // ---------------------------------------------------------------------------
  // BLOQUE: Validación de campos
  // DISEÑO LÓGICO: Verifica que ningún campo esté vacío antes de continuar.
  // DESCRIPCIÓN: Si falta algún dato, muestra un mensaje de advertencia y detiene el proceso.
  if (!nombre || !apellidos || !email || !password) {
    msg.style.color = "#ff0000ff";
    msg.textContent = "Por favor, rellena todos los campos.";
    return;
  }

  // ---------------------------------------------------------------------------
  // BLOQUE: Creación del objeto FormData
  // DISEÑO LÓGICO: Prepara los datos del formulario en formato adecuado para el envío.
  // DESCRIPCIÓN: Añade las claves esperadas por el backend con los valores introducidos.
  const formData = new FormData();
  formData.append("Nombre", nombre);
  formData.append("Apellidos", apellidos);
  formData.append("Email", email);
  formData.append("Contrasenya", password);

  try {
    // -------------------------------------------------------------------------
    // BLOQUE: Petición asíncrona al servidor
    // DISEÑO LÓGICO: Envía los datos del formulario mediante POST al endpoint PHP.
    // DESCRIPCIÓN: Se comunica con el backend y espera la respuesta en formato JSON.
    const response = await fetch("../api/index.php", {
      method: "POST",
      body: formData
    });

    const data = await response.json(); // Convierte la respuesta a formato JSON

    // -------------------------------------------------------------------------
    // BLOQUE: Procesamiento de la respuesta del servidor
    // DISEÑO LÓGICO: Analiza si el registro fue exitoso o fallido.
    // DESCRIPCIÓN: Muestra un mensaje visual acorde al resultado y 
    // redirige al usuario al login tras un breve retraso si el registro es correcto.
    if (data.status === "ok") {
      msg.style.color = "green";
      msg.textContent = data.message || "Registro exitoso.";
      setTimeout(() => window.location.href = "login.html", 1500);
    } else {
      msg.style.color = "#ff0000ff";
      msg.textContent = data.message || "Error en el registro.";
    }

  } catch (error) {
    // -------------------------------------------------------------------------
    // BLOQUE: Manejo de errores
    // DISEÑO LÓGICO: Controla excepciones durante la comunicación con el servidor.
    // DESCRIPCIÓN: Informa al usuario de fallos de conexión o errores inesperados.
    console.error(error);
    msg.style.color = "#ff0000ff";
    msg.textContent = "Error de conexión con el servidor.";
  }
});
