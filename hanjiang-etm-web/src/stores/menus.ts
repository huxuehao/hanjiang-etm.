import { defineStore } from "pinia";
import { ref } from "vue";
import setting from "@/config/setting"

export const useMenusStore = defineStore("menus", () => {
  // 当前菜单
  let currentMenu = ref<any>({});
  const setCurrentMenu = (menu: any) => {
    currentMenu.value = menu;
    addMenuToList(menu);
  };
  const getCurrentMenu = () => {
    return currentMenu.value;
  };

  // 已经打开过的菜单列表
  let openedMenuList = ref<any[]>([]);
  const addMenuToList = (menu:any) => {
    const existMenu = openedMenuList.value.filter(
      (item) => item == menu.fullPath
    );
    if (existMenu.length === 0) {
      openedMenuList.value.push(menu);
    }
    addTab(menu.title, menu.fullPath)
  };
  const getMenuList = () => {
    return openedMenuList.value;
  };
  const removeMenuFromList = (fullPath: string) => {
    openedMenuList.value = openedMenuList.value.filter(
      (item) => item.fullPath != fullPath
    );
  };
  const removeAllMenuFromList = () => {
    openedMenuList.value = [];
  };

  // 标签页
  let tabs = ref<any[]>([{ title: "首页", path: setting.homePath }])
  const addTab = (title:string, path: string) => {
    const index = tabs.value.findIndex((tab) => tab.path === path);
    if(index < 0) {
      tabs.value.push({ title, path });
    }
  };
  const closeTab = (index: number) => {
    if (tabs.value.length > 1) {
      tabs.value.splice(index, 1);
    }
  };
  const closeAllTabs = (indexTab: any[] = [{ title: "首页", path: setting.homePath }]) => {
    tabs.value = indexTab;
  };
  
  const closeOtherTabs = (currentTab: any) => {
    tabs.value = [{ title: "首页", path: setting.homePath }, currentTab];
  };

  const closeCurrentTab = (currentTab: any) => {
    const index = tabs.value.findIndex((tab) => tab.path === currentTab.path);
    closeTab(index);
  };

  return {
    currentMenu,
    setCurrentMenu,
    getCurrentMenu,
    openedMenuList,
    addMenuToList,
    getMenuList,
    removeMenuFromList,
    removeAllMenuFromList,
    tabs,
    addTab,
    closeTab,
    closeAllTabs,
    closeOtherTabs,
    closeCurrentTab,
  };
});
