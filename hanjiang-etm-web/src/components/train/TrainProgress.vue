<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import {
  trainUserCompletion,
  trainUserCounts,
  trainUserList,
  trainUserQualified,
} from "@/api/trainUser";
import { trainProgressExport } from "@/api/trainTemplate";
const props = defineProps({
  trainName: {
    type: String,
    default: () => {
      return "培训进展统计";
    },
  },
  trainId: {
    type: String,
    default: () => {
      return "";
    },
  },
  score: {
    type: Number,
    default: 60,
  },
});

const queryParams = reactive<any>({});
onMounted(() => {
  queryParams.trainId = props.trainId;
  initTitle();
  tableLoading();
});

let allUser = ref(0);
const completion = reactive({
  all: 0,
  done: 0,
});
const qualified = reactive({
  all: 0,
  done: 0,
});
// 统计初始化
const initTitle = () => {
  trainUserCounts(props.trainId).then((res) => {
    allUser.value = res.data.data;
  });
  trainUserCompletion(props.trainId).then((res) => {
    Object.assign(completion, res.data.data);
  });
  trainUserQualified(props.trainId, props.score).then((res) => {
    Object.assign(qualified, res.data.data);
  });
};

let dataLoading = ref(false);
let dataList = ref<any>([]);
const page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
// 列表加载
const tableLoading = () => {
  dataLoading.value = true;
  queryParams.trainId = props.trainId;
  trainUserList({ ...queryParams, ...page })
    .then((res: any) => {
      dataList.value = res.data.data["records"];
      page.total = res.data.data["total"];
    })
    .finally(() => {
      dataLoading.value = false;
    });
};

// 搜索
const handleSearch = () => {
  tableLoading();
};
// 重置
const handleReset = () => {
  for (let key in queryParams) {
    if (queryParams.hasOwnProperty(key)) {
      queryParams[key] = null;
    }
  }
  tableLoading();
};

// 分页号发生变化时
const changePageNo = (pageNo: number) => {
  page.current = pageNo;
  tableLoading();
};
// 分页大小发生变化时
const changePageSize = (pageSize: number) => {
  page.size = pageSize;
  tableLoading();
};

// 查看
const handleView = (row: any) => {
  const params_ = btoa(JSON.stringify({ trainId: row.trainId, userId: row.userId }))
  const params = encodeURIComponent(params_)
  window.open(`/train/view/${params}`, "_blank");
};

// 导出
const download = () => {
  dataLoading.value = true;
  trainProgressExport(props.trainId)
    .then((response) => {
      const blob = response.data;
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = props.trainName + "_考试统计.xlsx";
      link.click();
    })
    .finally(() => {
      dataLoading.value = false;
    });
};
</script>

<template>
  <el-row style="height: 100px">
    <el-col :span="8" class="chart-box" style="padding-left: 0">
      <div class="chart-box-item">
        <div class="title">
          <i class="el-icon-user" style="font-weight: bolder"></i>
          参与总人数（个）
        </div>
        <div class="content" style="color: #005e8a">{{ allUser }}</div>
      </div>
    </el-col>
    <el-col :span="8" class="chart-box">
      <div class="chart-box-item">
        <div class="title">
          <i class="el-icon-data-analysis" style="font-weight: bolder"></i>
          完成进度
        </div>
        <div class="content" style="color: #005e8a">
          {{ completion.done }}/{{ completion.all }}
        </div>
      </div>
    </el-col>
    <el-col :span="8" class="chart-box" style="padding-right: 0">
      <div class="chart-box-item">
        <div class="title">
          <i class="el-icon-data-line" style="font-weight: bolder"></i>
          合格率（%）
        </div>
        <div class="content" style="color: #005e8a">
          {{ Math.round((qualified.done / qualified.all) * 100) }}%
        </div>
      </div>
    </el-col>
  </el-row>
  <el-row style="height: calc(100vh - 200px)">
    <div class="search-box" ref="searchBox">
      <el-form
        ref="queryForm"
        class="query-form"
        :model="queryParams"
        :inline="true"
        label-width="70px"
      >
        <el-form-item label="用户姓名" prop="userName" class="form-item">
          <el-input
            v-model="queryParams.userName"
            placeholder="用户姓名"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="是否完成" prop="paperDone" class="form-item">
          <el-select
            v-model="queryParams.paperDone"
            placeholder="是否完成"
            class="input-search"
            clearable
          >
            <el-option label="已完成" :value="1"></el-option>
            <el-option label="未完成" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="form-item">
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
        <el-button
          type="primary"
          icon="Download"
          @click="download"
          title="导出"
          style="float: right;margin-top: 5px;"
        >导出</el-button>
      </el-form>
    </div>
    <div class="table-container">
      <el-table
        :data="dataList"
        v-loading="dataLoading"
        :header-cell-style="{ backgroundColor: '#f8f8f8', color: '#666666' }"
        height="100%"
        stripe
        border
      >
        <el-table-column
          type="index"
          label="#"
          width="50"
          align="center"
        ></el-table-column>
        <el-table-column
          label="用户名称"
          prop="userName"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="所属单位"
          prop="orgName"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span v-if="scope.row.orgName === 1">{{ scope.row.orgName }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="是否完成" prop="paperDone" align="center">
          <template #default="scope">
            <el-tag
              disable-transitions
              type="info"
              v-if="scope.row.paperDone === 1"
              >已完成</el-tag
            >
            <el-tag
              disable-transitions
              type="warning"
              v-else
              >未完成</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          label="培训时间"
          prop="doStartTime"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span v-if="scope.row.paperDone === 1">{{ scope.row.doTime }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="最终得分" prop="score" align="center">
          <template #default="scope">
            <span v-if="scope.row.paperDone === 1">{{ scope.row.score }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="答题平台" prop="plat" align="center">
          <template #default="scope">
            <span v-if="scope.row.plat">{{ scope.row.plat }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button
              @click="handleView(scope.row)"
              type="text"
              icon="View"
              :disabled="scope.row.paperDone !== 1"
              >查看</el-button
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
        :page-sizes="[10, 20, 30, 40, 50, 100]"
        :page-size="page.size"
        @current-change="changePageNo"
        @size-change="changePageSize"
      />
    </div>
  </el-row>
</template>

<style lang="scss" scoped>
.chart-box {
  padding: 0 4px;
  height: 100%;
  .chart-box-item {
    height: 100%;
    width: 100%;
    background-color: #f4f6f9;
    border-radius: 3px;
    .title {
      padding: 10px;
      font-size: 15px;
      font-weight: bolder;
      margin-bottom: 0;
    }
    .content {
      font-weight: bolder;
      text-align: center;
      font-size: 26px;
    }
  }
}

.search-box {
  width: 100%;
  min-height: 0;
  background-color: #fff;
  padding: 10px 0;
  .query-form {
    background-color: #f4f6f9;
    padding: 7px 10px;
    position: relative;
    .form-item {
      margin-top: 3px !important;
      margin-bottom: 3px !important;
      .input-search {
        width: 180px !important;
      }
    }
  }
}
.table-container {
  width: 100%;
  height: calc(100% - 105px);
  position: relative;
}

.page-box {
  width: 100%;
  height: 50px;
  padding: 10px 10px 0 10px;
  background-color: white;
  .page {
    float: right;
  }
}
</style>
