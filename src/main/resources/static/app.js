const API_URL = '/api/incidencias';

const selectFiltro = document.getElementById('filtro-estado');
const tabla = document.getElementById('tabla-incidencias');
const mensaje = document.getElementById('mensaje');

function mostrarMensaje(texto) {
    mensaje.textContent = texto;
    mensaje.hidden = false;
}

function ocultarMensaje() {
    mensaje.hidden = true;
}

function crearCelda(texto) {
    const td = document.createElement('td');
    td.textContent = texto;
    return td;
}

function pintarIncidencias(incidencias) {
    tabla.replaceChildren();

    if (incidencias.length === 0) {
        const fila = document.createElement('tr');
        const celda = crearCelda('No hay incidencias para mostrar');
        celda.colSpan = 6;
        fila.appendChild(celda);
        tabla.appendChild(fila);
        return;
    }

    for (const i of incidencias) {
        const fila = document.createElement('tr');
        fila.appendChild(crearCelda(i.id));
        fila.appendChild(crearCelda(i.titulo));
        fila.appendChild(crearCelda(i.prioridad));
        fila.appendChild(crearCelda(i.estado));
        fila.appendChild(crearCelda(i.usuarioNombre ?? 'Sin asignar'));
        fila.appendChild(crearCelda(new Date(i.fechaCreacion).toLocaleString('es-ES')));
        tabla.appendChild(fila);
    }
}

async function cargarIncidencias() {
    const estado = selectFiltro.value;
    const url = estado ? `${API_URL}?estado=${encodeURIComponent(estado)}` : API_URL;

    try {
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error(`La API respondió con el código ${respuesta.status}`);
        }
        const incidencias = await respuesta.json();
        ocultarMensaje();
        pintarIncidencias(incidencias);
    } catch (error) {
        tabla.replaceChildren();
        mostrarMensaje('No se pudieron cargar las incidencias. Inténtalo de nuevo más tarde.');
        console.error(error);
    }
}

selectFiltro.addEventListener('change', cargarIncidencias);
cargarIncidencias();