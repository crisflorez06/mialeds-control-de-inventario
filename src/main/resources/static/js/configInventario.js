/*este codigo tiene como proposito el de manejar la logica de los modales, teniendo en cuenta
que al utilizar materialize genera errores al momento de enviar formularios dentro de modales, 
con este codigo se soluciona ese problema aparte de ayudarnos a pasar datos a los inputs*/
document.addEventListener('DOMContentLoaded', function() {
    
    // Selecciona todos los elementos 'a' dentro de 'li' con el id 'settings'
    const settings = document.querySelectorAll('#settings li a');
    
    // Selecciona todos los elementos con el id 'selector'
    const selectors = document.querySelectorAll('#selector');    
    
    // Inicializa los modales utilizando Materialize para registrar movimientos, editar y eliminar productos
    const modalMovimiento = M.Modal.init(document.querySelector('#registrar_movimiento'));
    const modalEditar = M.Modal.init(document.querySelector('#editar_producto'));
    const modalEliminar = M.Modal.init(document.querySelector('#eliminar_producto'));
    const modalEscasos = M.Modal.init(document.querySelector('#productos_escasos'));
    const btnEscasos = document.querySelector('#btn_escasos');
        
    // Obtiene la fecha actual y la formatea en formato YYYY-MM-DD
    const fecha = new Date();
    const formatoFecha = fecha.toISOString().split('T')[0];

    // Itera sobre cada opción en 'settings' y agrega un event listener
    settings.forEach(setting => {
        setting.addEventListener('click', function() {
            
            // Obtiene el valor del atributo 'data-value' de la opción seleccionada
            const selectedValue = this.getAttribute('data-value');
            
            // Muestra todos los botones de selección
            selectors.forEach(selector => {
                selector.style.display = 'inline-block';
                
                // Dependiendo del valor seleccionado, configura los datos y abre el modal correspondiente
                switch (selectedValue) {
                    
                    // Caso para la opción 'editar'
                    case 'editar':
                        selector.addEventListener('click', function(event) {
                            event.preventDefault(); // Previene la acción por defecto del enlace
                            
                            // Obtiene los atributos 'data-' necesarios para la edición
                            let id = this.getAttribute('data-id');
                            let nombre = this.getAttribute('data-nombre');
                            let presentacion = this.getAttribute('data-presentacion');
                            let precioCompra = this.getAttribute('data-precio-compra');
                            let precioVenta = this.getAttribute('data-precio-venta');
                            let cantidad = this.getAttribute('data-cantidad');
                            
                            // Llena los campos del modal de edición con los datos obtenidos
                            document.querySelector('#editar_producto input[name="id_editar"]').value = id;                            
                            document.querySelector('#editar_producto input[name="nombre_editar"]').value = nombre;
                            document.querySelector('#editar_producto input[name="presentacion_editar"]').value = presentacion;
                            document.querySelector('#editar_producto input[name="precio_compra_editar"]').value = precioCompra;
                            document.querySelector('#editar_producto input[name="precio_venta_editar"]').value = precioVenta;
                            document.querySelector('#editar_producto input[name="cantidad_editar"]').value = cantidad;
                            
                            // Abre el modal de edición
                            modalEditar.open();
                        });
                        break;

                    // Caso para la opción 'eliminar'
                    case 'eliminar':
                        selector.addEventListener('click', function(event) {
                            event.preventDefault(); // Previene la acción por defecto del enlace
                            
                            // Obtiene los atributos 'data-' necesarios para la eliminación
                            let idEliminar = this.getAttribute('data-id');
                            let nombreEli = this.getAttribute('data-nombre');
                            let presentacionEli = this.getAttribute('data-presentacion');
                            
                            // Muestra el nombre del producto a eliminar en el modal
                            let productoEli = document.getElementById("producto_eliminar");
                            productoEli.textContent = `${nombreEli} en ${presentacionEli}`;
                            
                            // Llena el campo oculto del id a eliminar en el modal
                            document.querySelector('#eliminar_producto input[name="id_eliminar"]').value = idEliminar;     
                            
                            // Abre el modal de eliminación
                            modalEliminar.open();
                        });
                        break;

                    // Caso para la opción 'entrada'
                    case 'entrada':
                        selector.addEventListener('click', function(event) {
                            event.preventDefault(); // Previene la acción por defecto del enlace
                            
                            // Obtiene los atributos 'data-' necesarios para registrar una entrada
                            let idEntrada = this.getAttribute('data-id');
                            let nombreEntrada = this.getAttribute('data-nombre');  
                            let movimiento = 'entrada';  
                            
                            // Llena los campos del modal de movimiento con los datos obtenidos
                            document.querySelector('#registrar_movimiento input[name="id_movimiento"]').value = idEntrada;                            
                            document.querySelector('#registrar_movimiento input[name="nombre_producto_movimiento"]').value = nombreEntrada;
                            document.querySelector('#registrar_movimiento input[name="movimiento"]').value = movimiento;
                            document.querySelector('#registrar_movimiento input[name="fecha_movimiento"]').value = formatoFecha;
                            
                            // Abre el modal de movimiento para entrada
                            modalMovimiento.open();
                        });
                        break;

                    // Caso para la opción 'salida'
                    case 'salida': 
                        selector.addEventListener('click', function(event) {
                            let idSalida = this.getAttribute('data-id');
                            let nombreSalida = this.getAttribute('data-nombre');  
                            let movimiento2 = 'salida';       
                            
                            // Llena los campos del modal de movimiento con los datos obtenidos
                            document.querySelector('#registrar_movimiento input[name="id_movimiento"]').value = idSalida;                            
                            document.querySelector('#registrar_movimiento input[name="nombre_producto_movimiento"]').value = nombreSalida;
                            document.querySelector('#registrar_movimiento input[name="movimiento"]').value = movimiento2;
                            document.querySelector('#registrar_movimiento input[name="fecha_movimiento"]').value = formatoFecha;
                            
                            // Abre el modal de movimiento para salida
                            modalMovimiento.open();
                        });
                        break;
                }
            });
        });
    });
});
