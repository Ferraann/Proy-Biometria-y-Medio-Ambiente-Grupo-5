/* ------------------------------------------------------- */
/* VARIABLE GLOBAL PARA ACUMULAR IMÁGENES  */
/* ------------------------------------------------------- */
let archivosAcumulados = [];

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

    // Definimos 'box' fuera del try para poder usarlo en el catch si falla
    const box = document.getElementById("msg");
    box.textContent = ""; 
    box.className = "";

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

    limpiarFormularioCompleto();
});


const imagenInput = document.getElementById('imagenInput');

/*3. Lógica para ACUMULAR fotos (con filtro anti-duplicados) */

if (imagenInput) {
    imagenInput.addEventListener('change', function() {
        // 1. Cogemos los nuevos archivos
        const nuevosArchivos = Array.from(this.files);
        let hayDuplicados = false; // Chivato para saber si encontramos repetidos
        
        // 2. Recorremos los nuevos uno a uno
        nuevosArchivos.forEach(nuevoArchivo => {
            
            // Verificamos si YA existe un archivo con el mismo nombre y tamaño en nuestra lista
            const existe = archivosAcumulados.some(archivoGuardado => 
                archivoGuardado.name === nuevoArchivo.name && 
                archivoGuardado.size === nuevoArchivo.size
            );

            if (!existe) {
                // Si NO existe, lo añadimos
                archivosAcumulados.push(nuevoArchivo);
            } else {
                // Si existe, activamos el chivato
                hayDuplicados = true;
            }
        });
        
        // 3. Actualizamos la lista visual
        actualizarTextoVisual();

        // 4. Si encontramos duplicados, avisamos al usuario con un mensajito suave
        if (hayDuplicados) {
            Swal.fire({
                toast: true, // Tipo notificación pequeña
                position: 'top',
                icon: 'warning',
                title: 'Se han ignorado imágenes repetidas',
                showConfirmButton: false,
                timer: 3000 // Se va sola a los 3 segundos
            });
        }

        // 5. Reseteamos el input
        this.value = ""; 
    });
}

/* 4. Actualizar cajitas visuales */
function actualizarTextoVisual() {
    const container = document.getElementById('file-count');
    if (!container) return;
    container.innerHTML = "";
    if (archivosAcumulados.length) {
        archivosAcumulados.forEach(file => {
            const div = document.createElement('div');
            div.className = 'file-item-row';
            div.innerHTML = `<i class="fa-solid fa-paperclip" style="margin-right:8px; color:#666;"></i> ${file.name}`;
            container.appendChild(div);
        });
    }
}

/* 5. Limpiar todo (form + array + vista) */
function limpiarFormularioCompleto(form) {
    form.reset();
    archivosAcumulados = [];
    actualizarTextoVisual();
}

/* 6. Botón reset manual */
const btnReset = document.querySelector('button[type="reset"]');
if (btnReset) {
    btnReset.addEventListener('click', () => {
        setTimeout(() => {
            archivosAcumulados = [];
            actualizarTextoVisual();
        }, 10);
    });
}

/* 7. Helper base64 */
const toBase64 = file =>
    new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = err => reject(err);
    });