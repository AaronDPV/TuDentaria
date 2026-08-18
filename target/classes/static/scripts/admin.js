/**
 * TUDENTARIA - SCRIPT DE INTERACCIÓN Y RESPONSIVIDAD DEL PANEL ADMINISTRATIVO
 */

document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.querySelector('.sidebar');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebarClose = document.getElementById('sidebarClose');
    const backdrop = document.querySelector('.sidebar-backdrop');

    function openSidebar() {
        if (sidebar) sidebar.classList.add('show');
        if (backdrop) backdrop.classList.add('show');
        document.body.style.overflow = 'hidden';
    }

    function closeSidebar() {
        if (sidebar) sidebar.classList.remove('show');
        if (backdrop) backdrop.classList.remove('show');
        document.body.style.overflow = '';
    }

    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function (e) {
            e.preventDefault();
            if (sidebar && sidebar.classList.contains('show')) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });
    }

    if (sidebarClose) {
        sidebarClose.addEventListener('click', function (e) {
            e.preventDefault();
            closeSidebar();
        });
    }

    if (backdrop) {
        backdrop.addEventListener('click', function () {
            closeSidebar();
        });
    }

    // Cerrar sidebar al presionar Escape
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && sidebar && sidebar.classList.contains('show')) {
            closeSidebar();
        }
    });

    // Resetear overflow en resize a pantalla grande
    window.addEventListener('resize', function () {
        if (window.innerWidth >= 992) {
            if (sidebar) sidebar.classList.remove('show');
            if (backdrop) backdrop.classList.remove('show');
            document.body.style.overflow = '';
        }
    });
});
