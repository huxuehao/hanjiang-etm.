<script lang="ts" setup>
import { computed, onMounted, reactive, ref, shallowReactive } from "vue";
import {
  cancelImport,
  downloadExcelTemplate,
  questionFileParser,
  questionTemplateAdd,
  questionTemplateDelete,
  questionTemplateEdit,
  questionTemplateList,
  questionTemplateSelectById,
  submitImport,
} from "@/api/questionTemplate";
import {
  questionClassifyAdd,
  questionClassifyDelete,
  questionClassifyEdit,
  questionClassifyList,
  questionClassifyTree,
} from "@/api/questionClassify";
// 拖动组件
import ResizeBox from "@/components/resize/ResizeBox.vue";
// 左侧分类树
import SearchTree from "@/components/tree/SearchTree.vue";
import ClassifyTree from "@/components/tree/ClassifyTree.vue";
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
import { ElMessage, ElMessageBox } from "element-plus";
import ImportForm from "@/components/question/ImportForm.vue";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
let userId = computed(() => {
  return userStore.userInfo.id;
});

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

// 初始化
onMounted(() => {
  questionClassifyTree().then((res) => {
    classifyTreeList.value = res.data.data;
  });
  load();
  monitorSearchResize();
});

let tHeight = ref(0);
let searchBox = ref();
const monitorSearchResize = () => {
  const resizeObserver = new ResizeObserver((entries) => {
    for (const entry of entries) {
      const { height } = entry.contentRect;
      tHeight.value = height;
    }
  });
  resizeObserver.observe(searchBox.value);
};

let loading = ref(false);
// 列表数据
let dataList = ref<any>([]);
// 当前分类ID
let currentClassifyId = ref(null);
let classifyDict = ref<any>([]);
let classifyTreeList = ref<any>([]);
// 查询参数
let queryParams = reactive<any>({
  title: null,
  score: null,
  type: null,
  level: null,
  share: 1,
});
// 分页配置
let page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
// 当前所选行
let selectedRows = ref<any>([]);

//  点击分类树
const handleNodeClick = (node: any) => {
  currentClassifyId.value = node.id;
  load();
};
// 加载列表数据
const load = () => {
  loading.value = true;
  questionClassifyList()
    .then((classifyRes) => {
      classifyDict.value = classifyRes.data.data;
      questionTemplateList({
        classifyId: currentClassifyId.value,
        ...queryParams,
        ...page,
      })
        .then((listRes) => {
          dataList.value = listRes.data.data["records"];
          page.total = listRes.data.data["total"];
          // 填充classifyName
          dataList.value.forEach((bItem: any) => {
            const aItem = classifyDict.value.find(
              (aItem: any) => aItem.id === bItem.classifyId
            );
            if (aItem) {
              bItem.classifyName = aItem.name;
            } else {
              bItem.classifyName = "--";
            }
          });
        })
        .finally(() => {
          loading.value = false;
        });
    })
    .catch(() => {
      loading.value = false;
    });
};
// 点击搜索
const handleSearch = () => {
  load();
};
// 点击重置
const handleReset = () => {
  for (let key in queryParams) {
    if (queryParams.hasOwnProperty(key)) {
      queryParams[key] = null;
    }
  }
  currentClassifyId.value = null;
  queryParams.share = 1;
  load();
};

let mode = ref("add");
let currentType = ref(0);
let addVisible = ref(false);
let currentComponent = ref("");
let config = ref<any>({});
let showShare = ref(true);
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "5vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: false,
});

