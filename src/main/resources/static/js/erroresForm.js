function manejoForm(idForm, idErrorDiv, cantidadMensaje) {
    document.querySelector(idForm).addEventListener('submit', function(event) {
        event.preventDefault(); // Prevenir el envío normal del formulario

        const form = this;
        const formData = new FormData(form);
        const errorDiv = document.getElementById(idErrorDiv);

        const method = form.querySelector('input[name="_method"]')?.value === 'PUT' ? 'PUT' : 'POST';


        fetch(form.getAttribute('action'), {
            method: method,
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            switch(cantidadMensaje){
                case 1:
                    if(!data.success){
                        errorDiv.style.color = "red";
                        errorDiv.textContent = data.message;
                    } else {
                        location.reload();
                    }
                break;
                case 2:
                    if (data.success) {
                        //mostrar el mensaje de exito en el div
                        errorDiv.style.color = "green";
                        errorDiv.textContent = data.message;
                        limpiarInputs(form);
                    } else {
                        // Mostrar el mensaje de error en el div
                        errorDiv.style.color = "red";
                        errorDiv.textContent = data.message;
                    } 
                break;
            }

        })
        .catch(error => {
            console.error("Error en la solicitud:", error);
        });
    });
}

// Función para limpiar los campos del formulario
function limpiarInputs(form) {
    // Selecciona todos los inputs dentro del formulario y limpia su valor
    form.querySelectorAll('input, textarea, select').forEach(input => {
        if (input.type === 'date') {
            // Si el input es de tipo 'date', establecer la fecha actual
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            input.value = `${year}-${month}-${day}`;
        } else {
            // Si no es de tipo 'date', limpiar el valor
            input.value = '';
        }
    });
}


// Ejecutar la función cuando la página termine de cargar
window.addEventListener('load', function() {

    if(document.querySelector('#registrar_venta form')){
        manejoForm('#registrar_venta form', 'errorDivNuevaVenta', 2);

    } else if(document.querySelector('#editar_venta form')){
        manejoForm('#editar_venta form', 'errorDivNuevaVenta', 2);

    } else if(document.querySelector('#olvide_clave form')){
        manejoForm('#olvide_clave form','errorDivOlvidarClave', 2);

    } else if(this.document.querySelector('#registrar_precio form')) {
        manejoForm('#registrar_precio form', 'errorDivRegistrarPrecio', 2);

    } else if(this.document.querySelector('#cambio_clave form')) {
        manejoForm('#cambio_clave form', 'errorDivCambiarClave', 1);
    }
});