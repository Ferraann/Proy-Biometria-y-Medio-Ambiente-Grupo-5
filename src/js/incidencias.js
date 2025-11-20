/* 
===============================================================================
NOMBRE: incidencias.js
DESCRIPCIÓN: Listado completo de incidencias con fotos y búsqueda en tiempo real.
COPYRIGHT: © 2025 AITHER. Todos los derechos reservados.
FECHA: 21/11/2025
AUTOR: Basado en login.js por Ferran y Manuel
===============================================================================
*/

// ELEMENTOS DEL DOM
const lista = document.getElementById("lista-incidencias");
const buscador = document.getElementById("buscador");

// Mensaje de error / info
const msg = document.createElement("p");
msg.id = "message-incidencias";
msg.style.marginTop = "10px";
msg.style.fontWeight = "600";
msg.style.fontSize = "14px";
msg.style.textAlign = "center";
msg.style.color = "white";
msg.classList.remove("fade-out");

// URL de la API
const API_URL = "../api/index.php";

let incidencias = [];

// ---------------------------------------------------------------------------
// FUNCIÓN AUXILIAR: Mostrar mensaje con desvanecimiento
// ---------------------------------------------------------------------------
function mostrarMensaje(texto, duracion = 3000) {
  msg.textContent = texto;
  msg.classList.remove("fade-out");
  buscador.after(msg);
  setTimeout(() => msg.classList.add("fade-out"), duracion);
}

// ---------------------------------------------------------------------------
// CARGAR TODAS LAS INCIDENCIAS AL INICIAR
// ---------------------------------------------------------------------------
window.addEventListener("DOMContentLoaded", async () => {
  try {
    const res = await fetch(`${API_URL}?accion=getTodasIncidencias`);
    const data = await res.json();

    console.log("Respuesta cruda:", data); // ← ESTO ES CLAVE

    if (Array.isArray(data)) {
      incidencias = data;
      renderIncidencias(incidencias);
      cargarFotos();
    } else {
      mostrarMensaje("No se pudieron cargar las incidencias.", 4000);
    }
  } catch (err) {
    console.error(err);
    mostrarMensaje("Error de conexión con el servidor.", 4000);
  }
});

// ---------------------------------------------------------------------------
// RENDERIZAR INCIDENCIAS
// ---------------------------------------------------------------------------
function renderIncidencias(datos) {
  if (!datos.length) {
    lista.innerHTML = "<p style='text-align:center;'>No hay incidencias.</p>";
    return;
  }

  lista.innerHTML = datos.map(inc => `
    <div class="incidencia" data-id="${inc.id}">
      <h2>${inc.titulo}</h2>
      <p><strong>Descripción:</strong> ${inc.descripcion}</p>
      <p class="meta"><strong>Usuario:</strong> ${inc.usuario || "Anónimo"}</p>
      <p class="meta"><strong>Estado:</strong> ${inc.estado}</p>
      <p class="meta"><strong>Fecha:</strong> ${new Date(inc.fecha_creacion).toLocaleString()}</p>
      <div class="fotos" id="fotos-${inc.id}"></div>
    </div>
  `).join("");
}

// ---------------------------------------------------------------------------
// CARGAR FOTOS PARA CADA INCIDENCIA
// ---------------------------------------------------------------------------
async function cargarFotos() {
  for (const inc of incidencias) {
    try {
      const res = await fetch(`${API_URL}?accion=getFotosIncidencia&incidencia_id=${inc.id}`);
      const data = await res.json();

      const contenedor = document.getElementById(`fotos-${inc.id}`);
      if (data.status === "ok" && Array.isArray(data.fotos) && data.fotos.length) {
        contenedor.innerHTML = data.fotos.map(f =>
          `<img src="data:image/jpeg;base64,${f.foto}" alt="Foto incidencia ${inc.id}" />`
        ).join("");
      } else {
        contenedor.innerHTML = "<em>Sin fotos</em>";
      }
    } catch (err) {
      console.error(err);
      document.getElementById(`fotos-${inc.id}`).innerHTML = "<em>Error al cargar fotos</em>";
    }
  }
}

// ---------------------------------------------------------------------------
// BUSCADOR EN TIEMPO REAL
// ---------------------------------------------------------------------------
buscador.addEventListener("input", e => {
  const texto = e.target.value.toLowerCase();
  const filtradas = incidencias.filter(inc =>
    inc.titulo.toLowerCase().includes(texto) ||
    inc.descripcion.toLowerCase().includes(texto) ||
    (inc.usuario && inc.usuario.toLowerCase().includes(texto)) ||
    inc.estado.toLowerCase().includes(texto)
  );
  renderIncidencias(filtradas);
});