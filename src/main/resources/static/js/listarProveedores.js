/*este codigo se crea con el proposito de manejar el autocompletado en el modal nuevo, para poder
buscar productos mas facilmente, utilizando una la apiRest de restProducto */
$(document).ready(function() {

    // Solicitud AJAX para obtener la lista de productos del servidor
    $.ajax({
        url: '/api/proveedor/listar', // URL del servidor para obtener los productos
        type: 'GET', // Tipo de solicitud para obtener datos
        success: function(response) {
            let proveedores = {}; // Objeto para el autocompletado
            let proveedoresMap = {}; // Mapa para almacenar productos con su ID

            // Recorrer los productos obtenidos y prepararlos para el autocompletado
            response.forEach(function(proveedor) {
                let id = proveedor[0]; // ID del producto
                let nombre = proveedor[1]; // Nombre del producto
                proveedores[nombre] = null; // Añadir al autocompletado
                proveedoresMap[nombre] = id; // Guardar el ID en el mapa
            });

            // Configurar el autocompletado en los campos de texto con la lista de productos
            $('#buscar_proveedor').autocomplete({
                data: proveedores, // Datos para las sugerencias de autocompletado
                limit: 5, // Máximo de sugerencias mostradas
                minLength: 1, // Caracteres mínimos para activar el autocompletado
                onAutocomplete: function(texto) {
                    let idProveedor = proveedoresMap[texto]; // Obtener el ID del producto seleccionado
                    $('#id_proveedor').val(idProveedor); // Colocar el ID en el campo oculto
                }
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al obtener los productos:', error); // Mostrar el error si la solicitud falla
        }
    });
});
