<script lang="ts" setup>
import { nextTick, onMounted, reactive, ref } from 'vue';

const shortcuts1 = [{
  text: '当前时间',
  value: () => new Date()
}, {
  text: '24小时前',
  value: () => {
    const date = new Date();
    date.setTime(date.getTime() - 3600 * 1000 * 24);
    return date;
  }
}, {
  text: '7天前',
  value: () => {
    const date = new Date();
    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
    return date;
  }
}]
const shortcuts2 = [{
  text: '当前时间',
  value: () => new Date()
}, {
  text: '24小时后',
  value: () => {
    const date = new Date();
    date.setTime(date.getTime() + 3600 * 1000 * 24);
    return date;
  }
}, {
  text: '7天后',
  value: () => {
    const date = new Date();
    date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
    return date;
  }
}]
const treeProps = {
  value: 'id',
  label: 'name',
  children: 'children'
}
const validateTime = (rule:any, value:any, callback:any) => {
  const endTime = examForm.limitEndTime;
  if (value && endTime && new Date(value) >= new Date(endTime)) {
    callback(new Error('开始时间必须小于结束时间'));
  } else {
    callback();
  }
};
const rules = {
  classifyId: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  questionRule: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  name: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  duration: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  currentType: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  checkType: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  limitStartTime: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }, {
    validator: validateTime,
    trigger: 'blur'
  }],
  limitEndTime: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  successScore: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
  cheat: [{
    required: true,
    message: '必填项',
    trigger: 'blur'
  }],
};
const props = defineProps({
  classifyDict: {
    type: Array,
    default: () => {
      return []
    }
  },
  mode: {
    type: String,
    default: 'add'
  },
  disableEdit: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object,
    default: () => {
      return {}
    }
  }
})

let examForm = reactive({
  name: null,
  questionRule: 1,
  limitStartTime: null,
  limitEndTime: null,
  cheat: 0,
  classifyId: null,
  successScore: 60,
  duration: 60,
  currentType: 1,
  checkType: 0
})
onMounted(() => {
  nextTick(() => {
    if (['edit', 'view'].indexOf(props.mode) > -1) {
      Object.assign(examForm, props.data)
    }
  })
})
let form = ref()
defineExpose({examForm, form})
</script>

