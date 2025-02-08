<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { getToken } from "@/utils/auth";
import setting from "@/config/setting";

import { attachPage, attachDelete } from "@/api/attach";
import { formatFileSize } from "@/utils/tools";

// 初始化
onMounted(() => {
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

// 分页配置
let page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
let loading = ref(false);
// 列表数据
let dataList = ref<any>([]);

// 查询参数
let queryParams = reactive<any>({});
// 当前所选行
let selectedRows = ref<any>([]);

// 加载列表数据
const load = () => {
  loading.value = true;
  attachPage({ ...queryParams, ...page })
    .then((res) => {
      dataList.value = res.data.data;
      dataList.value = res.data.data["records"];
      page.total = res.data.data["total"];
    })
    .finally(() => {
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
  load();
};

// 下载
const handleDownload = (row: any) => {
  window.location.href = `/api/attach/download/${row.id}?${
    setting.tokenHeader
  }=${getToken()}`;
};

const changePageNo = (pageNo: number) => {
  page.current = pageNo;
  load();
};
const changePageSize = (pageSize: number) => {
  page.size = pageSize;
  load();
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

  ElMessageBox.confirm("此操作将删除所选择的数据 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    attachDelete(rows.map((item: any) => item.id))
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
</script>

<template>
  <div style="height: 100%">
    <!-- 查询区域-->
    <div class="search-box" ref="searchBox">
      <el-form
        ref="queryForm"
        class="query-form"
        :model="queryParams"
        :inline="true"
        label-width="70px"
      >
        <el-form-item label="附件名称" prop="originalName" class="form-item">
          <el-input
            v-model="queryParams.originalName"
            placeholder="附件名称"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="附件类型" prop="extension" class="form-item">
          <el-input
            v-model="queryParams.extension"
            placeholder="附件类型"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="存储协议" prop="protocol" class="form-item">
          <el-select
            v-model="queryParams.protocol"
            placeholder="存储协议"
            class="input-search"
            clearable
          >
            <el-option label="S3" value="S3"></el-option>
            <el-option label="FTP" value="FTP"></el-option>
            <el-option label="LOCAL" value="LOCAL"></el-option>
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
        v-permission="'attach_delete'"
        type="danger"
        icon="Delete"
        @click="handleDelete(selectedRows)"
      >
        删除
      </el-button>
      <el-button
        icon="RefreshRight"
        @click="handleSearch"
        title="刷新"
        style="float: right"
        >刷新</el-button
      >
    </div>
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
          label="附件名称"
          prop="originalName"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="附件类型"
          prop="extension"
          align="center"
          width="100px"
        >
        </el-table-column>
        <el-table-column
          label="附件大小"
          prop="attachSize"
          align="center"
          width="180px"
        >
          <template #default="scope">
            <span>{{ formatFileSize(scope.row.attachSize) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="存储协议"
          prop="protocol"
          align="center"
          width="100"
        >
        </el-table-column>
        <el-table-column
          label="上传时间"
          prop="createTime"
          align="center"
          width="200px"
        >
        </el-table-column>
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="'attach_download'"
              @click="handleDownload(scope.row)"
              link
              type=""
              icon="Download"
            >
              下载
            </el-button>
            <el-button
              v-permission="'attach_delete'"
              @click="handleDelete([scope.row])"
              link
              type=""
              icon="Delete"
              style="color: #f63434"
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

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;

.search-box,
.button-box,
.data-list {
  margin-left: 10px;
}
</style>
