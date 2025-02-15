<script lang="ts" setup>
import {
  computed,
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
import { attachUpload } from "@/api/attach";
import { formatDate, formatFileSize } from "@/utils/tools";
import QuestionChose from "@/components/paper/QuestionChose.vue";
import UserChose from "@/components/user/UserChose.vue";
import { fetchValueByKey } from "@/api/params";
import { uploadFileInChunks } from "@/chunkfile/index"

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

const treeProps = {
  value: "id",
  label: "name",
  children: "children",
};

const shortcuts1 = [
  {
    text: "当前时间",
    value: () => new Date(),
  },
  {
    text: "24小时前",
    value: () => {
      const date = new Date();
      date.setTime(date.getTime() - 3600 * 1000 * 24);
      return date;
    },
  },
  {
    text: "7天前",
    value: () => {
      const date = new Date();
      date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
      return date;
    },
  },
];

const shortcuts2 = [
  {
    text: "当前时间",
    value: () => new Date(),
  },
  {
    text: "24小时后",
    value: () => {
      const date = new Date();
      date.setTime(date.getTime() + 3600 * 1000 * 24);
      return date;
    },
  },
  {
    text: "7天后",
    value: () => {
      const date = new Date();
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
      return date;
    },
  },
];
function validateTime(rule: any, value: any, callback: any) {
  const endTime = config.limitEndTime;
  if (value && endTime && new Date(value) >= new Date(endTime)) {
    callback(new Error("开始时间必须小于结束时间"));
  } else {
    callback();
  }
}

const rules = {
  classifyId: [{ required: true, message: "必填项", trigger: "blur" }],
  title: [{ required: true, message: "必填项", trigger: "blur" }],
  des: [{ required: true, message: "必填项", trigger: "blur" }],
  duration: [{ required: true, message: "必填项", trigger: "blur" }],
  qualifiedScore: [{ required: true, message: "必填项", trigger: "blur" }],
  limitStartTime: [
    { required: true, message: "必填项", trigger: "blur" },
    { validator: validateTime, trigger: "blur" },
  ],
  limitEndTime: [{ required: true, message: "必填项", trigger: "blur" }],
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
  classifyDict: {
    type: Array,
    default: () => {
      return [];
    },
  },
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

let config = reactive<any>({
  classifyId: null,
  title: null,
  des: null,
  qualifiedScore: 60,
  duration: 60,
  limitStartTime: null,
  limitEndTime: null,

  file: [],
  question: [],
  user: [],
});
onMounted(() => {
  nextTick(() => {
    if (["edit", "view"].indexOf(props.mode) > -1) {
      Object.assign(config, props.data);
    }
  });
});
let selectedUserIds = computed(() => {
  return config.user.map((item: any) => item.userId);
});

let enableDrop = ref(false);
let sortableBody = ref<any>(null);
let subTable = ref();
// 监控拖拽按钮
watch(enableDrop, (val) => {
  if (val) {
    initSortable();
  } else {
    destroySortable();
  }
});
// 初始化拖拽
const initSortable = () => {
  if (!props.disableEdit) {
    enableDrop.value = true;
    const el = subTable.value.$el.querySelector(
      ".el-table__body-wrapper tbody"
    );
    sortableBody.value = new Sortable.create(el, {
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
// 销毁
const destroySortable = () => {
  enableDrop.value = false;
  if (sortableBody.value === null) {
    return;
  }
  sortableBody.value.destroy();
  sortableBody.value = null;
};

let enableDrop2 = ref(false);
let sortableBody2 = ref<any>(null);
let fileTable = ref();
// 监控拖拽按钮
watch(enableDrop2, (val) => {
  if (val) {
    initSortable2();
  } else {
    destroySortable2();
  }
});
// 初始化拖拽
const initSortable2 = () => {
  if (!props.disableEdit) {
    enableDrop2.value = true;
    const el = fileTable.value.$el.querySelector(
      ".el-table__body-wrapper tbody"
    );
    sortableBody2.value = new Sortable.create(el, {
      animation: 150,
      onEnd: handleDragEnd2,
    });
  }
};
// 处理拖拽结束
const handleDragEnd2 = (evt: any) => {
  const { newIndex, oldIndex } = evt;
  if (newIndex === oldIndex) {
    return;
  }
  const currRow = config.file.splice(oldIndex, 1)[0];
  config.file.splice(newIndex, 0, currRow);
};
// 销毁
const destroySortable2 = () => {
  enableDrop2.value = false;
  if (sortableBody2.value === null) {
    return;
  }
  sortableBody2.value.destroy();
  sortableBody2.value = null;
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

let activeStep = ref(0);
// 上一步
const previousStep = () => {
  if (activeStep.value > 0) {
    changeStep(activeStep.value - 1);
  } else {
    ElMessage({
      message: "当前已经是第一步了",
      type: "warning",
      plain: true,
    });
  }
};
// 下一步
const nextStep = () => {
  if (activeStep.value < 3) {
    changeStep(activeStep.value + 1);
  } else {
    ElMessage({
      message: "当前已经是最后一步了",
      type: "warning",
      plain: true,
    });
  }
};
// 改变步骤条
let form = ref();
const changeStep = (step: number) => {
  form.value.validate((valid: any) => {
    if (valid) {
      activeStep.value = step;
    }
  });
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

//  从题库中选择，并完成试题的载入
const handelQuestionChoseDo = (rows: any[] = []) => {
  // 获取不重复的实体
  const newQuestions = rows
    .filter((item) => {
      for (let q of config.question) {
        if (item.id + "" === q.id) {
          return false;
        }
      }
      return true;
    })
    .map((item) => {
      return JSON.parse(item.config);
    });
  // 插入到目标表单
  for (let questionConfig of newQuestions) {
    const config_ = { ...questionConfig, rowKey: questionConfig.id + "" };
    config.question.push(config_);
  }
  questionDrawerVisible.value = false;
};

// 点击添加试题
let questionConfig = ref<any>({});
let questionMode = ref("add");
let stopBtn = ref(false);
let currentType = ref(1);
let addVisible = ref(false);
let currentComponent = ref("");
const questionDialogProp = reactive({
  visible: false,
  title: "",
  top: "5vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: true,
});
let selectedQuestionRows = ref<any>([]);

// 点击添加试题
const handleQuestionAdd = (type: number) => {
  if (config.question.length < 99) {
    questionMode.value = "add";
    currentType.value = type;
    addVisible.value = false;
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
// 点击编辑试题
const handleQuestionEdit = (row: any) => {
  selectedQuestionRows.value = [row];
  questionMode.value = "edit";
  currentType.value = row.type;
  currentComponent.value = formComponents[row.type];
  questionConfig.value = row;
  questionDialogProp.title = "编辑-" + types[row.type];
  questionDialogProp.visible = true;
};

// 保存试题
const handleQuestionSave = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      if (questionMode.value === "add") {
        const config_ = {
          ...question.value.getConfig(),
          rowKey: config.question.length + "",
        };
        config.question.push(config_);
      } else {
        for (let i = 0; i < config.question.length; i++) {
          if (
            config.question[i].rowKey === selectedQuestionRows.value[0].rowKey
          ) {
            config.question[i] = {
              ...question.value.getConfig(),
              rowKey: selectedQuestionRows.value[0].rowKey,
            };
            break;
          }
        }
      }
      questionDialogProp.visible = false;
    }
  });
};

// 删除试题
const clickRemoveQuestions = () => {
  ElMessageBox.confirm("确认移除全部试题 ?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    config.question = [];
  });
};
const clickRemoveQuestion = (row: any, index: number) => {
  ElMessageBox.confirm("确认移除所选试题 ?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    config.question.splice(index, 1);
    for (let i = 0; i < config.question.length; i++) {
      config.question[i].rowKey = i + "";
    }
  });
};

// 预览
const handleQuestionView = (row: any) => {
  selectedQuestionRows.value = [row];
  previewDo(row);
};
// 预览试题
let question = ref();
const handleQuestionPreview = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      previewDo(question.value.config);
    }
  });
};
// 实际预览操作
let currentPreviewComponent = ref("");
const dialogPropPreview = reactive({
  visible: false,
  title: "",
  top: "10vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: true,
});
const previewDo = (config: any) => {
  questionConfig.value = config;
  currentType.value = config.type;
  currentPreviewComponent.value = previewComponents[currentType.value];
  dialogPropPreview.title = "预览-" + types[currentType.value];
  dialogPropPreview.visible = true;
};

