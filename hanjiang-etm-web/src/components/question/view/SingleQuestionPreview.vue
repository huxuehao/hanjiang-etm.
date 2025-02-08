<script lang="ts" setup>
import { nextTick, onMounted, reactive, ref, watch } from "vue";

let props = defineProps({
  mode: {
    type: String,
    default: () => {
      // preview（预览）|answer（答题）|correct（批改）|history（历史）
      return "preview";
    },
  },
  number: {
    type: Number,
    default: 1,
  },
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

let selected = ref("");
const config = reactive<any>({});
let corrected = ref(false);

const emits = defineEmits(["input", "correct-change"]);
watch(selected, (val) => {
  emits("input", val, config.type);
});

onMounted(() => {
  nextTick(() => {
    Object.assign(config, props.data);
    corrected.value = config.success !== -1;
    // 初始化答案
    if (["correct", "history"].indexOf(props.mode) > -1) {
      for (let item of config.option) {
        if (item.selected === 1) {
          selected.value = item.title;
        }
      }
    }
  });
});
</script>

<template>
  <div class="box">
    <div class="number">{{ number }}.</div>
    <div class="content">
      <el-row>
        <el-col :span="24" style="font-weight: bolder">
          [ 单选题 ] {{ config.title }}
        </el-col>
      </el-row>
      <el-row>
        <el-radio-group
          :model-value="selected"
          v-if="['answer', 'preview'].indexOf(mode) === -1"
        >
          <el-col
            class="option"
            v-for="(item, index) in config.option"
            :span="24"
            :key="index"
          >
            <el-radio :value="item.title">
              <span class="option">
                <span style="min-width: 30px; display: inline-block"
                  >{{ item.title }}.</span
                >
                <span>{{ item.value }}</span>
              </span>
            </el-radio>
          </el-col>
        </el-radio-group>
        <el-radio-group v-model="selected" v-else>
          <el-col
            class="option"
            v-for="(item, index) in config.option"
            :span="24"
            :key="index"
          >
            <el-radio :value="item.title">
              <span class="option">
                <span style="min-width: 30px; display: inline-block"
                  >{{ item.title }}.</span
                >
                <span>{{ item.value }}</span>
              </span>
            </el-radio>
          </el-col>
        </el-radio-group>
      </el-row>

      <el-row v-if="['correct'].indexOf(mode) > -1">
        <el-col :span="8"></el-col>
        <el-col :span="8" style="text-align: center;line-height: 60px;">
          <span style="margin-right: 10px;">批改结果</span>
          <el-radio-group
            v-model="config.success"
            @change="(res:any) => {emits('correct-change', res); corrected=true}"
          >
            <el-radio :label="1" :border="false">
              <el-icon
                size="24px"
                :style="{
                  color:
                    corrected && config.success === 1
                      ? '#00be00'
                      : '#bebebe',
                }"
                ><SuccessFilled
              /></el-icon>
            </el-radio>
            <el-radio :label="0" :border="false">
              <el-icon
                size="24px"
                :style="{
                  color:
                    corrected && config.success === 0
                      ? '#ff2828'
                      : '#bebebe',
                }"
                ><CircleCloseFilled
              /></el-icon>
            </el-radio>
          </el-radio-group>
        </el-col>
        <el-col :span="8"></el-col>
      </el-row>
    </div>
    <el-card
      v-if="['preview', 'correct', 'history'].indexOf(mode) > -1"
      class="question-info"
      shadow="never"
    >
      <template #header>
        <span>题目信息</span>
      </template>

      <el-form label-position="left" label-width="80px">
        <el-row v-if="['correct'].indexOf(mode) === -1">
          <el-form-item
            v-if="['preview', 'correct'].indexOf(mode) > -1"
            label="我的答案"
          >
            <el-icon size="24px" color="#bebebe" v-if="!corrected"
              ><QuestionFilled
            /></el-icon>
            <el-icon
              size="24px"
              color="#00be00"
              v-else-if="config.success === 1"
              ><SuccessFilled
            /></el-icon>
            <el-icon size="24px" color="#ff2828" v-else
              ><CircleCloseFilled
            /></el-icon>
          </el-form-item>
          <el-form-item v-else label="我的答案">
            <el-icon size="24px" color="#00be00" v-if="config.success === 1"
              ><SuccessFilled
            /></el-icon>
            <el-icon size="24px" color="#ff2828" v-else
              ><CircleCloseFilled
            /></el-icon>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="正确答案">
              <el-input :value="config.answer" readonly></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="题目分数">
              <el-input :value="config.score" readonly></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="" label-width="50px">
              <el-rate
                v-model="config.level"
                :texts="['简单', '一般', '中等', '较难', '很难']"
                show-text
                disabled
              >
              </el-rate>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="['preview', 'correct', 'history'].indexOf(mode) > -1">
          <el-col :span="24">
            <el-form-item label="答案解析">
              <el-input
                :value="config.analysis"
                type="textarea"
                :rows="2"
                readonly
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div style="clear: both"></div>
  </div>
</template>

<style lang="scss" scoped>
@use "/src/style/components/question/view/index.scss" as *;
</style>
