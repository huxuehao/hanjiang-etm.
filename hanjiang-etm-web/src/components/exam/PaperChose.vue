<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { paperClassifyList, paperClassifyTree } from "@/api/paperClassify";
import { paperTemplateList } from "@/api/paperTemplate"
import { ElMessageBox } from "element-plus";

const prop = {
  value: "id",
  label: "name",
  children: "children",
};
let tHeight = ref(0);
let loading = ref(false);
const queryParams = reactive<any>({
  share: 1,
});
let selectedRows = ref<any>([]);
const page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
onMounted(() => {
  paperClassifyTree().then((res) => {
    classifyDict.value = res.data.data;
  });
  load();
  monitorSearchResize();
});

let dataList = ref<any>([]);
let classifyDict = ref<any>([]);
const load = () => {
  loading.value = true
  paperClassifyList().then((res) => {
    const classifyList = res.data.data
    paperTemplateList({...queryParams, ...page}).then((res) => {
      dataList.value = res.data.data['records'];
      page.total = res.data.data['total'];
      // 填充classifyName
      dataList.value.forEach((bItem: any) => {
        const aItem = classifyList.find((aItem: any) => aItem.id === bItem.classifyId);
        if (aItem) {
          bItem.classifyName = aItem.name;
        } else {
          bItem.classifyName = "--"
        }
      });
    }).finally(() => {
      loading.value = false
    })
  })
};
const reload = () => {
  load()
};

const handleSearch = () => {
  load()
};
const handleReset = () => {
  for (let key in queryParams) {
    if (queryParams.hasOwnProperty(key)) {
      queryParams[key] = null;
    }
  }
  queryParams.share = 1
  load()
};
const handleCurrentChange = (val: any) => {
  selectedRows.value = val
};

const emit = defineEmits(["submit"])
const submit = () => {
  if(selectedRows.value.length !== 1){
    ElMessageBox.confirm(
      "请选择一套试题模板",
      "提示",
      {
        confirmButtonText: '确定',
        type: 'warning'
      }
    )
  } else {
    ElMessageBox.confirm(
      "确认覆盖已配置的试题 ?",
      "提示",
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      emit('submit',selectedRows.value)
    })
  }
};

// 窗口变化监视器
let searchBox = ref()
const monitorSearchResize = () => {
  const resizeObserver = new ResizeObserver(entries => {
    for (const entry of entries) {
      const { height } = entry.contentRect;
      tHeight.value = height
    }
  });
  resizeObserver.observe(searchBox.value);
};
const changePageNo = (pageNo: number) => {
  page.current = pageNo
  load()
};
const changePageSize = (pageSize: number) => {
  page.size = pageSize
  load()
};
</script>

<template>
  <!-- 查询区域-->
  <div class="search-box" ref="searchBox" >
    <el-form ref="queryForm" class="query-form" :model="queryParams" :inline="true"
              label-width="100px">
      <el-form-item label="分类" prop="title" class="form-item" label-width="50px">
        <el-tree-select
              v-model="queryParams.classifyId"
              :data="classifyDict"
              :render-after-expand="true"
              :default-expand-all="true"
              :props="prop"
              class="input-search"
              placeholder="请选择"
              filterable
              check-strictly
              clearable>
          </el-tree-select>
      </el-form-item>
      <el-form-item label="标题" prop="title" class="form-item" label-width="50px">
        <el-input v-model="queryParams.title" placeholder="标题" class="input-search" clearable></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type" class="form-item" label-width="50px">
        <el-select v-model="queryParams.type" placeholder="试卷类型" class="input-search" clearable>
          <el-option label="普通试卷" :value="1"></el-option>
          <el-option label="培训考试" :value="2"></el-option>
          <el-option label="练习试卷" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="共享" prop="share" class="form-item" label-width="50px">
        <el-select v-model="queryParams.share" placeholder="是否共享" class="input-search">
          <el-option label="共享" :value="1"></el-option>
          <el-option label="独享" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="有效" prop="level" class="form-item"  label-width="50px">
        <el-select v-model="queryParams.valid" placeholder="是否有效" class="input-search" clearable>
          <el-option label="有效" :value="1"></el-option>
          <el-option label="无效" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="form-item">
        <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
      </el-form-item>
      <el-form-item class="form-item">
        <el-button icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <!-- 按钮区域-->
  <div class="button-box">
    <el-button type="primary" icon="Pointer" @click="submit" :disabled="selectedRows.length === 0">确认选择</el-button>
    <el-button size="small" icon="RefreshRight" @click="reload" title="刷新" circle
                style="float: right"></el-button>
  </div>
  <!-- 列表区域 -->
  <div class="data-list" :style="[{height: 'calc(100% - 20px - 40px - 50px - ' + tHeight + 'px)'}]">
    <el-table v-loading="loading"
              :data="dataList" ref="tableRow"
              :header-cell-style="{backgroundColor: '#f8f8f8',color:'#666666'}"
              height="100%"
              @selection-change="handleCurrentChange" stripe border>
      <el-table-column type="selection"></el-table-column>
      <el-table-column label="所属分类" prop="classifyName" show-overflow-tooltip width="200"></el-table-column>
      <el-table-column label="试卷标题" prop="title" show-overflow-tooltip></el-table-column>
      <el-table-column label="试卷类型" prop="type" align="center" width="100">
        <template #default="scope">
          <span v-if="scope.row.type === 1">普通试卷</span>
          <span v-else-if="scope.row.type === 2">培训试卷</span>
          <span v-else-if="scope.row.type === 3">练习试卷</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="试卷时长" prop="duration"  align="center" width="100"></el-table-column>
      <el-table-column label="是否共享" prop="share"  align="center" width="100">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.share === 1">共享</el-tag>
          <el-tag type="info" v-else-if="scope.row.share === 0">不共享</el-tag>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="是否有效" prop="valid"  align="center" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.valid === 1">有效</el-tag>
          <el-tag type="warning" v-else-if="scope.row.valid === 0">无效</el-tag>
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
      :page-sizes="[10,20,30,40,50,100]"
      :page-size="page.size"
      @current-change="changePageNo"
      @size-change="changePageSize"/>
  </div>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
</style>
