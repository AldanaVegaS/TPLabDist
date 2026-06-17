const express = require('express');
const http = require('http');
const path = require('path');
const { Server } = require('socket.io');

const { PORT } = require('./config');

const app = express();
const server = http.createServer(app);
const io = new Server(server);

const jugadores = {};

function listaJugadores(){
    return Object.values(jugadores).map(j => ({ nombre: j.nombre, puntos: j.puntos }));

}

app.use(express.static(path.join(__dirname, 'public')));

io.on('connection', (socket) => {
    console.log('Se conecto cliente: ', socket.id);

    socket.on('unirse', ({ nombre }) => {
        jugadores[socket.id] = { nombre, puntos: 0 };
        io.emit('jugadores:lista', listaJugadores());
    });

    socket.on('disconnect', () => {
        console.log('Se desconecto cliente: ', socket.id);
        delete jugadores[socket.id];
        io.emit('jugadores:lista', listaJugadores());
    })
})


server.listen(PORT, () => {
    console.log(`Servidor del juego corriendo en http://localhost:${PORT}`);
});