<template>
  <div class="anuncio-page">
    <div v-if="loading" class="loading">
      Cargando anuncio...
    </div>
    
    <div v-else-if="anuncio" class="content-area">
      <div v-if="esAdmin" class="admin-actions">
        <button class="btn edit" @click="abrirEditor" title="Editar anuncio">
          ✏️ Editar
        </button>
        <button class="btn delete" @click="confirmarEliminar" title="Eliminar anuncio">
          🗑️ Eliminar
        </button>
      </div>
      <!-- Anuncio completo -->
      <div class="anuncio-completo">
        <div class="anuncio-badge">ANUNCIO</div>
        
        <h1 class="titulo">{{ anuncio.titulo }}</h1>
        
        <div class="meta-info">
          <span class="fecha">{{ formatDate(anuncio.fechaCreacion) }}</span>
        </div>
        
        <!-- Imagen del anuncio -->
        <div v-if="anuncio.imagen" class="imagen-container">
          <img :src="anuncio.imagen" alt="Imagen del anuncio" class="anuncio-imagen" />
        </div>
        
        <p class="contenido">{{ anuncio.contenido }}</p>
      </div>

      <!-- Modal de edición -->
      <div v-if="editando" class="modal-backdrop" @click.self="cancelarEdicion">
        <div class="modal">
          <h3>Editar anuncio</h3>
          <form @submit.prevent="guardarEdicion">
            <label>Título</label>
            <input v-model="form.titulo" type="text" required />

            <label>Contenido</label>
            <textarea v-model="form.contenido" rows="6" required></textarea>

            <label>URL de imagen (opcional)</label>
            <input v-model="form.imagen" type="text" placeholder="https://..." />

            <div class="modal-actions">
              <button type="button" class="btn secondary" @click="cancelarEdicion">Cancelar</button>
              <button type="submit" class="btn primary">Guardar cambios</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <div v-else class="error-state">
      <p>No se pudo cargar el anuncio.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "PaginaAnuncio",
  data() {
    return {
      anuncio: null,
      loading: true,
      editando: false,
      form: {
        titulo: '',
        contenido: '',
        imagen: ''
      }
    }
  },
  computed: {
    esAdmin() {
      try {
        const user = JSON.parse(localStorage.getItem('user') || 'null');
        return !!(user && typeof user.numeroContrato !== 'undefined');
      } catch { return false; }
    }
  },
  mounted() {
    this.cargarAnuncio();
  },
  methods: {
    async cargarAnuncio() {
      this.loading = true;
      try {
        const anuncioId = this.$route.params.id;
        
        if (!anuncioId) {
          this.$router.push('/inicio');
          return;
        }
        
        const response = await fetch(`http://localhost:8080/api/anuncio/${anuncioId}`);
        
        if (response.ok) {
          this.anuncio = await response.json();
          // Prellenar formulario de edición
          this.form.titulo = this.anuncio.titulo || '';
          this.form.contenido = this.anuncio.contenido || '';
          this.form.imagen = this.anuncio.imagen || '';
        }
        
      } catch (error) {
        // Error cargando anuncio
      } finally {
        this.loading = false;
      }
    },
    abrirEditor() {
      this.editando = true;
    },
    cancelarEdicion() {
      this.editando = false;
      // Restaurar valores del anuncio
      if (this.anuncio) {
        this.form.titulo = this.anuncio.titulo || '';
        this.form.contenido = this.anuncio.contenido || '';
        this.form.imagen = this.anuncio.imagen || '';
      }
    },
    async guardarEdicion() {
      try {
        const user = JSON.parse(localStorage.getItem('user') || 'null');
        if (!user || !user.username || !user.contraseña) {
          alert('No hay sesión de administrador.');
          return;
        }
        const resp = await fetch(`http://localhost:8080/api/anuncio/${this.anuncio.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'X-Admin-Username': user.username,
            'X-Admin-Password': user.contraseña
          },
          body: JSON.stringify({
            titulo: this.form.titulo,
            contenido: this.form.contenido,
            imagen: this.form.imagen
          })
        });
        if (!resp.ok) {
          const text = await resp.text();
          throw new Error(text || 'No se pudo guardar');
        }
        this.anuncio = await resp.json();
        this.editando = false;
      } catch (e) {
        alert(e.message || 'Error al guardar');
      }
    },
    async confirmarEliminar() {
      if (!confirm('¿Eliminar este anuncio? Esta acción no se puede deshacer.')) return;
      try {
        const user = JSON.parse(localStorage.getItem('user') || 'null');
        if (!user || !user.username || !user.contraseña) {
          alert('No hay sesión de administrador.');
          return;
        }
        const resp = await fetch(`http://localhost:8080/api/anuncio/${this.anuncio.id}`, {
          method: 'DELETE',
          headers: {
            'X-Admin-Username': user.username,
            'X-Admin-Password': user.contraseña
          }
        });
        if (!resp.ok && resp.status !== 204) {
          const text = await resp.text();
          throw new Error(text || 'No se pudo eliminar');
        }
        // Volver al inicio tras eliminar
        this.$router.push('/inicio');
      } catch (e) {
        alert(e.message || 'Error al eliminar');
      }
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('es-ES', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
}
</script>

<style scoped>
.anuncio-page {
  min-height: 100vh;
  background-color: #f9f9f9;
  padding: 2rem;
}

.admin-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.btn {
  font-family: 'FuenteHeader', sans-serif;
  border: none;
  border-radius: 10px;
  padding: 0.6rem 1rem;
  cursor: pointer;
  transition: transform 0.05s ease, box-shadow 0.2s ease;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.btn:hover { transform: translateY(-1px); }
.btn:active { transform: translateY(0); }

.btn.edit {
  background: linear-gradient(135deg, #8bc9a3, #a8d5ba);
  color: #fff;
}

.btn.delete {
  background: linear-gradient(135deg, #f38181, #f96f6f);
  color: #fff;
}

.btn.primary {
  background: #5a9f7a;
  color: #fff;
}

.btn.secondary {
  background: #e9ecef;
  color: #333;
}

.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}

.modal {
  background: #fff;
  border-radius: 12px;
  width: 100%;
  max-width: 600px;
  padding: 1.5rem;
  box-shadow: 0 10px 30px rgba(0,0,0,0.15);
}

.modal h3 {
  margin-top: 0;
  font-family: 'FuenteHeader', sans-serif;
}

.modal label {
  display: block;
  margin-top: 0.75rem;
  margin-bottom: 0.25rem;
}

.modal input, .modal textarea {
  width: 100%;
  box-sizing: border-box;
  border: 2px solid #ddd;
  border-radius: 8px;
  padding: 0.6rem 0.75rem;
  font-family: 'FuenteHeader', sans-serif;
}

.modal-actions {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.content-area {
  max-width: 800px;
  margin: 0 auto;
}

.loading {
  text-align: center;
  padding: 3rem;
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.1rem;
  color: #666;
}

.anuncio-completo {
  background: white;
  border-radius: 15px;
  padding: 2.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
  margin-bottom: 2rem;
}

.anuncio-badge {
  display: inline-block;
  background-color: #a94442;
  color: white;
  padding: 0.4rem 1rem;
  border-radius: 20px;
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-bottom: 1rem;
}

.titulo {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 2.2rem;
  font-weight: 700;
  color: #333;
  margin: 0 0 1rem 0;
  line-height: 1.3;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 2px solid #f0f0f0;
  margin-bottom: 1.5rem;
}

.fecha {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.9rem;
  color: #999;
}

.imagen-container {
  width: 100%;
  max-height: 400px;
  overflow: hidden;
  background: #f5f5f5;
  border-radius: 10px;
  margin-bottom: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.anuncio-imagen {
  width: 100%;
  height: auto;
  object-fit: contain;
  display: block;
}

.contenido {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.1rem;
  color: #444;
  line-height: 1.8;
  white-space: pre-wrap;
  margin: 0;
}

.error-state {
  text-align: center;
  padding: 3rem;
  background: white;
  border-radius: 15px;
  max-width: 500px;
  margin: 0 auto;
}

.error-state p {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 1.5rem;
}

@media (max-width: 768px) {
  .titulo {
    font-size: 1.8rem;
  }
  
  .anuncio-page {
    padding: 1rem;
  }
  
  .anuncio-completo {
    padding: 1.5rem;
  }
}
</style>

