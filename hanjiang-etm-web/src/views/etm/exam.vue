<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import {
  examTemplateAdd,
  examTemplateDelete,
  examTemplateEdit,
  examTemplateList,
  examTemplateSelectById,
  checkRandomOption,
  examTemplatePublish,
  examTemplatePublishV2,
  examTemplateCancelPublish,
  examTemplateCancelPublishV2,
} from "@/api/examTemplate";
import {
  examClassifyAdd,
  examClassifyDelete,
  examClassifyEdit,
  examClassifyList,
  examClassifyTree,
} from "@/api/examClassify";
// 拖动组件
import ResizeBox from "@/components/resize/ResizeBox.vue";
// 左侧分类树
import SearchTree from "@/components/tree/SearchTree.vue";
import ClassifyTree from "@/components/tree/ClassifyTree.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useUserStore } from "@/stores/user";
import ExamCorrector from "@/components/exam/ExamCorrector.vue";
import ExamForm from "@/components/exam/ExamForm.vue";
import ExamPaper from "@/components/exam/ExamPaper.vue";
import ExamProgress from "@/components/exam/ExamProgress.vue";
import ExamQuestionConfig from "@/components/exam/ExamQuestionConfig.vue";
import ExamUser from "@/components/exam/ExamUser.vue";
import PaperPreview from "@/components/paper/PaperPreview.vue";
import { formatDate } from "@/utils/tools";

const userStore = useUserStore();
let userId = computed(() => {
  return userStore.userInfo.id;
});