let drawerVisible = ref(false);
let drawerVisibleLoading = ref(false);
let classifyTree = ref();
const openClassifyDrawer = () => {
  drawerVisible.value = true;
  drawerVisibleLoading.value = true;
  questionClassifyTree()
    .then((res) => {
      classifyTreeList.value = res.data.data;
    })
    .finally(() => {
      drawerVisibleLoading.value = false;
    });
};
const addClassify = async (data: any) => {
  try {
    await questionClassifyAdd(data);
    await questionClassifyTree().then((res) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch {
    classifyTree.value.error();
  }
};
const editClassify = async (data: any) => {
  try {
    await questionClassifyEdit(data);
    await questionClassifyTree().then((res) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch (error) {
    console.log(error);

    classifyTree.value.error();
  }
};
const deleteClassify = async (node: any, data: any) => {
  await questionClassifyDelete([data.id]);
  await questionClassifyTree().then((res) => {
    classifyTreeList.value = res.data.data;
  });
};

// 点击新增
const handleAdd = (type: number) => {
  mode.value = "add";
  currentType.value = type;
  addVisible.value = false;
  currentComponent.value = formComponents[type];
  config.value = {};
  showShare.value = true;
  stopBtn.value = false;
  dialogProp.title = "新增-" + types[type];
  dialogProp.visible = true;
};

let question = ref();
const handleAddDo = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        questionTemplateAdd(question.value.genEntity())
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
          })
          .finally(() => {
            stopBtn.value = false;
          });
      } else if (mode.value === "edit") {
        questionTemplateEdit(question.value.genEntity())
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
          })
          .finally(() => {
            stopBtn.value = false;
          });
      } else {
        loading.value = false;
        ElMessage({
          message: "模式不匹配",
          type: "error",
          plain: true,
        });
      }
    }
  });
};
const handleView = (row: any) => {
  previewDo(JSON.parse(row.config));
};
const handlePreview = () => {
  question.value.form.validate((valid: any) => {
    if (valid) {
      previewDo(question.value.config);
    }
  });
};

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
const previewDo = (conf: any) => {
  config.value = conf;
  currentType.value = conf.type;
  currentPreviewComponent.value = previewComponents[currentType.value];
  dialogPropPreview.title = "预览-" + types[currentType.value];
  dialogPropPreview.visible = true;
};
const handleDelete = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  for (let row of rows) {
    if (row.createUser !== userId.value) {
      ElMessage({
        message: "无法删除不属于你数据",
        type: "error",
        plain: true,
      });
      return;
    }
  }

  mode.value = "delete";
  let alertTitle = "此操作将删除所选择的数据 , 是否确定?";

  ElMessageBox.confirm(alertTitle, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    questionTemplateDelete(rows.map((item: any) => item.id))
      .then(() => {
        ElMessage({
          message: "删除成功",
          type: "success",
          plain: true,
        });
        load();
      })
      .catch(() => {
        loading.value = false;
      })
      .finally(() => {
        selectedRows.value = [];
      });
  });
};

const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  currentType.value = row.type;
  currentComponent.value = formComponents[row.type];
  dialogProp.title = "编辑-" + types[row.type];
  config.value = {};
  showShare.value = row.createUser === userId.value;
  questionTemplateSelectById(row.id).then((res) => {
    config.value = JSON.parse(res.data.data.config);
    config.value.id = row.id;
    dialogProp.visible = true;
  });
};

// 可接受的文件类型
let acceptFiles = [".xlsx", ".xls"];
// 文件的最大值
let fileMaxSize = 50;
let importVisible = ref(false);
let importLoading = ref(false);
const importQuestionParserBox = reactive<any>({});
const beforeUpload = (file: File) => {
  const fileNames = file.name.split(".");
  const extension = fileNames[fileNames.length - 1].toLowerCase();
  if (acceptFiles.indexOf("." + extension) === -1) {
    ElMessage({
      message: `文件格式错误，只允许后缀名为 ${acceptFiles.join("、")} 的文件`,
      type: "error",
      plain: true,
    });
  } else {
    uploadFile(file);
  }
  return false;
};
const dialogPropImport = {
  visible: false,
  title: "",
  top: "10vh",
  width: "75%",
  modal: true,
  appendToBody: true,
  showClose: true,
  fullscreen: true,
  closeOnPressEscape: false,
};
const uploadFile = (file: File) => {
  importVisible.value = false;
  importLoading.value = true;
  questionFileParser(file)
    .then((res) => {
      Object.assign(importQuestionParserBox, res.data.data);
      dialogPropImport.title = "Excel解析预览";
      dialogPropImport.visible = true;
    })
    .finally(() => {
      importLoading.value = false;
    });
};
const handelDownloadExcel = () => {
  downloadExcelTemplate().then((res) => {
    const blob = res.data;
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "试题导入模板.xlsx";
    link.click();
    window.URL.revokeObjectURL(url);
  });
};

