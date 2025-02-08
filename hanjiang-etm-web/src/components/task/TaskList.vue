<script lang="ts" setup>
import { taskPage } from "@/api/task";
import { ElMessageBox } from "element-plus";
import { onMounted, onUnmounted, reactive, ref, watch } from "vue";

const props = defineProps({
  showType: {
    type: String,
    default: "",
  },
  refresh: {
    type: Boolean,
    default: false,
  },
});

watch(
  () => props.refresh,
  (newVal) => {
    if (newVal) {
      load();
    }
  }
);
watch(
  () => props.showType,
  (newVal) => {
    load();
  }
);

let selectedRows = ref<any>([]);
const queryParams = reactive<any>({});
let tHeight = ref(0);
// 初始化
onMounted(() => {
  monitorSearchResize();
  load();
  window.addEventListener("storage", handleReloadUserShow);
});
onUnmounted(() => {
  window.removeEventListener("storage", handleReloadUserShow);
});

const emit = defineEmits(["reload-index"]);
const handleReloadUserShow = (event: any) => {
  if (event.key === "reloadUserShow") {
    load();
    emit("reload-index");
  }
};

let loading = ref(false);
let dataList = ref<any>([]);
const page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
// 加载分页
const load = () => {
  loading.value = true;
  taskPage({
    activeType: props.showType,
    status: queryParams.status,
    ...queryParams,
    ...page,
  })
    .then((res) => {
      dataList.value = res.data.data["records"];
      page.total = res.data.data["total"];
    })
    .finally(() => {
      loading.value = false;
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
  load();
};

// 处理审批
const handleApproval = (row: any) => {
  const params_ = btoa(JSON.stringify({ exmaId: row.examTranId, userId: row.userCode }))
  const params = encodeURIComponent(params_)
  window.open(`/exam/correct/${params}`, "_blank");
};
// 查看
const handleView = (row: any) => {
  if (["train"].includes(props.showType)) {
    const params_ = btoa(JSON.stringify({ trainId: row.examTranId, userId: row.userCode }))
    const params = encodeURIComponent(params_)
    window.open(`/train/view/${params}`, "_blank");
  } else if (["exam"].includes(props.showType)) {
    const params_ = btoa(JSON.stringify({ exmaId: row.examTranId, userId: row.userCode }))
    const params = encodeURIComponent(params_)
    window.open(`/exam/question/${params}`, "_blank");
  }
};
// 操作
const handleEdit = (row: any) => {
  // 点击这一刻进行判断时间是有有效
  if (getRecordStatus(row) === 2) {
    ElMessageBox.alert(
      `[ ${row.showName} ] 超出规定的时间，禁止参加!`,
      "提示",
      {
        confirmButtonText: "确定",
        callback: () => {
          load();
        },
      }
    );
  } else {
    ElMessageBox.confirm(`确定要参加 [ ${row.showName} ] ？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      if (["train"].includes(props.showType)) {
        const params_ = btoa(JSON.stringify({ trainId: row.examTranId, userId: row.userCode }))
        const params = encodeURIComponent(params_)
        window.open(`/train/view/${params}`, "_blank");
      } else if (["exam"].includes(props.showType)) {
        const params_ = btoa(JSON.stringify({ exmaId: row.examTranId, userId: row.userCode }))
        const params = encodeURIComponent(params_)
        window.open(`/exam/question/${params}`, "_blank");
      }
    });
  }
};
const getRecordStatus = (row: any) => {
  let limitStartTime = new Date(row.limitStartTime).getTime();
  let limitEndTime = new Date(row.limitEndTime).getTime();
  let nowTime = new Date().getTime();
  if (nowTime < limitStartTime) {
    return 0; // 未开始
  }
  if (nowTime <= limitEndTime) {
    return 1; // 已开始
  }
  return 2; // 已过期
};

let searchBox = ref();
// 监听窗口大小
const monitorSearchResize = () => {
  const resizeObserver = new ResizeObserver((entries) => {
    for (const entry of entries) {
      const { height } = entry.contentRect;
      tHeight.value = height;
    }
  });
  resizeObserver.observe(searchBox.value);
};
</script>

<template>
  <!-- 查询区域-->
  <div class="search-box" ref="searchBox">
    <el-form
      ref="queryForm"
      class="query-form"
      :model="queryParams"
      :inline="true"
      label-width="40px"
    >
      <el-form-item label="名称" prop="name" class="form-item">
        <el-input
          v-model="queryParams.showName"
          placeholder="名称"
          class="input-search"
          clearable
        ></el-input>
      </el-form-item>

      <el-form-item
        label="状态"
        prop="status"
        class="form-item"
        v-if="['exam', 'train'].includes(showType)"
      >
        <el-select
          v-model="queryParams.status"
          placeholder="状态"
          class="input-search"
          clearable
        >
          <el-option label="待完成" value="0"></el-option>
          <el-option label="未开始" value="1"></el-option>
          <el-option label="已过期" value="2"></el-option>
          <el-option label="已完成" value="3"></el-option>
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
      <el-button
        icon="RefreshRight"
        @click="handleSearch"
        style="float: right; margin-top: 5px"
      >
        刷新
      </el-button>
    </el-form>
  </div>
  <!--列表区域-->
  <div
    class="data-list"
    :style="[{ height: 'calc(100vh - 206px - ' + tHeight + 'px)' }]"
  >
    <el-table
      :data="dataList"
      :header-cell-style="{ backgroundColor: '#f8f8f8', color: '#666666' }"
      height="100%"
      v-loading="loading"
      @selection-change="
        (val:any) => {
          selectedRows = val;
        }
      "
      stripe
      border
    >
      <el-table-column
        label="#"
        type="index"
        width="40"
        align="center"
      ></el-table-column>
      <el-table-column
        label="分类"
        prop="classifyName"
        align="center"
        show-overflow-tooltip
      >
        <template #default="scope">
          {{ scope.row.classifyName || "--" }}
        </template>
      </el-table-column>
      <el-table-column
        label="名称"
        prop="showName"
        show-overflow-tooltip
        align="center"
      ></el-table-column>
      <el-table-column
        :label="
          showType.includes('exam')
            ? '答题结束时间'
            : showType.includes('train')
            ? '培训结束时间'
            : ''
        "
        prop="doEndTime"
        width="180"
        align="center"
        v-if="['exam', 'train'].includes(showType)"
      >
        <template #default="scope">
          {{ scope.row.doEndTime || "--" }}
        </template></el-table-column
      >
      <el-table-column
        label="分数"
        prop="score"
        align="center"
        width="100"
        v-if="['exam', 'train'].includes(showType)"
      >
        <template #default="scope">
          {{
            scope.row.score !== null && scope.row.score !== undefined
              ? scope.row.score
              : "--"
          }}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="center" width="100">
        <template #default="scope">
          <el-tag disable-transitions
            v-if="
              getRecordStatus(scope.row) === 1 && scope.row.showStatus === 0
            "
            >待完成</el-tag
          >
          <el-tag disable-transitions v-else-if="getRecordStatus(scope.row) === 0" type="info"
            >未开始</el-tag
          >
          <el-tag disable-transitions v-else-if="scope.row.showStatus === 1" type="success"
            >已完成</el-tag
          >
          <el-tag disable-transitions
            v-else-if="
              getRecordStatus(scope.row) === 2 && scope.row.showStatus === 0
            "
            type="warning"
            >已过期</el-tag
          >
          <el-tag disable-transitions v-else-if="scope.row.showStatus === 2">待批改</el-tag>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template #default="scope">
          <el-button
            type="text"
            v-permission="scope.row.showStatus === 1 ? 'task_view' : 'task_opt'"
            :icon="scope.row.showStatus === 1 ? 'View' : 'Edit'"
            @click="
              scope.row.showStatus === 1
                ? handleView(scope.row)
                : handleEdit(scope.row)
            "
            v-if="['exam'].includes(showType)"
            :disabled="
              getRecordStatus(scope.row) === 0 ||
              (getRecordStatus(scope.row) === 2 && scope.row.showStatus === 0)
            "
          >
            {{ scope.row.showStatus === 1 ? "查看考试" : "开始考试" }}
          </el-button>
          <el-button
            type="text"
            v-permission="scope.row.showStatus === 1 ? 'task_view' : 'task_opt'"
            :icon="scope.row.showStatus === 1 ? 'View' : 'Edit'"
            @click="
              scope.row.showStatus === 1
                ? handleView(scope.row)
                : handleEdit(scope.row)
            "
            v-if="['train'].includes(showType)"
            :disabled="
              getRecordStatus(scope.row) === 0 ||
              (getRecordStatus(scope.row) === 2 && scope.row.showStatus === 0)
            "
          >
            {{ scope.row.showStatus === 1 ? "查看培训" : "开始培训" }}
          </el-button>
          <el-button
            type="text"
            v-permission="scope.row.showStatus === 1 ? 'task_view' : 'task_opt'"
            :icon="scope.row.showStatus === 1 ? 'View' : 'Edit'"
            @click="handleApproval(scope.row)"
            v-if="['approval'].includes(showType)"
            :disabled="
              getRecordStatus(scope.row) === 0 ||
              (getRecordStatus(scope.row) === 2 && scope.row.showStatus === 0)
            "
          >
            {{ scope.row.showStatus === 1 ? "查看批改" : "开始批改" }}
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
      :page-sizes="[10, 20, 30, 40, 50, 100]"
      :page-size="page.size"
      @current-change="changePageNo"
      @size-change="changePageSize"
    />
  </div>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
.search-box, .button-box, .data-list, .page-box {
  padding-right: 0;
}
</style>
