<script lang="ts" setup>
import { ElMessage, type FormInstance } from "element-plus";
import { computed, onUnmounted, reactive, ref, watch } from "vue";
import { useUserStore } from "@/stores/user";
import router from "@/router";
import { useRoute } from 'vue-router';

const route = useRoute()
const userStore = useUserStore();

const redirect = ref<any>(undefined);

watch(route, (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect;
}, { immediate: true });

// 模式
let mode = ref("pwd");
// 密码登录对象
let passwordForm = reactive({
  account: "administrator",
  password: "Admin@HanJ123.com",
});
// 短信登录对象
let smsForm = reactive({
  phoneNum: "",
  code: "",
});
// 表单校验规则
const rules = ref({
  account: [
    {
      required: true,
      message: "用户名不能为空",
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      message: "密码不能为空",
      trigger: "blur",
    },
  ],
  phoneNum: [
    {
      required: true,
      message: "手机号不能为空",
      trigger: "blur",
    },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: "请输入有效的手机号",
      trigger: "blur",
    },
  ],
  code: [
    {
      required: true,
      message: "验证码不能为空",
      trigger: "blur",
    },
  ],
});

let getCodeMsg = ref<string | number>("获取短信验证码");
let prefix = computed(() => (typeof getCodeMsg.value === "number" ? "秒" : ""));
// 发送验证码
const handelCode = () => {
  if (/^1[3-9]\d{9}$/.test(smsForm.phoneNum)) {
    ElMessage({
      message: "验证码已发送",
      type: "success",
      plain: true,
    });

    startCountdown(60);
  } else {
    ElMessage({
      message: "请输入有效的手机号",
      type: "warning",
      plain: true,
    });
  }
};

// 登录
let pwdRef = ref<FormInstance>();
let smsRef = ref<FormInstance>();
const handelSubmit = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  if (mode.value === "pwd") {
    formEl.validate((valid) => {
      if (valid) {
        userStore
          .doLogin({
            account: passwordForm.account.trim(),
            password: passwordForm.password,
          })
          .then(() => {
            window.open(decodeURIComponent(redirect.value || "/"), '_self')
          })
      }
    });
  } else if (mode.value === "sms") {
    // formEl.validate((valid) => {
    // if (valid) {
    ElMessage({
      message: "暂不支持此模式",
      type: "warning",
      plain: true,
    });
    // }
    // });
  } else {
    ElMessage({
      message: "模式匹配失败，请联系管理员",
      type: "info",
      plain: true,
    });
  }
};

// 60s倒计时
let interval = 0;
let isCounting = ref<boolean>(false);
const startCountdown = (value: number) => {
  if (isCounting.value) return;
  getCodeMsg.value = value;
  isCounting.value = true;
  interval = setInterval(() => {
    if (typeof getCodeMsg.value === "number" && getCodeMsg.value > 0) {
      getCodeMsg.value--;
    } else {
      getCodeMsg.value = "获取验证码";
      isCounting.value = false;
      clearInterval(interval);
    }
  }, 1000);
};

// 卸载
onUnmounted(() => {
  if (isCounting.value) {
    clearInterval(interval);
  }
});
</script>

<template>
  <div class="login-box">
    <el-tabs v-model="mode" class="demo-tabs">
      <el-tab-pane label="密码登录" name="pwd">
        <el-form
          v-if="mode === 'pwd'"
          :model="passwordForm"
          :rules="rules"
          ref="pwdRef"
          label-width="auto"
        >
          <el-form-item label="" prop="account" label-width="0">
            <el-input
              v-model="passwordForm.account"
              placeholder="请输入用户名"
              maxlength="100"
              prefix-icon="User"
              clearable
              size="default"
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="password" label-width="0">
            <el-input
              v-model="passwordForm.password"
              type="password"
              maxlength="100"
              autocomplete="off"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
              size="default"
            ></el-input>
          </el-form-item>
          <!-- <el-row :gutter="0" style="
              font-size: 12px;
              margin-bottom: 10px;
              color: #c6c6c6;
              letter-spacing: 1px;
            ">
            <el-col :span="12">
              <span style="cursor: pointer" class="hover">注册</span>
            </el-col>
            <el-col :span="12" style="text-align: right">
              <span style="cursor: pointer" class="hover">忘记密码</span>
            </el-col>
          </el-row> -->
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="短信登录" name="sms">
        <el-form
          v-if="mode === 'sms'"
          :model="smsForm"
          :rules="rules"
          ref="smsRef"
          label-width="auto"
        >
          <el-form-item label="" prop="phoneNum" label-width="0">
            <el-input
              v-model="smsForm.phoneNum"
              placeholder="请输入手机号"
              maxlength="11"
              prefix-icon="Iphone"
              clearable
              size="default"
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="code" label-width="0">
            <el-input
              v-model="smsForm.code"
              placeholder="请输入验证码"
              maxlength="6"
              prefix-icon="ChatLineRound"
              clearable
              size="default"
            >
              <template #append>
                <el-button
                  type="text"
                  @click="handelCode()"
                  :disabled="typeof getCodeMsg === 'number'"
                  style="font-size: 12px; padding: 0 10px"
                >
                  {{ getCodeMsg }} {{ prefix }}
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-button
        type="primary"
        @click="handelSubmit(mode === 'pwd' ? pwdRef : smsRef)"
        class="login-btn"
        size="default"
      >
        登 录
      </el-button>
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped>
.login-box {
  width: 100%;
  max-width: 400px;
}

.login-btn {
  width: 100%;
  height: 40px;
}
</style>
