<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { selectCurrentUserInfo, userUpdate, useRePwd} from '@/api/user';
import { ElMessage } from 'element-plus';

const loading = ref(false)
const activeStep = ref(0)
const changeStep = (num: number) => {
  loading.value = false
  activeStep.value = num
};

/**
 * 基本信息
 */
const rules = {
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  code: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  phone: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  account: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  orgId: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
};
const formData = ref<any>({})
onMounted(() => {
  selectCurrentUserInfo().then((res) => {
    formData.value = res.data.data
  })
})
const form = ref()
const submitUserInfo = () => {
  form.value.validate((valid: any) => {
    if(valid) {
      loading.value = true
      userUpdate(formData.value).then((res) => {
        if(res.data.code === 200) {
          ElMessage({
            message: "操作成功",
            type: "success",
            plain: true
          })
        }
        selectCurrentUserInfo().then((res) => {
          formData.value = res.data.data
        })
      }).finally(() => {
        loading.value = false
      })
    }
  })
};

/**
 * 修改密码
 */
// 验证两次密码输入的是否相同
const validatePwdEq = (rule:any, value:any, callback:any) => {
  const newPwd = pwdFormData.value.newPwd;
  if (value && newPwd !== value) {
    callback(new Error('两次新密码值不同'));
  } else {
    callback();
  }
};
const pwdRules = {
  oldPwd: [
    {
      required: true,
      message: "请填写旧密码",
      trigger: "blur",
    },
  ],
  newPwd: [
    {
      required: true,
      message: "请填写新密码",
      trigger: "blur",
    },
  ],
  newRepwd: [
    {
      required: true,
      message: "请再次填写新密码",
      trigger: "blur",
    }, {
      validator: validatePwdEq,
      trigger: 'blur'
    }
  ],
}
const pwdFormData = ref<any>({})
const formPwd = ref()
const submitRePwd = () => {
  formPwd.value.validate((valid: any) => {
    if(valid) {
      loading.value = true
      useRePwd(pwdFormData.value).then(() => {
        pwdFormData.value = {}
        ElMessage({
          message: "操作成功",
          type: "success",
          plain: true
        })
      }).finally(() => {
        loading.value = false
      })
    }
  })
};

</script>

<template>
  <el-steps :space="200" :active="activeStep" simple>
    <el-step title="基本信息" icon="Edit" class="task-step" @click="changeStep(0)"></el-step>
    <el-step title="密码管理" icon="Lock" class="task-step" @click="changeStep(1)"></el-step>
  </el-steps>
  <div v-show="activeStep == 0" style="padding: 30px 0;">
    <el-form ref="form" :model="formData" :rules="rules" label-width="80px">
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="登录账号" prop="account">
            <el-input
              placeholder="请填写登录账号"
              v-model="formData.account"
              maxlength="40"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="用户姓名" prop="name">
            <el-input
              placeholder="请填写用户姓名"
              v-model="formData.name"
              maxlength="10"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="用户编号" prop="code">
            <el-input
              placeholder="请填写用户编号"
              v-model="formData.code"
              maxlength="40"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="用户昵称" prop="nickname">
            <el-input
              placeholder="请填写用户昵称"
              v-model="formData.nickname"
              maxlength="10"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="手机号码" prop="phone">
            <el-input
              placeholder="请填写手机号码"
              maxlength="11"
              v-model="formData.phone"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="邮箱号码" prop="email">
            <el-input
              placeholder="请填写邮箱号码"
              maxlength="100"
              v-model="formData.email"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item>
            <el-button 
              v-permission="'submit_user_info'" 
              @click="submitUserInfo()" 
              type="primary" 
              icon="CircleCheck"
              :loading="loading"
              style="width: 100%;">
              提交用户信息
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
  <div v-show="activeStep == 1" style="padding: 30px 0;">
    <el-form ref="formPwd" :model="pwdFormData" :rules="pwdRules" label-width="80px">
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="旧密码" prop="oldPwd">
            <el-input
              placeholder="请填写旧密码"
              v-model="pwdFormData.oldPwd"
              maxlength="100"
              type="password"
              show-password
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="新密码" prop="newPwd">
            <el-input
              placeholder="请填写新密码"
              v-model="pwdFormData.newPwd"
              maxlength="100"
              type="password"
              show-password
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="新密码" prop="newRepwd">
            <el-input
              placeholder="请再次填写新密码"
              v-model="pwdFormData.newRepwd"
              maxlength="100"
              type="password"
              show-password
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item>
            <el-button 
              v-permission="'submit_repassword'"
              @click="submitRePwd()"
              type="primary" 
              icon="CircleCheck"
              :loading="loading"
              style="width: 100%;">
              提交重置密码
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<style lang="scss" scoped>
.task-step {
  cursor: pointer;
}
</style>