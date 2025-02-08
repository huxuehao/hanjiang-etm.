<script lang="ts" setup>
import QuestionTrainInfo from '@/components/statistics/QuestionTrainInfo.vue';
import QuestionLevel from '@/components/statistics/QuestionLevel.vue';
import QuestionType from '@/components/statistics/QuestionType.vue';
import UserActive from '@/components/statistics/UserActive.vue';

</script>

<template>
  <el-row style="background-color: #F5F6F9;height: calc((100% - 1px) / 2)">
    <el-col :span="16" class="chart-box" style="padding-left: 0;padding-top: 0;">
      <div class="chart-box-item">
        <UserActive />
      </div>
    </el-col>
    <el-col :span="8" class="chart-box" style="padding-right: 0;padding-top: 0;">
      <div class="chart-box-item">
        <!-- <AttachType /> -->
        <QuestionType />
      </div>
    </el-col>
  </el-row>
  <el-row style="background-color: #F5F6F9;height: calc((100% - 1px) / 2)">
    <el-col :span="8" class="chart-box" style="padding-left: 0;">
      <div class="chart-box-item">
        <QuestionLevel />
      </div>
    </el-col>
    <!-- <el-col :span="8" class="chart-box">
      <div class="chart-box-item">
        <QuestionType />
      </div>
    </el-col> -->
    <el-col :span="16" class="chart-box" style="padding-right: 0;">
      <div class="chart-box-item">
        <QuestionTrainInfo />
      </div>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
.chart-box {
  padding: 4px;
  height: 100%;

  .chart-box-item {
    height: 100%;
    width: 100%;
    background-color: white;
    border-radius: 3px;
  }
}

</style>
