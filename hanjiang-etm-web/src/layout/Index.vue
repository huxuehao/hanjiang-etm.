<script lang="ts" setup>
import { RouterView } from "vue-router";
import router from "@/router/index";
import HeaderNav from "@/components/layout/HeaderNav.vue";
import SideMenu from "@/components/layout/SideMenu.vue";
import PageHeader from "@/components/layout/PageHeader.vue";
import { ref } from "vue";
import setting from "@/config/setting"
import { useUserStore } from "@/stores/user";
import UserInfoForm from "@/components/user/UserInfoForm.vue";

let collapse = ref(false);

// 当菜单为一个的时候，菜单默认折叠
const userStore = useUserStore();
const menus = userStore.getMenus()
if(menus.length === 1 && !menus[0].children) {
  collapse.value = true
}

const changeBar = () => {
  collapse.value = !collapse.value;
};

const goBackHome = () => {
  router.push({ path: setting.homePath });
};

let userInfoVisible = ref(false)
const openUserInfo = () => {
  userInfoVisible.value = true
};
</script>

<template>
  <div class="box">
    <el-row :gutter="0" class="header-nav">
      <HeaderNav @openUserInfo="openUserInfo"></HeaderNav>
    </el-row>
    <el-row :gutter="0" class="header-tag">
      <el-col :span="23">
        <PageHeader></PageHeader>
      </el-col>
      <el-col :span="1" class="opt">
        <el-dropdown :hide-on-click="false">
          <el-button v-permission="'more'" link type="" icon="ArrowDown">更多</el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>
                <el-button v-permission="'home'" link type="text" icon="HomeFilled" @click="goBackHome()">回到首页</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button v-permission="'userInfo'" link type="text" icon="UserFilled" @click="openUserInfo()">个人信息</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-col>
    </el-row>
    <el-row :gutter="0" class="content">
      <el-col :span="collapse ? 0 : 3" class="side-menu">
        <SideMenu></SideMenu>
        <div v-show="!collapse" class="touch-bar-left" @click="changeBar" title="折叠菜单">
          <el-icon><ArrowLeft /></el-icon>
        </div>
      </el-col>
      <el-col :span="24 - (collapse ? 0 : 3)" class="main-view">
        <el-card
          class="main-app"
          :body-style="{ padding: '0', height: '100%' }"
          shadow="never"
        >
        <Suspense>
          <template #default>
            <router-view />
          </template>
          <template #fallback>
            <div v-loading="true" style="height: 100%;width: 100%;"></div>
          </template>
        </Suspense>
        </el-card>
        <div v-show="collapse" class="touch-bar-right" @click="changeBar"  title="展开菜单">
          <el-icon><ArrowRight /></el-icon>
        </div>
      </el-col>
    </el-row>
  </div>
  <!-- 个人信息 -->
  <el-drawer
    title="个人信息"
    v-model="userInfoVisible"
    :wrapper-closable="true"
    append-to-body
    size="40%"
  >
  <UserInfoForm v-if="userInfoVisible" />
  </el-drawer>
</template>

<style lang="scss" scoped>
.box {
  height: 100vh;
  width: 100vw;

  .header-nav {
    height: 45px;
    line-height: 45px;
    background-color: #ffffff;
    // background: linear-gradient(105deg, #001dac 0%, #215eda 100%);
  }

  .header-tag {
    height: 30px;
    background-color: #fff;
    border-top: 1px solid #EBEEF5;
    
    .opt {
      text-align: right;
      margin-top: 5px;
      padding-right: 10px;
    }
  }

  .content {
    height: calc(100vh - 50px - 25px);
    background-color: #f5f6f9;
    position: relative;

    .side-menu {
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
      margin-top: 5px;
      padding: 10px 0;
      background-color: #fff;
      position: relative;
      .touch-bar-left {
        user-select: none;
        cursor: pointer;
        width: 15px;
        height: 30px;
        line-height: 34px;
        position: absolute;
        right: 0px;
        top: 0;
        bottom: 0;
        margin: auto 0;
        color: #b2b3b6;
        background-color: #fff;
        border-radius: 15px 0 0 15px;
        border: 1px solid #ebeef5;
        transition: 100ms;
        z-index: 9;

        &:hover {
          background-color: #f5f6f9;
          color: #409EFF;
        }
      }
    }
    .main-view {
      position: relative;
      .touch-bar-right {
        user-select: none;
        cursor: pointer;
        width: 15px;
        height: 30px;
        line-height: 34px;
        position: absolute;
        left: 0px;
        top: 0;
        bottom: 0;
        margin: auto 0;
        color: #b2b3b6;
        background-color: #fff;
        border-radius: 0 15px 15px 0;
        border: 1px solid #ebeef5;
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
        transition: 100ms;
        z-index: 9;

        &:hover {
          background-color: #f5f6f9;
          color: #409EFF;
        }
      }
    }
    .main-app {
      width: calc(100% - 1px);
      height: calc(100% - 5px);
      margin: 5px 0 0 0;
      border: none;
      border-left: 1px solid #EBEEF5;
      // box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.05);
    }
  }
}
</style>

<style>
/* 定义过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
