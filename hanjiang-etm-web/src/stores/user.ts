import { ref } from "vue";
import { defineStore } from "pinia";
import { login, logout } from "@/api/user";
import cache from "@/utils/cache";
import {
  setToken,
  setRefreshToken,
  removeToken,
  removeRefreshToken,
  removePermissions
} from "@/utils/auth";
import { menuTreePermission } from "@/api/menu";
import dynamicRouter from "@/router/dynamicRouter";

export const useUserStore = defineStore("user", () => {
  // 全局加载
  let globalLoading = ref(false);

  // 用户信息
  const userInfo = ref(cache.local.getJSON("userInfo", {}));
  const setUserInfo= (user: any) => {
    userInfo.value = user;
    cache.local.setJSON("userInfo", user);
  };
  const getUserInfo = () => {
    if (userInfo.value.length === 0) {
      userInfo.value = cache.local.getJSON("userInfo", []);
    }
    return userInfo.value;
  };
  const removeUserInfo = () => {
    userInfo.value = [];
    cache.local.setJSON("userInfo", []);
  };
  

  // 菜单列表
  let menuList = ref(cache.local.getJSON("menus", []));
  const setMenus = (menus: any) => {
    menuList.value = menus;
    cache.local.setJSON("menus", menus);
    // 生成动态路由
    dynamicRouter.dynamicRouteGen()
  };
  const getMenus = () => {
    if (menuList.value.length === 0) {
      menuList.value = cache.local.getJSON("menus", []);
    }
    return menuList.value;
  };
  const removeMenus = () => {
    menuList.value = [];
    cache.local.setJSON("menus", []);
  };

  // 菜单路径缓存
  let menuPathMap = ref(cache.local.getJSON("menuPathMap", {}));
  const addMenuPathMap = (path: string, menuItem: any) => {
    menuPathMap.value[path] = menuItem
    cache.local.setJSON("menuPathMap", menuPathMap.value);
  };
  const getMenuPath = (path: string) => {
    return menuPathMap.value[path]
  };
  const removeMenuPath = () => {
    menuPathMap.value = {}
    cache.local.setJSON("menuPathMap", {});
  };

  // 登录
  const doLogin = (body: any) => {
    globalLoading.value = true;
    return new Promise((resolve, reject) => {
      login(body)
        .then((res) => {
          const userInfo_ = res.data.data;
          setUserInfo(userInfo_)
          setToken(userInfo_.accessToken);
          setRefreshToken(userInfo_.refreshToken);
          // 获取菜单权限
          menuTreePermission()
            .then((res) => {
              const menuList = res.data.data;
              setMenus(menuList)
              globalLoading.value = false;
              resolve({});
            })
            .catch((error) => {
              globalLoading.value = false;
              reject(error);
            });
        })
        .catch((error) => {
          globalLoading.value = false;
          reject(error);
        })
    });
  };

  // 退出
  const doLogout = (id: string) => {
    globalLoading.value = true;
    return new Promise((resolve, reject) => {
      logout(id)
        .then(() => {
          removeToken();
          removeRefreshToken();
          removeMenus()
          removeUserInfo()
          removeMenuPath()
          removePermissions()
          resolve({});
        })
        .catch((error) => {
          reject(error);
        })
        .finally(() => {
          globalLoading.value = false;
        });
    });
  };

  return { 
    userInfo, 
    globalLoading, 
    doLogin, 
    doLogout, 
    getMenus,

    addMenuPathMap,
    getMenuPath,
    removeMenuPath,
  };
});
