const form = document.getElementById("registreForm");
const msg = document.getElementById("message");

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  msg.textContent = "";

  const nombre = document.getElementById("nombre").value.trim();
  const apellidos = document.getElementById("apellido").value.trim();
  const email = document.getElementById("gmail").value.trim();
  const password = document.getElementById("password").value.trim();

  if (!nombre || !apellidos || !email || !password) {
    msg.style.color = "#ff0000ff";
    msg.textContent = "Por favor, rellena todos los campos.";
    return;
  }

  const formData = new FormData();
  formData.append("Nombre", nombre);
  formData.append("Apellidos", apellidos);
  formData.append("Email", email);
  formData.append("Contrasenya", password);

  try {
    const response = await fetch("../api/index.php", {
      method: "POST",
      body: formData
    });

    const data = await response.json();

    if (data.status === "ok") {
      msg.style.color = "green";
      msg.textContent = data.message || "Registro exitoso.";
      setTimeout(() => window.location.href = "login.html", 1500);
    } else {
      msg.style.color = "#ff0000ff";
      msg.textContent = data.message || "Error en el registro.";
    }

  } catch (error) {
    console.error(error);
    msg.style.color = "#ff0000ff";
    msg.textContent = "Error de conexión con el servidor.";
  }
});