const handelSubmitImport = () => {
  ElMessageBox.confirm("确认导入试题", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    importLoading.value = true;
    submitImport(importQuestionParserBox)
      .then(() => {
        ElMessage({
          message: "导入成功",
          type: "success",
          plain: true,
        });
        dialogPropImport.visible = false;
        questionClassifyTree().then((res) => {
          classifyTreeList.value = res.data.data;
        });
        load();
      })
      .finally(() => {
        importLoading.value = false;
      });
  });
};
const handelCancelImport = () => {
  ElMessageBox.confirm("确认取消导入试题", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    importLoading.value = true;
    cancelImport(importQuestionParserBox)
      .then(() => {
        ElMessage({
          message: "取消导入成功",
          type: "success",
          plain: true,
        });
        dialogPropImport.visible = false;
      })
      .finally(() => {
        importLoading.value = false;
      });
  });
};

const changePageNo = (pageNo: number) => {
  page.current = pageNo;
  load();
};
const changePageSize = (pageSize: number) => {
  page.size = pageSize;
  load();
};
</script>

<template>
  <ResizeBox :initialWidth="250" :minWidth="250" :maxWidth="450">
    <template #left>
      <div class="resize-left-box">
        <SearchTree
          :data="classifyTreeList"
          @node-click="handleNodeClick"
          height="calc(100vh - 170px)"
        ></SearchTree>
      </div>
    </template>
    <template #right>
      <div class="resize-right-box">
        <!-- 查询区域-->
        <div class="search-box" ref="searchBox">
          <el-form
            ref="queryForm"
            class="query-form"
            :model="queryParams"
            :inline="true"
            label-width="70px"
          >
            <el-form-item label="题目题干" prop="title" class="form-item">
              <el-input
                v-model="queryParams.title"
                placeholder="题目题干"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="题目分数" prop="score" class="form-item">
              <el-input
                v-model="queryParams.score"
                type="number"
                placeholder="题目分数"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="题目类型" prop="type" class="form-item">
              <el-select
                v-model="queryParams.type"
                placeholder="题目类型"
                class="input-search"
                clearable
              >
                <el-option label="单选题" :value="1"></el-option>
                <el-option label="多选题" :value="2"></el-option>
                <el-option label="判断题" :value="3"></el-option>
                <el-option label="填空题" :value="4"></el-option>
                <el-option label="解答题" :value="5"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="题目难度" prop="level" class="form-item">
              <el-select
                v-model="queryParams.level"
                placeholder="题目难度"
                class="input-search"
                clearable
              >
                <el-option label="简单" :value="1"></el-option>
                <el-option label="一般" :value="2"></el-option>
                <el-option label="中等" :value="3"></el-option>
                <el-option label="较难" :value="4"></el-option>
                <el-option label="很难" :value="5"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否共享" prop="share" class="form-item">
              <el-select
                v-model="queryParams.share"
                placeholder="是否共享"
                class="input-search"
              >
                <el-option label="共享" :value="1"></el-option>
                <el-option label="独享" :value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item class="form-item">
              <el-button type="primary" icon="Search" @click="handleSearch"
                >查询</el-button
              >
            </el-form-item>
            <el-form-item class="form-item">
              <el-button icon="Refresh" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <!-- 按钮区域-->
        <el-row class="button-box">
          <el-col :span="16">
            <el-popover placement="bottom" width="370">
              <el-button link type="" icon="TurnOff" @click="handleAdd(1)"
                >单选题</el-button
              >
              <el-button link type="" icon="SetUp" @click="handleAdd(2)"
                >多选题</el-button
              >
              <el-button link type="" icon="Finished" @click="handleAdd(3)"
                >判断题</el-button
              >
              <el-button link type="" icon="Edit" @click="handleAdd(4)"
                >填空题</el-button
              >
              <el-button link type="" icon="Edit" @click="handleAdd(5)"
                >解答题</el-button
              >
              <template #reference>
                <el-button
                  v-permission="'question_add'"
                  type="primary"
                  icon="CirclePlus"
                  >新增</el-button
                >
              </template>
            </el-popover>
            <el-button
              type="danger"
              icon="Delete"
              v-permission="'question_delete'"
              @click="handleDelete(selectedRows)"
            >
              删除
            </el-button>
            <el-popover placement="bottom" width="170">
              <div style="text-align: center">
                <el-upload
                  style="display: inline-block"
                  ref="upload"
                  :show-file-list="false"
                  :before-upload="beforeUpload"
                  :accept="acceptFiles.join(', ')"
                  :max-size="fileMaxSize * 1024 * 1024"
                  action="#"
                >
                  <el-button link type="" icon="Upload">导入题目</el-button>
                </el-upload>
                <el-button
                  style="margin-bottom: 5px"
                  link
                  type=""
                  icon="Tickets"
                  @click="handelDownloadExcel"
                  >下载模版</el-button
                >
              </div>
              <template #reference>
                <el-button
                  v-permission="'question_import'"
                  icon="UploadFilled"
                  plain
                  >批量导入</el-button
                >
              </template>
            </el-popover>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button
              icon="Operation"
              v-permission="'question_classify'"
              @click="openClassifyDrawer"
              title="分类管理"
              >分类管理</el-button
            >
            <el-button icon="RefreshRight" @click="handleSearch" title="刷新"
              >刷新</el-button
            >
          </el-col>
        </el-row>

        <!-- 列表区域 -->
        <div
          class="data-list"
          :style="[{ height: 'calc(100vh - 180px - ' + tHeight + 'px)' }]"
        >
          <el-table
            :stripe="false"
            :data="dataList"
            :header-cell-style="{
              backgroundColor: '#F5F7FA',
              color: '#666666',
            }"
            height="100%"
            v-loading="loading"
            @selection-change="(val: any) => { selectedRows = val }"
            row-key="id"
            border
          >
            <el-table-column
              type="selection"
              width="40"
              align="center"
            ></el-table-column>
            <el-table-column
              label="#"
              type="index"
              width="40"
              align="center"
            ></el-table-column>
            <el-table-column
              label="所属分类"
              prop="classifyName"
              show-overflow-tooltip
              width="200"
            ></el-table-column>
            <el-table-column
              label="题干"
              prop="title"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="分数"
              prop="score"
              align="center"
              width="100"
            ></el-table-column>
            <el-table-column
              label="类型"
              prop="type"
              align="center"
              width="100"
            >
              <template #default="scope">
                <span>{{ types[scope.row.type] }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="难度"
              prop="level"
              align="center"
              width="100"
            >
              <template #default="scope">
                <el-tag
                  v-if="scope.row.level === 1"
                  disable-transitions
                  type="info"
                  >简单</el-tag
                >
                <el-tag v-else-if="scope.row.level === 2" disable-transitions
                  >一般</el-tag
                >
                <el-tag
                  v-else-if="scope.row.level === 3"
                  disable-transitions
                  type="success"
                  >中等</el-tag
                >
                <el-tag
                  v-else-if="scope.row.level === 4"
                  disable-transitions
                  type="warning"
                  >较难</el-tag
                >
                <el-tag
                  v-else-if="scope.row.level === 5"
                  disable-transitions
                  type="danger"
                  >很难</el-tag
                >
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              label="是否共享"
              prop="share"
              align="center"
              width="100"
            >
              <template #default="scope">
                <el-tag v-if="scope.row.share === 1" disable-transitions
                  >共享</el-tag
                >
                <el-tag
                  v-else-if="scope.row.share === 0"
                  disable-transitions
                  type="info"
                  >独享</el-tag
                >
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="200"
              align="center"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  link
                  type=""
                  icon="View"
                  v-permission="'question_preview'"
                  @click="handleView(scope.row)"
                  >预览</el-button
                >
                <el-button
                  link
                  type=""
                  icon="Edit"
                  v-permission="'question_edit'"
                  @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  link
                  type=""
                  icon="Delete"
                  v-permission="'question_delete'"
                  style="color: #f63434"
                  @click="handleDelete([scope.row])"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- 分页区域 -->
        <div class="page-box">
          <el-pagination
            class="page"
            background
            layout="total,sizes,prev,pager,next,jumper"
            :total="page.total"
            :current-page.sync="page.current"
            :page-sizes="[20, 30, 40, 50, 60]"
            :page-size="page.size"
            @current-change="changePageNo"
            @size-change="changePageSize"
          />
        </div>
      </div>
    </template>
  </ResizeBox>
  <!--新增编辑From-->
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <component
      v-if="dialogProp.visible"
      :is="componentNames[currentComponent]"
      :data="config"
      :classifyDict="classifyDict"
      :mode="mode"
      :showShare="showShare"
      ref="question"
    ></component>
    <template #footer>
      <el-button type="primary" @click="handlePreview" icon="View" plain
        >题 目 预 览</el-button
      >
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handleAddDo"
        icon="CircleCheck"
        >保 存</el-button
      >
      <el-button @click="dialogProp.visible = false" icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
  <!--预览From-->
  <el-dialog v-bind="dialogPropPreview" v-model="dialogPropPreview.visible">
    <component
      v-if="dialogPropPreview.visible"
      :is="componentNames[currentPreviewComponent]"
      :data="config"
      mode="preview"
    ></component>
    <template #footer>
      <el-button
        @click="() => (dialogPropPreview.visible = false)"
        icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
  <!-- <el-drawer
    v-model="dialogPropPreview.visible"
    :title="dialogPropPreview.title"
    :wrapper-closable="true"
    direction="rtl"
    size="60%"
    append-to-body
  >
  <el-scrollbar height="calc(100vh - 120px)">
    <component
      v-if="dialogPropPreview.visible"
      :is="componentNames[currentPreviewComponent]"
      :data="config"
      mode="preview"
    ></component>
  </el-scrollbar>
  </el-drawer> -->
  <!--导入From-->
  <el-dialog
    v-bind="dialogPropImport"
    v-model="dialogPropImport.visible"
    v-loading="importLoading"
  >
    <ImportForm
      v-if="dialogPropImport.visible"
      :questionBox="importQuestionParserBox"
    ></ImportForm>
    <template #footer>
      <el-button
        type="primary"
        @click="handelSubmitImport"
        icon="CircleCheck"
        :loading="importLoading"
        >确认导入</el-button
      >
      <el-button
        type="danger"
        @click="handelCancelImport"
        icon="CircleClose"
        :loading="importLoading"
        plain
        >取消导入</el-button
      >
    </template>
  </el-dialog>
  <!--分类管理-->
  <el-drawer
    title="分类管理"
    v-model="drawerVisible"
    :wrapper-closable="true"
    direction="rtl"
    size="40%"
    append-to-body
  >
    <div
      v-if="drawerVisible"
      style="width: 100%; position: relative"
      v-loading="drawerVisibleLoading"
    >
      <div
        style="
          width: 100%;
          height: calc(100vh - 120px);
          position: relative;
          overflow: auto;
        "
      >
        <ClassifyTree
          :dict="classifyTreeList"
          ref="classifyTree"
          @add="addClassify"
          @edit="editClassify"
          @delete="deleteClassify"
        ></ClassifyTree>
      </div>
      <el-button
        type="primary"
        @click="
          () => {
            drawerVisible = false;
          }
        "
        style="width: calc(100% - 20px); margin: 0 10px"
        >关闭</el-button
      >
    </div>
  </el-drawer>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
</style>
