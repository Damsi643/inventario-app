document.addEventListener("DOMContentLoaded", () => {
  cargarProductos();
});

function cargarProductos() {
  fetch("http://localhost:8080/api/productos")
    .then(res => res.json())
    .then(data => {
      console.log("Productos:", data);
    })
    .catch(error => {
      console.error("Error conectando API:", error);
    });
}