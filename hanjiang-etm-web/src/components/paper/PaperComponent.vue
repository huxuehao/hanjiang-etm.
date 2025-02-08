<script lang="ts" setup>
const colors = [
  { color: "#f56c6c", percentage: 10 },
  { color: "#f56c6c", percentage: 20 },
  { color: "#e6a23c", percentage: 30 },
  { color: "#e6a23c", percentage: 40 },
  { color: "#6f7ad3", percentage: 50 },
  { color: "#6f7ad3", percentage: 60 },
  { color: "#1989fa", percentage: 70 },
  { color: "#1989fa", percentage: 80 },
  { color: "#5cb87a", percentage: 90 },
  { color: "#5cb87a", percentage: 100 },
];
defineProps({
  mode: {
    type: String,
    default: () => {
      // preview（预览）|answer（答题）|correct（批改）|history（历史）
      return "preview";
    },
  },
  duration: {
    type: Number,
    default: 0,
  },
  percentage: {
    type: Number,
    default: 0,
  },
  allScore: {
    type: Number,
    default: 100,
  },
  score: {
    type: Number,
    default: 0,
  },
});

const format = (percentage: any) => {
  return `${percentage}分`;
};
const emits = defineEmits(["timeout"]);
</script>

<template>
  <div>
    <!--试卷总分-->
    <div style="padding: 8px" v-if="['preview', 'answer'].indexOf(mode) > -1">
      <el-card style="text-align: center">
        <template #header>
          <span style="font-weight: bolder">试卷总分</span>
        </template>
        <div style="font-weight: bolder; font-size: 20px; color: #014b70">
          {{ allScore }}
        </div>
      </el-card>
    </div>
    <!--试卷得分-->
    <div style="padding: 8px" v-if="['correct', 'history'].indexOf(mode) > -1">
      <el-card style="text-align: center">
        <template #header>
          <span style="font-weight: bolder">得分/总分</span>
        </template>
        <div style="font-weight: bolder; font-size: 20px">
          <span style="color: #ffa400">{{ score }}</span
          >/<span style="color: #014b70">{{ allScore }}</span>
        </div>
      </el-card>
    </div>
    <!--正确率-->
    <div style="padding: 8px" v-if="['correct', 'history'].indexOf(mode) > -1">
      <el-card style="text-align: center">
        <template #header>
          <span style="font-weight: bolder">正确率</span>
        </template>
        <el-progress
          :text-inside="true"
          :stroke-width="20"
          :percentage="Math.round((score / allScore) * 100)"
          style="font-weight: bolder"
          status="success"
        >
        </el-progress>
      </el-card>
    </div>
    <!--答题进度-->
    <div style="padding: 8px" v-if="['preview', 'answer'].indexOf(mode) > -1">
      <el-card style="text-align: center">
        <template #header>
          <span style="font-weight: bolder">答题进度</span>
        </template>
        <el-progress
          type="dashboard"
          :percentage="percentage"
          :color="colors"
        ></el-progress>
      </el-card>
    </div>
    <!--答题剩余时间-->
    <div style="padding: 8px" v-if="['preview', 'answer'].indexOf(mode) > -1">
      <el-card style="text-align: center">
        <template #header>
          <span style="font-weight: bolder">答题剩余时间</span>
        </template>
        <div style="font-weight: bolder; font-size: 20px">
          <el-countdown
            v-if="duration > 0"
            @finish="
              () => {
                emits('timeout');
              }
            "
            :value="Date.now() + duration * 60 * 1000"
            time-indices
          ></el-countdown>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.el-card {
  --el-card-padding: 10px !important
}
</style>
