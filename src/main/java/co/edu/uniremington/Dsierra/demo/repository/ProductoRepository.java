package co.edu.uniremington.Dsierra.demo.repository;

import co.edu.uniremington.Dsierra.demo.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Métodos personalizados si los necesitas
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}