<template>
  <el-form
    label-width="100px"
    ref="form"
    style="margin-top: 10px;"
    :model="examForm"
    :rules="rules">
    <el-row>
      <el-col :span="12">
        <el-form-item label="所属类型" prop="classifyId">
          <el-tree-select
              v-model="examForm.classifyId"
              :data="classifyDict"
              :render-after-expand="true"
              :default-expand-all="true"
              :disabled="disableEdit"
              :props="treeProps"
              placeholder="请选择"
              filterable
              check-strictly
              clearable>
          </el-tree-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="考试名称" prop="name">
          <el-input v-model="examForm.name" :disabled="disableEdit" placeholder="考试名称" clearable></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="考试类型" prop="currentType">
          <template #label>
            <el-popover placement="top-start" width="250" trigger="hover">
              <span>
                <span style="font-weight: bolder">系统内部考试：</span>参加考试的人员需要选择，且只能是本平台的用户。
                <br/>
                <span style="font-weight: bolder">扫码开放考试：</span>参加考试的人员无需选择，用户通过钉钉扫码参加考试。
              </span>
              <template #reference>
                <span><el-icon><InfoFilled /></el-icon> 考试类型</span>
              </template>
            </el-popover>
          </template>
          <el-select v-model="examForm.currentType" @change="(val:number) => {if(val === 0){examForm.cheat=0}}" placeholder="请选择" :disabled="disableEdit">
            <el-option label="系统内部考试" :value="1"></el-option>
            <el-option label="扫码开放考试" :value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="题目规则" prop="questionRule">
          <template #label>
            <el-popover placement="top-start" width="350" trigger="hover">
              <span>
                <span style="font-weight: bolder">题目固定，试卷相同：</span>试卷配置需要选择已有的试卷，且每一个参加考试的用户试卷相同的。
                <br/>
                <span style="font-weight: bolder">题目随机，试卷不同：</span>试卷配置只需配置题目类型、数量、分数，试卷随机生成，且每一个参加考试的用户试卷都不同。
                <br/>
                <span style="font-weight: bolder">题目随机，试卷相同：</span>试卷配置只需配置题目类型、数量、分数，试卷随机生成, 且每一个参加考试的用户试卷相同。
              </span>
              <template #reference>
                <span><el-icon><InfoFilled /></el-icon> 题目规则</span>
              </template>
            </el-popover>
          </template>
          <el-select v-model="examForm.questionRule" placeholder="请选择" :disabled="disableEdit">
            <el-option label="题目固定，试卷相同" :value="0"></el-option>
            <el-option label="题目随机，试卷不同" :value="1"></el-option>
            <el-option label="题目随机，试卷相同" :value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="合格分数" prop="successScore">
          <el-input-number style="width: 100%" v-model="examForm.successScore" :step="10" :precision="0"
                           :disabled="disableEdit" clearable></el-input-number>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="答题时长" prop="duration" :span="12">
          <el-select v-model="examForm.duration" placeholder="请选择" :disabled="disableEdit">
            <el-option label="10分钟" :value="10"></el-option>
            <el-option label="20分钟" :value="20"></el-option>
            <el-option label="30分钟" :value="30"></el-option>
            <el-option label="40分钟" :value="40"></el-option>
            <el-option label="50分钟" :value="50"></el-option>
            <el-option label="60分钟" :value="60"></el-option>
            <el-option label="70分钟" :value="70"></el-option>
            <el-option label="80分钟" :value="80"></el-option>
            <el-option label="90分钟" :value="90"></el-option>
            <el-option label="100分钟" :value="100"></el-option>
            <el-option label="110分钟" :value="110"></el-option>
            <el-option label="120分钟" :value="120"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="开始时间" prop="limitStartTime">
          <el-date-picker v-model="examForm.limitStartTime" type="datetime" 
                          format="YYYY-MM-DD HH:mm:ss"
                          value-format="YYYY-MM-DD HH:mm:ss"
                          :disabled="disableEdit"
                          :shortcuts="shortcuts1"
                          placeholder="开始时间"
                          style="width: 100%" clearable></el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="结束时间" prop="limitEndTime">
          <el-date-picker v-model="examForm.limitEndTime" type="datetime" 
                          format="YYYY-MM-DD HH:mm:ss"
                          value-format="YYYY-MM-DD HH:mm:ss"
                          :disabled="disableEdit"
                          :shortcuts="shortcuts2"
                          placeholder="结束时间"
                          style="width: 100%" clearable></el-date-picker>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="批改方式" prop="checkType">
          <template #label>
            <el-popover placement="top-start" width="300" trigger="hover">
              <span>
                <span style="font-weight: bolder">机器批改：</span>用户提交试卷后，系统将完成试卷的自动批改。
                <br/>
                <span style="font-weight: bolder">人工批改：</span>用户提交试卷后，系统将自动批改单选、多选、判断题（人工可以二次批改），其他题型需人工批改。人工提交批改后用户考试成绩才有效。
              </span>
              <template #reference>
                <span><el-icon><InfoFilled /></el-icon> 批改方式</span>
              </template>
            </el-popover>
          </template>
          <el-radio-group v-model="examForm.checkType" :disabled="disableEdit">
            <el-radio :label="0">机器批改</el-radio>
            <el-radio :label="1">人工批改</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12" v-if="examForm.currentType === 1">
        <el-form-item label="防作弊" prop="cheat">
          <template #label>
            <el-popover placement="top-start" width="300" trigger="hover">
              <span>
                开启防作弊后，当用户在WEB端参加考试时，系统将采取一定的检测手段，有但不限于：检测切换浏览器标签、切换应用、最小化浏览器窗口等其他违规行为。
              </span>
              <template #reference>
                <span><el-icon><InfoFilled /></el-icon> 防作弊</span>
              </template>
            </el-popover>
          </template>
          <el-switch
            v-model="examForm.cheat"
            :active-value="1"
            :inactive-value="0"
            active-text="开启"
            inactive-text="关闭"
            :disabled="disableEdit">
          </el-switch>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<style lang="scss" scoped>
</style>