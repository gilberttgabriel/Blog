import { createRouter, createWebHistory } from 'vue-router'
import ChatMenu from '@/views/ChatMenu.vue'
import ProfilePage from '@/views/ProfilePage.vue'
import PostPage from '@/views/PostPage.vue'
import HomePage from "@/views/HomePage.vue";

const routes = [
    { path: '/', component: HomePage },
    { path: '/chats', component: ChatMenu },
    { path: '/profile', component: ProfilePage },
    { path: '/post', component: PostPage}
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
