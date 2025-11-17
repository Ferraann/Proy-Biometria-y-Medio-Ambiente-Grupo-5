// ============================
// AITHER - index.js
// Control del login, registro y transiciones
// ============================

// ELEMENTOS DEL DOM
const container = document.getElementById("container");
const signUpBtn = document.getElementById("signUpBtn");
const signInBtn = document.getElementById("signInBtn");

// FORMULARIOS
const loginForm = document.querySelector(".sign-in-container form");
const registerForm = document.querySelector(".sign-up-container form");

// Botón del header
const botonHeader = document.querySelector("nav ul li:last-child a");

// ============================
// MENSAJES DE ERROR (independientes)
// ============================

// Estilos del mensaje para login
let msgLogin = document.createElement("p");
msgLogin.id = "message-login";
msgLogin.style.marginTop = "10px";
msgLogin.style.fontWeight = "600";
msgLogin.style.fontSize = "14px"
msgLogin.style.textAlign = "center";
msgLogin.style.color = "white";
msgLogin.classList.remove("fade-out");

// Estilos del mensaje para registro
let msgRegister = document.createElement("p");
msgRegister.id = "message-register";
msgRegister.style.marginTop = "10px";
msgRegister.style.fontWeight = "600";
msgRegister.style.fontSize = "14px";
msgRegister.style.display = "flex";
msgRegister.style.alignItems = "flex-start";
msgRegister.style.color = "white";
msgRegister.classList.remove("fade-out");


// ============================
// MOSTRAR / OCULTAR CONTRASEÑA
// ============================

document.querySelectorAll(".toggle-password").forEach(icon => {
  icon.addEventListener("click", () => {

    // coge el input objetivo según el data-input
    const input = document.getElementById(icon.dataset.input);

    if (input.type === "password") {
      input.type = "text";
      icon.src = "../img/ojo.png"; // ojo abierto
    } else {
      input.type = "password";
      icon.src = "../img/ojo-cerrado.png"; // ojo cerrado
    }
  });
});


// ============================
// ANIMACIÓN ENTRE LOGIN Y REGISTRO
// ============================

// Cuando se pulsa el boton de registro se añade la clase active
signUpBtn.addEventListener("click", () => {
  container.classList.add("active");
});

// Cuando se pulsa el boton de inicio de sesión se quita la clase active
signInBtn.addEventListener("click", () => {
  container.classList.remove("active");
});

// Cambios de color del botón del header según el estado
container.addEventListener("transitionend", () => {
  if (container.classList.contains("active")) {
    botonHeader.classList.add("active");
  } else {
    botonHeader.classList.remove("active");
  }
});

// Actualizar el estado del boton de inicio de sesión del header
function updateHeaderLoginButton() {
  const isRegisterView = container.classList.contains("active");
  if (isRegisterView) {
    botonHeader.classList.remove("disabled");
    botonHeader.classList.add("enabled");
  } else {
    botonHeader.classList.add("disabled");
    botonHeader.classList.remove("enabled");
  }
}

// Llamada inicial al cargar la página
updateHeaderLoginButton();

// Actualizar tras animaciones o clics
container.addEventListener("transitionend", updateHeaderLoginButton);
signUpBtn.addEventListener("click", updateHeaderLoginButton);
signInBtn.addEventListener("click", updateHeaderLoginButton);

// Interceptar click en el header
botonHeader.addEventListener("click", (e) => {
  e.preventDefault();
  // Si el hoton header tiene la clase disabled nada
  if (botonHeader.classList.contains("disabled")) return;
  // Si no, es decir, que tiene la clase active, primero se le quita la clase active y se llama a updateHeaderLoginButton()
  container.classList.remove("active");
  updateHeaderLoginButton();
  document.getElementById("correo-sign-in")?.focus();
});


// ============================
// EVENTO LOGIN
// ============================
loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  // Mensaje que se mostrará en pantalla
  msgLogin.textContent = "";

  // email y contraseña
  const email = document.getElementById("correo-sign-in").value.trim();
  const password = document.getElementById("contraseña-sign-in").value.trim();

  // Si no hay email ni contraseña en los inputs...
  if (!email || !password) {
    // ... se pone ese mensaje de error y se muestra...
    loginForm.querySelector(".forgot").before(msgLogin);
    msgLogin.textContent = "Por favor, rellena todos los campos.";
    // ... y despues de 3 segundos se quita
    setTimeout(() => {
      msgLogin.classList.add("fade-out");
    }, 3000)
    msgLogin.classList.remove("fade-out");
    return;
  }

  // Se crea un formData con los datos recogidos
  const formData = new FormData();
  formData.append("gmail", email);
  formData.append("password", password);

  try {
    // Envia los datos a login.php
    const response = await fetch("../php/login.php", {
      method: "POST",
      body: formData,
    });

    // Crea un json
    const data = await response.json();

    // Si va bien, se guarda en el localStorage en "user" los datos en formato string y se redifije a dashboard.html
    if (data.success) {
      localStorage.setItem("user", JSON.stringify(data.user));
      window.location.href = "dashboard.html";
    } else {
      // Si falla se pone ese mensaje de error ...
      loginForm.querySelector(".forgot").before(msgLogin);
      msgLogin.textContent = data.message || "Usuario o contraseña incorrectos.";
      // ... y despues de 3 segundos se quita
      setTimeout(() => {
        msgLogin.classList.add("fade-out");
      }, 3000)
      msgLogin.classList.remove("fade-out");
    }
    // Si falla todo es que la conexión da error
  } catch (error) {
    // Se pone ese error en pantalla ...
    loginForm.querySelector(".forgot").before(msgLogin);
    msgLogin.textContent = "Error de conexión con el servidor.";
    // ... y despues de 3 segundos se quita
    setTimeout(() => {
      msgLogin.classList.add("fade-out");
    }, 3000)
    msgLogin.classList.remove("fade-out");
  }
});

