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

    socket.on('esperando:jugadores', ({ actuales, necesarios }) => {
        canvas.style.pointerEvents = 'none';
        document.querySelector('.contenedor-herramientas').style.pointerEvents = 'none';

        document.getElementById('texto-esperando').textContent =
            `${actuales}/${necesarios} jugadores conectados`;
        document.getElementById('overlay-esperando').style.display = 'flex';
    });

    const divUsuarios = document.getElementById('lista-jugadores');

    socket.on('jugadores:lista', (lista) => {
        divUsuarios.innerHTML = '<h3>Jugadores</h3>' +
            lista.map(j => `
                <div class="jugador ${j.esDibujante ? 'dibujante' : ''}">
                    <div class="avatar"><i class="fas fa-user"></i></div>
                    <div class="info-jugador">
                        <span class="nombre-jugador">${j.nombre}</span>
                        <span class="puntos-jugador">${j.puntos} pts</span>
                    </div>
                </div>`).join('');
    });

    const tiempoEl = document.getElementById('tiempo');

    socket.on('actualizar-hora', ({ temporizador }) => {
        tiempoEl.textContent = temporizador;
    });

    const palabraEl = document.getElementById('texto-palabra');
    const nroRondaEl = document.getElementById('numero-ronda')
    const audioRonda = document.getElementById('audio-ronda');

    socket.on('ronda:nueva', ({ dibujanteId, palabraOculta, numeroRonda }) => {
        document.getElementById('overlay-esperando').style.display = 'none';

        // sonido al comenzar un nuevo dibujante
        audioRonda.currentTime = 0;
        audioRonda.play().catch(() => {});

        const dibujante = socket.id === dibujanteId;
        canvas.style.pointerEvents = dibujante ? 'auto' : 'none';
        document.querySelector('.contenedor-herramientas').style.pointerEvents = dibujante ? 'auto' : 'none';

        if (!dibujante) {
            palabraEl.textContent = palabraOculta;
        }
        nroRondaEl.textContent = numeroRonda+"/3";
    }); 

    socket.on('ronda:palabra', ({ palabra }) => {
        palabraEl.textContent = palabra;
    });

    inicializarChat(socket);

    socket.on('juego:terminado', ({ ranking }) => {
        canvas.style.pointerEvents = 'none';
        document.querySelector('.contenedor-herramientas').style.pointerEvents = 'none';
        const listaRanking = document.getElementById('lista-ranking');

        listaRanking.innerHTML = ranking.map((j, i) => `
            <div class="fila-ranking">
                <span class="puesto">${i + 1}°</span>
                <span class="nombre">${j.nombre}</span>
                <span class="puntos">${j.puntos} pts</span>
            </div>
        `).join('');

        document.getElementById('overlay-ranking').style.display = 'flex'
        });

}

function volverInicio() {
    localStorage.removeItem('nombreJugador');
    window.location.href = 'index.html';
}
