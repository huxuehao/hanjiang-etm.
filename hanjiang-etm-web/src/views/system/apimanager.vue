<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
// 拖动组件
import ResizeBox from "@/components/resize/ResizeBox.vue";
// 左侧分类树
import SearchTree from "@/components/tree/SearchTree.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  menuApiAdd,
  menuApiUpdate,
  menuApiDelete,
  menuApiPage,
  menuApiSelectOne,
  menuApiValid,
  menuApiUnValid,
} from "@/api/menuApi";
import { endpointScanAndSaveApi } from "@/api/endpoint";

import { menuTree, menuList } from "@/api/menu";

const prop = {
  value: "id",
  label: "name",
  children: "children",
};

let currentMenuId = ref(null);
let menuTreeList = ref<any>([]);
// 初始化
onMounted(() => {
  menuTree({}).then((res: any) => {
    menuTreeList.value = res.data.data;
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
// 当前所选行
let selectedRows = ref<any>([]);

//  点击分类树
const handleNodeClick = (node: any) => {
  currentMenuId.value = node.id;
  load();
};
// 加载列表数据
const load = () => {
  loading.value = true;
  menuList()
    .then((menuRes) => {
      const menuList = menuRes.data.data;
      menuApiPage({ ...page, ...queryParams, menuId: currentMenuId.value })
        .then((res) => {
          dataList.value = res.data.data["records"];
          page.total = res.data.data["total"];
          dataList.value.forEach((bItem: any) => {
            const aItem = menuList.find(
              (aItem: any) => aItem.id === bItem.menuId
            );
            if (aItem) {
              bItem.menuName = aItem.name;
            } else {
              bItem.menuName = "--";
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
  currentMenuId.value = null;
  queryParams.share = 1;
  load();
};

// 接口采集校验
const handelEndpointScanAndSaveApi = () => {
  ElMessageBox.confirm(
    "此操作将会自动扫描后端接口，并完成插入/更新/删除操作，是否确定?",
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    loading.value = true;
    endpointScanAndSaveApi()
      .then(() => {
        ElMessage({
          message: "操作成功",
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

const rules = {
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  code: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  menuId: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  method: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  path: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  valid: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
};
let mode = ref("add");
let addVisible = ref(false);
let formData = ref<any>({
  valid: 1,
  type: 0,
});
let showShare = ref(true);
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "20vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: false,
  closeOnClickModal: false,
});
// 点击新增
const handleAdd = () => {
  mode.value = "add";
  addVisible.value = false;
  formData.value = {
    menuId: currentMenuId.value,
    valid: 1,
    type: 0,
  };
  showShare.value = true;
  stopBtn.value = false;
  dialogProp.title = "新增";
  dialogProp.visible = true;
};

let form = ref();
const handleAddDo = () => {
  form.value.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        menuApiAdd(formData.value)
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
          })
          .catch(() => {
            stopBtn.value = false;
          });
      } else if (mode.value === "edit") {
        menuApiUpdate(formData.value)
          .then((res) => {
            dialogProp.visible = false;
            load();
            if (res.data.code === 200) {
              ElMessage({
                message: "保存成功",
                type: "success",
                plain: true,
              });
            }
          })
          .catch(() => {
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

const handleView = (row: any) => {
  mode.value = "view";
  stopBtn.value = false;
  dialogProp.title = "查看";
  menuApiSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    dialogProp.visible = true;
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

  ElMessageBox.confirm("此操作将删除所选择的数据 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    menuApiDelete(rows.map((item: any) => item.id))
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

const handleValid = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  ElMessageBox.confirm("此操作将设置所选择的接口有效 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    menuApiValid(rows.map((item: any) => item.id))
      .then(() => {
        ElMessage({
          message: "操作成功",
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
const handleUnValid = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  ElMessageBox.confirm("此操作将设置所选择的接口无效 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    menuApiUnValid(rows.map((item: any) => item.id))
      .then(() => {
        ElMessage({
          message: "操作成功",
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
  menuApiSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
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
  <ResizeBox :initialWidth="260" :minWidth="260" :maxWidth="350">
    <template #left>
      <div class="resize-left-box">
        <SearchTree
          :data="menuTreeList"
          @node-click="handleNodeClick"
          title="菜单树"
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
            <el-form-item label="接口名称" prop="name" class="form-item">
              <el-input
                v-model="queryParams.name"
                placeholder="接口名称"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="权限编号" prop="code" class="form-item">
              <el-input
                v-model="queryParams.code"
                placeholder="权限编号"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="接口路径" prop="path" class="form-item">
              <el-input
                v-model="queryParams.path"
                placeholder="接口路径"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="接口方法" prop="method" class="form-item">
              <el-select
                v-model="queryParams.method"
                placeholder="接口方法"
                class="input-search"
                clearable
              >
                <el-option label="GET" value="GET"></el-option>
                <el-option label="POST" value="POST"></el-option>
                <el-option label="PUT" value="PUT"></el-option>
                <el-option label="DELETE" value="DELETE"></el-option>
                <el-option label="ALL" value="ALL"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否有效" prop="valid" class="form-item">
              <el-select
                v-model="queryParams.valid"
                placeholder="是否有效"
                class="input-search"
                clearable
              >
                <el-option label="是" :value="1"></el-option>
                <el-option label="否" :value="0"></el-option>
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
            <!-- <el-button
              v-permission="'api_add'"
              type="primary"
              icon="CirclePlus"
              @click="handleAdd()"
              >新增</el-button
            > -->
            <el-button
              v-permission="'api_collection'"
              type="primary"
              icon="Connection"
              @click="handelEndpointScanAndSaveApi()"
            >
              接口采集/校验
            </el-button>
            <el-button
              v-permission="'api_delete'"
              type="danger"
              icon="Delete"
              @click="handleDelete(selectedRows)"
            >
              删除
            </el-button>
            <el-button
              v-permission="'api_valid'"
              type="success"
              icon="CircleCheck"
              @click="handleValid(selectedRows)"
            >
              设置有效
            </el-button>
            <el-button
              v-permission="'api_unvalid'"
              type="danger"
              icon="CircleClose"
              @click="handleUnValid(selectedRows)"
            >
              设置无效
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button icon="RefreshRight" @click="handleSearch" title="刷新"
              >刷新
            </el-button>
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
              label="所属菜单"
              prop="menuName"
              align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="接口名称"
              prop="name"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="权限编号"
              prop="code"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="接口方法"
              prop="method"
              align="center"
              width="80"
            ></el-table-column>
            <el-table-column
              label="是否有效"
              prop="valid"
              align="center"
              width="80"
            >
              <template #default="scope">
                <el-tag
                  disable-transitions
                  type="success"
                  v-if="scope.row.valid === 1"
                  >是</el-tag
                >
                <el-tag
                  disable-transitions
                  type="danger"
                  v-else-if="scope.row.valid === 0"
                  >否</el-tag
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
                <el-button
                  v-permission="'api_view'"
                  link
                  type=""
                  icon="View"
                  @click="handleView(scope.row)"
                  >查看</el-button
                >
                <el-button
                  v-permission="'api_edit'"
                  link
                  type=""
                  icon="Edit"
                  @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  v-permission="'api_delete'"
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
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <el-form
      v-if="dialogProp.visible"
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="80px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="所属菜单" prop="menuId">
            <el-tree-select
              v-model="formData.menuId"
              :data="menuTreeList"
              :render-after-expand="true"
              :default-expand-all="false"
              :props="prop"
              :disabled="mode === 'view'"
              class="input-search"
              placeholder="请选择所属菜单"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接口名称" prop="name">
            <el-input
              placeholder="请填写接口名称"
              v-model="formData.name"
              maxlength="10"
              :disabled="mode === 'view'"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="权限编号" prop="code">
            <el-input
              placeholder="请填写权限编号"
              v-model="formData.code"
              maxlength="40"
              :disabled="mode === 'view' || formData.type === 1"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接口方法" prop="method">
            <el-select
              placeholder="请选择接口方法"
              v-model="formData.method"
              :disabled="mode === 'view' || formData.type === 1"
              clearable
            >
              <el-option label="GET" value="GET"></el-option>
              <el-option label="POST" value="POST"></el-option>
              <el-option label="PUT" value="PUT"></el-option>
              <el-option label="DELETE" value="DELETE"></el-option>
              <el-option label="ALL" value="ALL"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="接口路径" prop="path">
            <el-input
              placeholder="请填写接口路径"
              v-model="formData.path"
              maxlength="40"
              :disabled="mode === 'view' || formData.type === 1"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否有效" prop="valid">
            <!-- <el-switch 
              v-model="formData.valid" 
              style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
              active-text="有效"
              inactive-text="无效"
              :active-value="1"
              :inactive-value="0"
              :disabled="mode === 'view'">
            </el-switch> -->
            <el-select
              placeholder="请选择是否有效"
              v-model="formData.valid"
              :disabled="mode === 'view'"
              clearable
            >
              <el-option label="是" :value="1"></el-option>
              <el-option label="否" :value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
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
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
</style>
