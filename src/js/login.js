const form = document.getElementById("loginForm");
const msg = document.getElementById("message");

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  msg.textContent = "";

  const email = document.getElementById("gmail").value.trim();
  const password = document.getElementById("password").value.trim();

  if (!email || !password) {
    msg.style.color = "#ffdddd";
    msg.textContent = "Por favor, rellena todos los campos.";
    return;
  }

  const formData = new FormData();
  formData.append("gmail", email);
  formData.append("password", password);

  try {
    const response = await fetch("../php/login.php", {
      method: "POST",
      body: formData
    });

    const data = await response.json();

    if (data.success) {
  // Guardar usuario en localStorage
  localStorage.setItem("user", JSON.stringify(data.user));
  // Redirigir inmediatamente sin mensaje
  window.location.href = "dashboard.html";
} else {
      msg.style.color = "#ffdddd";
      msg.textContent = data.message || "Usuario o contraseña incorrectos.";
    }
  } catch (error) {
    console.error(error);
    msg.style.color = "#ffdddd";
    msg.textContent = "Error de conexión con el servidor.";
  }
});
