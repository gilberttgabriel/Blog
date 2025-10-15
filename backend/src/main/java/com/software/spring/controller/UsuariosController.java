import com.software.spring.model.entity.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {
    private final UsuarioService usuarioService;
    public UsuariosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @DeleteMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(201).body(creado);
    }
}