// ============================
// EVENTO REGISTRO
// ============================
registerForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  // Mensaje de registro
  msgRegister.textContent = "";

  // Campos del registro
  const nombre = document.getElementById("nombre").value.trim();
  const apellidos = document.getElementById("apellidos").value.trim();
  const correo = document.getElementById("correo-sign-up").value.trim();
  const pass = document.getElementById("contraseña-sign-up").value.trim();
  const confirm = document.getElementById("confirmar-contraseña-sign-up").value.trim();
  const politica = registerForm.querySelector("input[type='checkbox']").checked;

  // Si hay algo que no está escrito ...
  if (!nombre || !apellidos || !correo || !pass || !confirm) {
    // ... se pone este mensaje de error ...
    registerForm.querySelector(".btn-primary").before(msgRegister);

    msgRegister.textContent = "Por favor, completa todos los campos.";
    // ... y despues de 3 segundos se quita
    setTimeout(() => {
      msgRegister.classList.add("fade-out");
    }, 3000)
    msgRegister.classList.remove("fade-out");
    return;
  }

  // Comprobar contraseña (8 o mas caracteres, al menos un numero, una mayúscula y un caracter especial
  const numeros = "0123456789";
  const mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  const especiales = "!@#$%^&*(),.?\":{}|<>";

  var tieneNumero = false;
  var tieneMayuscula = false;
  var tieneEspecial = false;

  // Verificar números
  for (let i = 0; i < numeros.length; i++) {
    if (pass.includes(numeros[i])) {
      tieneNumero = true;
      break;
    }
  }

  // Verificar mayúsculas
  for (let i = 0; i < mayusculas.length; i++) {
    if (pass.includes(mayusculas[i])) {
      tieneMayuscula = true;
      break;
    }
  }

  // Verificar caracteres especiales
  for (let i = 0; i < especiales.length; i++) {
    if (pass.includes(especiales[i])) {
      tieneEspecial = true;
      break;
    }
  }

  if (
      pass.length < 8 || !tieneNumero || !tieneMayuscula || !tieneEspecial
  ) {
    registerForm.querySelector(".contraseña").before(msgRegister);
    msgRegister.textContent =
        "La contraseña debe contener al menos 8 caracteres, un número, una mayúscula y un carácter especial.";

    setTimeout(() => {
      msgRegister.classList.add("fade-out");
    }, 5000);

    msgRegister.classList.remove("fade-out");
    return;
  }


  // Si la contraseña es diferente a confirmar contraseña ...
  if (pass !== confirm) {
    // ... se pone este mensaje de error ...
    registerForm.querySelector(".btn-primary").before(msgRegister);
    msgRegister.textContent = "Las contraseñas no coinciden.";
    // ... y se quita despues de 3 segundos
    setTimeout(() => {
      msgRegister.classList.add("fade-out");
    }, 3000)
    msgRegister.classList.remove("fade-out");
    return;
  }

  // Si no se ha aceptado la politica de privacidad ...
  if (!politica) {
    // ... se pone este mensaje de error ...
    registerForm.querySelector(".btn-primary").before(msgRegister);
    msgRegister.textContent = "Debes aceptar la política de privacidad.";
    // ... y se quita despues de 3 segundos
    setTimeout(() => {
      msgRegister.classList.add("fade-out");
    }, 3000)
    msgRegister.classList.remove("fade-out");
    return;
  }

  // Se crea un formData con los datos obtenidos
  const formData = new FormData();
  formData.append("nombre", nombre);
  formData.append("apellidos", apellidos);
  formData.append("correo", correo);
  formData.append("password", pass);

  try {
    // Se intenta enviar a register.php
    const response = await fetch("../php/register.php", {
      method: "POST",
      body: formData,
    });

    // Se crear un json ...
    const data = await response.json();

    // ... y si va bien ...
    if (data.success) {
      // se le ponen estos estilos al mensaje ...
      msgRegister.style.color = "green";
      msgRegister.style.opacity = "90%";
      // ... y se pone este mensaje ...
      msgRegister.textContent = "Registro exitoso. ¡Ahora puedes iniciar sesión!";
      // ... y despues de 3 segundos se va ...
      setTimeout(() => {
        msgRegister.classList.add("fade-out");
      }, 3000)
      msgRegister.classList.remove("fade-out");
      // y se quita la clase active para volver a inicio de sesión
      setTimeout(() => container.classList.remove("active"), 1500);
    } else {
      // Si falla se se pone este mensaje de error
      msgRegister.textContent = data.message || "Error al registrarse.";
    }
    // Si falla todo es porque ha habido un error en la conexión con el servidor
  } catch (error) {
    // se pone este mensaje de error
    msgRegister.textContent = "Error de conexión con el servidor.";
    // se quita despues de 3 segundos
    setTimeout(() => {
      msgRegister.classList.add("fade-out");
    }, 3000)
    msgRegister.classList.remove("fade-out");
  }
});
