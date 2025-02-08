import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import "./style/element/override.scss"; // 添加自定义样式
import zhCn from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import "./permission";
import router from "./router";
import { getUserPermissions } from "./utils/tools"
// 生成动态路由
import dynamicRouter from "./router/dynamicRouter";
dynamicRouter.dynamicRouteGen()

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.use(ElementPlus, {
  size: "small",
  zIndex: 3000,
  locale: zhCn,
});

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

// 自定义权限指令
app.directive('permission', {
  async mounted(el, binding) {
    const requiredPermission = binding.value; // 获取绑定的权限值
    // 获取当前用户的权限
    await getUserPermissions().then((res: any) => {
      const userPermissions = res
      // 如果用户没有该权限，禁用或隐藏按钮
      if (!userPermissions.includes(requiredPermission)) {
        el.parentNode.removeChild(el); // 从 DOM 中移除按钮
      }
    })
  }
});

app.mount("#app");
