/* 1. Rellenar id_user desde localStorage */
const user = JSON.parse(localStorage.getItem("user"));
if (!user || !user.id) {
    alert("No se ha encontrado sesión. Redirigiendo al login.");
    location.href = "login.html";
} else {
    document.getElementById("id_user").value = user.id;
}

/* 2. Enviar formulario */
document.getElementById("incidenciaForm").addEventListener("submit", async e => {
    e.preventDefault();
    const form = e.target;
    const formData = new FormData(form);

    // Primera petición: crear la incidencia
    const payloadInc = {
        accion: "crearIncidencia",
        id_user: formData.get("id_user"),
        titulo: formData.get("titulo"),
        descripcion: formData.get("descripcion")
    };
try{
    const resInc = await fetch("../api/index.php", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payloadInc)
    }).then(r => r.json());

    const box = document.getElementById("msg");
    if (resInc.status !== "ok") {
        box.className = "text-danger";
        box.textContent = resInc.message || "Error al crear incidencia";
        return;
    }

    // Si hay imágenes, segunda petición
    const files = form.imagen.files;
    if (files.length) {
        const fotos = await Promise.all(
            [...files].map(f => toBase64(f))
        );

        const payloadImg = {
            accion: "guardarFotosIncidencia",
            incidencia_id: resInc.id_incidencia,
            fotos
        };

        const resImg = await fetch("../api/index.php", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payloadImg)
        }).then(r => r.json());

        if (resImg.status !== "ok") {
            box.className = "text-warning";
            box.textContent = "Incidencia creada, pero error al guardar imágenes: " + resImg.message;
            return;
        }
    }

    Swal.fire({
            title: '¡Enviado!',
            text: 'Tu incidencia se ha registrado correctamente.',
            icon: 'success',
            confirmButtonColor: '#152D9A', // Tu color corporativo azul
            confirmButtonText: 'Genial'
        });
    form.reset();
}catch (error){
console.error(error);
        box.className = "text-danger";
        box.textContent = "Error de conexión con el servidor.";
}
});

/* Helper para convertir File a base64 */
const toBase64 = file =>
    new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = err => reject(err);
    });