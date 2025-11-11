<template>
    <div class="chat-page">
    <div class="chat-header">
      <button @click="volverAChats" class="btn-back" data-tooltip="Volver a chats" data-tooltip-pos="bottom">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
      <h2>{{ nombreOtroUsuario }}</h2>
    </div>
  
      <div class="messages-container" ref="messagesContainer">
        <div v-if="loading" class="loading">
          Cargando mensajes...
        </div>
  
        <div v-else class="messages-list">
          <div v-if="mensajes.length === 0" class="empty-messages">
            <p>No hay mensajes aún. ¡Envía el primero!</p>
          </div>
  
          <div 
            v-for="mensaje in mensajes" 
            :key="mensaje.id" 
            :class="['mensaje', mensaje.autorId === usuarioActualId ? 'mensaje-propio' : 'mensaje-otro']"
          >
            <div class="mensaje-contenido">
              <p>{{ mensaje.contenido }}</p>
              <span class="mensaje-hora">{{ formatHora(mensaje.fechaCreacion) }}</span>
            </div>
          </div>
        </div>
      </div>
  
      <div class="input-container">
        <input 
          type="text" 
          v-model="nuevoMensaje" 
          @keyup.enter="enviarMensaje"
          placeholder="Escribe un mensaje..."
        />
        <button @click="enviarMensaje" class="btn-enviar" :disabled="!nuevoMensaje.trim()" :data-tooltip="nuevoMensaje.trim() ? 'Enviar mensaje' : 'Escribe un mensaje primero'">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M22 2L11 13M22 2L15 22L11 13M22 2L2 9L11 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "PaginaChat",
    data() {
      return {
        chat: null,
        mensajes: [],
        usuarios: [],
        usuarioActualId: '',
        nuevoMensaje: '',
        loading: false,
        chatId: null
      }
    },
    computed: {
      nombreOtroUsuario() {
        if (!this.chat || !this.chat.usuarioIds) return 'Cargando...';
        const otroUsuarioId = this.chat.usuarioIds.find(id => id !== this.usuarioActualId);
        const usuario = this.usuarios.find(u => u.id === otroUsuarioId);
        return usuario ? usuario.username : 'Usuario';
      }
    },
    mounted() {
      // Verificar si es admin - los admins no pueden acceder a chats
      const userData = localStorage.getItem('user');
      if (userData) {
        const userLocal = JSON.parse(userData);
        if (userLocal.username === 'admin') {
          this.$router.push('/inicio');
          return;
        }
      }
      
      this.chatId = parseInt(this.$route.params.chatId);
      this.cargarUsuarioActual();
      this.cargarChat();
    },
    methods: {
      cargarUsuarioActual() {
        const user = localStorage.getItem('user');
        if (user) {
          const userData = JSON.parse(user);
          this.usuarioActualId = userData.id;
        }
      },
  
      async cargarChat() {
        this.loading = true;
        try {
          const [chatResponse, usuariosResponse] = await Promise.all([
            fetch(`http://localhost:8080/api/chat/${this.chatId}`),
            fetch('http://localhost:8080/api/usuarios')
          ]);
  
          if (chatResponse.ok) {
            const data = await chatResponse.json();
            this.chat = data.chat;
            this.mensajes = data.mensajes || [];
            this.$nextTick(() => {
              this.scrollToBottom();
            });
          }
  
          if (usuariosResponse.ok) {
            this.usuarios = await usuariosResponse.json();
          }
        } catch (error) {
          // Error cargando chat
        } finally {
          this.loading = false;
        }
      },
  
      async enviarMensaje() {
        if (!this.nuevoMensaje.trim()) return;
  
        try {
          const mensaje = {
            contenido: this.nuevoMensaje,
            autorId: this.usuarioActualId,
            chatId: parseInt(this.chatId)
          };
          
          const response = await fetch('http://localhost:8080/api/mensaje/enviar', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(mensaje)
          });
  
          if (response.ok) {
            const mensajeEnviado = await response.json();
            this.mensajes.push(mensajeEnviado);
            this.nuevoMensaje = '';
            this.$nextTick(() => {
              this.scrollToBottom();
            });
          }
        } catch (error) {
          // Error al enviar mensaje
        }
      },
  
      volverAChats() {
        this.$router.push('/chats');
      },
  
      scrollToBottom() {
        const container = this.$refs.messagesContainer;
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      },
  
      formatHora(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleTimeString('es-ES', { 
          hour: '2-digit', 
          minute: '2-digit' 
        });
      }
    }
  }
  </script>
  
  <style scoped>
  .chat-page {
    display: flex;
    flex-direction: column;
    height: 100vh;
    background-color: #f9f9f9;
  }
  
  .chat-header {
    background: white;
    padding: 1rem 1.5rem;
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    display: flex;
    align-items: center;
    gap: 1rem;
    position: sticky;
    top: 0;
    z-index: 10;
  }
  
  .btn-back {
    background: none;
    border: none;
    cursor: pointer;
    padding: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: background-color 0.2s;
  }
  
  .btn-back:hover {
    background-color: #f0f0f0;
  }
  
  .btn-back svg {
    width: 24px;
    height: 24px;
    color: #333;
  }
  
  .chat-header h2 {
    font-family: 'FuenteHeader', sans-serif;
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    margin: 0;
  }
  
  .messages-container {
    flex: 1;
    overflow-y: auto;
    padding: 2rem;
    display: flex;
    flex-direction: column;
  }
  
  .loading {
    text-align: center;
    padding: 3rem;
    font-family: 'FuenteHeader', sans-serif;
    font-size: 1.1rem;
    color: #666;
  }
  
  .empty-messages {
    text-align: center;
    padding: 3rem 2rem;
    font-family: 'FuenteHeader', sans-serif;
    font-size: 1.1rem;
    color: #999;
  }
  
  .messages-list {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .mensaje {
    display: flex;
    max-width: 70%;
  }
  
  .mensaje-propio {
    align-self: flex-end;
  }
  
  .mensaje-otro {
    align-self: flex-start;
  }
  
  .mensaje-contenido {
    padding: 0.8rem 1.2rem;
    border-radius: 15px;
    font-family: 'FuenteHeader', sans-serif;
    word-wrap: break-word;
  }
  
  .mensaje-propio .mensaje-contenido {
    background-color: #a8d5ba;
    color: white;
    border-bottom-right-radius: 5px;
  }
  
  .mensaje-otro .mensaje-contenido {
    background-color: white;
    color: #333;
    border-bottom-left-radius: 5px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.08);
  }
  
  .mensaje-contenido p {
    margin: 0 0 0.3rem 0;
    font-size: 1rem;
  }
  
  .mensaje-hora {
    font-size: 0.75rem;
    opacity: 0.7;
    display: block;
  }
  
  .input-container {
    background: white;
    padding: 1rem 1.5rem;
    box-shadow: 0 -2px 8px rgba(0,0,0,0.05);
    display: flex;
    gap: 1rem;
    align-items: center;
  }
  
  .input-container input {
    flex: 1;
    padding: 0.8rem 1.2rem;
    border: none;
    background-color: #f5f5dc;
    border-radius: 25px;
    font-family: 'FuenteHeader', sans-serif;
    font-size: 1rem;
  }
  
  .input-container input:focus {
    outline: none;
    background-color: #eeeac7;
  }
  
  .btn-enviar {
    width: 45px;
    height: 45px;
    background-color: #a8d5ba;
    border: none;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .btn-enviar:disabled {
    background-color: #d9d9d9;
    cursor: not-allowed;
  }
  
  .btn-enviar:not(:disabled):hover {
    background-color: #8bc9a3;
    transform: scale(1.1);
  }
  
  .btn-enviar svg {
    width: 20px;
    height: 20px;
    color: white;
  }
  
  /* Scrollbar styles */
  .messages-container::-webkit-scrollbar {
    width: 6px;
  }
  
  .messages-container::-webkit-scrollbar-track {
    background: transparent;
  }
  
  .messages-container::-webkit-scrollbar-thumb {
    background: #d9d9d9;
    border-radius: 10px;
  }
  
  .messages-container::-webkit-scrollbar-thumb:hover {
    background: #c9c9c9;
  }
  </style>
  