<script lang="ts" setup>
import {
  h,
  nextTick,
  onMounted,
  onUnmounted,
  reactive,
  ref,
  shallowReactive,
  watch,
} from "vue";
// 表单组件
import SingleQuestionForm from "@/components/question/form/SingleQuestionForm.vue";
import MultipleQuestionForm from "@/components/question/form/MultipleQuestionForm.vue";
import JudgeQuestionForm from "@/components/question/form/JudgeQuestionForm.vue";
import QaQuestionForm from "@/components/question/form/QaQuestionForm.vue";
import CompletionQuestionForm from "@/components/question/form/CompletionQuestionForm.vue";
// 预览组价
import SingleQuestionPreview from "@/components/question/view/SingleQuestionPreview.vue";
import MultipleQuestionPreview from "@/components/question/view/MultipleQuestionPreview.vue";
import JudgeQuestionPreview from "@/components/question/view/JudgeQuestionPreview.vue";
import CompletionQuestionPreview from "@/components/question/view/CompletionQuestionPreview.vue";
import QaQuestionPreview from "@/components/question/view/QaQuestionPreview.vue";
import Sortable from "sortablejs";
import { ElMessage, ElMessageBox } from "element-plus";
import QuestionChose from "./QuestionChose.vue";

const componentNames = shallowReactive<any>({
  SingleQuestionForm,
  MultipleQuestionForm,
  JudgeQuestionForm,
  QaQuestionForm,
  CompletionQuestionForm,

  SingleQuestionPreview,
  MultipleQuestionPreview,
  JudgeQuestionPreview,
  CompletionQuestionPreview,
  QaQuestionPreview,
});

const types = ["--", "单选题", "多选题", "判断题", "填空题", "解答题"];
const formComponents = [
  "",
  "SingleQuestionForm",
  "MultipleQuestionForm",
  "JudgeQuestionForm",
  "CompletionQuestionForm",
  "QaQuestionForm",
];
const previewComponents = [
  "",
  "SingleQuestionPreview",
  "MultipleQuestionPreview",
  "JudgeQuestionPreview",
  "CompletionQuestionPreview",
  "QaQuestionPreview",
];

