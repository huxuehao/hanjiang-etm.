<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";

import {
  attachLogPage,
} from "@/api/attach";
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
  attachLogPage({...queryParams, ...page})
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
        <!-- <el-form-item label="操作IP" prop="optIp" class="form-item">
          <el-input
            v-model="queryParams.optIp"
            placeholder="操作IP"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item> -->
        <el-form-item label="用户行为" prop="optType" class="form-item">
          <el-select
            v-model="queryParams.optType"
            placeholder="用户行为"
            class="input-search"
            clearable
            >
            <el-option label="上传" value="UPLOAD"></el-option>
            <el-option label="下载" value="DOWNLOAD"></el-option>
            <el-option label="删除" value="DELETE"></el-option>
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
          title="刷新"
          style="float: right; margin-top: 5px;"
          >刷新</el-button
        >
      </el-form>
    </div>
    <!-- 列表区域 -->
    <div
      class="data-list"
      :style="[{ height: 'calc(100vh - 160px - ' + tHeight + 'px)' }]"
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
          label="#"
          type="index"
          width="40"
          align="center"
        ></el-table-column>
        <el-table-column label="附件名称" prop="originalName" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="附件类型" prop="extension" align="center" width="100">
        </el-table-column>
        <el-table-column label="附件大小" prop="attachSize" align="center" width="180">
          <template #default="scope">
            <span>{{ formatFileSize(scope.row.attachSize) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" prop="optTime" align="center" width="200">
        </el-table-column>
        <el-table-column label="操作用户" prop="optUserName" align="center" width="180">
        </el-table-column>
        <el-table-column label="用户行为" prop="optType" align="center" width="100">
          <template #default="scope">
              <el-tag disable-transitions type="success" v-if="scope.row.optType === 'UPLOAD'" >上传</el-tag>
              <el-tag disable-transitions v-else-if="scope.row.optType === 'DOWNLOAD'" >下载</el-tag>
              <el-tag disable-transitions type="danger" v-else-if="scope.row.optType === 'DELETE'" >删除</el-tag>
              <span v-else>--</span>
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

.search-box, .button-box, .data-list {
  margin-left: 10px;
}
</style>
