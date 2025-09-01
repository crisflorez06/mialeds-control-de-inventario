/*el proposito de este codigo es el de manejar la logica del editar y eliminar ventas, abriendo
los modales correspondientes*/ 
document.addEventListener('DOMContentLoaded', function() {
    
    // Selecciona los enlaces del menú de ajustes de venta
    const settings = document.querySelectorAll('#settings_venta li a');

    // Selecciona los elementos con id 'selector_venta'
    const selectors = document.querySelectorAll('#selector_venta');

    // Inicializa los modales de edición y eliminación
    const modalEditar = M.Modal.init(document.querySelector('#editar_venta'));
    const modalEliminar = M.Modal.init(document.querySelector('#eliminar_venta'));

    // Establece la fecha actual en el campo de 'fecha_venta'
    const fecha = new Date();
    const formatoFecha = fecha.toISOString().split('T')[0];
    document.querySelector('#registrar_venta input[name="fecha_venta"]').value = formatoFecha;

    // Asigna eventos de clic a las opciones de 'settings'
    settings.forEach(setting => {
        setting.addEventListener('click', function() {

            const selectedValue = this.getAttribute('data-value');

            selectors.forEach(selector => {
                selector.style.display = 'inline-block';

                // Si se selecciona editar venta
                switch (selectedValue) {
                    case 'editar_venta':
                        selector.addEventListener('click', function(event) {
                            event.preventDefault();

                            // Rellena el formulario de edición con los datos del producto
                            document.querySelector('#editar_venta input[name="id_venta_editar"]').value = this.getAttribute('data-id-venta');
                            document.querySelector('#editar_venta input[name="id_producto_venta"]').value = this.getAttribute('data-id-producto');
                            document.querySelector('#editar_venta input[name="producto_editar_venta"]').value = this.getAttribute('data-producto');
                            document.querySelector('#editar_venta input[name="cantidad_editar_venta"]').value = this.getAttribute('data-cantidad');
                            document.querySelector('#editar_venta input[name="cantidad_editar_total_venta"]').value = this.getAttribute('data-total-venta');
                            document.querySelector('#editar_venta input[name="fecha_editar_venta"]').value = this.getAttribute('data-fecha');

                            modalEditar.open(); // Abre el modal de edición
                        });
                    break;

                    // Si se selecciona eliminar venta
                    case 'eliminar_venta':
                        selector.addEventListener('click', function(event) {
                            event.preventDefault();

                            // Muestra el mensaje de confirmación con los datos de la venta
                            document.getElementById('mensaje_eliminar').innerHTML = `${this.getAttribute('data-cantidad')} ${this.getAttribute('data-producto')} de la fecha ${this.getAttribute('data-fecha')}`;

                            // Rellena el formulario de eliminación
                            document.querySelector('#eliminar_venta input[name="id_eliminar_venta"]').value = this.getAttribute('data-id-venta');

                            modalEliminar.open(); // Abre el modal de eliminación
                        });
                    break;
                }
            });
        });
    });
});