let fileMaxSize:number = 200;
let acceptFiles:any = [];
onMounted(() => {
  fetchValueByKey("ALLOW_UPLOAD_FILE_TYPE").then((res: any) => {
    acceptFiles = res.data.data.split(",")
  })
  fetchValueByKey("SINGLE_FILE_MAX_SIZE").then((res: any) => {
    fileMaxSize = Number(res.data.data)
  })
})
// 资料上传之前
let fileTableLoading = ref(false)
const beforeUpload = (file: any) => {
  fileTableLoading.value = true
  const maxSize = fileMaxSize * 1024 * 1024; // 500MB
  const fileNames = file.name.split(".");
  const extension = fileNames[fileNames.length - 1].toLowerCase();
  if (acceptFiles.indexOf("." + extension) === -1) {
    fileTableLoading.value = false
    ElMessage({
      message: `文件格式错误，只允许后缀名为 ${acceptFiles.join("、")} 的文件`,
      type: "warning",
      plain: true,
    });
  } else if (maxSize < file.size) {
    fileTableLoading.value = false
    ElMessage({
      message: `文件大小不可超过${fileMaxSize}MB`,
      type: "warning",
      plain: true,
    });
  } else {
    uploadFile(file, fileNames[0], extension);
  }
  return false;
};
// 上传文件
const uploadFile = (file: any, name: string, extension: string) => {
  // attachUpload(file)
  uploadFileInChunks(file)
    .then((res) => {
      config.file.push({
        fileId: res.data.data,
        fileName: name,
        fileExtension: extension,
        fileDesc: null,
        fileType: extension === "mp4" ? 1 : 2,
        fileSize: formatFileSize(file.size),
        fileDuration: null,
        uploadTime: formatDate(new Date()),
        sort: null,
      });
    })
    .finally(() => {
      fileTableLoading.value = false
    });
};

