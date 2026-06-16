// conexión al servidor de WebSockets (mismo origen).
const socket = io();

const bloques = Array.from(document.querySelectorAll('.anuncio')).map(article => ({
    article,
    titulo: article.querySelector('h2'),
    contenido: article.querySelector('p')
}));

// agrega un anuncia a un bloque
function actualizarBloqueAnuncio(indice, anuncio) {
    const bloque = bloques[indice];
    if (!bloque) return;

    bloque.titulo.innerText = anuncio.titulo;
    bloque.contenido.innerText = anuncio.contenido;

    bloque.article.classList.remove('actualizado');
    void bloque.article.offsetWidth;
    bloque.article.classList.add('actualizado');
}

const reloj = document.getElementById('hora');
// actualiza hora

function actualizarHora(hora) {
    if (!reloj) return;

    reloj.innerText = hora;
}

// el servidor manda un anuncio por bloque al conectarse
socket.on('estado-inicial', (anuncios) => {
    anuncios.forEach((anuncio, indice) => actualizarBloqueAnuncio(indice, anuncio));
});

// convierte un evento del socket en un flujo (Observable) de RxJS
const observarEvento = (nombre) => new rxjs.Observable(subscritor => {
    socket.on(nombre, (datos) => subscritor.next(datos));
    return () => socket.off(nombre);
});

// actualizaciones de bloque que empuja el servidor
const actualizaciones$ = observarEvento('actualizar-bloque');

// el observer reacciona y actualiza el bloque correspondiente.
actualizaciones$.subscribe({
    next: ({ indice, anuncio }) => {
        console.log(`Bloque ${indice} actualizado:`, anuncio);
        actualizarBloqueAnuncio(indice, anuncio);
    },
    error: (err) => console.error('Error en el flujo:', err),
    complete: () => console.log('Flujo terminado')
});

// actualizacion de hora que empuja el servidor
const actualizacionHora$ = observarEvento('actualizar-hora');

// el observer reacciona y actualiza la hora correspondiente.
actualizacionHora$.subscribe({
    next: ({ hora }) => {
        console.log(`Hora actualizada:`, hora);
        actualizarHora(hora);
    },
    error: (err) => console.error('Error en el flujo:', err),
    complete: () => console.log('Flujo terminado')
});
