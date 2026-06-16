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

// el servidor manda un anuncio por bloque al conectarse
socket.on('estado-inicial', (anuncios) => {
    anuncios.forEach((anuncio, indice) => actualizarBloqueAnuncio(indice, anuncio));
});

// actualizaciones de bloque que empuja el servidor
const actualizaciones$ = new rxjs.Observable(subscritor => {
    socket.on('actualizar-bloque', (datos) => subscritor.next(datos));
    return () => socket.off('actualizar-bloque');
});

// el observer reacciona y repinta el bloque correspondiente.
actualizaciones$.subscribe({
    next: ({ indice, anuncio }) => {
        console.log(`Bloque ${indice} actualizado:`, anuncio);
        actualizarBloqueAnuncio(indice, anuncio);
    },
    error: (err) => console.error('Error en el flujo:', err),
    complete: () => console.log('Flujo terminado')
});
