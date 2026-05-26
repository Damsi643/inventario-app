document.addEventListener("DOMContentLoaded", () => {
  cargarProductos();
});

function cargarProductos() {
  fetch("/api/productos")
    .then(res => res.json())
    .then(data => {
      console.log("Productos:", data);
    })
    .catch(error => {
      console.error("Error conectando API:", error);
    });
}