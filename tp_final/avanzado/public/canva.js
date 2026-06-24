const canvas = document.getElementById("lienzo");
const rangeInput = document.getElementById("grosor");
const ctx = canvas.getContext("2d");

let coord = { x: 0, y: 0 };
let drawColor = "black";
let lineWidth = 3;
let isDrawing = false;
let erasing = false;
let puedoDibujar = false;

document.addEventListener("mousedown", start);
document.addEventListener("mouseup", stop);
document.addEventListener("mousemove", draw);
rangeInput.addEventListener("input",grosorLinea);


function start(event) {
  if (!puedoDibujar) return;

  isDrawing = true;
  reposition(event);
}

function reposition(event) {
  coord.x = event.clientX - canvas.offsetLeft;
  coord.y = event.clientY - canvas.offsetTop;
}

function stop() {
  isDrawing=false
}

function dibujarTrazo({ fromX, fromY, toX, toY, color, width }) {
  ctx.beginPath();
  ctx.lineCap = "round";
  ctx.strokeStyle = color;
  ctx.lineWidth = width;
  ctx.moveTo(fromX, fromY);
  ctx.lineTo(toX, toY);
  ctx.stroke();
}

function draw(event) {
  if(!isDrawing) return

  const color = erasing ? "white" : drawColor;
  const width = erasing ? 20 : lineWidth;
  
  ctx.beginPath();
  ctx.lineCap = "round";
  ctx.strokeStyle = color;
  ctx.lineWidth = width;
  const fromX = coord.x; // guardá la posición anterior
  const fromY = coord.y;
  ctx.moveTo(coord.x, coord.y);
  reposition(event);
  const toX = coord.x;
  const toY = coord.y;
  ctx.lineTo(coord.x, coord.y);
  ctx.stroke();
  
  //Se envia el dibujo del jugador al servidor
  const trazo = { fromX, fromY, toX, toY, color, width };
  dibujarTrazo(trazo);
  socket.emit('draw', trazo);
  //console.log('emit draw enviado');
}

function changeColor(color){
  if(erasing) return ;
  drawColor=color;
  document.querySelectorAll('.circulo').forEach(c => c.classList.remove('active'));
  event.currentTarget.classList.add('active');
}

function erase() {
    erasing=true
    document.getElementById('eraser').classList.add('active');
    document.getElementById('pencil').classList.remove('active');
}

function pencil() {
    erasing=false
    document.getElementById('pencil').classList.add('active');
    document.getElementById('eraser').classList.remove('active');
}

 function grosorLinea() {
  if (erasing) return;
  lineWidth = this.value;
}

socket.on('canva:sincronizar',(trazos)=>{
        trazos.forEach(trazo=>{
            dibujarTrazo(trazo);
        })
    })

socket.on("draw", dibujarTrazo);

socket.on("canvas:limpiar", () => {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
});

socket.on("ronda:nueva", ({ dibujanteId }) => {
  puedoDibujar = (socket.id === dibujanteId);
  console.log("NUEVA RONDA: "+puedoDibujar+", "+socket.id+", "+dibujanteId)
});

