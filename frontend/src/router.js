import { createRouter, createWebHistory } from 'vue-router'
import MenuChats from '@/views/MenuChats.vue'
import PaginaChat from '@/views/PaginaChat.vue'
import PaginaPerfil from '@/views/PaginaPerfil.vue'
import PaginaPublicacion from '@/views/PaginaPublicacion.vue'
import PaginaAnuncio from '@/views/PaginaAnuncio.vue'
import PaginaInicio from "@/views/PaginaInicio.vue";
import PaginaLogin from "@/views/PaginaLogin.vue";
import PaginaRegistro from "@/views/PaginaRegistro.vue";
import CrearPublicacion from "@/views/CrearPublicacion.vue";
import CrearAnuncio from "@/views/CrearAnuncio.vue";

const routes = [
    { 
        path: '/', 
        redirect: () => {
            const user = localStorage.getItem('user');
            return user ? '/inicio' : '/autenticacion';
        }
    },
    { path: '/inicio', component: PaginaInicio, meta: { requiresAuth: true } },
    { path: '/chats', component: MenuChats, meta: { requiresAuth: true } },
    { path: '/chat/:chatId', component: PaginaChat, meta: { requiresAuth: true } },
    { path: '/perfil', component: PaginaPerfil, meta: { requiresAuth: true } },
    { path: '/perfil/:id', component: PaginaPerfil, meta: { requiresAuth: true } },
    { path: '/publicacion/:id', component: PaginaPublicacion, meta: { requiresAuth: true } },
    { path: '/anuncio/:id', component: PaginaAnuncio, meta: { requiresAuth: true } },
    { path: '/crear', component: CrearPublicacion, meta: { requiresAuth: true } },
    { path: '/crear-anuncio', component: CrearAnuncio, meta: { requiresAuth: true } },
    { path: '/autenticacion', component: PaginaLogin, meta: { hideLayout: true, isPublic: true } },
    { path: '/registro', component: PaginaRegistro, meta: { hideLayout: true, isPublic: true } },
    // Ruta catch-all para redirigir a login
    { path: '/:pathMatch(.*)*', redirect: '/autenticacion' }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// Comprobacion de si estas logeado o no
router.beforeEach((to, from, next) => {
    const user = localStorage.getItem('user');
    const isPublicRoute = to.meta.isPublic;
    
    // Si no hay usuario y la ruta NO es pública (login/register)
    if (!user && !isPublicRoute) {
        next('/autenticacion');
    } 
    // Si hay usuario y trata de ir a login/register, redirigir a home
    else if (user && isPublicRoute) {
        next('/inicio');
    } 
    // En cualquier otro caso, permitir la navegación
    else {
        next();
    }
});

export default router
