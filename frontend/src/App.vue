<template>
  <div>
    <aside v-if="!hideLayout">
      <nav>
        <h1><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none"><path d="M22 16.7399V4.66994C22 3.46994 21.02 2.57994 19.83 2.67994H19.77C17.67 2.85994 14.48 3.92994 12.7 5.04994L12.53 5.15994C12.24 5.33994 11.76 5.33994 11.47 5.15994L11.22 5.00994C9.44 3.89994 6.26 2.83994 4.16 2.66994C2.97 2.56994 2 3.46994 2 4.65994V16.7399C2 17.6999 2.78 18.5999 3.74 18.7199L4.03 18.7599C6.2 19.0499 9.55 20.1499 11.47 21.1999L11.51 21.2199C11.78 21.3699 12.21 21.3699 12.47 21.2199C14.39 20.1599 17.75 19.0499 19.93 18.7599L20.26 18.7199C21.22 18.5999 22 17.6999 22 16.7399Z" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/><path d="M12 5.48999V20.49" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/><path d="M7.75 8.48999H5.5" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/><path d="M8.5 11.49H5.5" stroke="#292D32" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg><span>blog</span></h1>

        <!-- Sidebar para Administrador -->
        <template v-if="isAdmin">
          <router-link to="/inicio" class="homePage">
            <svg viewBox="0 -0.5 25 25" fill="none" xmlns="http://www.w3.org/2000/svg">
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M18.867 15.8321L18.873 10.0391L14.75 5.92908C13.5057 4.69031 11.4942 4.69031 10.25 5.92908L6.13599 10.0291V15.8291C6.1393 17.5833 7.56377 19.0028 9.31799 19.0001H15.685C17.438 19.0029 18.862 17.5851 18.867 15.8321Z" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M19.624 6.01807C19.624 5.60385 19.2882 5.26807 18.874 5.26807C18.4598 5.26807 18.124 5.60385 18.124 6.01807H19.624ZM18.874 10.0391H18.124C18.124 10.2384 18.2033 10.4295 18.3445 10.5702L18.874 10.0391ZM19.9705 12.1912C20.2638 12.4837 20.7387 12.4829 21.0311 12.1896C21.3236 11.8962 21.3229 11.4214 21.0295 11.1289L19.9705 12.1912ZM6.66552 10.5602C6.95886 10.2678 6.95959 9.79289 6.66714 9.49955C6.3747 9.20621 5.89982 9.20548 5.60648 9.49793L6.66552 10.5602ZM3.97048 11.1289C3.67714 11.4214 3.67641 11.8962 3.96886 12.1896C4.2613 12.4829 4.73618 12.4837 5.02952 12.1912L3.97048 11.1289ZM13.75 19.0001C13.75 19.4143 14.0858 19.7501 14.5 19.7501C14.9142 19.7501 15.25 19.4143 15.25 19.0001H13.75ZM9.75 19.0001C9.75 19.4143 10.0858 19.7501 10.5 19.7501C10.9142 19.7501 11.25 19.4143 11.25 19.0001H9.75ZM18.124 6.01807V10.0391H19.624V6.01807H18.124ZM18.3445 10.5702L19.9705 12.1912L21.0295 11.1289L19.4035 9.50792L18.3445 10.5702ZM5.60648 9.49793L3.97048 11.1289L5.02952 12.1912L6.66552 10.5602L5.60648 9.49793ZM15.25 19.0001V17.2201H13.75V19.0001H15.25ZM15.25 17.2201C15.25 15.7013 14.0188 14.4701 12.5 14.4701V15.9701C13.1904 15.9701 13.75 16.5297 13.75 17.2201H15.25ZM12.5 14.4701C10.9812 14.4701 9.75 15.7013 9.75 17.2201H11.25C11.25 16.5297 11.8096 15.9701 12.5 15.9701V14.4701ZM9.75 17.2201V19.0001H11.25V17.2201H9.75Z" fill="#000000"></path>
              </g>
            </svg>
            <span>home</span>
          </router-link>
          
          <router-link to="/crear-anuncio" class="createPost">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path d="M14 6L8 12V16H12L18 10M14 6L17 3L21 7L18 10M14 6L18 10M10 4L4 4L4 20L20 20V14" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
              </g>
            </svg>
            <span>crear anuncio</span>
          </router-link>
        </template>

        <!-- Sidebar para Usuario normal -->
        <template v-else>
          <router-link  to="/inicio"  class="homePage"><svg viewBox="0 -0.5 25 25" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path fill-rule="evenodd" clip-rule="evenodd" d="M18.867 15.8321L18.873 10.0391L14.75 5.92908C13.5057 4.69031 11.4942 4.69031 10.25 5.92908L6.13599 10.0291V15.8291C6.1393 17.5833 7.56377 19.0028 9.31799 19.0001H15.685C17.438 19.0029 18.862 17.5851 18.867 15.8321Z" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M19.624 6.01807C19.624 5.60385 19.2882 5.26807 18.874 5.26807C18.4598 5.26807 18.124 5.60385 18.124 6.01807H19.624ZM18.874 10.0391H18.124C18.124 10.2384 18.2033 10.4295 18.3445 10.5702L18.874 10.0391ZM19.9705 12.1912C20.2638 12.4837 20.7387 12.4829 21.0311 12.1896C21.3236 11.8962 21.3229 11.4214 21.0295 11.1289L19.9705 12.1912ZM6.66552 10.5602C6.95886 10.2678 6.95959 9.79289 6.66714 9.49955C6.3747 9.20621 5.89982 9.20548 5.60648 9.49793L6.66552 10.5602ZM3.97048 11.1289C3.67714 11.4214 3.67641 11.8962 3.96886 12.1896C4.2613 12.4829 4.73618 12.4837 5.02952 12.1912L3.97048 11.1289ZM13.75 19.0001C13.75 19.4143 14.0858 19.7501 14.5 19.7501C14.9142 19.7501 15.25 19.4143 15.25 19.0001H13.75ZM9.75 19.0001C9.75 19.4143 10.0858 19.7501 10.5 19.7501C10.9142 19.7501 11.25 19.4143 11.25 19.0001H9.75ZM18.124 6.01807V10.0391H19.624V6.01807H18.124ZM18.3445 10.5702L19.9705 12.1912L21.0295 11.1289L19.4035 9.50792L18.3445 10.5702ZM5.60648 9.49793L3.97048 11.1289L5.02952 12.1912L6.66552 10.5602L5.60648 9.49793ZM15.25 19.0001V17.2201H13.75V19.0001H15.25ZM15.25 17.2201C15.25 15.7013 14.0188 14.4701 12.5 14.4701V15.9701C13.1904 15.9701 13.75 16.5297 13.75 17.2201H15.25ZM12.5 14.4701C10.9812 14.4701 9.75 15.7013 9.75 17.2201H11.25C11.25 16.5297 11.8096 15.9701 12.5 15.9701V14.4701ZM9.75 17.2201V19.0001H11.25V17.2201H9.75Z" fill="#000000"></path> </g></svg><span>home</span></router-link>
          <router-link  to="/perfil" class="profile"><svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M14.3365 12.3466L14.0765 11.9195C13.9082 12.022 13.8158 12.2137 13.8405 12.4092C13.8651 12.6046 14.0022 12.7674 14.1907 12.8249L14.3365 12.3466ZM9.6634 12.3466L9.80923 12.8249C9.99769 12.7674 10.1348 12.6046 10.1595 12.4092C10.1841 12.2137 10.0917 12.022 9.92339 11.9195L9.6634 12.3466ZM4.06161 19.002L3.56544 18.9402L4.06161 19.002ZM19.9383 19.002L20.4345 18.9402L19.9383 19.002ZM16 8.5C16 9.94799 15.2309 11.2168 14.0765 11.9195L14.5965 12.7737C16.0365 11.8971 17 10.3113 17 8.5H16ZM12 4.5C14.2091 4.5 16 6.29086 16 8.5H17C17 5.73858 14.7614 3.5 12 3.5V4.5ZM7.99996 8.5C7.99996 6.29086 9.79082 4.5 12 4.5V3.5C9.23854 3.5 6.99996 5.73858 6.99996 8.5H7.99996ZM9.92339 11.9195C8.76904 11.2168 7.99996 9.948 7.99996 8.5H6.99996C6.99996 10.3113 7.96342 11.8971 9.40342 12.7737L9.92339 11.9195ZM9.51758 11.8683C6.36083 12.8309 3.98356 15.5804 3.56544 18.9402L4.55778 19.0637C4.92638 16.1018 7.02381 13.6742 9.80923 12.8249L9.51758 11.8683ZM3.56544 18.9402C3.45493 19.8282 4.19055 20.5 4.99996 20.5V19.5C4.70481 19.5 4.53188 19.2719 4.55778 19.0637L3.56544 18.9402ZM4.99996 20.5H19V19.5H4.99996V20.5ZM19 20.5C19.8094 20.5 20.545 19.8282 20.4345 18.9402L19.4421 19.0637C19.468 19.2719 19.2951 19.5 19 19.5V20.5ZM20.4345 18.9402C20.0164 15.5804 17.6391 12.8309 14.4823 11.8683L14.1907 12.8249C16.9761 13.6742 19.0735 16.1018 19.4421 19.0637L20.4345 18.9402Z" fill="#000000"></path></svg><span>perfil</span></router-link>
          <router-link  to="/chats"  class="messages"><svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M8 10H8.01" stroke="#323232" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M12 10H12.01" stroke="#323232" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M16 10H16.01" stroke="#323232" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M21 13V7C21 5.11438 21 4.17157 20.4142 3.58579C19.8284 3 18.8856 3 17 3H7C5.11438 3 4.17157 3 3.58579 3.58579C3 4.17157 3 5.11438 3 7V13C3 14.8856 3 15.8284 3.58579 16.4142C4.17157 17 5.11438 17 7 17H7.5C7.77614 17 8 17.2239 8 17.5V20V20.1499C8 20.5037 8.40137 20.7081 8.6875 20.5L13.0956 17.2941C13.3584 17.103 13.675 17 14 17H17C18.8856 17 19.8284 17 20.4142 16.4142C21 15.8284 21 14.8856 21 13Z" stroke="#323232" stroke-width="2" stroke-linejoin="round"></path> </g></svg><span>chats</span></router-link>
          <router-link  to="/crear" class="createPost"><svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M14 6L8 12V16H12L18 10M14 6L17 3L21 7L18 10M14 6L18 10M10 4L4 4L4 20L20 20V14" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path> </g></svg><span>crear post</span></router-link>
        </template>

        <br>
        <br>
        <a @click="handleLogout" class="getOut"><svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M15 4H18C19.1046 4 20 4.89543 20 6V18C20 19.1046 19.1046 20 18 20H15M8 8L4 12M4 12L8 16M4 12L16 12" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path> </g></svg><span>salir</span></a>
      </nav>
    </aside>
    <main :class="hideLayout ? 'content-full' : 'content'">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      isAdmin: false
    }
  },
  computed: {
    hideLayout() {
      return this.$route.meta.hideLayout || false;
    }
  },
  watch: {
    '$route'() {
      this.checkUserType();
    }
  },
  mounted() {
    this.checkUserType();
  },
  methods: {
    checkUserType() {
      const user = localStorage.getItem('user');
      if (user) {
        const userData = JSON.parse(user);
        // Verificar si el username es "admin" para determinar si es administrador
        this.isAdmin = userData.username === 'admin';
      }
    },
    handleLogout() {
      localStorage.removeItem('user');
      this.isAdmin = false;
      this.$router.push('/autenticacion');
    }
  }
}
</script>

