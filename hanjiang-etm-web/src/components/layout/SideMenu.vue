<script lang="ts" setup>
import { computed, ref } from "vue";
import SideMenuItem from "./SideMenuItem.vue";
import { useUserStore } from "@/stores/user";
import { useMenusStore } from "@/stores/menus";

const userStore = useUserStore();
const userMenus = useMenusStore();

let menuList = computed(() => {
  return userStore.getMenus();
});

let isCollapse = ref(false);

const handleOpen = (key: string, keyPath: string[]) => {};
const handleClose = (key: string, keyPath: string[]) => {};
let activeMenu = computed(() => {
  return userMenus.getCurrentMenu();
});
</script>

<template>
  <div class="menu-box">
    <el-scrollbar height="calc(100vh - 50px - 30px - 20px)">
      <el-menu
        style="border: none"
        class="custom-menu"
        mode="vertical"
        text-color="#445160"
        active-text-color="#2079F4"
        :default-active="activeMenu.fullPath"
        :unique-opened="false"
        :router="false"
        :collapse="isCollapse"
        @open="handleOpen"
        @close="handleClose"
      >
        <SideMenuItem :data-list="menuList"></SideMenuItem>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<style lang="scss" scoped>
.menu-box {
  position: relative;
  height: calc(100%); 
  width: 100%; 
  transition: 300ms;
  background-color: #ffffff;
}

/* 被选中菜单项的样式 */
::v-deep .el-menu-item.is-active {
  background-color: #ECF5FF;
  // background-color: rgba(18, 135, 218, 0.1);
  position: relative;
}

/* 左侧竖条的样式 */
::v-deep .el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background-color: #2079F4;
}
</style>
