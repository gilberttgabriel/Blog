package com.software.spring.service;
import com.software.spring.model.entity.Chat;
import com.software.spring.repository.ChatRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository repo;
    
    public ChatService(ChatRepository repo) {
        this.repo = repo;
    }

    public Chat crearChat(List<String> usuarioIds) {
        if (usuarioIds == null || usuarioIds.size() < 2) {
            throw new IllegalArgumentException("Se requieren al menos 2 usuarios para crear un chat");
        }

        // Verificar si ya existe un chat entre estos usuarios
        List<Chat> chatsUsuario1 = repo.findByUsuarioId(usuarioIds.get(0));
        for (Chat chat : chatsUsuario1) {
            if (chat.getUsuarioIds().containsAll(usuarioIds) && usuarioIds.containsAll(chat.getUsuarioIds())) {
                return chat; // Ya existe un chat entre estos usuarios
            }
        }

        Integer id = Math.abs(UUID.randomUUID().hashCode());
        Chat nuevoChat = new Chat(id, usuarioIds, LocalDateTime.now(), null);
        repo.save(nuevoChat);
        return nuevoChat;
    }

    public Chat verChat(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el chat con ID: " + id));
    }

    public List<Chat> verChatsPorUsuarioId(String usuarioId) {
        return repo.findByUsuarioId(usuarioId);
    }

    public List<Chat> verTodosLosChats() {
        return repo.findAll();
    }

    public void actualizarUltimoMensaje(Integer chatId, String contenidoMensaje) {
        Chat chat = verChat(chatId);
        chat.setUltimoMensaje(contenidoMensaje);
        repo.update(chat);
    }
}
