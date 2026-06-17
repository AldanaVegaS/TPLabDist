const canvas = document.getElementById("lienzo");
const rangeInput = document.getElementById("grosor");
const ctx = canvas.getContext("2d");

let coord = { x: 0, y: 0 };
let drawColor = "black";
let lineWidth = 3;
let isDrawing = false;
let erasing = false;
let tool = "pencil"


document.addEventListener("mousedown", start);
document.addEventListener("mouseup", stop);
document.addEventListener("mousemove", draw);
rangeInput.addEventListener("input",grosorLinea);


function start(event) {
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

function draw(event) {
  if(!isDrawing) return
  ctx.beginPath();
  ctx.lineCap = "round";

  if(erasing){
    ctx.strokeStyle = "white"; 
    ctx.lineWidth = 20;        
  }else{
    ctx.strokeStyle=drawColor;
    ctx.lineWidth=lineWidth;
  }

  const fromX = coord.x; // guardá la posición anterior
  const fromY = coord.y;
  ctx.moveTo(coord.x, coord.y);
  reposition(event);
  ctx.lineTo(coord.x, coord.y);
  ctx.stroke();

  socket.emit('draw', {
    fromX,
    fromY,
    toX: coord.x,
    toY: coord.y,
    color: drawColor,
    width: lineWidth
  });
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
    tool="eraser"
    document.getElementById('eraser').classList.add('active');
    document.getElementById('pencil').classList.remove('active');
}

function pencil() {
    erasing=false
    tool="pencil"
    document.getElementById('pencil').classList.add('active');
    document.getElementById('eraser').classList.remove('active');
}

 function grosorLinea() {
  if (tool === "eraser") return;
  lineWidth = this.value;
}

socket.on("draw", data => {
  //console.log('draw recibido', data);
    ctx.beginPath();
    ctx.lineCap = "round";

    ctx.strokeStyle = data.color;
    ctx.lineWidth = data.width;

    ctx.moveTo(data.fromX, data.fromY);
    ctx.lineTo(data.toX, data.toY);

    ctx.stroke();
});