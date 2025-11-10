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

// Crear mensaje para login
let msgLogin = document.createElement("p");
msgLogin.id = "message-login";
msgLogin.style.marginTop = "10px";
msgLogin.style.fontWeight = "600";
msgLogin.style.textAlign = "center";
msgLogin.style.color = "red";
msgLogin.style.opacity = "70%";

// Crear mensaje para registro
let msgRegister = document.createElement("p");
msgRegister.id = "message-register";
msgRegister.style.marginTop = "10px";
msgRegister.style.fontWeight = "600";
msgRegister.style.alignItems = "center";
msgRegister.style.color = "red";
msgRegister.style.opacity = "70%";

// 🔧 Insertar los mensajes justo antes del botón de cada formulario
loginForm.querySelector(".forgot").before(msgLogin);
registerForm.querySelector(".btn-primary").before(msgRegister);

// ============================
// ANIMACIÓN ENTRE LOGIN Y REGISTRO
// ============================

signUpBtn.addEventListener("click", () => {
  container.classList.add("active");
});

signInBtn.addEventListener("click", () => {
  container.classList.remove("active");
});

// 🔧 Cambios de color del botón del header según el estado
container.addEventListener("transitionend", () => {
  if (container.classList.contains("active")) {
    botonHeader.classList.add("active");
  } else {
    botonHeader.classList.remove("active");
  }
});

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

// Actualizar tras animaciones o clicks
container.addEventListener("transitionend", updateHeaderLoginButton);
signUpBtn.addEventListener("click", updateHeaderLoginButton);
signInBtn.addEventListener("click", updateHeaderLoginButton);

// Interceptar click en el header
botonHeader.addEventListener("click", (e) => {
  e.preventDefault();
  if (botonHeader.classList.contains("disabled")) return;
  container.classList.remove("active");
  updateHeaderLoginButton();
  document.getElementById("correo-sign-in")?.focus();
});


// ============================
// EVENTO LOGIN
// ============================
loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  msgLogin.textContent = "";

  const email = document.getElementById("correo-sign-in").value.trim();
  const password = document.getElementById("contraseña-sign-in").value.trim();

  if (!email || !password) {
    msgLogin.textContent = "Por favor, rellena todos los campos.";
    setTimeout(() => {
      msgLogin.textContent = "";
    }, 3000)
    return;
  }

  const formData = new FormData();
  formData.append("gmail", email);
  formData.append("password", password);

  try {
    const response = await fetch("../php/login.php", {
      method: "POST",
      body: formData,
    });

    const data = await response.json();

    if (data.success) {
      localStorage.setItem("user", JSON.stringify(data.user));
      window.location.href = "dashboard.html";
    } else {
      msgLogin.textContent = data.message || "Usuario o contraseña incorrectos.";
      setTimeout(() => {
        msgLogin.textContent = "";
      }, 3000)
    }
  } catch (error) {
    console.error(error);
    msgLogin.textContent = "Error de conexión con el servidor.";
    setTimeout(() => {
      msgLogin.textContent = "";
    }, 3000)
  }
});

// ============================
// EVENTO REGISTRO
// ============================
registerForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  msgRegister.textContent = "";

  const nombre = document.getElementById("nombre").value.trim();
  const apellidos = document.getElementById("apellidos").value.trim();
  const correo = document.getElementById("correo-sign-up").value.trim();
  const pass = document.getElementById("contraseña-sign-up").value.trim();
  const confirm = document.getElementById("confirmar-contraseña-sign-up").value.trim();
  const politica = registerForm.querySelector("input[type='checkbox']").checked;

  if (!nombre || !apellidos || !correo || !pass || !confirm) {
    msgRegister.textContent = "Por favor, completa todos los campos.";
    setTimeout(() => {
      msgRegister.textContent = "";
    }, 3000)
    return;
  }

  if(pass.length <= 8) {
    msgRegister.textContent = "La contraseña debe tener al menos 8 caracteres.";
    setTimeout(() => {
      msgRegister.textContent = "";
    }, 3000)
    return;
  }

  if (pass !== confirm) {
    msgRegister.textContent = "Las contraseñas no coinciden.";
    setTimeout(() => {
      msgRegister.textContent = "";
    }, 3000)
    return;
  }

  if (!politica) {
    msgRegister.textContent = "Debes aceptar la política de privacidad.";
    setTimeout(() => {
      msgRegister.textContent = "";
    }, 3000)
    return;
  }

  const formData = new FormData();
  formData.append("nombre", nombre);
  formData.append("apellidos", apellidos);
  formData.append("correo", correo);
  formData.append("password", pass);

  try {
    const response = await fetch("../php/register.php", {
      method: "POST",
      body: formData,
    });

    const data = await response.json();

    if (data.success) {
      msgRegister.style.color = "green";
      msgRegister.style.opacity = "90%";
      msgRegister.textContent = "Registro exitoso. ¡Ahora puedes iniciar sesión!";
      setTimeout(() => {
        msgRegister.textContent = "";
      }, 3000)
      setTimeout(() => container.classList.remove("active"), 1500);
    } else {
      msgRegister.textContent = data.message || "Error al registrarse.";
    }
  } catch (error) {
    console.error(error);
    msgRegister.textContent = "Error de conexión con el servidor.";
    setTimeout(() => {
      msgRegister.textContent = "";
    }, 3000)
  }
});
