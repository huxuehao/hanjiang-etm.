<script lang="ts" setup>
import { taskList, taskPage } from '@/api/task';
import TaskList from '@/components/task/TaskList.vue';
import { onMounted, reactive, ref } from 'vue';

let activeStep = ref(0) // 当前步骤索引
const changeStep = (index:number) => {
  activeStep.value = index
};

let refreshFlag = ref(false) // 刷新标识
const queryParams = reactive({})
let trainCount = ref(0)  // 培训未完成数
let examCount = ref(0) // 考试未完成数
let approvalCount = ref(0) // 批改未完成数

onMounted(() => {
  loadCounts()
})

// 加载所有未完成数
const loadCounts = async () => {
  taskList({
    activeType: 'ntrain',
    ...queryParams,
  }).then((res: any) => {
    trainCount.value = res.data.data.length || 0
  })

  taskList({
    activeType: 'nexam',
    ...queryParams,
  }).then((res: any) => {
    examCount.value = res.data.data.length || 0
  })

  taskPage({
    activeType: 'approval',
    total: 0,
    current: 1,
    size: 100,
    ...queryParams,
  }).then((res: any) => {
    approvalCount.value = res.data.data.records.length || 0
  });
};
</script>
<template>
  <div style="padding: 10px;">
    <el-steps :space="200" :active="activeStep" simple>
      <el-step title="考试" icon="Edit" class="task-step" @click="changeStep(0)">
        <template #title v-if="examCount > 0">
          <div class="step-title">
            <el-badge :value="examCount" :max="99" class="badge-title">
              <span style="min-width: 40px;display: block;">考试</span>
            </el-badge>
          </div>
        </template>
      </el-step>
      <el-step title="培训" icon="VideoCamera" class="task-step" @click="changeStep(1)">
        <template #title v-if="trainCount > 0">
          <div class="step-title">
            <el-badge :value="trainCount" :max="99" class="badge-title">
              <span style="min-width: 40px;display: block;">培训</span>
            </el-badge>
          </div>
        </template>
      </el-step>
      <el-step title="审批" icon="DocumentChecked" class="task-step" @click="changeStep(2)">
        <template #title v-if="approvalCount > 0">
          <div class="step-title">
            <el-badge :value="approvalCount" :max="99" class="badge-title">
              <span style="min-width: 40px;display: block;">审批</span>
            </el-badge>
          </div>
        </template>
      </el-step>
    </el-steps>
    <!-- 根据步骤动态显示不同的内容 -->
    <div style="height: 93%;">
      <TaskList v-if="activeStep === 0"
                :showType="'exam'"
                :key="activeStep"
                :refresh="refreshFlag"
                @reload-index="loadCounts"></TaskList>
      <TaskList v-if="activeStep === 1"
                :showType="'train'"
                :key="activeStep"
                :refresh="refreshFlag"
                @reload-index="loadCounts"></TaskList>
      <TaskList v-if="activeStep === 2"
                :showType="'approval'"
                :key="activeStep"
                :refresh="refreshFlag"
                @reload-index="loadCounts"></TaskList>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.task-step {
  cursor: pointer;
}
.step-title {
  display: flex; 
  align-items: center; 
  justify-content: center;
}
</style>
