<template>
  <div class="auth-container">
    <div class="auth-box">
      <div class="auth-header">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" class="book-icon">
          <path d="M22 16.7399V4.66994C22 3.46994 21.02 2.57994 19.83 2.67994H19.77C17.67 2.85994 14.48 3.92994 12.7 5.04994L12.53 5.15994C12.24 5.33994 11.76 5.33994 11.47 5.15994L11.22 5.00994C9.44 3.89994 6.26 2.83994 4.16 2.66994C2.97 2.56994 2 3.46994 2 4.65994V16.7399C2 17.6999 2.78 18.5999 3.74 18.7199L4.03 18.7599C6.2 19.0499 9.55 20.1499 11.47 21.1999L11.51 21.2199C11.78 21.3699 12.21 21.3699 12.47 21.2199C14.39 20.1599 17.75 19.0499 19.93 18.7599L20.26 18.7199C21.22 18.5999 22 17.6999 22 16.7399Z" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M12 5.48999V20.49" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <h1>blog</h1>
      </div>
      
      <h2>Iniciar Sesión</h2>
      
      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label for="username">Usuario</label>
          <input 
            type="text" 
            id="username" 
            v-model="loginData.username" 
            placeholder="Ingresa tu usuario"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">Contraseña</label>
          <input 
            type="password" 
            id="password" 
            v-model="loginData.contraseña" 
            placeholder="Ingresa tu contraseña"
            required
          />
        </div>
        
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <button type="submit" class="btn-primary">Ingresar</button>
      </form>
      
      <div class="auth-footer">
        <p>¿No tienes cuenta? <router-link to="/registro">Regístrate aquí</router-link></p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PaginaLogin',
  data() {
    return {
      loginData: {
        username: '',
        contraseña: ''
      },
      errorMessage: ''
    }
  },
  methods: {
    async handleLogin() {
      this.errorMessage = '';
      
      try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.loginData)
        });
        
        if (response.ok) { // si no hubo error en el login / credenciales correctas
          const data = await response.json();
          // Guardar datos del usuario en localStorage
          localStorage.setItem('user', JSON.stringify(data));
          // Redirigir a la página principal
          this.$router.push('/inicio');
        } else {
          const error = await response.json();
          this.errorMessage = error.error || 'Error al iniciar sesión';
        }
      } catch (error) {
        this.errorMessage = 'Error de conexión. Intenta nuevamente.';
        console.error('Error:', error);
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
  padding: 3rem;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  max-width: 450px;
  width: 100%;
}

.auth-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.3rem;
  margin-bottom: 2rem;
}

.book-icon {
  height: 3em;
  width: 3em;
}

.auth-header h1 {
  font-family: 'FuenteHeader', sans-serif;
  font-style: italic;
  font-size: 3rem;
  letter-spacing: -4px;
  font-weight: 700;
  color: black;
}

h2 {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 2rem;
  text-align: center;
  color: #333;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  font-family: 'FuenteHeader', sans-serif;
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}

input {
  padding: 0.9rem 1.2rem;
  border: 2px solid #ddd;
  border-radius: 10px;
  font-size: 1rem;
  font-family: 'FuenteHeader', sans-serif;
  transition: border-color 0.3s;
}

input:focus {
  outline: none;
  border-color: #a8d5ba;
}

.btn-primary {
  padding: 1rem;
  background-color: #a8d5ba;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: 600;
  font-family: 'FuenteHeader', sans-serif;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 1rem;
}

.btn-primary:hover {
  background-color: #8bc9a3;
}

.error-message {
  background-color: #ffe6e6;
  color: #d32f2f;
  padding: 0.8rem;
  border-radius: 8px;
  text-align: center;
  font-size: 0.9rem;
}

.auth-footer {
  margin-top: 2rem;
  text-align: center;
  font-family: 'FuenteHeader', sans-serif;
}

.auth-footer p {
  color: #666;
  font-size: 0.95rem;
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



