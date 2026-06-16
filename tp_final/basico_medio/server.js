const express = require('express');
const http = require('http');
const path = require('path');
const { Server } = require('socket.io');

const { PORT, CANTIDAD_BLOQUES, INTERVALO_ACTUALIZACION_MS, INTERVALO_RELOJ_MS } = require('./config');
const { anuncioAleatorio } = require('./anuncios');

const app = express();
const server = http.createServer(app);
const io = new Server(server);


app.use(express.static(path.join(__dirname, 'public')));

io.on('connection', (socket) => {
    console.log('Un cliente se ha conectado: ' + socket.id);

    // estado inicial: un anuncio para cada uno de los bloques
    const estadoInicial = Array.from({ length: CANTIDAD_BLOQUES }, () => anuncioAleatorio());
    socket.emit('estado-inicial', estadoInicial);
});

// cada cierto intervalo el servidor empuja un anuncio nuevo a uno de los bloques, rotando el índice para que los bloques se vayan actualizando de a uno.
let indiceBloque = 0;
setInterval(() => {
    io.emit('actualizar-bloque', { indice: indiceBloque, anuncio: anuncioAleatorio() });
    indiceBloque = (indiceBloque + 1) % CANTIDAD_BLOQUES;
}, INTERVALO_ACTUALIZACION_MS);

// cada un segundo el servidor actualiza la hora
setInterval(() => {
    io.emit('actualizar-hora', { hora: new Date().toLocaleTimeString('es-AR', { hour12: false }) });
}, INTERVALO_RELOJ_MS);

server.listen(PORT, () => {
    console.log(`Servidor de anuncios corriendo en http://localhost:${PORT}`);
});
