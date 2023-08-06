import AuthView from '@/views/AuthView.vue';
import DashboardView from '@/views/DashboardView.vue';
import type { RouteRecordRaw } from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router'
import { useToken } from "@/service/principal/PrincipalService";
import { State } from "@/service/principal/Principal";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Authentifizierung",
    component: AuthView,
    meta: { title: "Authentifizierung" }
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: DashboardView,
    meta: { title: "Dashboard" }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

const { state } = useToken();

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title}`;
  // next()
  const openForAllRoutes = [/^\/\/?$/g];
  const isAuthenticated = state.value === State.LOGGED_IN;
  if (openForAllRoutes.some(r => r.test(to.path)) || isAuthenticated) {
    next();
  } else {
    next({ name: "Authentifizierung" });
  }
});

export default router
