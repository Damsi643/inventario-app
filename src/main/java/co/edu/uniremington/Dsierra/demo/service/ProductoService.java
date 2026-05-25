package co.edu.uniremington.Dsierra.demo.service;

import co.edu.uniremington.Dsierra.demo.modelo.Producto;
import co.edu.uniremington.Dsierra.demo.repository.ProductoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productos;

    public ProductoService(ProductoRepository productos) {
        this.productos = productos;
    }

    // LISTAR
    public List<Producto> listarTodo() {
        return productos.findAll();
    }

    // BUSCAR POR ID
    public Producto buscarPorId(Long id) {

        Optional<Producto> producto = productos.findById(id);

        return producto.orElse(null);
    }

    // CREAR
    public Producto guardar(Producto producto) {
        return productos.save(producto);
    }

    // ACTUALIZAR
    public Producto actualizar(Long id, Producto nuevoProducto) {

        Producto producto = productos.findById(id).orElse(null);

        if (producto == null) {
            return null;
        }

        producto.setNombre(nuevoProducto.getNombre());
        producto.setValor(nuevoProducto.getValor());
        if (nuevoProducto.getFoto() != null) {
            producto.setFoto(nuevoProducto.getFoto());
        }

        return productos.save(producto);
    }

    // ELIMINAR
    public boolean eliminar(Long id) {

        Producto producto = productos.findById(id).orElse(null);

        if (producto == null) {
            return false;
        }

        productos.deleteById(id);

        return true;
    }
}