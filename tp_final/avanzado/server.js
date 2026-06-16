const express = require('express');
const http = require('http');
const path = require('path');
const { Server } = require('socket.io');


const app = express();
const server = http.createServer(app);
const io = new Server(server);

app.use(express.static(path.join(__dirname, 'public')));

server.listen(PORT, () => {
    console.log(`Servidor de anuncios corriendo en http://localhost:${PORT}`);
});