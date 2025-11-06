<template>
  <div class="anuncio-page">
    <div v-if="loading" class="loading">
      Cargando anuncio...
    </div>
    
    <div v-else-if="anuncio" class="content-area">
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
      loading: true
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
        } else {
          console.error('Error cargando anuncio');
        }
        
      } catch (error) {
        console.error('Error cargando anuncio:', error);
      } finally {
        this.loading = false;
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

