<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import {
  paperTemplateAdd,
  paperTemplateDelete,
  paperTemplateEdit,
  paperTemplateList,
  paperTemplateSelectById,
} from "@/api/paperTemplate";
import {
  paperClassifyAdd,
  paperClassifyDelete,
  paperClassifyEdit,
  paperClassifyList,
  paperClassifyTree,
} from "@/api/paperClassify";
// 拖动组件
import ResizeBox from "@/components/resize/ResizeBox.vue";
// 左侧分类树
import SearchTree from "@/components/tree/SearchTree.vue";
import ClassifyTree from "@/components/tree/ClassifyTree.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import PaperForm from "@/components/paper/PaperForm.vue";
import PaperPreview from "@/components/paper/PaperPreview.vue";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
let userId = computed(() => {
  return userStore.userInfo.id;
});

// 初始化
onMounted(() => {
  paperClassifyTree().then((res) => {
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
  type: null,
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
  paperClassifyList()
    .then((classifyRes) => {
      classifyDict.value = classifyRes.data.data;
      paperTemplateList({
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
  paperClassifyTree()
    .then((res) => {
      classifyTreeList.value = res.data.data;
    })
    .finally(() => {
      drawerVisibleLoading.value = false;
    });
};
const addClassify = async (data: any) => {
  try {
    await paperClassifyAdd(data);
    await paperClassifyTree().then((res) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch {
    classifyTree.value.error();
  }
};
const editClassify = async (data: any) => {
  try {
    await paperClassifyEdit(data);
    await paperClassifyTree().then((res) => {
      classifyTreeList.value = res.data.data;
    });
    classifyTree.value.close();
  } catch (error) {
    console.log(error);

    classifyTree.value.error();
  }
};
const deleteClassify = async (node: any, data: any) => {
  await paperClassifyDelete([data.id]);
  await paperClassifyTree().then((res) => {
    classifyTreeList.value = res.data.data;
  });
};

let mode = ref("add");
let addVisible = ref(false);
let config = ref<any>({});
let showShare = ref(true);
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "10vh",
  width: "70%",
  modal: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: false,
});
// 点击新增
const handleAdd = () => {
  mode.value = "add";
  addVisible.value = false;
  config.value = {};
  showShare.value = true;
  stopBtn.value = false;
  dialogProp.title = "新增";
  dialogProp.visible = true;
};

let paper = ref();
const handleAddDo = () => {
  paper.value.form.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        paperTemplateAdd(paper.value.genEntity())
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
        paperTemplateEdit(paper.value.genEntity())
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

let paperMode = ref("preview");
let previewConfig = ref<any>({});
let paperPreviewVisible = ref(false);
let paperDialogConfigVisible = ref(false);
let paperDialogConfig = ref("");
// 预览
const handleView = (row: any, mode: string) => {
  paperMode.value = mode;
  paperTemplateSelectById(row.id).then((res) => {
    previewDo(JSON.parse(JSON.stringify(res.data.data)));
  });
};
// 预览
const handlePreview = () => {
  paper.value.form.validate((valid: any) => {
    if (valid) {
      previewDo(JSON.parse(JSON.stringify(paper.value.genEntity())));
    }
  });
};
// 实际预览操作
const previewDo = (config: any) => {
  previewConfig = config;
  paperPreviewVisible.value = true;
};

// 交卷
const handelPaperAnswer = (config: any) => {
  paperPreviewVisible.value = false;
  config.question.forEach((item: any) => {
    item.config = JSON.parse(item.config);
  });
  paperDialogConfig.value = "交卷成功";
  paperDialogConfigVisible.value = true;
};
// 提交审批
const handelPaperCorrect = (config: any) => {
  paperPreviewVisible.value = false;
  config.question.forEach((item: any) => {
    item.config = JSON.parse(item.config);
  });
  paperDialogConfig.value = "批改成功";
  paperDialogConfigVisible.value = true;
};
// 关闭预览
const handelPaperClose = () => {
  paperPreviewVisible.value = false;
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
    paperTemplateDelete(rows.map((item: any) => item.id))
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
  dialogProp.title = "编辑";
  config.value = {};
  showShare.value = row.createUser === userId.value;
  paperTemplateSelectById(row.id).then((res) => {
    config.value = res.data.data;
    dialogProp.visible = true;
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
            <el-form-item label="试卷标题" prop="title" class="form-item">
              <el-input
                v-model="queryParams.title"
                placeholder="试卷标题"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="试卷类型" prop="type" class="form-item">
              <el-select
                v-model="queryParams.type"
                placeholder="试卷类型"
                class="input-search"
                clearable
              >
                <el-option label="普通试卷" :value="1"></el-option>
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
            <el-button
              v-permission="'paper_add'"
              type="primary"
              icon="CirclePlus"
              @click="handleAdd()"
              >新增</el-button
            >
            <el-button
              v-permission="'paper_delete'"
              type="danger"
              icon="Delete"
              @click="handleDelete(selectedRows)"
            >
              删除
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button
              v-permission="'paper_classify'"
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
              show-overflow-tooltip
              width="200"
            ></el-table-column>
            <el-table-column
              label="试卷标题"
              prop="title"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="试卷类型"
              prop="type"
              align="center"
              width="100"
            >
              <template #default="scope">
                <el-tag
                  v-if="scope.row.type === 1"
                  disable-transitions
                  type="info"
                  >普通试卷</el-tag
                >
                <el-tag v-else-if="scope.row.type === 2" disable-transitions
                  >培训试卷</el-tag
                >
                <el-tag
                  v-else-if="scope.row.type === 3"
                  disable-transitions
                  type="success"
                  >练习试卷</el-tag
                >
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              label="试卷时长"
              prop="duration"
              align="center"
              width="100"
            ></el-table-column>
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
                <el-popover placement="bottom" width="350">
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
                      v-permission="'paper_preview'"
                      link
                      type=""
                      icon="View"
                      >预览</el-button
                    >
                  </template>
                </el-popover>
                <el-button
                  v-permission="'paper_edit'"
                  link
                  type=""
                  icon="Edit"
                  @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  v-permission="'paper_delete'"
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
  <!--表单-->
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <PaperForm
      v-if="dialogProp.visible"
      :data="config"
      :classifyDict="classifyDict"
      :showShare="showShare"
      :mode="mode"
      ref="paper"
    ></PaperForm>
    <template #footer>
      <el-button type="primary" @click="handlePreview" icon="View" plain
        >试 卷 预 览</el-button
      >
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handleAddDo"
        icon="CircleCheck"
        :loading="stopBtn"
        >保 存</el-button
      >
      <el-button @click="dialogProp.visible = false" icon="CircleClose"
        >关 闭</el-button
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
  <!--试卷预览-->
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
  <!--获取到的试卷配置-->
  <el-dialog
    v-model="paperDialogConfigVisible"
    title="试卷操作回调"
    top="20vh"
    append-to-body
    width="20%"
  >
    <span>{{ paperDialogConfig }}</span>
    <span slot="footer" class="dialog-footer">
      <el-button @click="paperDialogConfigVisible = false">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
</style>
