function inicializarChat(socket){
    const form = document.getElementById('form-chat');
    const input = document.getElementById('input-chat');
    const divMensajes = document.getElementById('mjs');

    // se envio el mensaje del jugador al servidor.
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const texto = input.value.trim();
        if (!texto) return;
        socket.emit('chat:enviar', { texto });
        input.value = '';
    })

    socket.on('chat:nuevo', ({ nombre, texto }) => {
        agregarMensaje(divMensajes, `${nombre}: ${texto}`);
    });

    socket.on('chat:sistema', ({ texto }) => {
        agregarMensaje(divMensajes, texto, 'msj-sistema');
    });

    function agregarMensaje(divMensajes, texto, claseExtra) {
        const div = document.createElement('div');
        div.classList.add('msj');
        if (claseExtra) div.classList.add(claseExtra);
        div.textContent = texto;
        divMensajes.appendChild(div);
        divMensajes.scrollTop = divMensajes.scrollHeight;
    }
}