// 初始化
onMounted(() => {
  examClassifyTree().then((res) => {
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
let queryParams = reactive<any>({});
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
  examClassifyList()
    .then((classifyRes) => {
      classifyDict.value = classifyRes.data.data;
      examTemplateList({
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

let drawerVisible = ref(false);
let drawerVisibleLoading = ref(false);
let classifyTree = ref();
const openClassifyDrawer = () => {
  drawerVisible.value = true;
  drawerVisibleLoading.value = true;
  examClassifyTree()
    .then((res) => {
      classifyTreeList.value = res.data.data;
    })
    .finally(() => {
      drawerVisibleLoading.value = false;
    });
};
const addClassify = async (data: any) => {
  try {
    await examClassifyAdd(data);
    await examClassifyTree().then((res) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch {
    classifyTree.value.error();
  }
};
const editClassify = async (data: any) => {
  try {
    await examClassifyEdit(data);
    await examClassifyTree().then((res) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch (error) {
    console.log(error);

    classifyTree.value.error();
  }
};
const deleteClassify = async (node: any, data: any) => {
  await examClassifyDelete([data.id]);
  await examClassifyTree().then((res) => {
    classifyTreeList.value = res.data.data;
  });
};

let questionRule = ref(0);
let active = ref(0);
let mode = ref("add");
let config = ref<any>({});
let examData = ref<any>({});
let paperData = ref<any>({});
let questionConfig = ref<any>({});
let userData = ref<any>({});
let correctorData = ref<any>({});
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "5vh",
  width: "75%",
  modal: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: false,
});
// 点击新增
const handleAdd = () => {
  examData.value = {};
  paperData.value = {};
  questionConfig.value = {};
  userData.value = {};
  correctorData.value = {};
  config.value = {};
  mode.value = "add";
  stopBtn.value = false;
  dialogProp.title = "新增";
  dialogProp.visible = true;
  active.value = 0;
};

let exam = ref();
let paper = ref();
let question = ref();
let user = ref();
let corrector = ref();
const handleSaveDo = () => {
  const examForm_ = exam.value.examForm;
  let paperForm_ = {};
  let questionConfigForm_ = {};
  if (examForm_.questionRule === 0) {
    paperForm_ = paper.value.paperForm;
  } else {
    questionConfigForm_ = question.value.questionConfigForm;
  }
  const userForm_ = user.value.userForm;

  const correctorForm_ = corrector.value.correctorForm;
  const form_ = {
    ...examForm_,
    ...paperForm_,
    ...questionConfigForm_,
    ...userForm_,
    ...correctorForm_,
  };
  exam.value.form.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        examTemplateAdd(form_)
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
        examTemplateEdit(form_)
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

const handleViewV2 = (row: any) => {
  mode.value = "view";
  stopBtn.value = false;
  dialogProp.title = "查看";
  config.value = {};
  active.value = 0;
  examTemplateSelectById(row.id).then((res) => {
    examData.value = res.data.data;
    questionRule.value = res.data.data.questionRule;
    paperData.value = {
      question: res.data.data.question || [],
      paperId: res.data.data.paperId || null,
    };
    questionConfig.value = {
      option: res.data.data.option || [],
      classifies: res.data.data.classifies || null,
    };
    userData.value = {
      user: res.data.data.user || [],
    };
    correctorData.value = {
      corrector: res.data.data.corrector || [],
    };
    dialogProp.visible = true;
  });
};
// 编辑
const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  dialogProp.title = "编辑";
  config.value = {};
  active.value = 0;
  examTemplateSelectById(row.id).then((res) => {
    examData.value = res.data.data;
    questionRule.value = res.data.data.questionRule;
    paperData.value = {
      question: res.data.data.question || [],
      paperId: res.data.data.paperId || null,
    };
    questionConfig.value = {
      option: res.data.data.option || [],
      classifies: res.data.data.classifies || null,
    };
    userData.value = {
      user: res.data.data.user || [],
    };
    correctorData.value = {
      corrector: res.data.data.corrector || [],
    };
    dialogProp.visible = true;
  });
};
// 删除
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
    examTemplateDelete(rows.map((item: any) => item.id))
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
// 发布
const handlePublish = (row: any) => {
  const message = `发布 [ ${row.name} ] , 是否确定?`;
  ElMessageBox.confirm(message, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    row.published = 1;
    row.publishTime = formatDate(new Date());
    if (row.currentType === 1) {
      examTemplatePublish(row.id)
        .then(() => {
          ElMessage({
            message: "发布成功",
            type: "success",
            plain: true,
          });
          load();
        })
        .catch(() => {
          row.published = 0;
          row.publishTime = null;
          loading.value = false;
        })
        .finally(() => {
          selectedRows.value = [];
        });
    } else {
      examTemplatePublishV2(row.id)
        .then(() => {
          ElMessage({
            message: "发布成功",
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
    }
  });
};
// 撤销
const handleCancelPublish = (row: any, force: number) => {
  const title =
    force === 0
      ? `确认撤销 [ ${row.name} ] 考试？`
      : `该操作会清除用户作答记录，确认强制撤销 [ ${row.name} ] 考试？`;
  ElMessageBox.confirm(title, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    if (row.currentType === 1) {
      examTemplateCancelPublish(row.id, force)
        .then(() => {
          ElMessage({
            message: "发布成功",
            type: "success",
            plain: true,
          });
          load();
        })
        .catch(() => {
          loading.value = false;
        });
    } else {
      examTemplateCancelPublishV2(row.id, force)
        .then(() => {
          ElMessage({
            message: "撤销成功",
            type: "success",
            plain: true,
          });
          load();
        })
        .catch(() => {
          loading.value = false;
        });
    }
  });
};

const changeStep = (currentActive: number) => {
  if (active.value === 1 && questionRule.value !== 0) {
    question.value.form.validate((valid: any) => {
      if (valid) {
        resetActive(currentActive);
      }
    });
  } else {
    exam.value.form.validate((valid: any) => {
      if (valid) {
        resetActive(currentActive);
      }
    });
  }
};

const resetActive = (currentActive: number) => {
  if (currentActive === active.value) {
    return;
  }
  questionRule.value = exam.value.examForm.questionRule;
  if (currentActive === 3) {
    if (exam.value.examForm.checkType === 1) {
      active.value = currentActive;
    } else {
      ElMessage({
        message: "当前审批方式为【机器审批】, 无需配置审批人员",
        type: "warning",
        plain: true,
      });
    }
  } else if (currentActive === 2) {
    if (exam.value.examForm.currentType === 1) {
      active.value = currentActive;
    } else {
      ElMessage({
        message: "当前考试类型为【扫码开放考试】, 无需配置考试人员",
        type: "warning",
        plain: true,
      });
    }
  } else {
    active.value = currentActive;
  }
};

const handlePrev = () => {
  if (active.value > 0) {
    changeStep(active.value - 1);
  } else {
    ElMessage({
      message: "当前已经是第一步了",
      type: "warning",
      plain: true,
    });
  }
};
const handleNext = () => {
  if (active.value < 3) {
    changeStep(active.value + 1);
  } else {
    ElMessage({
      message: "当前已经是最后一步了",
      type: "warning",
      plain: true,
    });
  }
};

// 验证试题配置合法性
let isCheckOption = ref(false);
const doCheckRandomOption = () => {
  isCheckOption.value = true;
  checkRandomOption(question.value.questionConfigForm)
    .then(() => {
      ElMessage({
        message: "验证通过",
        type: "success",
        plain: true,
      });
    })
    .finally(() => {
      isCheckOption.value = false;
    });
};

const progressDialogProp = reactive({
  examId: "",
  examName: "",
  examScore: 0,
  visible: false,
  title: "进展情况",
  top: "0",
  width: "100%",
  modal: true,
  appendToBody: true,
  showClose: true,
  fullscreen: true,
});
// 查看进展情况
const handelProgress = () => {
  if (selectedRows.value.length !== 1) {
    ElMessage({
      message: "请选择一条记录",
      type: "warning",
      plain: true,
    });
    return;
  } else if (selectedRows.value[0].published !== 1) {
    ElMessage({
      message: "当前考试未发布",
      type: "warning",
      plain: true,
    });
    return;
  }
  progressDialogProp.title = "进展-" + selectedRows.value[0].name;
  progressDialogProp.examId = selectedRows.value[0].id;
  progressDialogProp.examName = selectedRows.value[0].name;
  progressDialogProp.examScore = selectedRows.value[0].successScore;
  progressDialogProp.visible = true;
};

//试卷预览
const handlePreview = () => {
  let config_ = { ...exam.value.examForm, ...paper.value.getEntity() };
  previewDo(config_);
};
// 实际预览操作
let previewConfig = ref<any>({});
let paperPreviewVisible = ref(false);
const previewDo = (config: any) => {
  previewConfig.value = config;
  previewConfig.value.title = config.name;
  // console.log("previewConfig",previewConfig.value);
  paperPreviewVisible.value = true;
};
// 交卷
const handelPaperAnswer = (config: any) => {
  paperPreviewVisible.value = false;
  config.question.forEach((item: any) => {
    item.config = JSON.parse(item.config);
  });
};
// 提交审批
const handelPaperCorrect = (config: any) => {
  paperPreviewVisible.value = false;
  config.question.forEach((item: any) => {
    item.config = JSON.parse(item.config);
  });
};
// 关闭预览
const handelPaperClose = () => {
  paperPreviewVisible.value = false;
};

// 预览
let paperMode = ref("preview");
const handleView = (row: any, mode: string) => {
  paperMode.value = mode;
  selectedRows.value = [row];
  examTemplateSelectById(row.id).then((res) => {
    const config = res.data.data;
    previewDo(JSON.parse(JSON.stringify(config)));
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
  <ResizeBox :initialWidth="290" :minWidth="290" :maxWidth="450">
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
            <el-form-item label="考试名称" prop="name" class="form-item">
              <el-input
                v-model="queryParams.name"
                placeholder="考试名称"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item
              label="开始时间"
              prop="limitStartTime"
              class="form-item"
            >
              <el-date-picker
                v-model="queryParams.limitStartTime"
                type="datetime"
                class="input-search"
                style="width: 180px"
                placeholder="考试开始时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                clearable
              ></el-date-picker>
            </el-form-item>
            <el-form-item
              label="结束时间"
              prop="limitEndTime"
              class="form-item"
            >
              <el-date-picker
                v-model="queryParams.limitEndTime"
                type="datetime"
                class="input-search"
                style="width: 180px"
                placeholder="考试结束时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                clearable
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="是否发布" prop="published" class="form-item">
              <el-select
                v-model="queryParams.published"
                placeholder="是否发布"
                class="input-search"
                clearable
              >
                <el-option label="已发布" :value="1"></el-option>
                <el-option label="未发布" :value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="考试类型" prop="currentType" class="form-item">
              <el-select
                v-model="queryParams.currentType"
                placeholder="考试类型"
                class="input-search"
                clearable
              >
                <el-option label="系统内部考试" :value="1"></el-option>
                <el-option label="扫码开放考试" :value="0"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="题目规则"
              prop="questionRule"
              class="form-item"
            >
              <el-select
                v-model="queryParams.questionRule"
                placeholder="题目规则"
                class="input-search"
                clearable
              >
                <el-option label="题目固定，试卷相同" :value="0"></el-option>
                <el-option label="题目随机，试卷不同" :value="1"></el-option>
                <el-option label="题目随机，试卷相同" :value="2"></el-option>
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
            <el-button
              v-permission="'exam_add'"
              type="primary"
              icon="CirclePlus"
              @click="handleAdd()"
              >新增</el-button
            >
            <el-button
              v-permission="'exam_delete'"
              type="danger"
              icon="Delete"
              @click="handleDelete(selectedRows)"
            >
              删除
            </el-button>
            <el-button
              v-permission="'exam_progress'"
              type="primary"
              icon="Share"
              @click="handelProgress"
            >
              进展情况
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button
              v-permission="'exam_classify'"
              icon="Operation"
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
              align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="考试名称"
              prop="name"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="开始时间"
              prop="limitStartTime"
              align="center"
              width="150"
            ></el-table-column>
            <el-table-column
              label="结束时间"
              prop="limitEndTime"
              align="center"
              width="150"
            ></el-table-column>
            <el-table-column
              label="是否发布"
              prop="published"
              align="center"
              width="100"
            >
              <template #default="scope">
                <el-tag
                  type="success"
                  disable-transitions
                  v-if="scope.row.published === 1"
                  >已发布</el-tag
                >
                <el-tag
                  type="warning"
                  disable-transitions
                  v-else-if="scope.row.published === 0"
                  >未发布</el-tag
                >
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              label="类型"
              prop="currentType"
              align="center"
              width="100"
            >
              <template #default="scope">
                <el-tag disable-transitions v-if="scope.row.currentType === 1"
                  >内部</el-tag
                >
                <el-tag
                  disable-transitions
                  type="info"
                  v-else-if="scope.row.currentType === 0"
                  >开放</el-tag
                >
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="250"
              align="center"
              fixed="right"
            >
              <template #default="scope">
                <el-popover
                  placement="bottom"
                  width="350"
                  :disabled="scope.row.questionRule === 1"
                >
                  <el-button
                    link
                    type=""
                    icon="Tickets"
                    @click="handleView(scope.row, 'preview')"
                    >标准模式</el-button
                  >
                  <el-button
                    link
                    type=""
                    icon="Edit"
                    @click="handleView(scope.row, 'answer')"
                    >答题模式</el-button
                  >
                  <el-button
                    link
                    type=""
                    icon="Finished"
                    @click="handleView(scope.row, 'correct')"
                    >审批模式</el-button
                  >
                  <el-button
                    link
                    type=""
                    icon="DocumentChecked"
                    @click="handleView(scope.row, 'history')"
                    >历史模式</el-button
                  >
                  <template #reference>
                    <el-button
                      v-permission="'exam_preview'"
                      link
                      type=""
                      icon="View"
                      :disabled="scope.row.questionRule === 1"
                      >预览</el-button
                    >
                  </template>
                </el-popover>
                <el-button
                  v-permission="'exam_edit'"
                  link
                  type=""
                  v-if="scope.row.published === 0"
                  icon="Edit"
                  @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  v-permission="'exam_view'"
                  link
                  type=""
                  v-else
                  icon="Tickets"
                  @click="handleViewV2(scope.row)"
                  >查看</el-button
                >
                <el-button
                  v-permission="'exam_publish'"
                  link
                  type=""
                  v-if="scope.row.published === 0"
                  icon="Position"
                  style="color: #2d96ff"
                  @click="handlePublish(scope.row)"
                  >发布</el-button
                >
                <el-popover v-else placement="bottom" width="150">
                  <el-button
                    link
                    type=""
                    @click="handleCancelPublish(scope.row, 0)"
                    >普通撤销</el-button
                  >
                  <el-button
                    link
                    type=""
                    style="color: #f63434"
                    @click="handleCancelPublish(scope.row, 1)"
                    >强制撤销</el-button
                  >
                  <template #reference>
                    <el-button
                      v-permission="'exam_publish'"
                      link
                      type=""
                      icon="Remove"
                      style="margin: 0 0 0 10px; color: orange"
                      >撤销</el-button
                    >
                  </template>
                </el-popover>
                <el-button
                  v-permission="'exam_delete'"
                  link
                  type=""
                  icon="Delete"
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
  <!--表单-->
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <div v-if="dialogProp.visible">
      <el-steps simple :active="active">
        <el-step
          title="基础配置"
          icon="Tickets"
          @click.native="changeStep(0)"
          style="cursor: pointer"
        ></el-step>
        <el-step
          title="试卷配置"
          icon="Notebook"
          @click.native="changeStep(1)"
          style="cursor: pointer"
        ></el-step>
        <el-step
          title="考试人员"
          icon="User"
          @click.native="changeStep(2)"
          style="cursor: pointer"
        ></el-step>
        <el-step
          title="批改人员"
          icon="UserFilled"
          @click.native="changeStep(3)"
          style="cursor: pointer"
        ></el-step>
      </el-steps>
      <div style="height: 10px"></div>
      <!-- 基础配置 -->
      <div v-show="active === 0">
        <!--基础配置-->
        <ExamForm
          :classifyDict="classifyTreeList"
          :mode="mode"
          :disable-edit="mode === 'view'"
          ref="exam"
          :data="examData"
        ></ExamForm>
      </div>
      <!--试卷配置-->
      <div v-show="active === 1">
        <!--固定试卷-->
        <ExamPaper
          v-if="questionRule === 0"
          ref="paper"
          :mode="mode"
          :disable-edit="mode === 'view'"
          :data="paperData"
        ></ExamPaper>
        <!--随机试卷-->
        <ExamQuestionConfig
          v-else
          ref="question"
          :mode="mode"
          :disable-edit="mode === 'view'"
          :data="questionConfig"
        ></ExamQuestionConfig>
      </div>
      <!--考试人员配置-->
      <div v-show="active === 2">
        <ExamUser
          ref="user"
          :mode="mode"
          :disable-edit="mode === 'view'"
          :data="userData"
        ></ExamUser>
      </div>
      <!--审批人员配置-->
      <div v-show="active === 3">
        <ExamCorrector
          ref="corrector"
          :mode="mode"
          :disable-edit="mode === 'view'"
          :data="correctorData"
        ></ExamCorrector>
      </div>
    </div>
    <template #footer class="dialog-footer">
      <el-button
        @click="handlePreview"
        icon="View"
        v-if="active === 1 && questionRule === 0"
        >试 卷 预 览</el-button
      >
      <el-button
        @click="doCheckRandomOption"
        icon="CircleCheck"
        :loading="isCheckOption"
        v-if="active === 1 && questionRule > 0"
        >验 证 试 卷 配 置 合 法 性</el-button
      >
      <el-button @click="handlePrev">上一步</el-button>
      <el-button @click="handleNext">下一步</el-button>
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handleSaveDo"
        icon="CircleCheck"
        :loading="stopBtn"
        >保 存</el-button
      >
      <el-button @click="dialogProp.visible = false" icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
  <!-- 试卷预览 -->
  <el-drawer
    title="试卷预览"
    v-model="paperPreviewVisible"
    :wrapper-closable="true"
    :with-header="false"
    class="drawer-none-padding"
    append-to-body
    direction="btt"
    size="100%"
  >
    <PaperPreview
      v-if="paperPreviewVisible"
      :data="previewConfig"
      :mode="paperMode"
      :showAlert="false"
      @answer="handelPaperAnswer"
      @correct="handelPaperCorrect"
      @close="handelPaperClose"
    ></PaperPreview>
  </el-drawer>
  <!--进展情况-->
  <el-dialog
    v-bind="progressDialogProp"
    v-model="progressDialogProp.visible"
    @close="
      () => {
        progressDialogProp.visible = false;
      }
    "
  >
    <ExamProgress
      v-if="progressDialogProp.visible"
      :score="progressDialogProp.examScore"
      :exam-name="progressDialogProp.examName"
      :exam-id="progressDialogProp.examId"
    >
    </ExamProgress>
  </el-dialog>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
a {
  color: #f63434;
}
</style>
