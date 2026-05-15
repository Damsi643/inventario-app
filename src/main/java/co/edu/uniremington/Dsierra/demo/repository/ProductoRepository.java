package co.edu.uniremington.Dsierra.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.uniremington.Dsierra.demo.modelo.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}