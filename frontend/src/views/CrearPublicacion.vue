<template>
  <div class="create-post-page">
    <div class="create-post-content">
      <h1>crear publicacion</h1>
      
      <form @submit.prevent="handlePublish" class="post-form">
        <div class="form-section">
          <label for="titulo">titulo</label>
          <input 
            type="text" 
            id="titulo" 
            v-model="postData.titulo" 
            placeholder="Escribe el título de tu publicación"
            required
          />
        </div>
        
        <div class="form-section">
          <label for="contenido">contenido</label>
          <textarea 
            id="contenido" 
            v-model="postData.contenido" 
            placeholder="Escribe el contenido de tu publicación"
            rows="8"
            required
          ></textarea>
        </div>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <div v-if="successMessage" class="success-message">
          {{ successMessage }}
        </div>
        
        <div class="button-group">
          <button type="button" @click="handleCancel" class="btn-cancel">cancelar</button>
          <button type="submit" class="btn-publish">publicar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CrearPublicacion',
  data() {
    return {
      postData: {
        titulo: '',
        contenido: '',
        autorId: ''
      },
      errorMessage: '',
      successMessage: ''
    }
  },
  mounted() {
    // Obtener el usuario del localStorage
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      this.postData.autorId = user.id;
    } else {
      // Si no hay usuario, redirigir al login
      this.$router.push('/autenticacion');
    }
  },
  methods: {
    async handlePublish() {
      this.errorMessage = '';
      this.successMessage = '';
      
      try {
        const response = await fetch('http://localhost:8080/api/publicacion/crear', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.postData)
        });
        
        if (response.ok) {
          this.successMessage = 'Publicación creada exitosamente!';
          
          // Limpiar el formulario
          this.postData.titulo = '';
          this.postData.contenido = '';
          
          // Redirigir después de 1.5 segundos
          setTimeout(() => {
            this.$router.push('/inicio');
          }, 1500);
        } else {
          const error = await response.json();
          this.errorMessage = error.message || 'Error al crear la publicación';
        }
      } catch (error) {
        this.errorMessage = 'Error de conexión. Intenta nuevamente.';
        console.error('Error:', error);
      }
    },
    
    handleCancel() {
      // Limpiar el formulario
      this.postData.titulo = '';
      this.postData.contenido = '';
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
  min-height: 200px;
  border-radius: 30px;
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


