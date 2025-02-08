<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { HomeFilled, Close, MoreFilled } from "@element-plus/icons-vue";
import { useMenusStore as useTabStore } from "@/stores/menus";

const router = useRouter();
const tabStore = useTabStore();

// 标签页数据
const tabs = computed(() => {
  return tabStore.tabs;
});

// 当前激活的标签页
const activeTab = computed(() => {
  return tabStore.tabs.find(
    (tab) => tab.path === router.currentRoute.value.path
  );
});

// 判断是否是当前标签页
const isActive = (path) => {
  return router.currentRoute.value.path === path;
};

// 切换标签页
const switchTab = (tab) => {
  router.push(tab.path);
};

// 关闭标签页
const closeTab = (index) => {
  if (tabs.value.length > 1) {
    tabStore.closeTab(index);
    if (isActive(tabs.value[index]?.path)) {
      switchTab(tabs.value[index - 1] || tabs.value[0]);
    }
  }
};

// 回到首页
const goHome = () => {
  router.push("/");
};

// 右键菜单
const contextMenu = ref({
  visible: false,
  top: 0,
  left: 0,
  targetTab: null,
});

const openContextMenu = (event, tab) => {
  contextMenu.value = {
    visible: true,
    top: event.clientY,
    left: event.clientX,
    targetTab: tab,
  };
};

// 关闭所有标签页
const closeAllTabs = () => {
  tabStore.closeAllTabs();
  router.push("/");
  contextMenu.value.visible = false;
};

// 关闭其他标签页
const closeOtherTabs = () => {
  const targetTab = contextMenu.value.targetTab;
  tabStore.closeOtherTabs(targetTab);
  switchTab(targetTab);
  contextMenu.value.visible = false;
};

// 关闭当前标签页
const closeCurrentTab = () => {
  const targetTab = contextMenu.value.targetTab;
  const index = tabs.value.findIndex((tab) => tab.path === targetTab.path);
  closeTab(index);
  contextMenu.value.visible = false;
};

// 功能按钮处理
const handleAction = (command) => {
  switch (command) {
    case "closeAll":
      closeAllTabs();
      break;
    case "closeOthers":
      closeOtherTabs();
      break;
    case "closeCurrent":
      closeCurrentTab();
      break;
  }
};
</script>

<template>
  <div class="custom-tabs">
    <!-- 回到首页按钮 -->
    <div class="home-button" title="回到首页" @click="goHome">
      <el-icon><HomeFilled /></el-icon>
    </div>

    <!-- 标签页区域 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab.path" type="card" @tab-remove="closeTab" closable>
        <el-tab-pane
          v-for="(tab, index) in tabs"
          :key="tab.path"
          :label="tab.title"
          :name="tab.path"
        >
        <template #label>
          <span>
            <el-icon style="margin-right: 5px;"><calendar /></el-icon>
            <span >{{ tab.title }}</span>
          </span>
        </template>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 功能按钮 -->
    <div class="actions">
      <el-dropdown trigger="click" @command="handleAction">
        <el-icon><MoreFilled /></el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="closeAll">关闭所有</el-dropdown-item>
            <el-dropdown-item command="closeOthers">关闭其他</el-dropdown-item>
            <el-dropdown-item command="closeCurrent">关闭当前</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.custom-tabs {
  display: flex;
  align-items: center;
  height: 100%;
}

.home-button {
  cursor: pointer;
  padding: 0 20px;
  color: #ebebeb;
  // border-right: #283542 solid 1px;
}
.tabs-container {
  height: 40px;
  width: calc(100vw - 100px);
}

::v-deep .el-tabs {
  .el-tabs__header {
    margin: 0;
    .el-tabs__nav-wrap {
      .el-tabs__nav-prev {
        color: rgb(125, 130, 134);
        line-height: 0;
        line-height: 20px;
      }
      .el-tabs__nav-next {
        color: rgb(85, 90, 94);
        font-weight: bolder;
        line-height: 20px;
      }
      .el-tabs__nav-scroll {
        .el-tabs__nav {
          border: none;
          .el-tabs__item {
            color: rgb(85, 90, 94);
            height: auto;
            border: none;
            min-width: 100px;
            font-size: 13px;
          }
          .is-active {
            color: #b3bcc1;
          }

        }
      }
    }
    
    
  }
}
</style>
