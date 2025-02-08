import router from "@/router";
import { getToken } from "./utils/auth";
import setting from "./config/setting";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
NProgress.configure({ easing: "ease", speed: 150, showSpinner: false });
import { useMenusStore } from '@/stores/menus'
import cache from "./utils/cache";
import { getButtonPermissions } from "./api/role";
import { isEmpty } from "./utils/tools";

let userMenus:any = null

router.beforeEach(async (to, from, next) => {
  NProgress.start();

  // 使用pinia
  if(userMenus === null) {
    userMenus =  useMenusStore()
  }

  if (getToken()) {
    // 获取按钮权限
    if(isEmpty(cache.local.getJSON(setting.permissions))) {
      await getButtonPermissions().then((res) => {
        cache.local.setJSON(setting.permissions, res.data.data)
      })
    }
    if (to.path === "/login") {
      next({ path: "/" });
      NProgress.done();
    } else {
      userMenus.setCurrentMenu({title: to.meta.name ,fullPath: to.fullPath, icon: to.meta.icon, path: to.path});
      next();
    }
  } else {
    if (setting.whiteList.indexOf(to.path) !== -1) {
      next();
    } else {
      if (to.fullPath.length > 1) {
        next(`/login?redirect=${to.fullPath}`);
      } else {
        next("/login");
      }
      NProgress.done();
    }
  }
});

router.afterEach((to, from) => {
  NProgress.done();
});
