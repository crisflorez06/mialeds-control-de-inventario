/*el proposito de este codigo es el de manejar la logica del editar y eliminar ventas, abriendo
los modales correspondientes*/ 
document.addEventListener('DOMContentLoaded', function() {
    
    // Selecciona los enlaces del menú de ajustes de venta
    const settings = document.querySelectorAll('#settings_proveedor li a');

    // Selecciona los elementos con id 'selector_venta'
    const selectors = document.querySelectorAll('#selector_proveedor');

    // Inicializa los modales de edición y eliminación
    const modalNuevo = M.Modal.init(document.querySelector('#nuevo_proveedor'));
    const modalEditar = M.Modal.init(document.querySelector('#editar_proveedor'));
    const modalEliminar = M.Modal.init(document.querySelector('#eliminar_proveedor'));


    // Asigna eventos de clic a las opciones de 'settings'
    settings.forEach(setting => {
        setting.addEventListener('click', function() {

            const selectedValue = this.getAttribute('data-value');

            selectors.forEach(selector => {
                selector.style.display = 'inline-block';

                switch (selectedValue) {
                    case 'nuevo_proveedor':
                        selector.style.display = 'none';
                        break;

                    case 'editar_proveedor':
                        selector.addEventListener('click', function(event) {
                            event.preventDefault();

                            // Rellena el formulario de edición con los datos del producto
                            document.querySelector('#editar_proveedor input[name="editar_id_proveedor"]').value = this.getAttribute('data-idProveedor');
                            document.querySelector('#editar_proveedor input[name="editar_nombre_proveedor"]').value = this.getAttribute('data-nombreProveedor');
                            document.querySelector('#editar_proveedor input[name="editar_nit_proveedor"]').value = this.getAttribute('data-nitProveedor');
                            document.querySelector('#editar_proveedor input[name="editar_correo_proveedor"]').value = this.getAttribute('data-correoProveedor');
                            document.querySelector('#editar_proveedor input[name="editar_telefono_proveedor"]').value = this.getAttribute('data-telefonoProveedor');

                            modalEditar.open(); // Abre el modal de edición
                        });
                    break;

                    // Si se selecciona eliminar venta
                    case 'eliminar_proveedor':
                        selector.addEventListener('click', function(event) {
                            event.preventDefault();

                            document.querySelector('#proveedor_eliminar').textContent = `¿Estás seguro de eliminar a ${this.getAttribute('data-nombreProveedor')}?`;
                            // Rellena el formulario de eliminación
                            document.querySelector('#eliminar_proveedor input[name="id_proveedor_eliminar"]').value = this.getAttribute('data-idProveedor');

                            modalEliminar.open(); // Abre el modal de eliminación
                        });
                    break;
                }
            });
        });
    });
});
