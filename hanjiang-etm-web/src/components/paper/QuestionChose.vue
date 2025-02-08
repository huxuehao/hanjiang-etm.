<script lang="ts" setup>
// 预览组价
import SingleQuestionPreview from "@/components/question/view/SingleQuestionPreview.vue";
import MultipleQuestionPreview from "@/components/question/view/MultipleQuestionPreview.vue";
import JudgeQuestionPreview from "@/components/question/view/JudgeQuestionPreview.vue";
import CompletionQuestionPreview from "@/components/question/view/CompletionQuestionPreview.vue";
import QaQuestionPreview from "@/components/question/view/QaQuestionPreview.vue";

const componentNames = shallowReactive<any>({
  SingleQuestionPreview,
  MultipleQuestionPreview,
  JudgeQuestionPreview,
  CompletionQuestionPreview,
  QaQuestionPreview,
});

import {
  questionClassifyList,
  questionClassifyTree,
} from "@/api/questionClassify";

import { questionTemplateList } from "@/api/questionTemplate";
import { onMounted, reactive, ref, shallowReactive } from "vue";
import { ElMessage } from "element-plus";

const previewComponents = [
  "",
  "SingleQuestionPreview",
  "MultipleQuestionPreview",
  "JudgeQuestionPreview",
  "CompletionQuestionPreview",
  "QaQuestionPreview",
];
const types = ["--", "单选题", "多选题", "判断题", "填空题", "解答题"];

const prop = {
  value: "id", // ID字段名
  label: "name", // 显示名称
  children: "children", // 子级字段名
};

let classifyDict = ref<any[]>([]);
onMounted(() => {
  questionClassifyTree().then((res) => {
    classifyDict.value = res.data.data;
  });
  load();
  monitorSearchResize();
});

