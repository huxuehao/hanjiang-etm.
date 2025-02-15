<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import {
  trainCancelPublish,
  trainPublish,
  trainTemplateAdd,
  trainTemplateDelete,
  trainTemplateEdit,
  trainTemplateList,
  trainTemplateSelectById,
} from "@/api/trainTemplate";
import {
  trainClassifyAdd,
  trainClassifyDelete,
  trainClassifyEdit,
  trainClassifyList,
  trainClassifyTree,
} from "@/api/trainClassify";
// 拖动组件
import ResizeBox from "@/components/resize/ResizeBox.vue";
// 左侧分类树
import SearchTree from "@/components/tree/SearchTree.vue";
import ClassifyTree from "@/components/tree/ClassifyTree.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useUserStore } from "@/stores/user";
import TrainProgress from "@/components/train/TrainProgress.vue";
import TrainForm from "@/components/train/TrainForm.vue";
import TrainPreview from "@/components/train/TrainPreview.vue";

const userStore = useUserStore();
let userId = computed(() => {
  return userStore.userInfo.id;
});

// 初始化
onMounted(() => {
  trainClassifyTree().then((res: any) => {
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
  trainClassifyList()
    .then((classifyRes: any) => {
      classifyDict.value = classifyRes.data.data;
      trainTemplateList({
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
  trainClassifyTree()
    .then((res: any) => {
      classifyTreeList.value = res.data.data;
    })
    .finally(() => {
      drawerVisibleLoading.value = false;
    });
};
const addClassify = async (data: any) => {
  try {
    await trainClassifyAdd(data);
    await trainClassifyTree().then((res: any) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch {
    classifyTree.value.error();
  }
};
const editClassify = async (data: any) => {
  try {
    await trainClassifyEdit(data);
    await trainClassifyTree().then((res: any) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch (error) {
    console.log(error);

    classifyTree.value.error();
  }
};
const deleteClassify = async (node: any, data: any) => {
  await trainClassifyDelete([data.id]);
  await trainClassifyTree().then((res: any) => {
    classifyTreeList.value = res.data.data;
  });
};

let mode = ref("add");
let addVisible = ref(false);
let disableEdit = ref(false);
let config = ref<any>({});
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
  mode.value = "add";
  config.value = {};
  addVisible.value = false;
  disableEdit.value = false;
  stopBtn.value = false;
  dialogProp.title = "新增";
  dialogProp.visible = true;
};

let train = ref();
const handleAddDo = () => {
  train.value.form.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        trainTemplateAdd(train.value.genEntity())
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
        trainTemplateEdit(train.value.genEntity())
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

const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  disableEdit.value = false;
  dialogProp.title = "编辑";
  config.value = {};
  trainTemplateSelectById(row.id).then((res) => {
    config.value = res.data.data;
    config.value.id = row.id;
    dialogProp.visible = true;
  });
};

// preview 标准模式，answer 培训模式, history 历史模式
let trainMode = ref("preview");
let trainPreviewVisible = ref(false);
const handlePreview = (row: any, mode_: string) => {
  trainMode.value = mode_;
  config.value = {};
  trainTemplateSelectById(row.id).then((res) => {
    config.value = res.data.data;
    trainPreviewVisible.value = true;
  });
};
const handlePreviewV2 = (mode_: string = "preview") => {
  train.value.form.validate((valid: any) => {
    if (valid) {
      const config_ = train.value.genEntity();
      if (!config_.file || config_.file.length === 0) {
        ElMessage({
          message: "课件配置不可为空",
          type: "success",
          plain: true,
        });
      } else if (!config_.question || config_.question.length === 0) {
        ElMessage({
          message: "试题配置不可为空",
          type: "success",
          plain: true,
        });
      } else {
        trainMode.value = mode_;
        config.value = config_;
        trainPreviewVisible.value = true;
      }
    }
  });
};

const handleView = (row: any) => {
  mode.value = "view";
  stopBtn.value = false;
  disableEdit.value = true;
  dialogProp.title = "查看";
  config.value = {};
  trainTemplateSelectById(row.id).then((res) => {
    config.value = JSON.parse(res.data.data.config);
    config.value.id = row.id;
    dialogProp.visible = true;
  });
};

const handlePublish = (row: any) => {
  ElMessageBox.confirm(`确认发布 [ ${row.title} ] 培训？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    trainPublish(row.id)
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
  });
};
const handleTrainCancelPublish = (row: any, force: number) => {
  const title =
    force === 0
      ? `确认撤销 [ ${row.title} ] 培训？`
      : `该操作会清除用户作答记录，确认强制撤销 [ ${row.title} ] 培训？`;
  ElMessageBox.confirm(title, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    trainCancelPublish(row.id, force)
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
  });
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
    trainTemplateDelete(rows.map((item: any) => item.id))
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

// 点击上一步
const previousStep = () => {
  train.value.previousStep();
};
// 点击下一步
const nextStep = () => {
  train.value.nextStep();
};

const progressDialogProp = reactive({
  trainId: "",
  trainName: "",
  trainScore: 0,
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
  progressDialogProp.title = "进展-" + selectedRows.value[0].title;
  progressDialogProp.trainId = selectedRows.value[0].id;
  progressDialogProp.trainName = selectedRows.value[0].title;
  progressDialogProp.trainScore = selectedRows.value[0].successScore;
  progressDialogProp.visible = true;
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
  <ResizeBox :initialWidth="190" :minWidth="190" :maxWidth="300">
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
            <el-form-item label="培训标题" prop="title" class="form-item">
              <el-input
                v-model="queryParams.title"
                placeholder="培训标题"
                class="input-search"
                clearable
              ></el-input>
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
              v-permission="'train_add'"
              type="primary"
              icon="CirclePlus"
              @click="handleAdd()"
              >新增</el-button
            >
            <el-button
              v-permission="'train_delete'"
              type="danger"
              icon="Delete"
              @click="handleDelete(selectedRows)"
            >
              删除
            </el-button>
            <el-button
              v-permission="'train_progress'"
              type="primary"
              icon="Share"
              @click="handelProgress"
            >
              进展情况
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button
              v-permission="'train_classify'"
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
              label="培训标题"
              prop="title"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="培训开始时间"
              prop="limitStartTime"
              align="center"
              width="180"
            ></el-table-column>
            <el-table-column
              label="培训结束时间"
              prop="limitEndTime"
              align="center"
              width="180"
            ></el-table-column>
            <el-table-column
              label="发布时间"
              prop="publishTime"
              align="center"
              width="180"
            >
              <template #default="scope">
                <span v-if="scope.row.published === 1">{{
                  scope.row.publishTime
                }}</span>
                <span v-else>未发布</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="250"
              align="center"
              fixed="right"
            >
              <template #default="scope">
                <el-popover placement="bottom" width="260">
                  <el-button
                    link
                    type="text"
                    icon="Tickets"
                    @click="handlePreview(scope.row, 'preview')"
                    >标准模式</el-button
                  >
                  <el-button
                    link
                    type="text"
                    icon="Edit"
                    @click="handlePreview(scope.row, 'answer')"
                    >培训模式</el-button
                  >
                  <el-button
                    link
                    type="text"
                    icon="DocumentChecked"
                    @click="handlePreview(scope.row, 'history')"
                    >历史模式</el-button
                  >
                  <template #reference>
                    <el-button
                      v-permission="'train_preview'"
                      link
                      type=""
                      icon="View"
                      >预览</el-button
                    >
                  </template>
                </el-popover>
                <el-button
                  v-permission="'train_edit'"
                  link
                  type=""
                  v-if="scope.row.published === 0"
                  icon="Edit"
                  @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  v-permission="'train_view'"
                  link
                  type=""
                  v-else
                  icon="Tickets"
                  @click="handleView(scope.row)"
                  >查看</el-button
                >
                <el-button
                  v-permission="'train_publish'"
                  link
                  type=""
                  v-if="scope.row.published === 0"
                  icon="Position"
                  style="color: #2d96ff"
                  @click="handlePublish(scope.row)"
                  >发布</el-button
                >
                <el-popover v-else placement="bottom" width="120">
                  <el-button
                    link
                    type=""
                    @click="handleTrainCancelPublish(scope.row, 0)"
                    >普通撤销</el-button
                  >
                  <el-button
                    link
                    type=""
                    style="color: #f63434"
                    @click="handleTrainCancelPublish(scope.row, 1)"
                    >强制撤销</el-button
                  >
                  <template #reference>
                    <el-button
                      v-permission="'train_publish'"
                      link
                      type=""
                      icon="Remove"
                      style="margin: 0 0 0 10px; color: orange"
                      >撤销</el-button
                    >
                  </template>
                </el-popover>
                <el-button
                  v-permission="'train_delete'"
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
  <!--新增编辑From-->
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <TrainForm
      v-if="dialogProp.visible"
      :mode="mode"
      :data="config"
      :disable-edit="disableEdit"
      :classifyDict="classifyDict"
      ref="train"
    ></TrainForm>
    <template #footer class="dialog-footer">
      <el-button @click="previousStep()">上 一 步</el-button>
      <el-button @click="nextStep()">下 一 步</el-button>
      <el-button type="primary" @click="handlePreviewV2()" icon="View" plain
        >培 训 预 览</el-button
      >
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handleAddDo()"
        icon="CircleCheck"
        :disabled="stopBtn"
        >保 存</el-button
      >
      <el-button @click="dialogProp.visible = false" icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
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
    <TrainProgress
      v-if="progressDialogProp.visible"
      :score="progressDialogProp.trainScore"
      :train-name="progressDialogProp.trainName"
      :train-id="progressDialogProp.trainId"
    >
    </TrainProgress>
  </el-dialog>
  <!--培训预览-->
  <el-drawer
    title="培训预览"
    v-model="trainPreviewVisible"
    :wrapper-closable="true"
    :with-header="false"
    class="drawer-none-padding"
    direction="rtl"
    size="100%"
  >
    <TrainPreview
      v-if="trainPreviewVisible"
      :mode="trainMode"
      :data="config"
      @close="
        () => {
          trainPreviewVisible = false;
        }
      "
    ></TrainPreview>
  </el-drawer>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
</style>
