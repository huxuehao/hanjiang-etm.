<script lang="ts" setup>
import router from "@/router/index";
import { tansRequestParams, isEmpty } from "@/utils/tools";

// 定义props并设置initvalue
withDefaults(
  defineProps<{
    dataList: any[];
  }>(),
  {
    dataList: () => [],
  }
);

const openMenu = (item: any) => {
  const { path, query } = parseMenu(item);
  router.push({ path, query });
};

/**
 * 解析菜单
 * @param menu 菜单元素
 */
const parseMenu = (menu: any) => {
  let querys = JSON.parse(menu.params || "[]");
  let query_: any = {};
  querys.forEach((item: any) => {
    query_[item.key] = item.value;
  });

  let fullPath = menu.path;
  if (!isEmpty(query_)) {
    fullPath += "?" + tansRequestParams(query_);
  }

  return {
    path: menu.path,
    query: query_,
    fullPath,
  };
};
</script>

<template>
  <template v-for="item in dataList">
    <el-menu-item
      v-if="!item.children?.length"
      @click="openMenu(item)"
      :index="parseMenu(item).fullPath"
    >
      <template #title>
        <el-icon class="default-icon">
          <component :is="item.icon ? item.icon : 'Tickets'" />
        </el-icon>
        <span class="default-font">{{ item.name }}</span>
      </template>
    </el-menu-item>
    <el-sub-menu v-else :index="item.path">
      <template #title>
        <el-icon class="default-icon">
          <component :is="item.icon ? item.icon : 'Tickets'" />
        </el-icon>
        <span>{{ item.name }}</span>
      </template>
      <SideMenuItem :dataList="item.children"></SideMenuItem>
    </el-sub-menu>
  </template>
</template>

<style lang="scss" scoped>
.default-font {
  font-size: 14px;
  user-select: none;
}
.default-icon {
  font-size: 15px;
}
</style>
