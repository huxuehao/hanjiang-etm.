<script lang="ts" setup>
import { computed, nextTick, onMounted, reactive, ref } from "vue";
import {
  orgTree,
} from "@/api/organization";
import {
  userPage,
  selectByIds,
} from "@/api/user";
import { ElMessage } from "element-plus";

const prop = {
  value: "id",
  label: "name",
  children: "children",
};

const props = defineProps({
  defaultQueryParams: {
    type: Object,
    default: function () {
      return {}
    },
  },
  multi: {
    type: Boolean,
    default: true
  },
  defaultCheckIds: {
    type: Array,
    default: function () {
      return []
    }
  },
  userSelectedIds: {
    type: Array,
    default: function () {
      return []
    }
  }
})

let currentOrgId = ref(null);
let orgTreeList = ref<any>([]);
// 初始化
onMounted(() => {
  orgTree({}).then((res: any) => {
    orgTreeList.value = res.data.data;
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
// 查询参数
let queryParams = reactive<any>({});
// 分页配置
let page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
// 当前页所选行
let currentSelectedRows = ref<any>([]);
// 当前页所移除的行
let currentRemoveRows = ref<any>([]);
// 所选的所有的行
let totalSelectedRows = ref<any>([]);
// 所选ID
let ids = computed(() => {
  return totalSelectedRows.value.map((row: any) => row.id)
})
// 所选用户
let users = computed(() => {
  return totalSelectedRows.value
})

// 初始化选择用户
let initted = ref(false)
const initSelectedUsers = async () => {
  if(props.userSelectedIds.length > 0 && !initted.value) {
    const res = await selectByIds(props.userSelectedIds)
    totalSelectedRows.value = res.data.data
    initted.value = true
  }
};

const addCurrentSelectedRows = () => {
  if(currentSelectedRows.value.length > 0) {
    // 添加新选择的用户
    currentSelectedRows.value.forEach((row: any) => {
      if(!ids.value.includes(row.id)) {
        totalSelectedRows.value.push(row)
      }
    })
    // 删除已经取消选择的用户
    const removeIds = currentRemoveRows.value.map((row: any) => row.id)
    totalSelectedRows.value = totalSelectedRows.value.filter((row: any) => !removeIds.includes(row.id))
  }
};

const handelSelectionChange = (rows: any) => {
  const ids_ =  rows.map((row: any) => row.id)
  // 获取并存储已删除的数据
  const removeRows = currentSelectedRows.value.filter((row:any) => !ids_.includes(row.id))
  currentRemoveRows.value = removeRows
  // 保存已选择的数据
  currentSelectedRows.value = rows
  addCurrentSelectedRows()
};

let table = ref()
// 加载列表数据
const load = async() => {
  loading.value = true;
  await initSelectedUsers();
  addCurrentSelectedRows();
  userPage({...page,...queryParams}).then((res) => {
    const data = res.data.data
    dataList.value = data["records"];
    page.total = data["total"];
    nextTick(() => {
      dataList.value.forEach((row:any) => {
        if(ids.value.includes(row.id)) {
          table.value.toggleRowSelection(row, true)
        }
      })
    })
  }).finally(() => {
    loading.value = false;
  })
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
  currentOrgId.value = null;
  queryParams.share = 1;
  load();
};

const emit = defineEmits(["submit"])
// 执行提交事件
const doSubmitEmit = () => {
  addCurrentSelectedRows()
  if(totalSelectedRows.value.length === 0) {
    ElMessage({
      message:"至少选择1条数据",
      type: "warning",
      plain: true
    })
  } else {
    emit("submit", totalSelectedRows.value)
  }
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
  <div>
    <!-- 查询区域-->
    <div class="search-box" ref="searchBox">
      <el-form
        ref="queryForm"
        class="query-form"
        :model="queryParams"
        :inline="true"
        label-width="70px"
      >
        <el-form-item label="所属单位" prop="orgId" class="form-item">
          <el-tree-select
              v-model="queryParams.orgId"
              :data="orgTreeList"
              :render-after-expand="true"
              :default-expand-all="false"
              :props="prop"
              class="input-search"
              placeholder="所属单位"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
        </el-form-item>
        <el-form-item label="用户名称" prop="name" class="form-item">
          <el-input
            v-model="queryParams.name"
            placeholder="用户名称"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="用户编号" prop="code" class="form-item">
          <el-input
            v-model="queryParams.code"
            placeholder="用户编号"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="登录账号" prop="account" class="form-item">
          <el-input
            v-model="queryParams.account"
            placeholder="登录账号"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone" class="form-item">
          <el-input
            v-model="queryParams.phone"
            placeholder="手机号码"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="是否有效" prop="valid" class="form-item">
          <el-select
            v-model="queryParams.valid"
            placeholder="是否有效"
            class="input-search"
            clearable
          >
            <el-option label="有效" :value="1"></el-option>
            <el-option label="无效" :value="0"></el-option>
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
      <el-button type="primary" icon="User" @click="doSubmitEmit()" :disabled="ids.length === 0">
        确认选择
      </el-button>
    </div>
    <!-- 列表区域 -->
    <div
      class="data-list"
      :style="[{ height: 'calc(100vh - 165px - ' + tHeight + 'px)' }]"
    >
      <el-table
        :stripe="false"
        :data="dataList"
        :header-cell-style="{
          backgroundColor: '#F5F7FA',
          color: '#666666',
        }"
        height="100%"
        ref="table"
        v-loading="loading"
        @selection-change="handelSelectionChange"
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
          label="用户名称"
          prop="name"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="用户编码"
          prop="code"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="登录账号"
          prop="account"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="是否有效"
          prop="valid"
          align="center"
          width="150"
        >
          <template #default="scope">
            <el-tag disable-transitions type='success' v-if="scope.row.valid === 1">有效</el-tag>
            <el-tag disable-transitions type='danger' v-else-if="scope.row.valid === 0">无效</el-tag>
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
.search-box {
  padding-top: 0;
}
.page-box{
  padding-top: 10px;
}
</style>
