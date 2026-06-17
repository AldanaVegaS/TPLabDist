const socket = io();

// El nombre lo cargó la pantalla de inicio (index.html) en localStorage.
const nombre = localStorage.getItem('nombreJugador');

if (!nombre) {
    // Si entraron directo a juego.html sin elegir nombre, los mandamos al inicio.
    window.location.href = 'index.html';
} else {
    iniciarJuego(nombre);
}

function iniciarJuego(nombre) {
    socket.on('connect', () => {
        console.log('Conectado al servidor con id', socket.id);
        socket.emit('unirse', { nombre });
    });

    const divUsuarios = document.getElementById('usuarios');

    socket.on('jugadores:lista', (lista) => {
        divUsuarios.innerHTML = '<h3>Jugadores</h3>' +
            lista.map(j => `<div>${j.nombre}: ${j.puntos}</div>`).join('');
    });
}
