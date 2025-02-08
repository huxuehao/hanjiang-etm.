<script lang="ts" setup>
import { nextTick, onMounted, reactive, ref } from "vue";
import { chineseNumbers, prop, addReadStar } from "./common";

let props = defineProps({
  showClassify: {
    type: Boolean,
    default: true,
  },
  classifyDict: {
    type: Array,
    default: () => {
      return [];
    },
  },
  showShare: {
    type: Boolean,
    default: () => {
      return true;
    },
  },
  mode: {
    type: String,
    default: "add",
  },
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
  disableEdit: {
    type: Boolean,
    default: false,
  },
});

let form = ref();
const config = reactive<any>({
  classifyId: null,
  name: null,
  title: null,
  score: 15,
  analysis: null,
  level: 2,
  type: 5,
  share: 1,
  success: -1,
  keyWords: [],
  matchNum: 1,
});
const rules = {
  classifyId: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  title: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  score: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  keyWords: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  matchNum: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  share: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  level: [
    {
      required: true,
      message: "必填项",
      trigger: "change",
    },
  ],
};

onMounted(() => {
  nextTick(() => {
    if (props.mode === "edit") {
      Object.assign(config, props.data);
      config.success = -1;
    }
  });
});

// 生成实体
const genEntity = () => {
  return {
    id: config.id || null,
    classifyId: config.classifyId || null,
    type: config.type,
    name: config.name || null,
    title: config.title || null,
    score: config.score,
    level: config.level,
    config: JSON.stringify(config),
    share: config.share,
    success: config.success || 0,
  };
};
// 获取配置
const getConfig = () => {
  return config;
};

defineExpose({ config, form, genEntity, getConfig });
</script>

<template>
  <div style="margin: 0 20px">
    <el-form
      label-width="75px"
      ref="form"
      :model="config"
      :rules="rules"
    >
      <el-row v-if="showClassify">
        <el-col :span="24">
          <el-form-item label="分类" prop="classifyId">
            <el-tree-select
              v-model="config.classifyId"
              :data="classifyDict"
              :render-after-expand="true"
              :default-expand-all="true"
              :disabled="disableEdit"
              :props="prop"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="题干" prop="title">
            <el-input
              v-model="config.title"
              type="textarea"
              maxlength="300"
              rows="3"
              :disabled="disableEdit"
              show-word-limit
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="解析" prop="analysis">
            <el-input
              v-model="config.analysis"
              type="textarea"
              maxlength="500"
              rows="3"
              :disabled="disableEdit"
              show-word-limit
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="关键字" prop="keyWords">
            <template #label>
              <el-popover placement="top-start" width="250" trigger="hover">
                <span>
                  <span style="font-weight: bolder">关键字：</span
                  >其作用是用来衡量考生答案的基础，在计算考生答案是否正确时，将作为重要的依据。
                </span>
                <template #reference>
                  <span><el-icon><InfoFilled /></el-icon> 关键字</span>
                </template>
              </el-popover>
            </template>
            <el-select
              v-model="config.keyWords"
              multiple
              filterable
              allow-create
              default-first-option
              clearable
              placeholder="输入关键字，后回车"
            >
              <el-option
                label="输入关键字后，回车"
                value="输入关键字后，回车"
                disabled
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="匹配数" prop="matchNum">
            <template #label>
              <el-popover placement="top-start" :width="300" trigger="hover">
                <span>
                  <span style="font-weight: bolder">匹配数：</span
                  >其作用是用来判断考生答案的核心，系统将会对考生的答案进行“分词”处理，分词的结果包含的“关键字”的数量是否大于匹配数，将决定答案是否有效。
                </span>
                <template #reference>
                  <span><el-icon><InfoFilled /></el-icon> 匹配数</span>
                </template>
              </el-popover>
            </template>
            <el-input-number
              style="width: 100%"
              v-model="config.matchNum"
              :min="1"
              :precision="0"
              :max="config.keyWords.length || 1"
              :disabled="disableEdit"
              clearable
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分数" prop="score">
            <el-input-number
              style="width: 100%"
              v-model="config.score"
              :min="1"
              :max="20"
              :precision="0"
              :disabled="disableEdit"
              clearable
            ></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="难度" prop="level">
            <el-rate
              v-model="config.level"
              :disabled="disableEdit"
              :texts="['简单', '一般', '中等', '较难', '很难']"
              show-text
            >
            </el-rate>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showShare">
          <el-form-item label="共享" prop="share">
            <el-switch
              v-model="config.share"
              active-color="#3C9EDC"
              inactive-color="#909399"
              :active-value="1"
              :inactive-value="0"
              :disabled="disableEdit"
              active-text=""
              inactive-text=""
            >
            </el-switch>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<style lang="scss" scoped></style>
