/* 
 * Este script inicializa varios componentes de Materialize CSS al cargar la página.
 */

document.addEventListener('DOMContentLoaded', function() {
    // Inicializa los sliders en la página.
    // Se seleccionan todos los elementos con la clase 'slider'.
    var elems = document.querySelectorAll('.slider');
    // Configuración de los sliders: sin indicadores, altura de 426px, duración de 700ms, y intervalo de 6000ms.
    var instancesSlider = M.Slider.init(elems, {
        indicators: false,   // No mostrar indicadores de la posición actual del slider.
        height: 426,         // Altura del slider.
        duration: 700,       // Duración de la transición entre slides.
        interval: 6000       // Intervalo entre cada transición automática.
    });

    // Inicializa los modales en la página.
    // Se seleccionan todos los elementos con la clase 'modal'.
    elems = document.querySelectorAll('.modal');
    // Configuración de los modales.
    var instancesModal = M.Modal.init(elems);

    // Inicializa los sidenav (barra lateral) en la página.
    // Se seleccionan todos los elementos con la clase 'sidenav'.
    elems = document.querySelectorAll('.sidenav');
    // Configuración de los sidenav.
    var instancesSidenav = M.Sidenav.init(elems);

    // Inicializa los dropdowns (menús desplegables) en la página.
    // Se seleccionan todos los elementos con la clase 'dropdown-trigger'.
    elems = document.querySelectorAll('.dropdown-trigger');
    // Configuración de los dropdowns: sin restricción de ancho.
    var instancesDropdown = M.Dropdown.init(elems, {
        constrainWidth: false
    });

    // Inicializa los tooltips (información emergente) en la página.
    // Se seleccionan todos los elementos con la clase 'tooltipped'.
    elems = document.querySelectorAll('.tooltipped');
    // Configuración de los tooltips: retraso de aparición de 500ms.
    var instancesTooltip = M.Tooltip.init(elems, {
        enterDelay: 500
    });

    // Función para ocultar los tooltips en dispositivos móviles.
    function checkScreenSize() {
        // Si el ancho de la ventana es menor o igual a 600px (dispositivos móviles).
        if (window.innerWidth <= 600) {
            // Se destruyen las instancias de tooltips para evitar mostrar la información emergente en dispositivos móviles.
            instancesTooltip.forEach(instance => {
                instance.destroy(); 
            });
        } else {
            // Si el ancho de la ventana es mayor a 600px (dispositivos de escritorio).
            elems.forEach(elem => {
                // Si no hay una instancia de tooltip para el elemento actual.
                if (!M.Tooltip.getInstance(elem)) { 
                    // Inicializa un nuevo tooltip para el elemento con el retraso de aparición de 500ms.
                    M.Tooltip.init(elem, {
                        enterDelay: 500
                    });
                }
            });
        }
    }

    // Llama a la función para establecer la visibilidad de los tooltips al cargar la página.
    checkScreenSize();
    // Añade un event listener para verificar el tamaño de la pantalla cada vez que la ventana se redimensiona.
    window.addEventListener('resize', checkScreenSize);
});
