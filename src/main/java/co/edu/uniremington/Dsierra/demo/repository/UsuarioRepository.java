package co.edu.uniremington.Dsierra.demo.repository;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    Usuario findByCorreo(String correo);

}