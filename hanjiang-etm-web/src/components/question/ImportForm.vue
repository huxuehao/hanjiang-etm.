<script lang="ts" setup>
import { ref } from "vue";
import SingleQuestionPreview from "@/components/question/view/SingleQuestionPreview.vue";
import MultipleQuestionPreview from "@/components/question/view/MultipleQuestionPreview.vue";
import JudgeQuestionPreview from "@/components/question/view/JudgeQuestionPreview.vue";
import CompletionQuestionPreview from "@/components/question/view/CompletionQuestionPreview.vue";
import QaQuestionPreview from "@/components/question/view/QaQuestionPreview.vue";

defineProps({
  questionBox: {
    type: Object,
    default: () => {
      return {
        completion: {
          classifyIds: [],
          configs: [],
        },
        judge: {
          classifyIds: [],
          configs: [],
        },
        multiple: {
          classifyIds: [],
          configs: [],
        },
        qa: {
          classifyIds: [],
          configs: [],
        },
        single: {
          classifyIds: [],
          configs: [],
        },
      };
    },
  },
});

let activeName = ref("1");
</script>

<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="单选题" name="1">
      <template #label>
        <el-icon><TurnOff /></el-icon>&nbsp;单选题
      </template>
      <el-scrollbar height="calc(100vh - 170px)">
        <SingleQuestionPreview
          v-for="(item, index) in questionBox.single.configs"
          :data="item"
          :number="index + 1"
          :key="index"
          mode="preview"
        >
        </SingleQuestionPreview>
        <el-empty
          v-if="
            !questionBox.single.configs ||
            questionBox.single.configs.length === 0
          "
          :image-size="120"
          description="无数据"
        >
        </el-empty>
      </el-scrollbar>
    </el-tab-pane>
    <el-tab-pane label="多选题" name="2">
      <template #label>
        <el-icon><SetUp /></el-icon>&nbsp;多选题
      </template>
      <el-scrollbar height="calc(100vh - 170px)">
        <MultipleQuestionPreview
          v-for="(item, index) in questionBox.multiple.configs"
          :data="item"
          :number="index + 1"
          :key="index"
          mode="preview"
        >
        </MultipleQuestionPreview>
        <el-empty
          v-if="
            !questionBox.multiple.configs ||
            questionBox.multiple.configs.length === 0
          "
          :image-size="120"
          description="无数据"
        >
        </el-empty>
      </el-scrollbar>
    </el-tab-pane>
    <el-tab-pane label="判断题" name="3">
      <template #label>
        <el-icon><Finished /></el-icon>&nbsp;判断题
      </template>
      <el-scrollbar height="calc(100vh - 170px)">
        <JudgeQuestionPreview
          v-for="(item, index) in questionBox.judge.configs"
          :data="item"
          :number="index + 1"
          :key="index"
          mode="preview"
        >
        </JudgeQuestionPreview>
        <el-empty
          v-if="
            !questionBox.judge.configs || questionBox.judge.configs.length === 0
          "
          :image-size="120"
          description="无数据"
        >
        </el-empty>
      </el-scrollbar>
    </el-tab-pane>
    <el-tab-pane label="填空题" name="4">
      <template #label>
        <el-icon><Edit /></el-icon>&nbsp;填空题
      </template>
      <el-scrollbar height="calc(100vh - 170px)">
        <CompletionQuestionPreview
          v-for="(item, index) in questionBox.completion.configs"
          :data="item"
          :number="index + 1"
          :key="index"
          mode="preview"
        >
        </CompletionQuestionPreview>
        <el-empty
          v-if="
            !questionBox.completion.configs ||
            questionBox.completion.configs.length === 0
          "
          :image-size="120"
          description="无数据"
        >
        </el-empty>
      </el-scrollbar>
    </el-tab-pane>
    <el-tab-pane label="解答题" name="5">
      <template #label>
        <el-icon><Edit /></el-icon>&nbsp;解答题
      </template>
      <el-scrollbar height="calc(100vh - 170px)">
        <QaQuestionPreview
          v-for="(item, index) in questionBox.qa.configs"
          :data="item"
          :number="index + 1"
          :key="index"
          mode="preview"
        >
        </QaQuestionPreview>
        <el-empty
          v-if="!questionBox.qa.configs || questionBox.qa.configs.length === 0"
          :image-size="120"
          description="无数据"
        >
        </el-empty>
      </el-scrollbar>
    </el-tab-pane>
  </el-tabs>
</template>

<style lang="scss" scoped></style>
