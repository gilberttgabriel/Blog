<template>
  <div class="perfil-page">
    <div class="header">
      <div class="user-info" v-if="usuario">
        <div class="avatar">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" class="user-icon">
            <circle cx="12" cy="8" r="4" stroke="#5a9f7a" stroke-width="2"/>
            <path d="M6 21C6 17.686 8.686 15 12 15C15.314 15 18 17.686 18 21" stroke="#5a9f7a" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>

        <div class="user-details">
          <h1 data-tooltip="Ver perfil completo">{{ usuario.nombre }} {{ usuario.apellido }}</h1>
          <p class="username" data-tooltip="Username">@{{ usuario.username }}</p>
          <p class="descripcion" v-if="usuario.descripcion">{{ usuario.descripcion }}</p>

          <div v-if="esMiPerfil" class="actions-container">
             <button @click="iniciarEdicion" class="btn-action edit">Editar Perfil</button>
             <button @click="abrirConfirmacionEliminar" class="btn-action delete">Eliminar Cuenta</button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="editando" class="modal-overlay">
      <div class="modal-content">
          <h3>Editar Información</h3>
          <div class="form-grid">
              <div class="input-group">
                  <label>Nombre</label>
                  <input v-model="editData.nombre" class="edit-input" />
              </div>
              <div class="input-group">
                  <label>Apellido</label>
                  <input v-model="editData.apellido" class="edit-input" />
              </div>
              <div class="input-group">
                  <label>Usuario (@)</label>
                  <input v-model="editData.username" class="edit-input" />
              </div>
          </div>
          <div class="input-group full">
              <label>Descripción</label>
              <textarea v-model="editData.descripcion" rows="3" class="edit-input"></textarea>
          </div>
          <div class="modal-actions">
              <button @click="editando = false" class="btn-cancel">Cancelar</button>
              <button @click="guardarCambios" class="btn-save">Guardar Cambios</button>
          </div>
      </div>
    </div>

    <div v-if="eliminando" class="modal-overlay">
      <div class="modal-content delete-modal">
          <h3 class="danger-text">¿Eliminar cuenta?</h3>
          <p class="warning-msg">Esta acción borrará tu perfil y tus publicaciones permanentemente. No se puede deshacer.</p>

          <div class="modal-actions centered">
              <button @click="eliminando = false" class="btn-cancel">Cancelar</button>
              <button @click="confirmarEliminacion" class="btn-delete-confirm">Sí, Eliminar</button>
          </div>
      </div>
    </div>

    <div class="content-area">
      <div v-if="loading" class="loading">Cargando publicaciones...</div>
      <div v-else-if="publicaciones.length === 0" class="empty-state">
        <p>No has creado publicaciones aún</p>
        <router-link to="/crear" class="btn-create">Crear mi primera publicación</router-link>
      </div>
      <div v-else class="publicaciones-list">
        <div v-for="publicacion in publicaciones" :key="publicacion.id" class="publicacion-card" @click="irAPublicacion(publicacion.id)">
          <h2>{{ publicacion.titulo }}</h2>
          <p class="contenido">{{ publicacion.contenido }}</p>
          <div class="meta"><span class="fecha">{{ formatDate(publicacion.fechaCreacion) }}</span></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "VerPerfil",
  data() {
    return {
      usuario: null,
      publicaciones: [],
      loading: true,

      // Controla el modal de Editar
      editando: false,
      editData: { nombre: '', apellido: '', username: '', descripcion: '' },

      // NUEVO: Controla el modal de Eliminar
      eliminando: false
    }
  },
  computed: {
    esMiPerfil() {
      const userData = localStorage.getItem('user');
      if (!userData || !this.usuario) return false;
      return JSON.parse(userData).id === this.usuario.id;
    }
  },
  watch: {
    '$route.params.id'() {
      this.loading = true;
      this.cargarPerfil();
    }
  },
  mounted() {
    this.checkAdmin();
    this.cargarPerfil();
  },
  methods: {
    checkAdmin() {
      const userData = localStorage.getItem('user');
      if (userData) {
        const userLocal = JSON.parse(userData);
        if (userLocal.username === 'admin') this.$router.push('/inicio');
      }
    },
    async cargarPerfil() {
      try {
        const userData = localStorage.getItem('user');
        if (!userData) {
          this.$router.push('/autenticacion');
          return;
        }
        const userLocal = JSON.parse(userData);
        const userId = this.$route.params.id || userLocal.id;

        const response = await fetch(`http://localhost:8080/api/usuarios/${userId}`);
        if (response.ok) {
          this.usuario = await response.json();
          this.cargarPublicaciones();
        } else if (!this.$route.params.id) {
            this.usuario = userLocal;
        }
      } catch (error) {
        if (!this.$route.params.id) {
          const userData = localStorage.getItem('user');
          if (userData) this.usuario = JSON.parse(userData);
        }
      } finally {
        this.loading = false;
      }
    },

    // --- LÓGICA DE EDICIÓN ---
    iniciarEdicion() {
        this.editData = { ...this.usuario };
        this.editando = true;
    },
    async guardarCambios() {
        try {
            const url = `http://localhost:8080/api/usuarios/editar/${this.usuario.id}`;
            const response = await fetch(url, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.editData)
            });

            if (response.ok) {
                const usuarioActualizado = await response.json();
                this.usuario = usuarioActualizado;
                localStorage.setItem('user', JSON.stringify(this.usuario));
                this.editando = false;
            } else {
                console.error("Error al guardar");
            }
        } catch (e) {
            console.error(e);
        }
    },

    // --- NUEVA LÓGICA DE ELIMINACIÓN (SIN ALERTAS FEAS) ---

    // Paso 1: Solo abre la ventanita
    abrirConfirmacionEliminar() {
        this.eliminando = true;
    },

    // Paso 2: Hace el borrado real cuando das "Sí" en la ventanita
    async confirmarEliminacion() {
        try {
            const response = await fetch(`http://localhost:8080/api/usuarios/eliminar/${this.usuario.id}`, {
                method: 'DELETE'
            });
            if(response.ok) {
                localStorage.removeItem('user');
                this.$router.push('/autenticacion');
            }
        } catch (e) { console.error(e); }
    },

    async cargarPublicaciones() {
      try {
        const response = await fetch('http://localhost:8080/api/publicacion');
        if (response.ok) {
          const todas = await response.json();
          this.publicaciones = todas.filter(pub => pub.autorId === this.usuario.id);
        }
      } catch (error) { console.error(error); }
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' });
    },
    irAPublicacion(id) {
      this.$router.push(`/publicacion/${id}`);
    }
  }
}
</script>

