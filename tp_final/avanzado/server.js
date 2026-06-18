const express = require('express');
const http = require('http');
const path = require('path');
const { Server } = require('socket.io');

const { PORT } = require('./config');
const { palabras } = require('./palabras');

const app = express();
const server = http.createServer(app);
const io = new Server(server);

const jugadores = {};
let temporizador = 60;
let palabra = '';
let dibujanteId = null;
let adivinaron = new Set();

function listaJugadores(){
    return Object.values(jugadores).map(j => ({ nombre: j.nombre, puntos: j.puntos }));
}


function iniciarRonda() {
    const ids = Object.keys(jugadores);
    if (ids.length === 0) {           // nadie conectado no hay ronda
        dibujanteId = null;
        return;
    }

    // rotar el dibujante al siguiente jugador
    const idxActual = ids.indexOf(dibujanteId);
    dibujanteId = ids[(idxActual + 1) % ids.length];

    // elegir palabra al azar y resetear el estado de la ronda
    palabra = palabras[Math.floor(Math.random() * palabras.length)];
    adivinaron.clear();
    temporizador = 60;

    const palabraOculta = palabra.replace(/\S/g, '_ ').trim();
    io.emit('ronda:nueva', {
        dibujanteId,
        dibujanteNombre: jugadores[dibujanteId].nombre,
        palabraOculta
    });

    io.to(dibujanteId).emit('ronda:palabra', { palabra: palabra });
}

app.use(express.static(path.join(__dirname, 'public')));

io.on('connection', (socket) => {
    console.log('Se conecto cliente: ', socket.id);

     socket.on('unirse', ({ nombre }) => {
        jugadores[socket.id] = { nombre, puntos: 0 };
        io.emit('jugadores:lista', listaJugadores());

        if (!dibujanteId) {
            iniciarRonda();
        } else {
            // hay una ronda activa le mandamos el estado actual al jugador
            const palabraOculta = palabra.replace(/\S/g, '_ ').trim();
            socket.emit('ronda:nueva', {
                dibujanteId,
                dibujanteNombre: jugadores[dibujanteId].nombre,
                palabraOculta
            });
        }
    });

    socket.on('chat:enviar', ({texto}) => {
        const jugador = jugadores[socket.id];
        if (!jugador || !texto || !texto.trim()) return;
        
        const intento = texto.trim().toLowerCase();

        if (socket.id !== dibujanteId &&
            intento === palabra.toLowerCase() &&
            !adivinaron.has(socket.id)) {

            adivinaron.add(socket.id);

            const transcurrido = 60 - temporizador;
            const puntos = transcurrido <= 20 ? 100 : 70;
            jugador.puntos += puntos;

            io.emit('chat:sistema', {
                texto: `${jugador.nombre} ha acertado la palabra (+${puntos} pts)`
            });

            io.emit('jugadores:lista', listaJugadores());
            return;
        }

        io.emit('chat:nuevo', { nombre: jugador.nombre, texto: texto.trim() });
    })

    socket.on('draw', (linea) => {
        console.log('servidor recibió draw:', linea);
        socket.broadcast.emit('draw', linea);
    });

    socket.on('disconnect', () => {
        console.log('Se desconecto cliente: ', socket.id);
        const eraDibujante = (socket.id === dibujanteId);
        delete jugadores[socket.id];
        io.emit('jugadores:lista', listaJugadores());

         if (eraDibujante) {
            dibujanteId = null;
            iniciarRonda();
        }
    })
})

/// reloj
setInterval(() => {

    if (!dibujanteId) return;

    io.emit('actualizar-hora', { temporizador });
    temporizador--;
    if (temporizador < 0){
        iniciarRonda();
    }
}, 1000);


server.listen(PORT, () => {
    console.log(`Servidor del juego corriendo en http://localhost:${PORT}`);
});