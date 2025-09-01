/* Este código se crea con el propósito de manejar el autocompletado en el modal nuevo,
   para poder buscar productos más fácilmente, utilizando la API REST de ProductoRestController */
$(document).ready(function() {
    // Solicitud AJAX para obtener la lista de productos más vendidos del servidor
    $.ajax({
        url: '/api/grafico/ProductosMasVendidos', // URL del servidor para obtener los productos
        type: 'GET', // Tipo de solicitud para obtener datos
        success: function(response) {
            let nombresCompletos = []; // Array para almacenar los nombres completos (nombre + presentación)
            let totalVentas = []; // Array para almacenar los totales de ventas

            // Recorrer los productos obtenidos y prepararlos para el autocompletado y arrays
            response.forEach(function(producto) {
                let id = producto[0]; // ID del producto
                let nombre = producto[1]; // Nombre del producto
                let presentacion = producto[2]; // Presentación del producto
                let cantidadVendida = producto[3]; // Cantidad vendida del producto
                let nombreCompleto = nombre + " " + presentacion; // Nombre completo del producto

                nombresCompletos.push(nombreCompleto); // Añadir el nombre completo al array
                totalVentas.push(cantidadVendida); // Añadir el total de ventas al array
            });

            // Generar el gráfico con Chart.js usando los arrays obtenidos
            generarGrafico(nombresCompletos, totalVentas,'graficoProductos1',  'bar', 'Productos más vendidos');
        },
        error: function(xhr, status, error) {
            console.error('Error al obtener los productos:', error); // Mostrar el error si la solicitud falla
        }
    });
    $.ajax({
        url: '/api/grafico/ProductosMenosVendidos', // URL del servidor para obtener los productos
        type: 'GET', // Tipo de solicitud para obtener datos
        success: function(response) {
            let nombresCompletos = []; // Array para almacenar los nombres completos (nombre + presentación)
            let totalVentas = []; // Array para almacenar los totales de ventas

            // Recorrer los productos obtenidos y prepararlos para el autocompletado y arrays
            response.forEach(function(producto) {
                let id = producto[0]; // ID del producto
                let nombre = producto[1]; // Nombre del producto
                let presentacion = producto[2]; // Presentación del producto
                let cantidadVendida = producto[3]; // Cantidad vendida del producto
                let nombreCompleto = nombre + " " + presentacion; // Nombre completo del producto

                nombresCompletos.push(nombreCompleto); // Añadir el nombre completo al array
                totalVentas.push(cantidadVendida); // Añadir el total de ventas al array
            });

            // Generar el gráfico con Chart.js usando los arrays obtenidos
            generarGrafico(nombresCompletos, totalVentas,'graficoProductos2', 'bar',  'Productos menos vendidos');
        },
        error: function(xhr, status, error) {
            console.error('Error al obtener los productos:', error); // Mostrar el error si la solicitud falla
        }
    });
    $.ajax({
        url: '/api/grafico/VentasUltimos30Dias', // URL del servidor para obtener los productos
        type: 'GET', // Tipo de solicitud para obtener datos
        success: function(response) {
            let fechas = []; // Array para almacenar los nombres completos (nombre + presentación)
            let totalVentas = []; // Array para almacenar los totales de ventas


            // Recorrer los productos obtenidos y prepararlos para el autocompletado y arrays
            response.forEach(function(venta) {
                let fecha = venta[0]; // ID del producto
                let totalVenta = venta[1]; // Nombre del producto

                fechas.push(fecha); // Añadir el nombre completo al array
                totalVentas.push(totalVenta); // Añadir el total de ventas al array
            });

            // Generar el gráfico con Chart.js usando los arrays obtenidos
            generarGrafico(fechas, totalVentas, 'graficoProductos3', 'line', 'Venta de los últimos 30 días');
        },
        error: function(xhr, status, error) {
            console.error('Error al obtener las ventas mensuales:', error); // Mostrar el error si la solicitud falla
        }
    });
    $.ajax({
        url: '/api/grafico/GananciasUltimos30Dias', // URL del servidor para obtener los productos
        type: 'GET', // Tipo de solicitud para obtener datos
        success: function(response) {
            let fechas = []; // Array para almacenar los nombres completos (nombre + presentación)
            let totalVentas = []; // Array para almacenar los totales de ventas


            // Recorrer los productos obtenidos y prepararlos para el autocompletado y arrays
            response.forEach(function(venta) {
                let fecha = venta[0]; // ID del producto
                let totalVenta = venta[1]; // Nombre del producto

                fechas.push(fecha); // Añadir el nombre completo al array
                totalVentas.push(totalVenta); // Añadir el total de ventas al array
            });

            // Generar el gráfico con Chart.js usando los arrays obtenidos
            generarGrafico(fechas, totalVentas, 'graficoProductos4', 'line', 'Ganancias de los últimos 30 días');
        },
        error: function(xhr, status, error) {
            console.error('Error al obtener las ventas mensuales:', error); // Mostrar el error si la solicitud falla
        }
    });
});

// Función para generar el gráfico de barras con Chart.js
function generarGrafico(fila, columna, grafico, type, label) {
    const ctx = document.getElementById(grafico).getContext('2d'); // Obtén el contexto del canvas
    const myChart = new Chart(ctx, {
        type: type, // Tipo de gráfico
        data: {
            labels: fila, // Nombres completos de los productos
            datasets: [{
                label: label, // Etiqueta de la barra
                data: columna, // Total de ventas correspondientes
                 // Color de fondo de las barras
                backgroundColor:[ 
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(255, 159, 64, 0.6)',
                ],
                borderColor:[ 
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(255, 159, 64, 0.6)',
                ], // Color del borde de las barras
                borderWidth: 1 // Ancho del borde
            }]
        },
        borderWidth: 10.5 // Ancho del borde
    });
}