<style scoped>
.perfil-page {
  min-height: 100vh;
  background-color: #f9f9f9;
  padding-top: 80px;
}

.header {
  width: 100%;
  padding: 1.5rem 2rem;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
  position: sticky;
  /* Cambiamos top: 0 por top: 60px para que al bajar no se pegue totalmente al techo */
  top: 0;
  z-index: 10;
}

.user-info { max-width: 900px; margin: 0 auto; display: flex; align-items: center; gap: 1.5rem; }
.avatar { width: 60px; height: 60px; background-color: #f5f5dc; border-radius: 50%; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.user-icon { width: 40px; height: 40px; }
.user-details h1 { font-family: 'FuenteHeader', sans-serif; font-size: 1.5rem; font-weight: 600; color: #333; margin: 0 0 0.3rem 0; }
.username { font-family: 'FuenteHeader', sans-serif; font-size: 0.95rem; color: #5a9f7a; margin: 0 0 0.3rem 0; font-weight: 500; }
.descripcion { font-family: 'FuenteHeader', sans-serif; font-size: 0.9rem; color: #666; margin: 0; line-height: 1.4; }

.actions-container { margin-top: 15px; display: flex; gap: 10px; }
.btn-action { border: none; padding: 6px 15px; border-radius: 15px; font-weight: 600; cursor: pointer; font-size: 0.85rem; transition: transform 0.2s; }
.btn-action.edit { background-color: #a8d5ba; color: white; }
.btn-action.edit:hover { transform: scale(1.05); }
.btn-action.delete { background-color: #ffebee; color: #c62828; }
.btn-action.delete:hover { transform: scale(1.05); }


.modal-overlay {
    position: fixed; top: 0; left: 0; width: 100%; height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex; justify-content: center; align-items: center;
    z-index: 2000;
}
.modal-content {
    background: white; padding: 30px; border-radius: 15px;
    width: 90%; max-width: 600px;
    box-shadow: 0 5px 20px rgba(0,0,0,0.2);
    animation: fadeIn 0.3s ease;
}
.modal-content h3 { margin-top: 0; color: #5a9f7a; margin-bottom: 20px; font-family: 'FuenteHeader', sans-serif; }
.form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; margin-bottom: 15px; }
.input-group { display: flex; flex-direction: column; gap: 5px; }
.input-group label { font-size: 0.85rem; font-weight: bold; color: #555; }
.input-group.full { width: 100%; }
.edit-input { padding: 10px; border: 1px solid #ddd; border-radius: 8px; font-size: 1rem; width: 100%; box-sizing: border-box;}
.modal-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 25px; border-top: 1px solid #eee; padding-top: 20px; }


.delete-modal { text-align: center; max-width: 400px; }
.danger-text { color: #c62828 !important; }
.warning-msg { color: #555; margin-bottom: 30px; line-height: 1.5; }
.centered { justify-content: center; }

/* BOTONES */
.btn-save { background-color: #5a9f7a; color: white; border: none; padding: 10px 25px; border-radius: 8px; font-weight: bold; cursor: pointer; font-size: 1rem;}
.btn-save:hover { background-color: #4a8f6a; }
.btn-cancel { background-color: #eee; color: #333; border: none; padding: 10px 25px; border-radius: 8px; cursor: pointer; font-size: 1rem;}
.btn-cancel:hover { background-color: #e0e0e0; }
.btn-delete-confirm { background-color: #ef5350; color: white; border: none; padding: 10px 25px; border-radius: 8px; font-weight: bold; cursor: pointer; font-size: 1rem;}
.btn-delete-confirm:hover { background-color: #d32f2f; }

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(-20px); }
    to { opacity: 1; transform: translateY(0); }
}

/* PUBLICACIONES */
.content-area { max-width: 900px; margin: 0 auto; padding: 2rem; }
.loading, .empty-state { text-align: center; padding: 3rem; color: #666; }
.btn-create { display: inline-block; padding: 0.8rem 2rem; background-color: #a8d5ba; color: white; border-radius: 25px; text-decoration: none; font-weight: 600; }
.publicaciones-list { display: flex; flex-direction: column; gap: 1.5rem; }
.publicacion-card { background: white; border-radius: 15px; padding: 2rem; box-shadow: 0 2px 10px rgba(0,0,0,0.08); cursor: pointer; transition: transform 0.2s; }
.publicacion-card:hover { transform: translateY(-3px); }
.publicacion-card h2 { font-size: 1.6rem; font-weight: 600; color: #333; margin-bottom: 1rem; }
.publicacion-card .contenido { color: #555; line-height: 1.6; margin-bottom: 1rem; display: -webkit-box; -webkit-line-clamp: 3; line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; }
.meta { display: flex; justify-content: flex-end; padding-top: 0.5rem; border-top: 1px solid #f0f0f0; }
.fecha { font-size: 0.85rem; color: #999; }
</style>