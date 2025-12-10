<template>
  <div class="create-post-page">
    <div class="create-post-content">
      <h1>crear anuncio</h1>
      
      <form @submit.prevent="handlePublish" class="post-form">
        <div class="form-section">
          <label for="titulo">titulo</label>
          <input 
            type="text" 
            id="titulo" 
            v-model="anuncioData.titulo" 
            placeholder="Escribe el título del anuncio"
            required
          />
        </div>
        
        <div class="form-section">
          <label for="contenido">contenido</label>
          <textarea 
            id="contenido" 
            v-model="anuncioData.contenido" 
            placeholder="Escribe el contenido del anuncio"
            rows="6"
            required
          ></textarea>
        </div>
        
        <div class="form-section">
          <label for="imagen">imagen del banner</label>
          <p class="image-hint">Tamaño recomendado: 970 x 90 píxeles</p>
          <div class="image-upload-area">
            <input 
              type="file" 
              id="imagen" 
              @change="handleImageUpload" 
              accept="image/*"
              ref="fileInput"
              style="display: none"
            />
            <button 
              type="button" 
              @click="$refs.fileInput.click()" 
              class="btn-upload"
              data-tooltip="Seleccionar imagen"
            >
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" style="width: 1.5em; height: 1.5em;">
                <path d="M7 10L12 15L17 10M12 15V3M5 20H19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
              {{ imagenNombre || 'Seleccionar imagen' }}
            </button>
            <div v-if="imagenPreview" class="image-preview">
              <img :src="imagenPreview" alt="Preview" />
            </div>
          </div>
        </div>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <div v-if="successMessage" class="success-message">
          {{ successMessage }}
        </div>
        
        <div class="button-group">
          <button type="button" @click="handleCancel" class="btn-cancel" data-tooltip="Cancelar anuncio">cancelar</button>
          <button type="submit" class="btn-publish" data-tooltip="Publicar anuncio">publicar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CrearAnuncio',
  data() {
    return {
      anuncioData: {
        titulo: '',
        contenido: '',
        imagen: null
      },
      imagenNombre: '',
      imagenPreview: null,
      errorMessage: '',
      successMessage: ''
    }
  },
  mounted() {
    const userData = localStorage.getItem('user');
    if (!userData) {
      this.$router.push('/autenticacion');
      return;
    }
    
    const userLocal = JSON.parse(userData);
    if (userLocal.username !== 'admin') {
      this.$router.push('/inicio');
      return;
    }
  },
  methods: {
    handleImageUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.imagenNombre = file.name;
        
        // Crear preview
        const reader = new FileReader();
        reader.onload = (e) => {
          this.imagenPreview = e.target.result;
          this.anuncioData.imagen = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    
    async handlePublish() {
      this.errorMessage = '';
      this.successMessage = '';
      
      try {
        const response = await fetch('http://localhost:8080/api/anuncio/crear', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.anuncioData)
        });
        
        if (response.ok) {
          this.successMessage = 'Anuncio creado exitosamente!';
          
          // Limpiar el formulario
          this.anuncioData = {
            titulo: '',
            contenido: '',
            imagen: null
          };
          this.imagenNombre = '';
          this.imagenPreview = null;
          
          // Redirigir después de 1.5 segundos
          setTimeout(() => {
            this.$router.push('/inicio');
          }, 1500);
        } else {
          const error = await response.json();
          this.errorMessage = error.message || 'Error al crear el anuncio';
        }
      } catch (error) {
        this.errorMessage = 'Error de conexión. Intenta nuevamente.';
        // Error creando anuncio
      }
    },
    
    handleCancel() {
      // Limpiar el formulario
      this.anuncioData = {
        titulo: '',
        contenido: '',
        imagen: null
      };
      this.imagenNombre = '';
      this.imagenPreview = null;
      this.errorMessage = '';
      this.successMessage = '';
      
      // Regresar a la página principal
      this.$router.push('/inicio');
    }
  }
}
</script>

<style scoped>
.create-post-page {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #f9f9f9;
  overflow-y: auto;
  box-sizing: border-box;
}

.create-post-content {
  max-width: 900px;
  margin: 2rem auto;
  padding: 2rem;
  min-height: calc(100vh - 4rem);
  display: flex;
  flex-direction: column;
  justify-content: center;
}

h1 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 2.5rem;
  font-weight: 600;
  margin-bottom: 2.5rem;
  color: #333;
  text-align: center;
}

.post-form {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

label {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.3rem;
  font-weight: 500;
  color: #333;
}

.image-hint {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.9rem;
  color: #666;
  background-color: #e8f5e9;
  padding: 0.8rem 1rem;
  border-radius: 10px;
  margin: 0;
  border-left: 4px solid #4caf50;
}

input, textarea {
  padding: 1.2rem 1.5rem;
  border: none;
  background-color: #d9d9d9;
  border-radius: 25px;
  font-size: 1rem;
  font-family: 'FuenteHeader', sans-serif;
  transition: background-color 0.3s;
}

input:focus, textarea:focus {
  outline: none;
  background-color: #c9c9c9;
}

textarea {
  resize: vertical;
  min-height: 150px;
  border-radius: 30px;
}

.image-upload-area {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.btn-upload {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 1.2rem 1.5rem;
  background-color: #d9d9d9;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  font-family: 'FuenteHeader', sans-serif;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-upload:hover {
  background-color: #c9c9c9;
}

.image-preview {
  margin-top: 1rem;
  max-width: 100%;
  border-radius: 15px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  border-radius: 15px;
}

.error-message {
  background-color: #ffe6e6;
  color: #d32f2f;
  padding: 1rem;
  border-radius: 15px;
  text-align: center;
  font-size: 0.95rem;
  font-family: 'FuenteHeader', sans-serif;
}

.success-message {
  background-color: #e6f7ee;
  color: #2e7d32;
  padding: 1rem;
  border-radius: 15px;
  text-align: center;
  font-size: 0.95rem;
  font-family: 'FuenteHeader', sans-serif;
}

.button-group {
  display: flex;
  gap: 2rem;
  justify-content: center;
  margin-top: 1rem;
}

.btn-cancel, .btn-publish {
  padding: 1rem 3rem;
  border: none;
  border-radius: 30px;
  font-size: 1.2rem;
  font-weight: 600;
  font-family: 'FuenteHeader', sans-serif;
  cursor: pointer;
  transition: all 0.3s;
  min-width: 150px;
}

.btn-cancel {
  background-color: #ff6b6b;
  color: white;
}

.btn-cancel:hover {
  background-color: #ff5252;
  transform: translateY(-2px);
}

.btn-publish {
  background-color: #a8d5ba;
  color: white;
}

.btn-publish:hover {
  background-color: #8bc9a3;
  transform: translateY(-2px);
}
</style>

