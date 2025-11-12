<template>
  <div class="auth-container">
    <div class="auth-box">
      <div class="auth-header">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" class="book-icon">
          <path d="M22 16.7399V4.66994C22 3.46994 21.02 2.57994 19.83 2.67994H19.77C17.67 2.85994 14.48 3.92994 12.7 5.04994L12.53 5.15994C12.24 5.33994 11.76 5.33994 11.47 5.15994L11.22 5.00994C9.44 3.89994 6.26 2.83994 4.16 2.66994C2.97 2.56994 2 3.46994 2 4.65994V16.7399C2 17.6999 2.78 18.5999 3.74 18.7199L4.03 18.7599C6.2 19.0499 9.55 20.1499 11.47 21.1999L11.51 21.2199C11.78 21.3699 12.21 21.3699 12.47 21.2199C14.39 20.1599 17.75 19.0499 19.93 18.7599L20.26 18.7199C21.22 18.5999 22 17.6999 22 16.7399Z" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M12 5.48999V20.49" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <h1>the blog</h1>
      </div>
      
      <h2>Crear Cuenta</h2>
      
      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-row">
          <div class="form-group">
            <label for="nombre">Nombre</label>
            <input 
              type="text" 
              id="nombre" 
              v-model="registerData.nombre" 
              placeholder="Tu nombre"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="apellido">Apellido</label>
            <input 
              type="text" 
              id="apellido" 
              v-model="registerData.apellido" 
              placeholder="Tu apellido"
              required
            />
          </div>
        </div>
        
        <div class="form-group">
          <label for="username">Usuario</label>
          <input 
            type="text" 
            id="username" 
            v-model="registerData.username" 
            placeholder="Elige un nombre de usuario"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="edad">Edad</label>
          <input 
            type="number" 
            id="edad" 
            v-model.number="registerData.edad" 
            placeholder="Tu edad"
            min="13"
            max="120"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="descripcion">Descripción</label>
          <textarea 
            id="descripcion" 
            v-model="registerData.descripcion" 
            placeholder="Cuéntanos algo sobre ti"
            rows="2"
            required
          ></textarea>
        </div>
        
        <div class="form-group">
          <label for="password">Contraseña</label>
          <input 
            type="password" 
            id="password" 
            v-model="registerData.contraseña" 
            placeholder="Crea una contraseña"
            required
          />
        </div>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <div v-if="successMessage" class="success-message">
          {{ successMessage }}
        </div>
        
        <button type="submit" class="btn-primary" data-tooltip="Crear tu cuenta nueva">Registrarse</button>
      </form>
      
      <div class="auth-footer">
        <p>¿Ya tienes cuenta? <router-link to="/autenticacion" data-tooltip="Ir a la página de inicio de sesión">Inicia sesión aquí</router-link></p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PaginaRegistro',
  data() {
    return {
      registerData: {
        nombre: '',
        apellido: '',
        username: '',
        edad: null,
        descripcion: '',
        contraseña: ''
      },
      errorMessage: '',
      successMessage: ''
    }
  },
  methods: {
    async handleRegister() {
      this.errorMessage = '';
      this.successMessage = '';
      
      try {
        
        const response = await fetch('http://localhost:8080/api/usuarios/crear', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.registerData)
        });
        
        if (response.ok) {
          const data = await response.json();
          this.successMessage = 'Registro exitoso! Redirigiendo...';
          
          // Guardar datos del usuario en localStorage
          localStorage.setItem('user', JSON.stringify(data));
          
          // Redirigir después de 1.5 segundos
          setTimeout(() => {
            this.$router.push('/inicio');
          }, 1500);
        } else {
          // El backend devuelve texto plano en caso de error
          const errorText = await response.text();
          this.errorMessage = errorText.replace('Error en el registro: ', '') || 'Error en el registro';
        }
      } catch (error) {
        this.errorMessage = 'Error de conexión. Inténtalo de nuevo.';
        // Error en registro
      }
    }
  }
}
</script>

<style scoped>
.auth-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5dc;
  padding: 2rem;
  overflow: hidden;
  box-sizing: border-box;
}

.auth-box {
  background-color: white;
  padding: 1.5rem;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  width: 100%;
}

.auth-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.3rem;
  margin-bottom: 0.8rem;
}

.book-icon {
  height: 2.5em;
  width: 2.5em;
}

.auth-header h1 {
  font-family: 'FuenteHeader', sans-serif;
  font-style: italic;
  font-size: 2.5rem;
  letter-spacing: -4px;
  font-weight: 700;
  color: black;
}

h2 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1rem;
  text-align: center;
  color: #333;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
  min-width: 0;
}

label {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 0.9rem;
  font-weight: 500;
  color: #333;
}

input, textarea {
  width: 100%;
  padding: 0.6rem 0.9rem;
  border: 2px solid #ddd;
  border-radius: 10px;
  font-size: 0.95rem;
  font-family: 'FuenteHeader', sans-serif;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

input:focus, textarea:focus {
  outline: none;
  border-color: #a8d5ba;
}

textarea {
  resize: vertical;
  min-height: 50px;
}

.btn-primary {
  padding: 0.75rem;
  background-color: #a8d5ba;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: 600;
  font-family: 'FuenteHeader', sans-serif;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 0.3rem;
}

.btn-primary:hover {
  background-color: #8bc9a3;
}

.error-message {
  background-color: #ffe6e6;
  color: #d32f2f;
  padding: 0.6rem;
  border-radius: 8px;
  text-align: center;
  font-size: 0.85rem;
}

.success-message {
  background-color: #e6f7ee;
  color: #2e7d32;
  padding: 0.6rem;
  border-radius: 8px;
  text-align: center;
  font-size: 0.85rem;
}

.auth-footer {
  margin-top: 0.8rem;
  text-align: center;
  font-family: 'FuenteHeader', sans-serif;
}

.auth-footer p {
  color: #666;
  font-size: 0.9rem;
}

.auth-footer a {
  color: #5a9f7a;
  font-weight: 600;
  text-decoration: none;
}

.auth-footer a:hover {
  text-decoration: underline;
}
</style>



