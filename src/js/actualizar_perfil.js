/* ======================================================
   Manejo de editar campo y envío: sólo validar campos editados
   ====================================================== */

// Selecciona todos los iconos de edición
const editIcons = document.querySelectorAll('.edit-icon');

editIcons.forEach(icon => {
    icon.addEventListener('click', (e) => {
        e.preventDefault();

        // encontrar la fila y el input principal
        const row = icon.closest('.input-row');
        if (!row) return;
        const input = row.querySelector('input');

        if (!input) return;

        // habilitar el campo y marcarlo como editado
        input.disabled = false;
        input.dataset.edited = "1";
        input.focus();

        // Si es correo -> habilitar repetir-correo pero NO marcarla editada
        if (input.id === "correo") {
            const rep = document.getElementById('repetir-correo');
            if (rep) {
                rep.disabled = false;
                // La confirmación NO se marca como editada, solo sirve para validar
                rep.dataset.confirmFor = "gmail";
            }
        }

        // Si es contraseña -> habilitar repetir-contrasena
        if (input.id === "contrasena") {
            const rep = document.getElementById('repetir-contrasena');
            if (rep) {
                rep.disabled = false;
                rep.dataset.confirmFor = "password";
            }
        }

        // Opcional: añadir clase visual para indicar edición
        row.classList.add('editing');
    });
});

// Interceptar submit para validar SOLO campos editados
const form = document.querySelector('.perfil-form');

form.addEventListener('submit', function(e) {
    e.preventDefault();

    // Recolectar los campos editados
    const editedInputs = Array.from(form.querySelectorAll('input[data-edited="1"]'));

    // Validaciones por cada tipo editado
    for (const input of editedInputs) {
        const id = input.id;
        const val = (input.value || "").trim();

        // Nombre: no vacío
        if (id === 'nombre') {
            if (val === '') {
                alert('El nombre no puede estar vacío.');
                input.focus();
                return;
            }
        }

        // Correo: formato y confirmación
        if (id === 'correo') {
            const rep = form.querySelector('input[id="repetir-correo"]');
            // comprobar formato simple
            const reEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!reEmail.test(val)) {
                alert('Introduce un correo válido.');
                input.focus();
                return;
            }
            if (!rep || rep.disabled) {
                alert('Rellena la confirmación del correo.');
                rep && rep.focus();
                return;
            }
            if (val !== rep.value.trim()) {
                alert('Los correos no coinciden.');
                rep.focus();
                return;
            }
        }

        // Contraseña: longitud mínima y confirmación
        if (id === 'contrasena') {
            const rep = form.querySelector('input[id="repetir-contrasena"]');
            if (val.length < 6) {
                alert('La contraseña debe tener al menos 6 caracteres.');
                input.focus();
                return;
            }
            if (!rep || rep.disabled) {
                alert('Rellena la confirmación de la contraseña.');
                rep && rep.focus();
                return;
            }
            if (val !== rep.value) {
                alert('Las contraseñas no coinciden.');
                rep.focus();
                return;
            }
        }
    }

    // Si no hay campos editados -> nada que guardar
    if (editedInputs.length === 0) {
        alert('No has modificado nada.');
        return;
    }

    // Enviar el formulario normalmente (POST a actualizar_perfil.php)
    form.submit();
});