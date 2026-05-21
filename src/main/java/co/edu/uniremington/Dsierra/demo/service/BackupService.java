package co.edu.uniremington.Dsierra.demo.service;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupService {

    private static final Logger log = LoggerFactory.getLogger(BackupService.class);
    
    // 👈 CAMBIA EL NOMBRE AQUÍ
private static final String JSON_FILE_PATH = "backup/usuarios.json";  // Guarda en carpeta backup
    
    private final ObjectMapper objectMapper;

    public BackupService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void guardarUsuarioEnJson(Usuario usuario) {
        try {
            List<Usuario> usuarios = leerTodosLosUsuarios();
            boolean existe = usuarios.stream().anyMatch(u -> u.getId().equals(usuario.getId()));
            if (!existe) {
                usuarios.add(usuario);
            }
            objectMapper.writeValue(new File(JSON_FILE_PATH), usuarios);
            log.info("✅ Usuario guardado en JSON: {}", usuario.getCorreo());
        } catch (IOException e) {
            log.error("❌ Error al guardar usuario en JSON: {}", e.getMessage());
        }
    }

    public List<Usuario> leerTodosLosUsuarios() {
        try {
            File file = new File(JSON_FILE_PATH);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<List<Usuario>>() {});
            }
        } catch (IOException e) {
            log.error("❌ Error al leer usuarios de JSON: {}", e.getMessage());
        }
        return new ArrayList<>();
    }

    public Usuario buscarPorCorreoEnJson(String correo) {
        return leerTodosLosUsuarios().stream()
                .filter(u -> u.getCorreo().equals(correo))
                .findFirst()
                .orElse(null);
    }

    public long contarUsuariosEnJson() {
        return leerTodosLosUsuarios().size();
    }
    
    public void guardarTodosUsuariosEnJson(List<Usuario> usuarios) {
        try {
            objectMapper.writeValue(new File(JSON_FILE_PATH), usuarios);
            log.info("✅ Todos los usuarios guardados en JSON: {} usuarios", usuarios.size());
        } catch (IOException e) {
            log.error("❌ Error al guardar todos los usuarios en JSON: {}", e.getMessage());
        }
    }
}