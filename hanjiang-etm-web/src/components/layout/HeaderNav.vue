<script lang="ts" setup>
import setting from "@/config/setting";
import { ElMessageBox } from "element-plus";
import { useUserStore } from "@/stores/user";
import { useRoute } from "vue-router";
import router from "@/router/index";

const userStore = useUserStore();
const route = useRoute();

const goBackHome = () => {
  router.push({ path: setting.homePath });
};

const handelLogout = () => {
  ElMessageBox.confirm("确认退出当前系统?", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    userStore.doLogout(userStore.userInfo.id).then(() => {
      window.open(`/login?redirect=${encodeURIComponent(route.fullPath)}`, '_self')
    })
  });
};
</script>

<template>
  <el-row style="width: 100%; height: 100%;">
    <el-col :span="12" class="header-box">
      <!-- <div class="logo"></div> -->
      <span class="title">{{ setting.systemName }}</span>
    </el-col>
    <el-col :span="12" class="control">
      <el-icon v-permission="'home'" @click="goBackHome" class="hover pointer icon" title="回到首页"
        ><House/></el-icon>
      <el-icon v-permission="'userInfo'" class="hover pointer icon" title="个人信息"
        ><User/></el-icon>
      <el-icon @click="handelLogout()" class="hover pointer icon" title="退出登录"
        ><SwitchButton/></el-icon>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
.header-box {
  display: flex;
  justify-content: left;
  align-items: center;
  padding: 0 10px;

  .logo {
    height: 24px;
    width: 24px;
    background-image: url("@/assets/logo.png");
    background-size: cover;
    background-position: center;
  }

  .title {
    user-select: none;
    font-size: 20px;
    color: #4093ff;
    text-shadow: 
    2px 2px 0 #dcdcdc,  /* 上右 */
    -2px -2px 0 #dcdcdc, /* 下左 */
    2px -2px 0 #dcdcdc,  /* 上左 */
    -2px 2px 0 #dcdcdc;  /* 下右 */;
    font-style: italic;
    font-weight: bolder;
    margin-left: 7px;
    letter-spacing: 2px;
  }
}

.control {
  display: flex;
  justify-content: right;
  align-items: center;
  padding: 0 10px;
  color: #6c6c6c !important;
  font-size: 18px;
  .icon {
    margin-left: 10px;
  }
}
</style>
