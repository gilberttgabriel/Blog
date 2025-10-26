import { createRouter, createWebHistory } from 'vue-router'
import MenuChats from '@/views/MenuChats.vue'
import PaginaPerfil from '@/views/PaginaPerfil.vue'
import PaginaPublicacion from '@/views/PaginaPublicacion.vue'
import PaginaInicio from "@/views/PaginaInicio.vue";
import PaginaLogin from "@/views/PaginaLogin.vue";
import PaginaRegistro from "@/views/PaginaRegistro.vue";
import CrearPublicacion from "@/views/CrearPublicacion.vue";

const routes = [
    { 
        path: '/', 
        redirect: () => {
            const user = localStorage.getItem('user');
            return user ? '/home' : '/login';
        }
    },
    { path: '/home', component: PaginaInicio, meta: { requiresAuth: true } },
    { path: '/chats', component: MenuChats, meta: { requiresAuth: true } },
    { path: '/profile', component: PaginaPerfil, meta: { requiresAuth: true } },
    { path: '/profile/:id', component: PaginaPerfil, meta: { requiresAuth: true } },
    { path: '/post/:id', component: PaginaPublicacion, meta: { requiresAuth: true } },
    { path: '/create', component: CrearPublicacion, meta: { requiresAuth: true } },
    { path: '/login', component: PaginaLogin, meta: { hideLayout: true, isPublic: true } },
    { path: '/register', component: PaginaRegistro, meta: { hideLayout: true, isPublic: true } },
    // Ruta catch-all para redirigir a login
    { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// Guard para verificar autenticación - OBLIGATORIO para todas las rutas
router.beforeEach((to, from, next) => {
    const user = localStorage.getItem('user');
    const isPublicRoute = to.meta.isPublic;
    
    // Si no hay usuario y la ruta NO es pública (login/register)
    if (!user && !isPublicRoute) {
        next('/login');
    } 
    // Si hay usuario y trata de ir a login/register, redirigir a home
    else if (user && isPublicRoute) {
        next('/home');
    } 
    // En cualquier otro caso, permitir la navegación
    else {
        next();
    }
});

export default router
