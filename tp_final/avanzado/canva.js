const canvas = document.getElementById("lienzo");
const ctx = canvas.getContext("2d");
let coord = { x: 0, y: 0 };

document.addEventListener("mousedown", start);
document.addEventListener("mouseup", stop);

function start(event) {
  document.addEventListener("mousemove", draw);
  reposition(event);
}

function reposition(event) {
  coord.x = event.clientX - canvas.offsetLeft;
  coord.y = event.clientY - canvas.offsetTop;
}

function stop() {
  document.removeEventListener("mousemove", draw);
}

function draw(event) {
  ctx.beginPath();
  ctx.lineCap = "round";
  ctx.lineWidth = 3;
  ctx.moveTo(coord.x, coord.y);
  reposition(event);
  ctx.lineTo(coord.x, coord.y);
  ctx.stroke();
}

function changeColor(color){
    ctx.strokeStyle=color;
}

function erase(color){
    ctx.strokeStyle=color;
    ctx.linewidth=10;
}
