import { createRouter, createWebHistory } from "vue-router";
import Layout from "@/layout/Index.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/login.vue"),
    },
    {
      path: "/404",
      name: "404",
      component: () => import("@/views/404.vue"),
    },
    {
      path: '/train/view/:params',
      name: 'trainView',
      component: () => import('@/components/train/TrainView.vue'),
      meta: {
        name: '我的培训',
        isAuth: true
      },
    },
    {
      path: "/exam/correct/:params",
      name: "examCorrect",
      component: () => import("@/components/exam/ExamCorrect.vue"),
      meta: {
        name: '我的批改',
        isAuth: true
      },
    },
    {
      path: "/exam/question/:params",
      name: "examQuestion",
      component: () => import("@/components/exam/ExamQuestion.vue"),
      meta: {
        name: '我的考试',
        isAuth: true
      },
    },
    {
      path: "/statistics",
      component: Layout,
      children: [
        {
          path: "",
          name: "statistics",
          component: () => import("@/views/etm/statistics.vue")
        }
      ]
    },
    {
      path: "/",
      redirect: "/etm/task"
    },
    {
      path: "/etm/task",
      component: Layout,
      children: [
        {
          path: "",
          name: "任务中心",
          component: () => import("@/views/etm/task.vue"),
        }
      ]
    },
    {
      path: '/:catchAll(.*)',
      redirect: '/404'
    }
  ],
});

export default router;
