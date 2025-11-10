package com.software.spring.service;
import com.software.spring.model.entity.Mensaje;
import com.software.spring.repository.MensajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MensajeService {

    private final MensajeRepository repo;
    private final ChatService chatService;
    
    public MensajeService(MensajeRepository repo, ChatService chatService) {
        this.repo = repo;
        this.chatService = chatService;
    }

    public Mensaje enviarMensaje(Mensaje mensaje) {
        if (!StringUtils.hasText(mensaje.getContenido())) {
            throw new IllegalArgumentException("El contenido del mensaje es obligatorio");
        }
        
        if (mensaje.getAutorId() == null || mensaje.getChatId() == null) {
            throw new IllegalArgumentException("Se requiere autorId y chatId");
        }

        Integer id = Math.abs(UUID.randomUUID().hashCode());
        Mensaje nuevoMensaje = new Mensaje(
            mensaje.getContenido(),
            mensaje.getAutorId(),
            LocalDateTime.now(),
            id,
            mensaje.getChatId()
        );
        
        repo.save(nuevoMensaje);
        
        // Actualizar el último mensaje del chat
        chatService.actualizarUltimoMensaje(mensaje.getChatId(), mensaje.getContenido());
        
        return nuevoMensaje;
    }

    public List<Mensaje> verMensajesPorChatId(Integer chatId) {
        return repo.findByChatId(chatId);
    }

    public Mensaje verMensaje(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el mensaje con ID: " + id));
    }

    public List<Mensaje> verTodosLosMensajes() {
        return repo.findAll();
    }
}
