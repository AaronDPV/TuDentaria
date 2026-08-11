const hamburguesa = document.querySelector('.menu-hamburguesa');
const navMenu = document.querySelector('.nav-menu');

// Evento de clic para el botón hamburguesa
hamburguesa.addEventListener('click', () => {
    navMenu.classList.toggle('active'); 
});

const scrollUpBtn = document.querySelector('.scroll-up');

// 1. función para mostrar/ocultar el botón
function checkScrollButton() {
    if (scrollUpBtn) {
        if (window.scrollY > 300) {
            scrollUpBtn.style.display = 'block';
        } else {
            scrollUpBtn.style.display = 'none';
        }
    }
}

// 2. La ejecutamos CUANDO LA PÁGINA CARGA
document.addEventListener('DOMContentLoaded', () => {
    checkScrollButton(); 
});

// 3. La ejecutamos CUANDO SE HACE SCROLL
window.addEventListener('scroll', () => {
    checkScrollButton();
});

// 4. La funcionalidad de clic
if (scrollUpBtn) {
    scrollUpBtn.addEventListener('click', (e) => {
        e.preventDefault(); 
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
}


//ACTIVACIÓN DEL CARRUSEL
$(document).ready(function(){
    if ($('.bxslider').length) {
        $('.bxslider').bxSlider({
            mode: 'fade',       
            captions: true,     
            slideWidth: 0,      
            auto: true,        
            pager: false,      
            speed: 1000,        
            pause: 4000,        
            touchEnabled: true 
        });
    }
});

// Esperamos a que todo el documento cargue por completo
document.addEventListener("DOMContentLoaded", function () {
    const logoutAlert = document.getElementById("logout-alert");

    // Validamos si la alerta de cierre de sesión existe en la pantalla actual
    if (logoutAlert) {

        // 🌟 CORRECCIÓN DE RAÍZ: Limpiamos el "?logout" de la URL APENAS CARGA la página
        if (window.history.replaceState) {
            // Borra el parámetro de la barra de navegación de inmediato sin recargar
            window.history.replaceState(null, null, window.location.pathname);
        }

        // 1. DESAPARECER A LOS 5 SEGUNDOS (5000 milisegundos)
        const autoDismissTimeout = setTimeout(() => {
            dismissAlert();
        }, 5000);

        // 2. DESAPARECER AL HACER CLIC EN CUALQUIER PARTE DE LA PANTALLA
        const clickDismissHandler = function () {
            dismissAlert();
        };

        // Registramos el evento de clic en todo el documento
        document.addEventListener("click", clickDismissHandler);

        // Función encargada únicamente de ocultar el bloque visualmente
        function dismissAlert() {
            // Removemos el detector de clics para evitar consumo innecesario de memoria
            document.removeEventListener("click", clickDismissHandler);
            // Cancelamos el temporizador por si el usuario hizo clic antes de los 5 segundos
            clearTimeout(autoDismissTimeout);

            // Buscamos el div de la alerta interna de Bootstrap para desvanecerlo suavemente
            const alertInstance = logoutAlert.querySelector('.alert');
            if (alertInstance) {
                alertInstance.classList.remove('show');

                // Esperamos 150ms a que termine la animación de Bootstrap y borramos todo el contenedor
                setTimeout(() => {
                    logoutAlert.remove();
                }, 150);
            } else {
                logoutAlert.remove();
            }
        }
    }
});