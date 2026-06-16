// Datos de los anuncios, separados de la lógica del servidor.

const anuncios = [
    { 
        titulo: "Alerta de Café", 
        contenido: "El café de la cocina se está enfriando. Por favor, pase a reclamar su dosis de cafeína antes de que se vuelva código incomprensible." 
    },
    { 
        titulo: "Cyber-Oferta", 
        contenido: "Últimas 2 horas: 70% de descuento en suscripciones Premium!" 
    },
    { 
        titulo: "Martes de Pizza", 
        contenido: "Pedí una grande de muzzarella y la segunda te queda a mitad de precio. Código de cupón: PIZZA_YA." 
    },
    { 
        titulo: "Próximo Lanzamiento", 
        contenido: "El RPG más esperado del año llega este viernes. Pre-compralo ya y obtené una skin exclusiva." 
    },
    { 
        titulo: "Al rincón de pensar", 
        contenido: "Si algo no anda bien, despegate de la pantalla 5 minutos." 
    },
    { 
        titulo: "Viajá Hoy", 
        contenido: "Vuelos de última hora hacia el Caribe con 12 cuotas sin interés. Tu cuerpo pide playa!." 
    },
    { 
        titulo: "Búsqueda Laboral", 
        contenido: "Buscamos desarrollador Fullstack que sepa Python, FasAPI, cocina internacional y astrofísica. Sueldo: competitivo (a discutir)." 
    }
];

function anuncioAleatorio() {
    return anuncios[Math.floor(Math.random() * anuncios.length)];
}

module.exports = { anuncios, anuncioAleatorio };
