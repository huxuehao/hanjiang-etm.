<script lang="ts" setup>
import { useMenusStore } from '@/stores/menus'
import { useUserStore } from '@/stores/user'
import { computed } from 'vue';
import setting from "@/config/setting"
const menusStore = useMenusStore()
const userStore = useUserStore()

// 基于当前菜单的菜单列表
const menuPathList = computed(() => {
  const targetPath = menusStore.currentMenu.path
  let list = userStore.getMenuPath(targetPath)
  if(!list) {
    list = findMenuPath(targetPath)
    userStore.addMenuPathMap(targetPath, list)
  }
  return list
})

const menus = userStore.getMenus()
// 迭代（栈模拟递归）获取菜单的生命线集合
const findMenuPath = (targetPath: string) => {
  // 使用栈模拟递归
  const stack:any = [];
  for (const menu of menus) {
    // 初始化路径，并移除 children 属性
    const { children, ...menuWithoutChildren } = menu;
    stack.push({ menu: menu, path: [menuWithoutChildren] });
  }

  while (stack.length > 0) {
    const { menu, path } = stack.pop();
    // 如果找到目标菜单，返回路径
    if (menu.path === targetPath) {
      return path;
    }
    // 如果有子菜单，将子菜单加入栈
    if (menu.children && menu.children.length > 0) {
      for (const child of menu.children) {
        // 创建不包含 children 的菜单对象
        const { children, ...childWithoutChildren } = child;
        stack.push({ menu: child, path: [...path, childWithoutChildren] });
      }
    }
  }
  // 未找到目标菜单
  return null;
}
</script>

<template>
  <el-page-header>
    <template #breadcrumb>
      <el-breadcrumb separator="/">
        <!-- <el-breadcrumb-item :to="{ path: setting.homePath }" v-permission="'home'">
          <el-button link type="text" icon="HomeFilled" title="点击回到首页">首页</el-button>
        </el-breadcrumb-item> -->
        <el-breadcrumb-item v-for="menuItem in menuPathList">
          <el-button link type="text" :icon="menuItem.icon" disabled>{{ menuItem.name }}</el-button>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </template>
  </el-page-header>
</template>

<style lang="scss" scoped>
::v-deep .el-page-header {
  &__breadcrumb {
    margin: 5px 0 0 15px;
    .el-breadcrumb {
      &__item {
        .is-link {
          color: #747679;
          font-weight: normal;
          &:hover {
            color: #000;
          }
        }
        .el-breadcrumb__inner {
          font-size: 12px;
          color: #6d6f74;
        }
      }
    }
  }

  &__header {
    display: none;
  }
}

.is-link {
  color: #606266;
  &:hover {
    color: #000;
    font-weight: bold;
  }
}
</style>