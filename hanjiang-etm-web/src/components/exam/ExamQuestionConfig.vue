<script lang="ts" setup>
import { questionClassifyTree } from "@/api/questionClassify";
import { ElMessage, ElMessageBox } from "element-plus";
import { computed, h, nextTick, onMounted, reactive, ref } from "vue";
const prop = {
  value: "id",
  label: "name",
  children: "children",
};
const props = defineProps({
  mode: {
    type: String,
    default: "add",
  },
  disableEdit: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

let classifyDict = ref<any>([]);
const questionTypeList = reactive([
  {
    label: "单选题",
    value: 1,
    disabled: false,
  },
  {
    label: "多选题",
    value: 2,
    disabled: false,
  },
  {
    label: "判断题",
    value: 3,
    disabled: false,
  },
  {
    label: "填空题",
    value: 4,
    disabled: false,
  },
  {
    label: "解答题",
    value: 5,
    disabled: false,
  },
]);

const questionConfigForm = reactive<any>({
  option: [],
  classifies: null,
});
let totalQuestionNum = computed(() => {
  let num = 0;
  questionConfigForm.option.forEach((item: any) => {
    num += item.questionNum;
  });
  return num;
});
let totalQuestionScore = computed(() => {
  let score = 0;
  questionConfigForm.option.forEach((item: any) => {
    score += item.questionNum * item.questionScore;
  });
  return score;
});

onMounted(() => {
  questionClassifyTree().then((res) => {
    classifyDict.value = res.data.data;
  });
  nextTick(() => {
    if (["edit", "view"].indexOf(props.mode) > -1) {
      Object.assign(questionConfigForm, props.data);
    }
  });
});

// 添加
const clickAddOption = () => {
  if (questionConfigForm.option.length > 8) {
    ElMessage({
      message: "抱歉，最多支持5个配置",
      type: "warning",
      plain: true,
    });
    return;
  }
  questionConfigForm.option.push({
    questionType: null,
    questionNum: 4,
    questionScore: 5,
  });
};
// 删除
const clickRemoveOption = (index: number) => {
  questionConfigForm.option.splice(index, 1);
};

// 题目类型变化时
const questionTypeChange = (value: any, index: number) => {
  const res = questionConfigForm.option.filter((item: any) => {
    return item.questionType === value;
  });
  if (res.length > 1) {
    ElMessageBox.confirm("当前所选题目类型已存在，请重新选择", "题目类型重复", {
      confirmButtonText: "确定",
      showClose: true,
      showCancelButton: false,
      type: "warning",
    }).then(() => {
      questionConfigForm.option[index].questionType = null;
    });
  }
};

// 为table头添加*
const addReadStar = (data: { column: any; $index: number }) => {
  return [
    h(
      "span",
      {
        style: "color: #f67a7a",
      },
      "*"
    ),
    h("span", " " + data.column.label),
  ];
};

let form = ref()
defineExpose({questionConfigForm, form})
</script>

<template>
  <el-form
    ref="form"
    :rules="{}"
    :model="questionConfigForm">
    <el-row>
      <el-col :span="24">
        <el-form-item prop="classifies" label="">
          <template #label>
            <el-popover placement="top-start" width="300" trigger="hover">
            <span>
              <span style="font-weight: bolder">说明：</span><br/>随机生成的试卷将从你选择的分类中进行挑选，若不选择默认使用所有分类的试题。
            </span>
            <template #reference>
              <span><el-icon><InfoFilled /></el-icon> 目标分类</span>
            </template>
          </el-popover>
          </template>
          <el-tree-select
            v-model="questionConfigForm.classifies"
            :data="classifyDict"
            :props="prop"
            :disabled="disableEdit"
            :render-after-expand="false"
            multiple
            filterable
            clearable
            check-strictly
            check-on-click-node
            dataType="string">
          </el-tree-select>
        </el-form-item>
      </el-col>
      
    </el-row>
    <el-row>
      当前题目总数：{{totalQuestionNum}}，生成试卷总分：{{totalQuestionScore}}
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-table :data="questionConfigForm.option" :maxHeight="250" border ref="subTable"
                  :header-cell-style="{backgroundColor: '#f8f8f8',color:'#666666'}"
                  style="width: 100%;margin-bottom: 15px">
          <el-table-column type="index" label="#" width="50" align="center"></el-table-column>
          <el-table-column prop="questionType" label="题目类型" :render-header="addReadStar" align="center">
            <template #default="scope">
              <el-form-item label-width="0" :style="{marginBottom: 0}"
                            :prop="'option[' + scope.$index + '].questionType'" :rules="[{required: true, message: '必填项', trigger: 'blur' }]">
                <el-select v-model="scope.row.questionType" placeholder="选择题目类型"  @change="(value: any) => {questionTypeChange(value, scope.$index)}" :disabled="disableEdit">
                  <el-option v-for="(item,index) in questionTypeList" :label="item.label" :value="item.value" :key="index"></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="questionNum" label="题目数量（道）" :render-header="addReadStar" align="center">
            <template #default="scope">
              <el-form-item label-width="0" :style="{marginBottom: 0}"
                            :prop="'option[' + scope.$index + '].questionNum'" :rules="[{required: true, message: '必填项', trigger: 'blur' }]">
                <el-input-number style="width: 100%" v-model="scope.row.questionNum" :min="1" :max="100" :precision="0" clearable :disabled="disableEdit"></el-input-number>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column prop="questionScore" label="每题分数（分）" :render-header="addReadStar" align="center">
            <template #default="scope">
              <el-form-item label-width="0" :style="{marginBottom: 0}"
                            :prop="'option[' + scope.$index + '].questionScore'" :rules="[{required: true, message: '必填项', trigger: 'blur' }]">
                <el-input-number style="width: 100%" v-model="scope.row.questionScore" :min="1" :max="20" :precision="0" clearable :disabled="disableEdit"></el-input-number>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column align="center" width="50px" v-if="!disableEdit">
            <template #header>
              <el-button title="添加" @click="clickAddOption()" icon="Plus" circle
                          type="primary"></el-button>
            </template>
            <template #default="scope">
              <el-button title="删除" @click="clickRemoveOption(scope.$index)" icon="Delete" circle
                          type="danger"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </el-form>
</template>

<style lang="scss" scoped></style>
