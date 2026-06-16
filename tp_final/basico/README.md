# TP Final — Nivel Básico: Bloque de anuncios en tiempo real

Aplicación con comunicación bidireccional cliente–servidor donde el **servidor empuja anuncios al navegador** sin que el cliente tenga que refrescar la página. La pantalla muestra un título **"Anuncios"** y una grilla de **4 bloques** ; el servidor va actualizando los bloques de a uno, en forma rotativa, cada 3
segundos.

## Tecnologías

- **Back-end:** Node.js + [Express](https://expressjs.com/) (sirve el front-end) +
  [Socket.IO](https://socket.io/) (canal WebSocket bidireccional).
- **Front-end:** HTML + JavaScript en el navegador, usando
  [RxJS](https://rxjs.dev/) para tratar los eventos del socket como un flujo reactivo.

## Cómo funciona

1. El cliente abre la página servida por Express y se conecta al servidor con `io()`.
2. Al conectarse, el servidor envía el estado inicial con un anuncio para cada bloque
   (`socket.emit('estado-inicial', [...])`).
3. Los 4 bloques están definidos directamente en `index.html`; `client.js` solo guarda
   las referencias a sus elementos y los pinta con los anuncios recibidos.
4. Cada cierto intervalo el servidor elige un anuncio al azar y lo **empuja a todos** los
   clientes hacia un bloque puntual.
5. En el cliente, las actualizaciones se transforman en un `Observable` de RxJS y el
   `subscribe` repinta el bloque correspondiente.

## Requisitos

- [Node.js](https://nodejs.org/) (incluye `npm`).

## Instalación

Desde la carpeta `basico/`:

```bash
npm install
```

## Ejecución

```bash
npm start
```

Luego abrir en el navegador:

```
http://localhost:3000
```

Los bloques se irán actualizando solos. Para verlo en acción con "push" a múltiples clientes, abrir la misma URL en varias pestañas: todas cambian a la vez.

Para detener el servidor: `Ctrl + C` en la terminal.
