const contenedor = document.getElementById("contenedor-productos");

fetch("/api/productos")

.then(response => response.json())

.then(productos => {

    contenedor.innerHTML = "";

    productos.forEach(producto => {

        contenedor.innerHTML += `

        <div class="col-lg-3 col-md-6">

            <div class="product-card">

                <img src="${producto.foto || 'https://via.placeholder.com/200'}" alt="">

                <div class="product-info">

                    <h4>${producto.nombre}</h4>

                    <p class="category">${producto.marca}</p>

                    <div class="price-box">
                        <span class="price">
                            $${producto.precio}
                        </span>
                    </div>

                    <button class="cart-btn">

                        <i class="bi bi-cart-plus"></i>
                        Agregar

                    </button>

                </div>

            </div>

        </div>

        `;

    });

})

.catch(error => console.log(error));