let props = defineProps({
  classifyDict: {
    type: Array,
    default: () => {
      return [];
    },
  },
  showShare: {
    type: Boolean,
    default: true,
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

let questionConfig = ref<any>({});
const prop = reactive({
  value: "id",
  label: "name",
  children: "children",
});

const rules = {
  classifyId: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  type: [
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
  duration: [
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
};

let selectedRows = ref<any>([]);
let rows = ref<any>([]);
const config = reactive({
  classifyId: null,
  type: 1,
  title: null,
  question: [],
  duration: 60,
  share: 1,
});
onMounted(() => {
  if (props.mode === "edit") {
    nextTick(() => {
      Object.assign(config, props.data);
    });
  }
});

const genEntity = () => {
  return config;
};

let enableDrop = ref(false);
let sortableBody = <any>null;
watch(enableDrop, (val) => {
  if (val) {
    initSortable();
  } else {
    destroySortable();
  }
});
let subTable = ref();
// 初始化拖拽
const initSortable = () => {
  if (!props.disableEdit) {
    enableDrop.value = true;
    const el = subTable.value.$el.querySelector(
      ".el-table__body-wrapper tbody"
    );
    sortableBody = new Sortable.create(el, {
      animation: 150,
      onEnd: handleDragEnd,
    });
  }
};
// 处理拖拽结束
const handleDragEnd = (evt: any) => {
  const { newIndex, oldIndex } = evt;
  if (newIndex === oldIndex) {
    return;
  }
  const currRow = config.question.splice(oldIndex, 1)[0];
  config.question.splice(newIndex, 0, currRow);
};
// 销毁拖拽
const destroySortable = () => {
  enableDrop.value = false;
  if (sortableBody === null) {
    return;
  }
  sortableBody.destroy();
  sortableBody = null;
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

// 选择试题
let questionDrawerVisible = ref(false);
const handelChoseQuestion = () => {
  if (config.question.length < 99) {
    questionDrawerVisible.value = true;
  } else {
    ElMessage({
      message: "最多支持99道试题",
      type: "warning",
      plain: true,
    });
  }
};
const handelQuestionChoseDo = (rows: any = []) => {
  // 获取不重复的实体
  const newQuestions = rows
    .filter((item: any) => {
      for (let q of <any>config.question) {
        if (item.id + "" === q.id) {
          return false;
        }
      }
      return true;
    })
    .map((item: any) => {
      return JSON.parse(item.config);
    });
  // 插入到目标表单
  for (let questionConfig of newQuestions) {
    const conf = { ...questionConfig, rowKey: questionConfig.id + "" };
    (config.question as any[]).push(conf);
  }
  questionDrawerVisible.value = false;
};

// 删除试题
const clickRemoveQuestions = () => {
  ElMessageBox.confirm("确认移除所有试题", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    config.question = [];
  });
};
const clickRemoveQuestion = (row: any, index: number) => {
  ElMessageBox.confirm(`确认移除 [ ${row.name} ] ?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    config.question.splice(index, 1);
    for (let i = 0; i < config.question.length; i++) {
      (config.question[i] as { rowKey?: string }).rowKey = i + "";
    }
  });
};

let stopBtn = ref(false);
let currentType = ref(0);
let questionMode = ref("add");
let currentComponent = ref("");
let currentPreviewComponent = ref("");
const questionDialogProp = reactive({
  visible: false,
  title: "",
  top: "10vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: false,
  closeOnClickModal: false
});
// 添加自定义试题
const handleQuestionAdd = (type: number) => {
  if (config.question.length < 99) {
    questionMode.value = "add";
    currentType.value = type;
    currentComponent.value = formComponents[type];
    questionConfig.value = {};
    stopBtn.value = false;
    questionDialogProp.title = "新增-" + types[type];
    questionDialogProp.visible = true;
  } else {
    ElMessage({
      message: "最多支持99道试题",
      type: "warning",
      plain: true,
    });
  }
};
let question = ref();
// 保存自定义试题
const handleQuestionSave = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      if (questionMode.value === "add") {
        const config_: any = {
          ...question.value.getConfig(),
          rowKey: config.question.length + "",
        };
        (config.question as any[]).push(config_);
      } else {
        for (let i = 0; i < config.question.length; i++) {
          if (
            (config.question as any[])[i].rowKey ===
            selectedRows.value[0].rowKey
          ) {
            (config.question as any[])[i] = {
              ...question.value.getConfig(),
              rowKey: selectedRows.value[0].rowKey,
            };
            break;
          }
        }
      }
      questionDialogProp.visible = false;
    }
  });
};

const handleQuestionView = (row: any) => {
  selectedRows.value = [row];
  previewDo(row);
};
// 预览试题
const handleQuestionPreview = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      previewDo(question.value.config);
    }
  });
};

const dialogPropPreview = reactive({
  visible: false,
  title: "",
  top: "10vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: false,
});
// 实际预览操作
const previewDo = (config: any) => {
  questionConfig.value = config;
  currentType.value = config.type;
  currentPreviewComponent.value = previewComponents[currentType.value];
  dialogPropPreview.title = "预览-" + types[currentType.value];
  dialogPropPreview.visible = true;
};

// 编辑试题
const handleQuestionEdit = (row: any) => {
  selectedRows.value = [row];
  questionMode.value = "edit";
  currentType.value = row.type;
  currentComponent.value = formComponents[row.type];
  questionConfig.value = row;
  questionDialogProp.title = "编辑-" + types[row.type];
  questionDialogProp.visible = true;
};

onUnmounted(() => {
  destroySortable();
});

let form = ref()
defineExpose({ form, genEntity });
</script>

<template>
  <el-form label-width="80px" ref="form" :model="config" :rules="rules">
    <el-row>
      <el-col :span="24">
        <el-form-item label="所属分类" prop="classifyId">
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
        <el-form-item label="试卷类型" prop="type">
          <el-radio v-model="config.type" :label="1">普通试卷</el-radio>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="试卷标题" prop="title">
          <el-input
            v-model="config.title"
            type="textarea"
            maxlength="50"
            show-word-limit
            :rows="1"
            :disabled="disableEdit"
            clearable
          >
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row v-if="!disableEdit">
      <el-col :span="12">
        <el-switch
          v-model="enableDrop"
          active-text="开启试题拖动"
          inactive-text="关闭试题拖动"
        ></el-switch>
      </el-col>
      <el-col :span="12" style="text-align: right; margin-bottom: 5px">
        <el-button
          @click="clickRemoveQuestions"
          icon="Remove"
          type="danger"
          plain
        >
          移 除 所 有
        </el-button>
        <el-button
          @click="handelChoseQuestion"
          icon="Pointer"
          type="primary"
          plain
        >
          从 试 题 模 版 库 中 选 择
        </el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-table
          style="width: 100%; margin-bottom: 15px"
          :header-cell-style="{ backgroundColor: '#F5F7FA', color: '#666666' }"
          :style="{ cursor: enableDrop && !disableEdit ? 'move' : 'default' }"
          :data="config.question"
          @selection-change="(val:any) =>{rows = val}"
          height="calc(100vh - 500px)"
          border
          ref="subTable"
          row-key="rowKey"
        >
          <!--<el-table-column type="selection" align="center" width="40px"></el-table-column>-->
          <el-table-column
            type="index"
            label="#"
            width="50"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="type"
            label="类型"
            :render-header="addReadStar"
            align="center"
            width="100"
          >
            <template #default="scope">
              <span v-if="scope.row.type === 1">单选题</span>
              <span v-else-if="scope.row.type === 2">多选题</span>
              <span v-else-if="scope.row.type === 3">判断题</span>
              <span v-else-if="scope.row.type === 4">填空题</span>
              <span v-else-if="scope.row.type === 5">解答题</span>
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="title"
            label="题干"
            :render-header="addReadStar"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="score"
            label="分数"
            :render-header="addReadStar"
            align="center"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="level"
            label="难度"
            :render-header="addReadStar"
            align="center"
            width="100"
          >
            <template #default="scope">
              <span v-if="scope.row.level === 1">简单</span>
              <span v-else-if="scope.row.level === 2">一般</span>
              <span v-else-if="scope.row.level === 3">中等</span>
              <span v-else-if="scope.row.level === 4">较难</span>
              <span v-else-if="scope.row.level === 5">很难</span>
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width="180px" v-if="!disableEdit">
            <template #header>
              <el-popover placement="bottom" width="370">
                <div style="text-align: center">
                  <el-button
                    link
                    type=""
                    icon="TurnOff"
                    @click="handleQuestionAdd(1)"
                    >单选题</el-button
                  >
                  <el-button
                    link
                    type=""
                    icon="SetUp"
                    @click="handleQuestionAdd(2)"
                    >多选题</el-button
                  >
                  <el-button
                    link
                    type=""
                    icon="Finished"
                    @click="handleQuestionAdd(3)"
                    >判断题</el-button
                  >
                  <el-button
                    link
                    type=""
                    icon="Edit"
                    @click="handleQuestionAdd(4)"
                    >填空题</el-button
                  >
                  <el-button
                    link
                    type=""
                    icon="Edit"
                    @click="handleQuestionAdd(5)"
                    >解答题</el-button
                  >
                </div>
                <template #reference>
                  <el-button icon="Plus" type="" link>添加试题</el-button>
                </template>
              </el-popover>
            </template>
            <template #default="scope">
              <el-button
                @click="handleQuestionView(scope.row)"
                icon="View"
                type=""
                link
                >预览</el-button
              >
              <el-button
                @click="handleQuestionEdit(scope.row)"
                icon="Edit"
                type=""
                link
                >编辑</el-button
              >
              <el-button
                @click="clickRemoveQuestion(scope.row, scope.$index)"
                icon="Remove"
                style="color: red"
                type=""
                link
                >移除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="答题时长" prop="duration">
          <el-select
            v-model="config.duration"
            placeholder="请选择"
            :disabled="disableEdit"
          >
            <el-option label="10分钟" :value="10"></el-option>
            <el-option label="20分钟" :value="20"></el-option>
            <el-option label="30分钟" :value="30"></el-option>
            <el-option label="40分钟" :value="40"></el-option>
            <el-option label="50分钟" :value="50"></el-option>
            <el-option label="60分钟" :value="60"></el-option>
            <el-option label="70分钟" :value="70"></el-option>
            <el-option label="80分钟" :value="80"></el-option>
            <el-option label="90分钟" :value="90"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12" v-if="showShare">
        <el-form-item label="是否共享" prop="share" label-width="100px">
          <el-select 
            v-model="config.share" 
            :disabled="mode === 'view'"
            placeholder="请选择是否共享">
            <el-option label="共享" :value="1" />
            <el-option label="独享" :value="0" />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="是否共享" prop="share" label-width="100px">
          <el-switch
            v-model="config.share"
            active-color="#014B70"
            inactive-color="#BFCBD9"
            :active-value="1"
            :inactive-value="0"
            active-text=""
            inactive-text=""
          >
          </el-switch>
        </el-form-item> -->
      </el-col>
    </el-row>
    <!--新增编辑From-->
    <el-dialog v-bind="questionDialogProp" v-model="questionDialogProp.visible">
      <component
        v-if="questionDialogProp.visible"
        :is="componentNames[currentComponent]"
        :show-classify="false"
        :data="questionConfig"
        :mode="questionMode"
        ref="question"
      ></component>
      <template #footer>
        <el-button
          type="primary"
          @click="handleQuestionPreview"
          icon="View"
          plain
          >题 目 预 览</el-button
        >
        <el-button
          v-if="mode !== 'view'"
          type="primary"
          @click="handleQuestionSave"
          :disabled="stopBtn"
          icon="CircleCheck"
          >保 存</el-button
        >
        <el-button
          @click="questionDialogProp.visible = false"
          icon="CircleClose"
          >关 闭</el-button
        >
      </template>
    </el-dialog>
    <!--试题-->
    <el-dialog v-bind="dialogPropPreview" v-model="dialogPropPreview.visible">
      <component
        v-if="dialogPropPreview.visible"
        :is="componentNames[currentPreviewComponent]"
        :data="questionConfig"
      ></component>
      <template #footer>
        <el-button
          @click="dialogPropPreview.visible = false"
          icon="CircleClose"
          >关 闭</el-button
        >
      </template>
    </el-dialog>
    <!--选择试题-->
    <el-drawer
      title="选择试题"
      v-model="questionDrawerVisible"
      :wrapper-closable="false"
      :append-to-body="true"
      direction="rtl"
      size="60%"
    >
      <QuestionChose
        v-if="questionDrawerVisible"
        @submit="handelQuestionChoseDo"
      ></QuestionChose>
    </el-drawer>
  </el-form>
</template>

<style lang="scss" scoped></style>
