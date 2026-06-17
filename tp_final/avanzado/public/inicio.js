const form = document.getElementById('form-inicio');
const input = document.getElementById('input-nombre');

form.addEventListener('submit', (e) => {
    e.preventDefault();
    const nombre = input.value.trim();
    if (!nombre) return;

    // Guardamos el nombre para que la pantalla del juego (client.js) lo use.
    localStorage.setItem('nombreJugador', nombre);

    // Pasamos a la pantalla del juego.
    window.location.href = 'juego.html';
});