let loading = ref(false);
let dataList = ref<any[]>([]);
let classifyList = ref<any[]>([]);
const page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
const queryParams = reactive<any>({
  share: 1,
});
// 加载列表（分页）
const load = () => {
  loading.value = true;
  questionClassifyList().then((res) => {
    classifyList.value = res.data.data;
    questionTemplateList({ ...queryParams, ...page })
      .then((res) => {
        dataList.value = res.data.data["records"];
        page.total = res.data.data["total"];
        // 填充classifyName
        dataList.value.forEach((bItem) => {
          const aItem = classifyList.value.find(
            (aItem) => aItem.id === bItem.classifyId
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
  });
};

let searchBox = ref();
let tHeight = ref(0);
// 窗口变化监视器
const monitorSearchResize = () => {
  const resizeObserver = new ResizeObserver((entries) => {
    for (const entry of entries) {
      const { height } = entry.contentRect;
      tHeight.value = height;
    }
  });
  resizeObserver.observe(searchBox.value);
};

// 搜索
const handleSearch = () => {
  load();
};
// 搜索重置
const handleReset = () => {
  for (let key in queryParams) {
    if (queryParams.hasOwnProperty(key)) {
      queryParams[key] = null;
    }
  }
  queryParams.share = 1;
  load();
};

let selectedRows = ref<any[]>([]);
const emits = defineEmits(["submit"]);
// 确认选择
const submit = () => {
  if (selectedRows.value.length === 0) {
    ElMessage({
      message: "至少选择一个试题",
      type: "warning",
      plain: true,
    });
  } else {
    emits("submit", selectedRows.value);
  }
};
// 预览
const handleView = (row: any) => {
  selectedRows.value = [row];
  previewDo(JSON.parse(row.config));
};

const config = ref<any>({});
let currentPreviewComponent = ref("");
const dialogPropPreview = reactive({
  visible: false,
  title: "",
  top: "10vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: false,
});

let currentType = ref(0);
// 实际预览操作
const previewDo = (conf: any) => {
  config.value = conf;
  currentType.value = conf.type;
  currentPreviewComponent.value = previewComponents[conf.type];
  dialogPropPreview.title = "预览-" + types[conf.type];
  dialogPropPreview.visible = true;
};

// 分页号发生变化时
const changePageNo = (pageNo: number) => {
  page.current = pageNo;
  load();
};
// 分页大小发生变化时
const changePageSize = (pageSize: number) => {
  page.size = pageSize;
  load();
};
</script>

<template>
  <div style=" height: calc(100vh - 120px);">
    <!-- 查询区域-->
    <div class="search-box" ref="searchBox">
      <el-form
        ref="queryForm"
        class="query-form"
        :model="queryParams"
        :inline="true"
        label-width="40px"
      >
        <el-form-item label="分类" prop="classifyId" class="form-item">
          <el-tree-select
            v-model="queryParams.classifyId"
            :data="classifyDict"
            :render-after-expand="true"
            :default-expand-all="true"
            :props="prop"
            class="input-search"
            filterable
            check-strictly
            clearable
          ></el-tree-select>
        </el-form-item>
        <el-form-item label="题干" prop="title" class="form-item">
          <el-input
            v-model="queryParams.title"
            placeholder="题干"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type" class="form-item">
          <el-select
            v-model="queryParams.type"
            placeholder="类型"
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
        <el-form-item label="分数" prop="score" class="form-item">
          <el-input
            v-model="queryParams.score"
            placeholder="分数"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="难度" prop="level" class="form-item">
          <el-select
            v-model="queryParams.level"
            placeholder="难度"
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
        <el-form-item label="共享" prop="share" class="form-item">
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
    <div class="button-box">
      <el-button
        @click="submit"
        type="primary"
        icon="Pointer"
        :disabled="selectedRows.length === 0"
      >
        确认选择
      </el-button>
      <el-button
        icon="RefreshRight"
        @click="handleSearch"
        title="刷新"
        circle
        style="float: right"
      ></el-button>
    </div>
    <!-- 列表区域 -->
    <div
      class="data-list"
      :style="[{ height: 'calc(100% - 45px - ' + tHeight + 'px)' }]"
    >
      <el-table
        :data="dataList"
        :header-cell-style="{ backgroundColor: '#F5F7FA', color: '#666666' }"
        height="100%"
        v-loading="loading"
        @selection-change="(val:any) =>{selectedRows = val}"
        :stripe="false"
        border
      >
        <el-table-column type="selection" width="40" align="center"></el-table-column>
        <el-table-column
          type="index"
          label="#"
          width="40"
          align="center"
        ></el-table-column>
        <el-table-column
          label="分类"
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
          label="分数"
          prop="score"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column label="难度" prop="level" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.level === 1">简单</span>
            <span v-else-if="scope.row.level === 2">一般</span>
            <span v-else-if="scope.row.level === 3">中等</span>
            <span v-else-if="scope.row.level === 4">较难</span>
            <span v-else-if="scope.row.level === 5">很难</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="scope">
            <el-button
            link
              type=""
              icon="View"
              @click="handleView(scope.row)"
              >预览</el-button
            >
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
    <!--预览From-->
    <el-dialog v-bind="dialogPropPreview">
      <component
        v-if="dialogPropPreview.visible"
        :is="componentNames[currentPreviewComponent]"
        :data="config"
      ></component>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="dialogPropPreview.visible = false"
          icon="el-icon-circle-close"
          >关 闭</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.search-box {
  min-height: 0;
  background-color: #fff;
  padding:0 10px 5px 0;
  .query-form {
    background-color: #F5F7FA;
    padding: 7px 10px;
    position: relative;
    .form-item {
      margin-right: 10px;
      margin-top: 3px !important;
      margin-bottom: 3px !important;
      .input-search {
        width: 180px !important;
      }
    }
  }
}

.button-box {
  max-height: 40px;
  min-height: 0;
  padding: 0 10px 0 0px;
  background-color: #fff;
}

.data-list {
  background-color: white;
  padding: 5px 10px 10px 0;
  position: relative;
}

.page-box {
  padding: 10px 10px 0 0;
  background-color: white;
  .page {
    float: right;
  }
}
</style>
