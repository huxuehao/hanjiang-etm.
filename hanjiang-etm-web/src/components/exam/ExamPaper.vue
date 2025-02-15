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
import {
    paperTemplateSelectById,
  } from '@/api/paperTemplate'
import PaperChose from './PaperChose.vue'
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

const props = defineProps({
  data: {
    type: Object,
    default: () => {
      return {}
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
})

let paperTitle = ref("")
let paperForm = reactive<any>({
  question: [],
  paperId:null
})
onMounted(() => {
  nextTick(() => {
    if (['edit','view'].indexOf(props.mode) > -1) {
      Object.assign(paperForm, props.data)
      if (paperForm.paperId) {
        paperTemplateSelectById(paperForm.paperId ).then((res) => {
          paperTitle.value = res.data.data.title;
        })
      }
    }
  })
})

let questionSelection = ref(false)
watch(() => paperForm.question, (newVal) => {
  if(newVal){
    questionSelection.value = true
  }
})

let enableDrop = ref(false)
watch(enableDrop, (newVal) => {
  if (newVal) {
    initSortable()
  } else {
    destroySortable()
  }
})

let addVisible = ref(false)
let paperConfig = reactive({})
const questionDialogProp = reactive({
  visible: false,
  title: '',
  top: '5vh',
  width: '75%',
  modal: true,
  appendToBody: true,
  showClose: true,
})
let questionNumber = ref(0)

// 打开试卷选择
let paperDrawerVisible = ref(false)
const handlePaper = () => {
  paperDrawerVisible.value = true
};
// 移除试题
const removeExamPaper = () => {
  ElMessageBox.confirm(
    `确认移除所有试题 ?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      paperForm.question = []
      paperForm.paperId = null
      paperTitle.value = ""
    })
};

// 选择试卷回显
const handlePaperChose = (rows:any[] = []) => {
  paperForm.paperId = rows[0].id
  paperTitle.value = rows[0].title;
  paperTemplateSelectById(paperForm.paperId).then((res) => {
    paperForm.question = res.data.data.question;
  })
  paperDrawerVisible.value = false
};

let questionConfig = ref<any>({})
let questionMode = ref("add")
let currentPreviewComponent = ref("")
let currentComponent = ref("")
let selectedRows = ref<any>([])
let currentType = ref("")
const dialogPropPreview = reactive({
  visible: false,
  title: '',
  top: '5vh',
  width: '75%',
  modal: true,
  appendToBody: true,
  showClose: true,
})
//试题预览
const handleQuestionView = (row: any) => {
  previewDo(row)
};
const previewDo = (config: any) => {
  questionConfig.value = config
  currentPreviewComponent.value = previewComponents[config.type]
  dialogPropPreview.title = '预览-' + types[config.type]
  dialogPropPreview.visible = true
}
// 试题编辑
const handleQuestionEdit = (row: any) => {
  selectedRows.value = [row]
  questionMode.value = 'edit'
  currentType.value = row.type
  currentComponent.value = formComponents[row.type]

  questionConfig.value = row
  questionDialogProp.title = '编辑-' + types[row.type]
  questionDialogProp.visible = true
};

let question = ref()
//题目预览
const handleQuestionPreview = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      previewDo(question.value.config)
    }
  });
};
// 移除试题
const clickRemoveQuestion = (row: any, index: number) => {
  ElMessageBox.confirm(
    "确认移除所选择的试题?",
    "提示",
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    paperForm.question.splice(index, 1);
    for (let i = 0; i < paperForm.question.length; i++) {
      paperForm.question[i].rowKey = i + ""
    }
    if (paperForm.question.length === 0) {
      paperForm.paperId = null
      paperTitle.value = ""
    }
  })
};

// 添加自定义试题
const handleQuestionAdd = (type: any) => {
  if (paperForm.question.length < 99) {
    questionMode.value = 'add'
    currentType.value = type
    addVisible.value = false
    currentComponent.value = formComponents[type]
    questionConfig.value = {}
    questionDialogProp.title = '新增-' + types[type]
    questionDialogProp.visible = true
  } else {
    ElMessage({
      message: "最多支持99道试题",
      type: "warning",
      plain: true
    })
  }
};
// 保存自定义试题
const handleSaveQuestion = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      if (questionMode.value === 'add') {
        const config = {
          ...question.value.getConfig(),
          rowKey: paperForm.question.length + ''
        }
        paperForm.question.push(config)
      } else {
        for (let i = 0; i < paperForm.question.length; i++) {
          if (paperForm.question[i].rowKey === selectedRows.value[0].rowKey) {
            paperForm.question[i] = {
              ...question.value.getConfig(),
              rowKey: selectedRows.value[0].rowKey
            }
            break
          }
        }
      }
      questionDialogProp.visible = false
    }
  });
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
// 初始化拖拽
let subTable = ref()
let sortableBody = ref<any>(null)
const initSortable = () => {
  enableDrop.value = true
  const el = subTable.value.$el.querySelector('.el-table__body-wrapper tbody');
  sortableBody.value = new Sortable.create(el, {
    animation: 150,
    onEnd: handleDragEnd,
  });
};
// 销毁拖拽
const destroySortable = () => {
  enableDrop.value = false
  if (sortableBody.value === null) {
    return
  }
  sortableBody.value.destroy()
  sortableBody.value = null
};
// 处理拖拽结束
const handleDragEnd = (evt: any) => {
  const {
    newIndex,
    oldIndex
  } = evt
  if (newIndex === oldIndex) {
    return
  }
  const currRow = paperForm.question.splice(oldIndex, 1)[0];
  paperForm.question.splice(newIndex, 0, currRow);
};

const getEntity = () => {
  return paperForm
};

defineExpose({getEntity, paperForm})

onUnmounted(() => {
  destroySortable();
})

</script>

<template>
  <el-form
      label-width="100px"
      ref="form"
      label-position="left"
      :model="paperForm">
    <el-row>
      <el-col :span="24">
        <span style="font-size: 13px;margin-top: 5px;">
          当前试题模版： {{ paperTitle ? paperTitle : "暂无" }}
        </span>
      </el-col>
    </el-row>
    <el-row v-if="!disableEdit">
      <el-col :span="12" style="margin-top: 5px">
        <el-switch v-model="enableDrop" active-text="开启试题拖动" inactive-text="关闭试题拖动"></el-switch>
      </el-col>
      <el-col :span="12" style="text-align: right">
        <el-button icon="Remove" @click="removeExamPaper"
                    type="danger" plain>移除所有
        </el-button>
        <el-button  icon="Pointer" @click="handlePaper">
          从 试 卷 模 版 库 中 选 择
        </el-button>
      </el-col>
    </el-row>
    
    <el-row>
      <el-table style="width: 100%;"
                :header-cell-style="{backgroundColor: '#f8f8f8',color:'#666666'}"
                :style="{cursor:(enableDrop  ? 'move' : 'default')}"
                :data="paperForm.question"
                height="calc(100vh - 400px)" border ref="subTable" row-key="rowKey">
        <el-table-column type="index" label="#" width="50" align="center"></el-table-column>
        <el-table-column prop="type" label="类型" :render-header="addReadStar" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.type === 1">单选题</span>
            <span v-else-if="scope.row.type === 2">多选题</span>
            <span v-else-if="scope.row.type === 3">判断题</span>
            <span v-else-if="scope.row.type === 4">填空题</span>
            <span v-else-if="scope.row.type === 5">解答题</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" :render-header="addReadStar" show-overflow-tooltip width="200"></el-table-column>
        <el-table-column prop="title" label="题干" :render-header="addReadStar" show-overflow-tooltip></el-table-column>
        <el-table-column prop="score" label="分数" :render-header="addReadStar" align="center" width="100"></el-table-column>
        <el-table-column prop="level" label="难度" :render-header="addReadStar" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.level === 1">简单</span>
            <span v-else-if="scope.row.level === 2">一般</span>
            <span v-else-if="scope.row.level === 3">中等</span>
            <span v-else-if="scope.row.level === 4">较难</span>
            <span v-else-if="scope.row.level === 5">很难</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column align="center" width="180px" >
          <template #header slot-scope="scope">
            <el-popover
              placement="bottom"
              width="370"
              v-if="!disableEdit"
              v-model="addVisible">
              <el-button link type="" icon="TurnOff" @click="handleQuestionAdd(1)"
                >单选题</el-button
              >
              <el-button link type="" icon="SetUp" @click="handleQuestionAdd(2)"
                >多选题</el-button
              >
              <el-button link type="" icon="Finished" @click="handleQuestionAdd(3)"
                >判断题</el-button
              >
              <el-button link type="" icon="Edit" @click="handleQuestionAdd(4)"
                >填空题</el-button
              >
              <el-button link type="" icon="Edit" @click="handleQuestionAdd(5)"
                >解答题</el-button
              >
              <template #reference>
                <el-button type="text" icon="CirclePlus" style="width: 60px;display: inline-block;text-align: center">新增</el-button>
              </template>
            </el-popover>
            <span v-else>操作</span>
          </template>
          <template #default="scope">
            <el-button link @click="handleQuestionView(scope.row)"icon="View"
                        type="text">预览</el-button>
            <el-button link v-if="!disableEdit" @click="handleQuestionEdit(scope.row)" icon="Edit"
                        type="text">编辑</el-button>
            <el-button link v-if="!disableEdit" @click="clickRemoveQuestion(scope.row, scope.$index)" icon="RemoveOutline"
                        style="color: red"
                        type="text">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <!--选择试题-->
    <el-drawer
      title='选择试题'
      v-model="paperDrawerVisible"
      :wrapper-closable="false"
      :append-to-body="true"
      direction="rtl"
      size="75%">
      <div style="width: 100%;height: calc(100vh - 100px);position: relative;overflow: auto;">
        <PaperChose v-if="paperDrawerVisible" @submit="handlePaperChose"></PaperChose>
      </div>
    </el-drawer>
    <!--试题预览-->
    <el-dialog v-bind="dialogPropPreview" v-model="dialogPropPreview.visible">
      <component
        v-if="dialogPropPreview.visible"
        :is="componentNames[currentPreviewComponent]"
        :data="questionConfig"></component>
      <template #footer class="dialog-footer">
        <el-button type="primary" @click="dialogPropPreview.visible = false" icon="CircleClose">关 闭</el-button>
      </template>
    </el-dialog>
    <!--新增编辑From-->
    <el-dialog v-bind="questionDialogProp" v-model="questionDialogProp.visible">
      <component
        v-if="questionDialogProp.visible"
        :is="componentNames[currentComponent]"
        :show-classify="false"
        :data="questionConfig"
        :mode="questionMode"
        ref="question"></component>
      <template #footer class="dialog-footer">
        <el-button  type="primary" @click="handleSaveQuestion"  icon="CircleCheck">保 存</el-button>
        <el-button type="primary" @click="handleQuestionPreview" icon="View" plain>题 目 预 览</el-button>
        <el-button @click="questionDialogProp.visible = false" icon="CircleClose">关 闭</el-button>
      </template>
    </el-dialog>
  </el-form>
</template>

<style lang="scss" scoped>
</style>