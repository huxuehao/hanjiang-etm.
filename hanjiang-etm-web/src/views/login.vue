<script lang="ts" setup>
import LoginForm from "@/components/login/LoginForm.vue";
import setting from '@/config/setting';
</script>

<template>
  <div class="login-page">
    <div class="background"></div>
    <div class="login-container">
      <div class="title-box">
        <span class="title-left">欢迎使用</span>
        <span class="title-right">{{ setting.systemName }}</span>
      </div>
      <login-form />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;

  .background {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("@/assets/login_bg.png");
    background-size: cover;
    background-position: center;
    transform-origin: center;
    transform: scaleX(-1);
    // filter: blur(4px);
    z-index: -1;
  }

  .login-container {
    width: 350px;
    position: absolute;
    right: 10%;
    background-color: rgba(255, 255, 255, 1);
    padding: 40px;
    border-radius: 3px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);

    .title-box {
      margin-bottom: 20px;
      letter-spacing: 1px;

      .title-left {
        font-size: 25px;
        font-weight: bolder;
        color: #474747;
      }

      .title-right {
        font-size: 14px;
        margin-left: 10px;
        user-select: none;
        color: #4093ff;
        text-shadow: 
        2px 2px 0 #e7e7e7,  /* 上右 */
        -2px -2px 0 #e7e7e7, /* 下左 */
        2px -2px 0 #e7e7e7,  /* 上左 */
        -2px 2px 0 #e7e7e7;  /* 下右 */;
        font-style: italic;
        font-weight: bolder;
        margin-left: 7px;
        letter-spacing: 2px;
      }
    }
  }
}
</style>
