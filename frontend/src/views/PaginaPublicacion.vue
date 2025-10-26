<template>
  <div class="publicacion-page">
    <div v-if="loading" class="loading">
      Cargando publicación...
    </div>
    
    <div v-else-if="publicacion" class="content-area">
      <!-- Publicación principal -->
      <div class="publicacion-completa">
        <h1 class="titulo">{{ publicacion.titulo }}</h1>
        
        <div class="meta-info">
          <span 
            class="autor" 
            @click="irAPerfil(publicacion.autorId)"
          >
            {{ nombreAutor }}
          </span>
          <span class="separador">•</span>
          <span class="fecha">{{ formatDate(publicacion.fechaCreacion) }}</span>
        </div>
        
        <p class="contenido">{{ publicacion.contenido }}</p>
      </div>
      
      <!-- Sección de comentarios -->
      <div class="comentarios-section">
        <h2 class="comentarios-titulo">
          Comentarios ({{ comentarios.length }})
        </h2>
        
        <!-- Formulario para agregar comentario -->
        <div class="agregar-comentario">
          <textarea 
            v-model="nuevoComentario" 
            placeholder="Escribe un comentario..."
            rows="3"
          ></textarea>
          <button 
            @click="agregarComentario" 
            class="btn-comentar"
            :disabled="!nuevoComentario.trim()"
          >
            Comentar
          </button>
        </div>
        
        <!-- Lista de comentarios -->
        <div v-if="comentarios.length === 0" class="sin-comentarios">
          <p>No hay comentarios aún. ¡Sé el primero en comentar!</p>
        </div>
        
        <div v-else class="comentarios-lista">
          <div 
            v-for="comentario in comentarios" 
            :key="comentario.id" 
            class="comentario-card"
          >
            <div class="comentario-header">
              <span 
                class="comentario-autor"
                @click="irAPerfil(comentario.autor)"
              >
                {{ obtenerNombreAutorComentario(comentario.autor) }}
              </span>
              <span class="comentario-fecha">{{ formatDate(comentario.fechaCreacion) }}</span>
            </div>
            <p class="comentario-contenido">{{ comentario.contenido }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else class="error-state">
      <p>No se pudo cargar la publicación.</p>
      <button @click="$router.push('/home')" class="btn-volver">Volver al inicio</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "PaginaPublicacion",
  data() {
    return {
      publicacion: null,
      comentarios: [],
      usuarios: [],
      nuevoComentario: '',
      nombreAutor: '',
      loading: true
    }
  },
  mounted() {
    this.cargarPublicacion();
  },
  methods: {
    async cargarPublicacion() {
      this.loading = true;
      try {
        const publicacionId = this.$route.params.id;
        
        if (!publicacionId) {
          this.$router.push('/home');
          return;
        }
        
        // Cargar publicación, comentarios y usuarios en paralelo
        const [pubRes, comRes, usersRes] = await Promise.all([
          fetch(`http://localhost:8080/api/publicacion/${publicacionId}`),
          fetch(`http://localhost:8080/api/comentarios/publicacion/${publicacionId}`),
          fetch('http://localhost:8080/api/usuarios')
        ]);
        
        if (pubRes.ok) {
          this.publicacion = await pubRes.json();
          
          // Obtener nombre del autor
          if (usersRes.ok) {
            this.usuarios = await usersRes.json();
            const autor = this.usuarios.find(u => u.id === this.publicacion.autorId);
            this.nombreAutor = autor ? autor.username : 'Usuario desconocido';
          }
        }
        
        if (comRes.ok) {
          this.comentarios = await comRes.json();
        }
        
      } catch (error) {
        console.error('Error cargando publicación:', error);
      } finally {
        this.loading = false;
      }
    },
    async agregarComentario() {
      if (!this.nuevoComentario.trim()) return;
      
      try {
        const userData = localStorage.getItem('user');
        if (!userData) {
          this.$router.push('/login');
          return;
        }
        
        const user = JSON.parse(userData);
        
        const comentario = {
          contenido: this.nuevoComentario,
          autor: user.id,
          publicacionId: parseInt(this.$route.params.id)
        };
        
        const response = await fetch('http://localhost:8080/api/comentarios', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(comentario)
        });
        
        if (response.ok) {
          const nuevoComentarioCreado = await response.json();
          this.comentarios.push(nuevoComentarioCreado);
          this.nuevoComentario = '';
        }
      } catch (error) {
        console.error('Error agregando comentario:', error);
      }
    },
    obtenerNombreAutorComentario(autorId) {
      const usuario = this.usuarios.find(u => u.id === autorId);
      return usuario ? usuario.username : 'Usuario desconocido';
    },
    irAPerfil(autorId) {
      this.$router.push(`/profile/${autorId}`);
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
.publicacion-page {
  min-height: 100vh;
  background-color: #f9f9f9;
  padding: 2rem;
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

.publicacion-completa {
  background: white;
  border-radius: 15px;
  padding: 2.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
  margin-bottom: 2rem;
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

.autor {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1rem;
  color: #5a9f7a;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s;
  word-spacing: 0.15em;
}

.autor:hover {
  color: #4a8f6a;
  text-decoration: underline;
}

.separador {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.85rem;
  color: #ccc;
}

.fecha {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.9rem;
  color: #999;
}

.contenido {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.1rem;
  color: #444;
  line-height: 1.8;
  white-space: pre-wrap;
  margin: 0;
}

.comentarios-section {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
}

.comentarios-titulo {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0 0 1.5rem 0;
}

.agregar-comentario {
  margin-bottom: 2rem;
}

.agregar-comentario textarea {
  width: 100%;
  padding: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1rem;
  resize: vertical;
  margin-bottom: 1rem;
  transition: border-color 0.3s;
}

.agregar-comentario textarea:focus {
  outline: none;
  border-color: #a8d5ba;
}

.btn-comentar {
  padding: 0.8rem 2rem;
  background-color: #a8d5ba;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: 600;
  font-family: 'FuenteHeader', sans-serif;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.btn-comentar:hover:not(:disabled) {
  background-color: #8bc9a3;
  transform: translateY(-2px);
}

.btn-comentar:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.sin-comentarios {
  text-align: center;
  padding: 2rem;
  color: #999;
}

.sin-comentarios p {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1rem;
  margin: 0;
}

.comentarios-lista {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comentario-card {
  background: #f9f9f9;
  border-radius: 10px;
  padding: 1.2rem;
  transition: background-color 0.2s;
}

.comentario-card:hover {
  background: #f5f5f5;
}

.comentario-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.7rem;
}

.comentario-autor {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.95rem;
  color: #5a9f7a;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s;
  word-spacing: 0.15em;
}

.comentario-autor:hover {
  color: #4a8f6a;
  text-decoration: underline;
}

.comentario-fecha {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.85rem;
  color: #999;
}

.comentario-contenido {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.95rem;
  color: #555;
  line-height: 1.6;
  margin: 0;
  white-space: pre-wrap;
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

.btn-volver {
  padding: 0.8rem 2rem;
  background-color: #a8d5ba;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: 600;
  font-family: 'FuenteHeader', sans-serif;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-volver:hover {
  background-color: #8bc9a3;
}
</style>



