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
let numeroRonda = 1;
let primerDibujante = null;
const MAX_RONDAS = 3;
let juegoTerminado = false;
let PAUSA_TURNO = 5; 
const MIN_JUGADORES = 2;
let trazos = []; //guarda los trazos de la ronda actual

function listaJugadores(){
    return Object.entries(jugadores).map(([id, j]) => ({
        nombre: j.nombre,
        puntos: j.puntos,
        esDibujante: id === dibujanteId
    }));
}


function iniciarRonda() {
    const ids = Object.keys(jugadores);
    if (ids.length === 0) {           // nadie conectado no hay ronda
        dibujanteId = null;
        primerDibujante = null;
        return;
    }
    trazos = [];
    
    // rotar el dibujante al siguiente jugador
    const idxActual = ids.indexOf(dibujanteId);
    dibujanteId = ids[(idxActual + 1) % ids.length];

    if(primerDibujante === null){
        primerDibujante = dibujanteId;
    }else if (dibujanteId === primerDibujante){
        numeroRonda++;
        if (numeroRonda > MAX_RONDAS) {
            terminarJuego();
            return; 
        }
    }

    // elegir palabra al azar y resetear el estado de la ronda
    palabra = palabras[Math.floor(Math.random() * palabras.length)];
    adivinaron.clear();
    temporizador = 60;
    juegoTerminado = false;

    const palabraOculta = palabra.replace(/\S/g, '_ ').trim();
    io.emit('ronda:nueva', {
        dibujanteId,
        dibujanteNombre: jugadores[dibujanteId].nombre,
        palabraOculta,
        numeroRonda
    });

    io.emit('chat:sistema', {
        texto: `¡${jugadores[dibujanteId].nombre} comienza a dibujar!`
    })

    // actualizar el listado para resaltar al nuevo dibujante
    io.emit('jugadores:lista', listaJugadores());

    io.to(dibujanteId).emit('ronda:palabra', { palabra: palabra });
    
    io.emit('canvas:limpiar');
}

function terminarJuego() {
    juegoTerminado = true;
    dibujanteId = null;

    const ranking = listaJugadores().sort((a, b) => b.puntos - a.puntos);
    io.emit('juego:terminado', { ranking });
}

function reiniciarJuego() {
    dibujanteId = null;
    primerDibujante = null;
    numeroRonda = 1;
    temporizador = 60;
    palabra = '';
    adivinaron.clear();
    juegoTerminado = false;

    // los puntos arrancan de cero en cada
    for (const id in jugadores) {
        jugadores[id].puntos = 0;
    }
}

app.use(express.static(path.join(__dirname, 'public')));

io.on('connection', (socket) => {
    console.log('Se conecto cliente: ', socket.id);

     socket.on('unirse', ({ nombre }) => {
        jugadores[socket.id] = { nombre, puntos: 0 };
        io.emit('jugadores:lista', listaJugadores());

        const cantidadJugadores = Object.keys(jugadores).length;

        if (!dibujanteId && cantidadJugadores>=MIN_JUGADORES) {
            reiniciarJuego();
            iniciarRonda();
        } else if (!dibujanteId) {
            io.emit('esperando:jugadores', {
                actuales: cantidadJugadores,
                necesarios: MIN_JUGADORES
            });
        } else {
            // hay una ronda activa le mandamos el estado actual al jugador
            const palabraOculta = palabra.replace(/\S/g, '_ ').trim();
            socket.emit('ronda:nueva', {
                dibujanteId,
                dibujanteNombre: jugadores[dibujanteId].nombre,
                palabraOculta,
                numeroRonda
            });
            socket.emit('canva:sincronizar', trazos)
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
        if(socket.id != dibujanteId) return;
        trazos.push(linea);
        socket.broadcast.emit('draw', linea);
    });

    socket.on('disconnect', () => {
        console.log('Se desconecto cliente: ', socket.id);
        const eraDibujante = (socket.id === dibujanteId);
        delete jugadores[socket.id];
        io.emit('jugadores:lista', listaJugadores());

        const cantidadJugadores = Object.keys(jugadores).length;

        if (cantidadJugadores < MIN_JUGADORES) {
            dibujanteId = null;
            terminarJuego();
            return;
        }

        if (socket.id === primerDibujante)
            primerDibujante = null;

        if (eraDibujante) {
            dibujanteId = null;
            iniciarRonda();
        }
    })
})

/// reloj
setInterval(() => {

    if (!dibujanteId || juegoTerminado) return;

    io.emit('actualizar-hora', { temporizador });
    temporizador--;
    if (temporizador < 0){
        iniciarRonda();
    }
}, 1000);


server.listen(PORT, () => {
    console.log(`Servidor del juego corriendo en http://localhost:${PORT}`);
});