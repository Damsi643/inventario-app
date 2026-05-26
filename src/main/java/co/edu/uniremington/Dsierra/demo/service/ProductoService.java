package co.edu.uniremington.Dsierra.demo.service;

import co.edu.uniremington.Dsierra.demo.modelo.Producto;
import co.edu.uniremington.Dsierra.demo.repository.ProductoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
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
    @Transactional
    public Producto guardar(Producto producto) {
        logger.info("ProductoService.guardar() llamado - nombre: {}", producto.getNombre());
        try {
            Producto resultado = productos.save(producto);
            logger.info("ProductoService.guardar() EXITOSO - ID: {}", resultado.getId());
            return resultado;
        } catch (Exception e) {
            logger.error("ProductoService.guardar() falló - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    // ACTUALIZAR
    @Transactional
    public Producto actualizar(Long id, Producto nuevoProducto) {

        Producto producto = productos.findById(id).orElse(null);

        if (producto == null) {
            return null;
        }

        producto.setNombre(nuevoProducto.getNombre());
        producto.setPrecio(nuevoProducto.getPrecio());
        producto.setMarca(nuevoProducto.getMarca());
        producto.setDescripcion(nuevoProducto.getDescripcion());
        producto.setStock(nuevoProducto.getStock());
        if (nuevoProducto.getFoto() != null) {
            producto.setFoto(nuevoProducto.getFoto());
        }

        return productos.save(producto);
    }

    // ELIMINAR
    @Transactional
    public boolean eliminar(Long id) {

        Producto producto = productos.findById(id).orElse(null);

        if (producto == null) {
            return false;
        }

        productos.deleteById(id);

        return true;
    }
}