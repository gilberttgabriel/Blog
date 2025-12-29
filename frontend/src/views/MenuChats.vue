<template>
  <div class="menu-chats-page">
    <div class="header">
      <h1>chats</h1>
      <button @click="mostrarModalUsuarios" class="btn-nuevo-chat" data-tooltip="Nuevo chat" data-tooltip-pos="bottom">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 5V19M5 12H19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </div>

    <div v-if="loading" class="loading">
      Cargando chats...
    </div>

    <div v-else-if="chats.length === 0" class="empty-state">
      <p>No tienes conversaciones aún</p>
      <button @click="mostrarModalUsuarios" class="btn-create" data-tooltip="Crear tu primer chat">Iniciar una conversación</button>
    </div>

    <div v-else class="chats-list">
      <div
        v-for="chat in chats"
        :key="chat.id"
        class="chat-card"
        @click="irAChat(chat.id)"
        data-tooltip="Ver chat"
      >
        <div class="chat-info">
          <h3>{{ obtenerNombreOtroUsuario(chat) }}</h3>
          <p class="ultimo-mensaje">{{ chat.ultimoMensaje || 'No hay mensajes aún' }}</p>
        </div>
        <div class="chat-fecha">
          <span>{{ formatDate(chat.fechaCreacion) }}</span>
        </div>
        <button class="btn-eliminar" @click.stop="eliminarChat(chat.id)">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M10 12V17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M14 12V17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M4 7H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M6 10V18C6 19.6569 7.34315 21 9 21H15C16.6569 21 18 19.6569 18 18V10" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5V7H9V5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
    </div>

    <div v-if="mostrarModal" class="modal-overlay" @click="cerrarModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>nueva conversación</h2>
          <button @click="cerrarModal" class="btn-close" data-tooltip="Cerrar" data-tooltip-pos="left">×</button>
        </div>
        
        <div class="search-usuario">
          <input 
            type="text" 
            v-model="busquedaUsuario" 
            placeholder="Buscar usuario..."
          />
        </div>

        <div v-if="loadingUsuarios" class="loading-usuarios">
          Cargando usuarios...
        </div>

        <div v-else class="usuarios-list">
          <div 
            v-for="usuario in usuariosFiltrados" 
            :key="usuario.id" 
            class="usuario-item"
            @click="crearChatConUsuario(usuario.id)"
          >
            <div class="usuario-info">
              <h4>{{ usuario.username }}</h4>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MenuChats",
  data() {
    return {
      chats: [],
      usuarios: [],
      usuarioActualId: '',
      loading: false,
      loadingUsuarios: false,
      mostrarModal: false,
      busquedaUsuario: ''
    }
  },
  computed: {
    usuariosFiltrados() {
      const otrosUsuarios = this.usuarios.filter(u => u.id !== this.usuarioActualId);

      if (!this.busquedaUsuario) {
        return otrosUsuarios;
      }

      const query = this.busquedaUsuario.toLowerCase();
      return otrosUsuarios.filter(u =>
        u.username.toLowerCase().includes(query)
      );
    }
  },
  async mounted() {
      // Verificar si es admin - los admins no pueden acceder a chats
      const userData = localStorage.getItem('user');
      if (userData) {
        const userLocal = JSON.parse(userData);
        if (userLocal.username === 'admin') {
          this.$router.push('/inicio');
          return;
        }
      }
      
      await this.cargarUsuarios();
      this.cargarUsuarioActual();
      await this.cargarChats();
    },
  methods: {
    cargarUsuarioActual() {
      const user = localStorage.getItem('user');
      if (user) {
        const userData = JSON.parse(user);
        this.usuarioActualId = userData.id;
      }
    },

    async cargarChats() {
      this.loading = true;
      try {
        const response = await fetch(`http://localhost:8080/api/chats/${this.usuarioActualId}`);
        if (response.ok) {
          this.chats = await response.json();
        }
      } catch (error) {
        // Error cargando chats
      } finally {
        this.loading = false;
      }
    },

    async cargarUsuarios() {
      this.loadingUsuarios = true;
      try {
        const response = await fetch('http://localhost:8080/api/usuarios');
        if (response.ok) {
          this.usuarios = await response.json();
        }
      } catch (error) {
        // Error cargando usuarios
      } finally {
        this.loadingUsuarios = false;
      }
    },
    obtenerNombreOtroUsuario(chat) {
      const otroUsuarioId = chat.usuarioIds.find(id => id !== this.usuarioActualId);
      const otroUsuario = this.usuarios.find(u => u.id === otroUsuarioId);
      return otroUsuario ? otroUsuario.username : 'Usuario desconocido';
    },
    async mostrarModalUsuarios() {
      this.mostrarModal = true;
      await this.cargarUsuarios();
    },

    cerrarModal() {
      this.mostrarModal = false;
      this.busquedaUsuario = '';
    },

    async crearChatConUsuario(otroUsuarioId) {
      try {
        const response = await fetch('http://localhost:8080/api/chat/crear', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            usuarioIds: [this.usuarioActualId, otroUsuarioId]
          })
        });

        if (response.ok) {
          const chat = await response.json();
          this.cerrarModal();
          this.$router.push(`/chat/${chat.id}`);
        }
      } catch (error) {
        // Error creando chat
      }
    },

    irAChat(chatId) {
      this.$router.push(`/chat/${chatId}`);
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      const now = new Date();
      const diffDays = Math.floor((now - date) / (1000 * 60 * 60 * 24));
      
      if (diffDays === 0) return 'Hoy';
      if (diffDays === 1) return 'Ayer';
      if (diffDays < 7) return `Hace ${diffDays} días`;
      
      return date.toLocaleDateString('es-ES', { 
        day: 'numeric', 
        month: 'short' 
      });
    },
    async eliminarChat(chatId) {
      if (!confirm('¿Estás seguro de que quieres eliminar este chat?')) {
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/api/chat/${chatId}`, {
          method: 'DELETE'
        });

        if (response.ok || response.status === 204) {
          // Eliminar el chat de la lista local
          this.chats = this.chats.filter(c => c.id !== chatId);
        } else {
          alert('Error al eliminar el chat');
        }
      } catch (error) {
        alert('Error al eliminar el chat');
      }
    }
  }
}
</script>

<style scoped>
  .menu-chats-page {
  min-height: 100vh;
  background-color: #f9f9f9;
  padding: 2rem;
}

.header {
  max-width: 100vw;
  margin: 0 auto 2rem auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h1 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 2.5rem;
  font-weight: 600;
  color: #333;
}
.btn-nuevo-chat {
  width: 50px;
  height: 50px;
  background-color: #a8d5ba;
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-nuevo-chat svg {
  width: 24px;
  height: 24px;
  color: white;
}

.btn-nuevo-chat:hover {
  background-color: #8bc9a3;
  transform: scale(1.1);
}

.loading {
  text-align: center;
  padding: 3rem;
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.1rem;
  color: #666;
}
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  max-width: 900px;
  margin: 0 auto;
}

.empty-state p {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.3rem;
  color: #666;
  margin-bottom: 1.5rem;
}

.btn-create {
  display: inline-block;
  padding: 0.8rem 2rem;
  background-color: #a8d5ba;
  color: white;
  border: none;
  border-radius: 25px;
  font-family: 'FuenteHeader', sans-serif;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.btn-create:hover {
  background-color: #8bc9a3;
  transform: translateY(-2px);
}
.chats-list {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.chat-card {
  background: white;
  border-radius: 15px;
  padding: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
}

.chat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.12);
}

.chat-info {
  flex: 1;
}
.chat-info h3 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.3rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}

.ultimo-mensaje {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.95rem;
  color: #666;
  display: -webkit-box;
  line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.chat-fecha {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.85rem;
  color: #999;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-header {
  padding: 1.5rem;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}
.btn-close {
  background: none;
  border: none;
  font-size: 2rem;
  color: #666;
  cursor: pointer;
  line-height: 1;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close:hover {
  color: #333;
}

.search-usuario {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e0e0e0;
}

.search-usuario input {
  width: 100%;
  padding: 0.8rem 1rem;
  border: none;
  background-color: #f5f5dc;
  border-radius: 20px;
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1rem;
}
.loading-usuarios {
  padding: 2rem;
  text-align: center;
  font-family: 'FuenteHeader', sans-serif;
  color: #666;
}

.usuarios-list {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem;
}

.usuario-item {
  padding: 1rem 1.5rem;
  cursor: pointer;
  transition: background-color 0.2s;
  border-radius: 10px;
  margin-bottom: 0.3rem;
}
.usuario-info h4 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.1rem;
  font-weight: 500;
  color: #333;
}
  .btn-eliminar {
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: background-color 0.2s;
  }

  .btn-eliminar:hover {
    background-color: rgba(0,0,0,0.05); /* leve sombreado al pasar el mouse */
  }

  .btn-eliminar svg {
    width: 22px;
    height: 22px;
  }
</style>



