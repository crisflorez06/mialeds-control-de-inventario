/*este codigo se crea con el proposito de manejar el autocompletado en el modal nuevo, para poder
buscar productos mas facilmente, utilizando una la apiRest de ProductoRestController  */
$(document).ready(function() {
    // Evento que se activa al hacer clic en el botón "Agregar Producto"

    // Solicitud AJAX para obtener la lista de productos del servidor
    $.ajax({
        url: '/api/producto/listar', // URL del servidor para obtener los productos
        type: 'GET', // Tipo de solicitud para obtener datos
        success: function(response) {
            let productos = {}; // Objeto para el autocompletado
            let productosMap = {}; // Mapa para almacenar productos con su ID

            // Recorrer los productos obtenidos y prepararlos para el autocompletado
            response.forEach(function(producto) {
                let id = producto[0]; // ID del producto
                let nombre = producto[1]; // Nombre del producto
                let presentacion = producto[2]; // Presentación del producto
                let nombreCompleto = nombre + " " + presentacion; // Nombre completo del producto
                productos[nombreCompleto] = null; // Añadir al autocompletado
                productosMap[nombreCompleto] = id; // Guardar el ID en el mapa
            });

            // Configurar el autocompletado en los campos de texto con la lista de productos
            $('#buscar_producto').autocomplete({
                data: productos, // Datos para las sugerencias de autocompletado
                limit: 5, // Máximo de sugerencias mostradas
                minLength: 1, // Caracteres mínimos para activar el autocompletado
                onAutocomplete: function(texto) {
                    let idProducto = productosMap[texto]; // Obtener el ID del producto seleccionado
                    $('#id_producto').val(idProducto); // Colocar el ID en el campo oculto
                }
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al obtener los productos:', error); // Mostrar el error si la solicitud falla
        }
    });
});
