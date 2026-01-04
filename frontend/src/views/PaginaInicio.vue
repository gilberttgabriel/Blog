<template>
  <div class="home-page">
    <div class="header">
      <div class="search-bar">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="Buscar..." 
          @input="handleSearch"
        />
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="search-icon">
          <path d="M11 19C15.4183 19 19 15.4183 19 11C19 6.58172 15.4183 3 11 3C6.58172 3 3 6.58172 3 11C3 15.4183 6.58172 19 11 19Z" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M21 21L16.65 16.65" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
    </div>

    <!-- Banner de Anuncio -->
    <div v-if="anuncio" class="anuncio-banner" @click="irAAnuncio(anuncio.id)" data-tooltip="Ver anuncio" data-tooltip-pos="bottom">
      <span class="anuncio-label">Anuncio</span>
      <img v-if="anuncio.imagen" :src="anuncio.imagen" alt="Anuncio" class="anuncio-imagen" />
    </div>

    <!-- Contenido solo para usuarios normales (no admin) -->
    <div v-if="!isAdmin" class="content-area">
      <div v-if="loading" class="loading">
        Cargando publicaciones...
      </div>
      
      <div v-else-if="publicaciones.length === 0" class="empty-state">
        <p>No hay publicaciones aún</p>
        <router-link to="/crear" class="btn-create">Crear la primera publicación</router-link>
      </div>
      
      <div v-else class="publicaciones-list">
        <div 
          v-for="publicacion in filteredPublicaciones"
          :key="publicacion.id" 
          class="publicacion-card"
          @click="irAPublicacion(publicacion.id)"
          data-tooltip="Ver publicación"
        >
          <h2>{{ publicacion.titulo }}</h2>
          <p class="contenido">{{ publicacion.contenido }}</p>
          <div class="meta">
            <span 
              class="autor" 
              @click.stop="irAPerfil(publicacion.autorId)"
              data-tooltip="Ver perfil"
            >
              {{ obtenerNombreAutor(publicacion.autorId) }}
            </span>
            <span class="separador">•</span>
            <span class="fecha">{{ formatDate(publicacion.fechaCreacion) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PaginaInicio",
  data() {
    return {
      publicaciones: [],
      usuarios: [],
      anuncio: null,
      searchQuery: '', 
      loading: false,
      isAdmin: false
    }
  },
  computed: {
    filteredPublicaciones() {
      if (!this.searchQuery) {
        return this.publicaciones;
      }
      const query = this.searchQuery.toLowerCase();
      return this.publicaciones.filter(pub => 
        pub.titulo.toLowerCase().includes(query) || 
        pub.contenido.toLowerCase().includes(query)
      );
    }
  }, 
  mounted() {
    this.checkUserType();
    this.cargarDatos();
  },
  methods: {
    checkUserType() {
      const user = localStorage.getItem('user');
      if (user) {
        const userData = JSON.parse(user);
        this.isAdmin = userData.username === 'admin';
      }
    },
    async cargarDatos() {
      this.loading = true;
      try {
        // Cargar publicaciones, usuarios y anuncio en paralelo
        const [publicacionesRes, usuariosRes, anuncioRes] = await Promise.all([
          fetch('http://localhost:8080/api/publicacion'),
          fetch('http://localhost:8080/api/usuarios'),
          fetch('http://localhost:8080/api/anuncio')
        ]);
        
        if (publicacionesRes.ok) {
          this.publicaciones = await publicacionesRes.json();
        }
        
        if (usuariosRes.ok) {
          this.usuarios = await usuariosRes.json();
        }
        
        if (anuncioRes.ok) {
          this.anuncio = await anuncioRes.json();
        }
      } catch (error) {
        console.error("Error cargando datos");
      } finally {
        this.loading = false;
      }
    },
    obtenerNombreAutor(autorId) {
      const usuario = this.usuarios.find(u => u.id === autorId);
      return usuario ? usuario.username : 'Usuario desconocido';
    },
    irAPublicacion(publicacionId) {
      this.$router.push(`/publicacion/${publicacionId}`);
    },
    irAPerfil(autorId) {
      this.$router.push(`/perfil/${autorId}`);
    },
    irAAnuncio(anuncioId) {
      this.$router.push(`/anuncio/${anuncioId}`);
    },
    handleSearch() {
      // La búsqueda se hace automáticamente con el computed property
    }, 
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('es-ES', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
      });
    }
  }
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background-color: #f9f9f9;
}

.header {
  width: 100%;
  padding: 1.5rem 2rem;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  border-bottom: 1px solid #e0e0e0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-bar {
  display: flex;
  align-items: center;
  max-width: 600px;
  margin-left: auto;
  background-color: #f5f5dc;
  border-radius: 30px;
  padding: 0.6rem 1.5rem;
}

.search-bar input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  font-size: 1rem;
  font-family: 'FuenteHeader', sans-serif;
  color: #333;
}

.search-bar input::placeholder {
  color: #999;
}

.search-icon {
  width: 1.5em;
  height: 1.5em;
  margin-left: 0.5rem;
  flex-shrink: 0;
}

.content-area {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
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
  border-radius: 25px;
  text-decoration: none;
  font-family: 'FuenteHeader', sans-serif;
  font-weight: 600;
  transition: background-color 0.3s, transform 0.2s;
}

.btn-create:hover {
  background-color: #8bc9a3;
  transform: translateY(-2px);
}

.publicaciones-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.publicacion-card {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.publicacion-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.12);
}

.publicacion-card h2 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.6rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 1rem;
}

.publicacion-card .contenido {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1rem;
  color: #555;
  line-height: 1.6;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.meta {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 0.5rem;
  padding-top: 0.5rem;
  border-top: 1px solid #f0f0f0;
}

.autor {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.9rem;
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
  font-size: 0.85rem;
  color: #999;
}

/* Estilos del banner de anuncio */
.anuncio-banner {
  position: relative;
  width: 100%;
  max-width: 970px;
  height: 90px;
  margin: 1.5rem auto 0.5rem auto;
  background-color: #a94442;
  border-radius: 0;
  overflow: visible;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  z-index: 1;
}

.anuncio-banner:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 15px rgba(0,0,0,0.15);
}

/* Asegurar que el tooltip del anuncio se vea */
.anuncio-banner[data-tooltip]:before,
.anuncio-banner[data-tooltip]:after {
  z-index: 10001 !important;
}

.anuncio-label {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-family: 'FuenteHeader', sans-serif;
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  z-index: 1;
  pointer-events: none;
}

.anuncio-imagen {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.9;
}
</style>



