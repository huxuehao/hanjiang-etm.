<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { HomeFilled, Close, MoreFilled } from "@element-plus/icons-vue";
import { useMenusStore as useTabStore } from '@/stores/menus'

const router = useRouter();
const tabStore = useTabStore()

// 标签页数据
const tabs = computed(() => {
  return tabStore.tabs
})

// 当前激活的标签页
const activeTab = computed(() => {
  return tabStore.tabs.find((tab) => tab.path === router.currentRoute.value.path);
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
    tabStore.closeTab(index)
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
  tabStore.closeAllTabs()
  router.push("/");
  contextMenu.value.visible = false;
};

// 关闭其他标签页
const closeOtherTabs = () => {
  const targetTab = contextMenu.value.targetTab;
  tabStore.closeOtherTabs(targetTab)
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
        <div
          v-for="(tab, index) in tabs"
          :key="tab.path"
          class="tab-item"
          :class="{ active: isActive(tab.path) }"
          @click="switchTab(tab)"
          @contextmenu.prevent="openContextMenu($event, tab)"
        >
          <span style="margin-left: 5px;">{{ tab.title }}</span>
          <!-- <el-icon
            v-if="tabs.length > 1"
            class="close-icon"
            @click.stop="closeTab(index)"
          >
            <Close />
          </el-icon> -->
        </div>
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
  
      <!-- 右键菜单 -->
      <div
        v-if="contextMenu.visible"
        class="context-menu"
        :style="{ top: contextMenu.top + 'px', left: contextMenu.left + 'px' }"
      >
        <div @click="closeAllTabs">关闭所有</div>
        <div @click="closeOtherTabs">关闭其他</div>
        <div @click="closeCurrentTab">关闭当前</div>
      </div>
    </div>
  </template>

<style scoped>
.custom-tabs {
  display: flex;
  align-items: center;
  height: 100%;
  /* background-color: #283542; */
  /* padding: 0 10px; */
}

.home-button {
  cursor: pointer;
  padding: 0 20px;
  /* margin-top: 5px; */
  color: #F5F6F9;
  border-right: #283542 solid 1px;
}

.tabs-container {
  display: flex;
  flex: 1;
  overflow-x: auto;
  margin: 0 10px;
  height: 100%;
}

.tab-item {
  position: relative;
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  text-align: center; /* 多行文字水平居中 */
  
  min-width: 50px;
  padding: 0 10px;
  /* margin: 0 5px; */
  height: 100%;
  color: #636D75;
  background-color: #1C2834;
  cursor: pointer;
  font-size: 12px;
  letter-spacing: 1px;
  border-right: #283542 solid 1px;
  /* border-radius: 4px; */
}

.tab-item.active {
  /* background-color: #F5F6F9; */
  color: #dadada;
}

.tab-item .close-icon {
  margin-left: 10px;
  font-size: 12px;
  color: #636D75;
}

.tab-item.active .close-icon {
  color: #000;
}

.actions {
  cursor: pointer;
  padding: 10px;
  color: #F5F6F9;
}

.context-menu {
  position: fixed;
  background-color: #F5F6F9;
  border: 1px solid #ddd;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
  z-index: 1000;
}

.context-menu div {
  padding: 10px;
  cursor: pointer;
}

.context-menu div:hover {
  background-color: #f5f5f5;
}
</style>
