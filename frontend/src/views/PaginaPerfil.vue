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
          <h1>{{ usuario.nombre }} {{ usuario.apellido }}</h1>
          <p class="username">@{{ usuario.username }}</p>
          <p class="descripcion" v-if="usuario.descripcion">{{ usuario.descripcion }}</p>
        </div>
      </div>
    </div>

    <div class="content-area">
      <div v-if="loading" class="loading">
        Cargando publicaciones...
      </div>
      
      <div v-else-if="publicaciones.length === 0" class="empty-state">
        <p>No has creado publicaciones aún</p>
        <router-link to="/create" class="btn-create">Crear mi primera publicación</router-link>
      </div>
      
      <div v-else class="publicaciones-list">
        <div 
          v-for="publicacion in publicaciones" 
          :key="publicacion.id" 
          class="publicacion-card"
        >
          <h2>{{ publicacion.titulo }}</h2>
          <p class="contenido">{{ publicacion.contenido }}</p>
          <div class="meta">
            <span class="fecha">{{ formatDate(publicacion.fechaCreacion) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PaginaPerfil",
  data() {
    return {
      usuario: null,
      publicaciones: [],
      loading: true
    }
  },
  watch: {
    '$route.params.id'() {
      // Recargar perfil cuando cambie el ID en la URL
      this.loading = true;
      this.cargarPerfil();
    }
  },
  mounted() {
    this.cargarPerfil();
  },
  methods: {
    async cargarPerfil() {
      try {
        // Verificar si hay usuario logueado
        const userData = localStorage.getItem('user');
        
        if (!userData) {
          // Si no hay datos, redirigir al login
          this.$router.push('/login');
          return;
        }
        
        const userLocal = JSON.parse(userData);
        
        // Determinar qué ID de usuario cargar (de la URL o del usuario actual)
        const userId = this.$route.params.id || userLocal.id;
        
        // Hacer una llamada al backend para obtener datos completos del usuario
        const response = await fetch(`http://localhost:8080/api/usuarios/${userId}`);
        
        if (response.ok) {
          this.usuario = await response.json();
        } else {
          // Si falla y es el perfil actual, usar datos de localStorage
          if (!this.$route.params.id) {
            this.usuario = userLocal;
          }
        }
        
        // Cargar las publicaciones del usuario
        await this.cargarPublicaciones();
        
      } catch (error) {
        console.error('Error al cargar perfil:', error);
        // En caso de error y si es el perfil actual, usar datos de localStorage
        if (!this.$route.params.id) {
          const userData = localStorage.getItem('user');
          if (userData) {
            this.usuario = JSON.parse(userData);
          }
        }
      } finally {
        this.loading = false;
      }
    },
    async cargarPublicaciones() {
      try {
        const response = await fetch('http://localhost:8080/api/publicacion');
        if (response.ok) {
          const todasPublicaciones = await response.json();
          // Filtrar solo las publicaciones del usuario actual
          this.publicaciones = todasPublicaciones.filter(
            pub => pub.autorId === this.usuario.id
          );
        }
      } catch (error) {
        console.error('Error cargando publicaciones:', error);
      }
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
.perfil-page {
  min-height: 100vh;
  background-color: #f9f9f9;
}

.header {
  width: 100%;
  padding: 1.5rem 2rem;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  border-bottom: 1px solid #e0e0e0;
}

.user-info {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.avatar {
  width: 60px;
  height: 60px;
  background-color: #f5f5dc;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.user-icon {
  width: 40px;
  height: 40px;
}

.user-details h1 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0 0 0.3rem 0;
  word-spacing: 0.2em;
}

.username {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.95rem;
  color: #5a9f7a;
  margin: 0 0 0.3rem 0;
  font-weight: 500;
}

.descripcion {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.9rem;
  color: #666;
  margin: 0;
  line-height: 1.4;
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
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.meta {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-top: 0.5rem;
  border-top: 1px solid #f0f0f0;
}

.fecha {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.85rem;
  color: #999;
}
</style>