let currentFile = ref<any>({});
const dialogFileMore = reactive({
  visible: false,
  title: "",
  top: "20vh",
  width: "50%",
  modal: true,
  appendToBody: true,
  showClose: true,
});
// 点击资料-更多
const handleMoreFile = (row: any) => {
  currentFile.value = JSON.parse(JSON.stringify(row));
  dialogFileMore.title = "更多";
  dialogFileMore.visible = true;
};
// 保存更多
let moreFile = ref();
const saveMoreFile = () => {
  moreFile.value.validate((valid: any) => {
    if (valid) {
      config.file.forEach((item: any) => {
        if (item.fileId === currentFile.value.fileId) {
          item.fileName = currentFile.value.fileName;
          item.fileDesc = currentFile.value.fileDesc;
        }
      });
      dialogFileMore.visible = false;
    }
  });
};
// 移除资料
const handleRemoveFile = (row: any) => {
  ElMessageBox.confirm(`确认移除 [ ${row.fileName} ] ?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    config.file = config.file.filter((item: any) => item.fileId !== row.fileId)
  });
};

let userSelectVisible = ref(false);
let selectedUserRows = ref<any>([]);
// 配置人员
const userSelect = () => {
  userSelectVisible.value = true;
};
const handleTrainPeople = (selectUsers: any) => {
  if (selectUsers.length === 0) {
    config.user = [];
  } else if (selectUsers.length > 500) {
    ElMessage({
      message: "人数已达上限，最多支持500人",
      type: "warning",
      plain: true,
    });
  } else {
    // 获取第一个比第二个多的元素，以及第一个比第二个少的元素
    const { moreInOne, lessInOne } = getDiffArrays(selectUsers, config.user);
    // 添加用户
    moreInOne.forEach((item: any) => {
      config.user.push({
        userId: item.id,
        orgId: item.orgId,
        userName: item.name,
        userCode: item.code
      });
    });
    // 删除用户
    if (lessInOne.length > 0) {
      config.user = config.user.filter((item: any) => {
        for (let v of lessInOne) {
          if (item.userId === v.userId) {
            return false;
          }
        }
        return true;
      });
    }
    userSelectVisible.value = false;
  }
};

// 获取两个数组的不同
const getDiffArrays = (arrA: any[], arrB: any[]) => {
  const idsInOne = arrA.map((item) => item.id);
  const idsInTwo = arrB.map((item) => item.userId);
  const moreInOne = arrA.filter((item) => !idsInTwo.includes(item.id));
  const lessInOne = arrB.filter((item) => !idsInOne.includes(item.userId));
  return { moreInOne, lessInOne };
};

// 移除人员
const userDelete = () => {
  ElMessageBox.confirm("确认删除所选择的用户？", "提示").then(() => {
    config.user = config.user.filter((item: any) => {
      return !selectedUserRows.value.some(
        (item2: any) => item2.userId === item.userId
      );
    });
    selectedUserRows.value = [];
  });
};

// 删除用户
const handleDelete = (row: any, index: number) => {
  ElMessageBox.confirm(
    `确认移除 [ ${row.userName} ] ?`,
    "提示",
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    config.user.splice(index,1)
  })
};

const genEntity = () => {
  return config;
};
defineExpose({
  genEntity,
  previousStep,
  nextStep,
  config,
  form
});
onUnmounted(() => {
  destroySortable();
  destroySortable2();
});
</script>

<template>
  <el-steps :active="activeStep" simple>
    <el-step
      title="基础配置"
      icon="Tickets"
      style="cursor: pointer"
      @click.native="changeStep(0)"
    ></el-step>
    <el-step
      title="资料配置"
      icon="DocumentCopy"
      style="cursor: pointer"
      @click.native="changeStep(1)"
    ></el-step>
    <el-step
      title="试题配置"
      icon="Notebook"
      style="cursor: pointer"
      @click.native="changeStep(2)"
    ></el-step>
    <el-step
      title="人员配置"
      icon="User"
      style="cursor: pointer"
      @click.native="changeStep(3)"
    ></el-step>
  </el-steps>
  <el-form
    :model="config"
    :rules="rules"
    label-width="80px"
    style="margin-top: 10px;"
    ref="form"
  >
    <!--基础配置-->
    <div v-show="activeStep === 0" style="margin-top: 20px;">
      <el-row>
        <el-col :span="24">
          <el-form-item label="所属分类" prop="classifyId">
            <el-tree-select
              v-model="config.classifyId"
              :data="classifyDict"
              :render-after-expand="true"
              :default-expand-all="true"
              :disabled="disableEdit"
              :props="treeProps"
              placeholder="请选择"
              filterable
              check-strictly
              clearable
            >
            </el-tree-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="培训标题" prop="title">
            <el-input
              v-model="config.title"
              placeholder="培训标题"
              :disabled="disableEdit"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="培训描述" prop="des">
            <el-input
              v-model="config.des"
              type="textarea"
              :rows="2"
              maxlength="100"
              show-word-limit
              placeholder="培训描述"
              :disabled="disableEdit"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="答题时长" prop="duration">
            <el-select
              v-model="config.duration"
              placeholder="请选择"
              :disabled="disableEdit"
              clearable
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
        <el-col :span="12">
          <el-form-item label="合格分数" prop="qualifiedScore">
            <el-input-number
              style="width: 100%"
              v-model="config.qualifiedScore"
              :step="10"
              :precision="0"
              :disabled="disableEdit"
              clearable
            ></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="开始时间" prop="limitStartTime">
            <el-date-picker
              type="datetime"
              v-model="config.limitStartTime"
              :shortcuts="shortcuts1"
              :disabled="disableEdit"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
              placeholder="选择开始时间"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" prop="limitEndTime">
            <el-date-picker
              type="datetime"
              v-model="config.limitEndTime"
              :shortcuts="shortcuts2"
              :disabled="disableEdit"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
              placeholder="选择结束时间"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </div>
    <!--资料配置-->
    <div v-show="activeStep === 1">
      <el-row v-if="!disableEdit" style="margin-bottom: 5px;">
        <el-col :span="12">
          <el-switch
            v-model="enableDrop2"
            active-text="开启资料拖动"
            inactive-text="关闭资料拖动"
          ></el-switch>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <el-upload
            class="upload-box"
            ref="upload"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :accept="acceptFiles.join(', ')"
            :max-size="fileMaxSize * 1024 * 1024"
            action="#"
          >
            <el-button type="primary" icon="UploadFilled"
              >点击上传</el-button
            >
            <!-- <template #tip class="el-upload__tip">
              只能上传 {{ acceptFiles.join("、") }} 文件，且单个文件不超过
              {{ fileMaxSize }}MB
            </template> -->
          </el-upload>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-table
            style="width: 100%; margin-bottom: 15px; cursor: move"
            v-loading="fileTableLoading"
            element-loading-text="文件上传中"
            :header-cell-style="{
              backgroundColor: '#f8f8f8',
              color: '#666666',
            }"
            :style="{
              cursor: enableDrop2 && !disableEdit ? 'move' : 'default',
            }"
            :data="config.file"
            height="calc(100vh - 425px)"
            border
            ref="fileTable"
            row-key="fileId"
          >
            <el-table-column
              type="index"
              label="#"
              width="50"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="fileName"
              label="资料名称"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="fileSize"
              label="资料大小"
              align="center"
              width="150px"
            ></el-table-column>
            <el-table-column
              prop="fileDuration"
              label="资料时长"
              align="center"
              width="150px"
            >
              <template #default="scope">
                <span v-if="scope.row.fileDuration">{{
                  scope.row.fileDuration
                }}</span>
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="fileType"
              label="资料类型"
              align="center"
              width="150px"
            >
              <template #default="scope">
                <el-tag type="info">{{ scope.row.fileExtension }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="uploadTime"
              label="上传时间"
              align="center"
              width="160px"
            ></el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="150px"
              v-if="!disableEdit"
            >
              <template #default="scope">
                <el-button link
                  @click="handleMoreFile(scope.row)"
                  icon="Operation"
                  type="text"
                  >更多</el-button
                >
                <el-button link
                  @click="handleRemoveFile(scope.row)"
                  icon="Remove"
                  type="text"
                  style="color: red"
                  >移除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>
    <!--试题配置-->
    <div v-show="activeStep === 2">
      <el-row v-if="!disableEdit" style="margin-bottom: 5px;">
        <el-col :span="12">
          <el-switch
            v-model="enableDrop"
            active-text="开启试题拖动"
            inactive-text="关闭试题拖动"
          ></el-switch>
        </el-col>
        <el-col :span="12" style="text-align: right">
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
            :header-cell-style="{
              backgroundColor: '#f8f8f8',
              color: '#666666',
            }"
            :style="{ cursor: enableDrop && !disableEdit ? 'move' : 'default' }"
            :data="config.question"
            height="calc(100vh - 400px)"
            border
            ref="subTable"
            row-key="rowKey"
          >
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
              prop="name"
              label="名称"
              :render-header="addReadStar"
              show-overflow-tooltip
              width="200"
            ></el-table-column>
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
            <el-table-column align="center" width="200px">
              <template #header>
                <el-popover
                  placement="bottom"
                  width="380"
                  v-if="!disableEdit"
                  v-model="addVisible"
                >
                  <div style="text-align: center">
                    <el-button link
                      type="text"
                      icon="TurnOff"
                      @click="handleQuestionAdd(1)"
                      >单选题</el-button
                    >
                    <el-button link
                      type="text"
                      icon="SetUp"
                      @click="handleQuestionAdd(2)"
                      >多选题</el-button
                    >
                    <el-button link
                      type="text"
                      icon="Finished"
                      @click="handleQuestionAdd(3)"
                      >判断题</el-button
                    >
                    <el-button link
                      type="text"
                      icon="Edit"
                      @click="handleQuestionAdd(4)"
                      >填空题</el-button
                    >
                    <el-button link
                      type="text"
                      icon="Edit"
                      @click="handleQuestionAdd(5)"
                      >解答题</el-button
                    >
                  </div>
                  <template #reference>
                    <el-button
                      icon="Plus"
                      style="
                        width: 60px;
                        display: inline-block;
                        text-align: center;
                      "
                      type="text"
                      >添加试题</el-button
                    >
                  </template>
                  
                </el-popover>
                <span v-else>操作</span>
              </template>
              <template #default="scope">
                <el-button link
                  @click="handleQuestionView(scope.row)"
                  icon="View"
                  type=""
                  >预览</el-button
                >
                <el-button link
                  v-if="!disableEdit"
                  @click="handleQuestionEdit(scope.row)"
                  icon="Edit"
                  type=""
                  >编辑</el-button
                >
                <el-button link
                  v-if="!disableEdit"
                  @click="clickRemoveQuestion(scope.row, scope.$index)"
                  icon="Remove"
                  style="color: red"
                  type=""
                  >移除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>
    <!--人员配置-->
    <div v-show="activeStep === 3">
      <el-row v-if="!disableEdit" style="margin-bottom: 5px;">
        <el-col :span="12" style="text-align: left">
          <el-button
            @click="userDelete"
            type="danger"
            plain
            icon="Delete"
            :disabled="selectedUserRows.length === 0"
            >删除</el-button
          >
        </el-col>
        <el-col :span="12" style="text-align: right">
          <el-button
            @click="userSelect"
            type="primary"
            plain
            icon="User"
            >选择人员</el-button
          >
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-table
            style="width: 100%; margin-bottom: 15px; cursor: move"
            :header-cell-style="{
              backgroundColor: '#f8f8f8',
              color: '#666666',
            }"
            :data="config.user"
            @selection-change="(val:any) =>{selectedUserRows = val}"
            height="calc(100vh - 400px)"
            border
            ref="table"
            row-key="userId"
          >
            <el-table-column
              type="selection"
              width="50"
              align="center"
            ></el-table-column>
            <el-table-column
              type="index"
              label="#"
              width="50"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="userName"
              label="人员姓名"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              prop="userCode"
              label="人员编号"
              align="center"
              show-overflow-tooltip
            >
              <template #default="scope">
                <span v-if="scope.row.userCode">{{ scope.row.userCode }}</span>
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" v-if="!disableEdit">
            <template #default="scope">
              <el-button type="text" icon="Delete" style="color: #ff2d2d"
                        @click="handleDelete(scope.row, scope.$index)">
                移除人员
              </el-button>
            </template>
          </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>
  </el-form>
  <!--新增编辑From-->
  <el-dialog v-bind="questionDialogProp" v-model="questionDialogProp.visible">
    <component
      v-if="questionDialogProp.visible"
      :is="currentComponent"
      :show-classify="false"
      :data="questionConfig"
      :mode="questionMode"
      ref="question"
    ></component>
    <span slot="footer" class="dialog-footer">
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handleQuestionSave"
        :disabled="stopBtn"
        icon="CircleCheck"
        >保 存</el-button
      >
      <el-button
        type="primary"
        @click="handleQuestionPreview"
        icon="View"
        plain
        >题 目 预 览</el-button
      >
      <el-button
        @click="questionDialogProp.visible = false"
        icon="CircleClose"
        >关 闭</el-button
      >
    </span>
  </el-dialog>
  <!--更多资料-->
  <el-dialog v-bind="dialogFileMore" v-model="dialogFileMore.visible">
    <el-form ref="moreFile" :model="currentFile" label-width="100">
      <el-form-item
        label="资料名称"
        prop="fileName"
        :rules="[{ required: true, message: '必填项', trigger: 'blur' }]"
      >
        <el-input
          v-model="currentFile.fileName"
          placeholder="资料名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="资料描述" prop="fileDesc">
        <el-input
          type="textarea"
          :rows="2"
          v-model="currentFile.fileDesc"
          maxlength="100"
          show-word-limit
          placeholder="资料描述"
          clearable
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer class="dialog-footer">
      <el-button
        @click="saveMoreFile"
        icon="CircleCheck"
        type="primary"
        >保 存</el-button
      >
      <el-button
        @click="dialogFileMore.visible = false"
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
    <span slot="footer" class="dialog-footer">
      <el-button
        @click="dialogPropPreview.visible = false"
        icon="CircleClose"
        >关 闭</el-button
      >
    </span>
  </el-dialog>
  <!--选择试题-->
  <el-drawer
    title="选择试题"
    v-model="questionDrawerVisible"
    :wrapper-closable="false"
    :append-to-body="true"
    direction="rtl"
    size="75%"
  >
  <QuestionChose
    v-if="questionDrawerVisible"
    @submit="handelQuestionChoseDo"
  ></QuestionChose>
  </el-drawer>
  <!--选择用户-->
  <el-drawer
      title="选择参考人员"
      size="75%"
      direction="rtl"
      destroy-on-close
      :close-on-click-modal="false"
      v-model="userSelectVisible"
      append-to-body>
      <UserChose
        style="height: 500px"
        v-if="userSelectVisible"
        @submit="handleTrainPeople"
        :user-selected-ids="selectedUserIds"
        ref="userChose">
      </UserChose>
    </el-drawer>
</template>

<style lang="scss" scoped></style>
