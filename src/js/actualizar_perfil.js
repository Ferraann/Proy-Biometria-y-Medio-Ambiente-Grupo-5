/* ======================================================
   Manejo de editar campo y envío: toggle edición
   ====================================================== */

// Selecciona todos los iconos de edición
const editIcons = document.querySelectorAll('.edit-icon');

editIcons.forEach(icon => {
    icon.addEventListener('click', (e) => {
        e.preventDefault();

        const row = icon.closest('.input-row');
        if (!row) return;
        const input = row.querySelector('input');
        if (!input) return;

        // Toggle: si ya estaba activo -> desactivar edición
        if (input.dataset.editing === "1") {
            input.dataset.editing = "0";
            input.disabled = true;
            input.style.backgroundColor = "";
            row.classList.remove('editing');

            // Desactivar confirmación si aplica
            if (input.id === "correo") {
                const rep = document.getElementById('repetir-correo');
                if (rep) rep.disabled = true, rep.style.backgroundColor = "";
            }
            if (input.id === "contrasena") {
                const rep = document.getElementById('repetir-contrasena');
                if (rep) rep.disabled = true, rep.style.backgroundColor = "";
            }

            return; // salir porque se desactivó
        }

        // Activar edición
        input.dataset.editing = "1";
        input.disabled = false;
        input.focus();
        input.style.backgroundColor = "#f0f0f0";
        row.classList.add('editing');

        // Activar confirmación si aplica
        if (input.id === "correo") {
            const rep = document.getElementById('repetir-correo');
            if (rep) rep.disabled = false, rep.style.backgroundColor = "#f0f0f0";
        }
        if (input.id === "contrasena") {
            const rep = document.getElementById('repetir-contrasena');
            if (rep) rep.disabled = false, rep.style.backgroundColor = "#f0f0f0";
        }

        // Marcar como editado al cambiar el valor
        input.addEventListener('input', () => {
            input.dataset.edited = "1";
        });
    });
});

// Interceptar submit para validar SOLO campos activamente en edición
const form = document.querySelector('.perfil-form');

form.addEventListener('submit', function(e) {
    e.preventDefault();

    // Recolectar los campos activamente en edición
    const editedInputs = Array.from(form.querySelectorAll('input[data-editing="1"]'));

    if (editedInputs.length === 0) {
        alert('No has modificado nada.');
        return;
    }

    // Validaciones por cada tipo editado
    for (const input of editedInputs) {
        const id = input.id;
        const val = (input.value || "").trim();

        // Nombre: no vacío
        if (id === 'nombre' && val === '') {
            alert('El nombre no puede estar vacío.');
            input.focus();
            return;
        }

        // Correo: formato y confirmación
        if (id === 'correo') {
            const rep = form.querySelector('input[id="repetir-correo"]');
            const reEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!reEmail.test(val)) {
                alert('Introduce un correo válido.');
                input.focus();
                return;
            }
            if (!rep || rep.disabled || val !== rep.value.trim()) {
                alert('Los correos no coinciden o no están rellenados.');
                rep && rep.focus();
                return;
            }
        }

        // Contraseña: longitud mínima y validación fuerte + confirmación
        if (id === 'contrasena') {
            const rep = form.querySelector('input[id="repetir-contrasena"]');

            // Validar longitud mínima
            if (val.length < 8) {
                alert('La contraseña debe tener al menos 8 caracteres.');
                input.focus();
                return;
            }

            // Validar número, mayúscula y carácter especial
            const tieneNum = /\d/.test(val);
            const tieneMay = /[A-Z]/.test(val);
            const tieneEsp = /[!@#$%^&*(),.?":{}|<>]/.test(val);
            if (!tieneNum || !tieneMay || !tieneEsp) {
                alert('La contraseña debe contener al menos un número, una mayúscula y un carácter especial.');
                input.focus();
                return;
            }

            // Confirmación
            if (!rep || rep.disabled || val !== rep.value) {
                alert('Las contraseñas no coinciden o no están rellenadas.');
                rep && rep.focus();
                return;
            }
        }

    }

    // Enviar formulario normalmente
    form.submit();
});
