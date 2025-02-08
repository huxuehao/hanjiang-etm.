import router from "./index";
import cache from "@/utils/cache";
import { defineAsyncComponent } from 'vue';
const views = import.meta.glob('@/views/**/*.vue');

/**
 * 生成路由
 * @param config 菜单配置
 */
const genRouter = (config: any) => {
  const { id, name, icon, alias, path } = config;

  // 规范路径
  let componentPath = path.startsWith("/") ? path.slice(1) : path;
  componentPath = componentPath.endsWith(".vue")
    ? componentPath.slice(0, componentPath.length - 3)
    : componentPath;

  // 路由校验
  if(!views[`/src/views/${componentPath}.vue`]) {
    return
  }
  // 动态加载组件
  const component = defineAsyncComponent(() =>
    views[`/src/views/${componentPath}.vue`]()
      .then((module: any) => module.default)
      .catch(() => import('@/views/404.vue')) // 404
  );

  // 动态加载组件
  const currentRoute = {
    path: `/${componentPath}`,
    component: () => import("@/layout/Index.vue"), // 布局组件
    children: [
      {
        path: "",
        component: component, // 动态加载视图组件
        name: id,
        meta: {
          name: alias || name,
          icon,
        },
      },
    ],
  };

  // 添加路由
  router.addRoute(currentRoute);
};

/**
 * 递归生成路由
 * @param menuList 菜单列表
 */
const doGenRouter = (menuList: any) => {
  for (let menu of menuList) {
    if (!menu.children || menu.children.length === 0) {
      genRouter(menu); // 生成路由
    } else {
      doGenRouter(menu.children); // 递归处理子菜单
    }
  }
};

/**
 * 生成动态路由
 */
const dynamicRouteGen = () => {
  const menus = cache.local.getJSON("menus", []); // 从缓存中获取菜单
  doGenRouter(menus); // 生成路由
};

export default {
  dynamicRouteGen,
  genRouter
};