<style>
  :root {
    --sidebar-width: clamp(150px, 20vw, 320px);
  }

  * {
    margin: 0;
    padding: 0;
    border: 0;
    font-size: 100%;
    font: inherit;
    box-sizing: border-box;
    text-decoration: none;
  }

  body{
    flex-direction: column;
  }

  aside {
    display: flex;
    width: var(--sidebar-width);
    min-width: 180px;
    height: 100vh;
    position: fixed;
    top: 0;
    left: 0;
    background-color: antiquewhite;
    z-index: 1000;
  }



  .header {
    width: 100%;
    min-width: 0;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    height: 10vh;
    padding: 1rem 2rem;
    background-color: #f5f5f5;
    box-shadow: 0 2px 8px rgba(0,0,0,0.07);
    border-bottom: 1px solid #e0e0e0;
    position: sticky;
    top: 0;
  }


  nav{
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    width: 100%;
    height: 100%;
    overflow: hidden;
    margin: 1.25rem;

  }

  h1{
    display: flex;
    align-items: center;
    gap: 0.1em;

    font-family: 'FuenteHeader', sans-serif;
    font-style: italic;


    font-size: 2rem;
    letter-spacing: -4px;
    font-weight: 700;
    margin: 0  0 1.5rem 0;
    color: black;
  }

  nav>a{
    display: flex;
    align-items: center;
    gap: 0.6rem;
    font-family: 'FuenteHeader', sans-serif;
    font-size: 1.1rem;
    color: black;
    margin-bottom: 0.2rem;
    padding-left: 7px;
  }

  nav>a:hover{
    color: blue;
    cursor: pointer;
  }

  .getOut {
    cursor: pointer;
  }

  svg {
    height: 2em;
    width: 2em;
  }

  .content {
    margin-left: var(--sidebar-width);
    padding: 0;
  }

  .content-full {
    margin-left: 0;
    padding: 0;
    width: 100%;
  }

</style>
