<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  roleAdd,
  roleUpdate,
  roleDelete,
  roleTree,
  roleSelectOne,
  saveAuthConfig,
} from "@/api/role";
import AuthConfig from "@/components/role/AuthConfig.vue";

const prop = {
  value: "id",
  label: "name",
  children: "children",
};

// 初始化
onMounted(() => {
  load();
  tree();
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
let dataTree = ref<any>([]);

// 表单数据
let formData = ref<any>({});
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
  sort: [
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
// 查询参数
let queryParams = reactive<any>({});
// 当前所选行
let selectedRows = ref<any>([]);

// 加载列表数据
const load = () => {
  loading.value = true;
  roleTree(queryParams)
    .then((res) => {
      dataList.value = res.data.data;
    })
    .finally(() => {
      loading.value = false;
    });
};
// 加载列表数据
const tree = () => {
  roleTree({}).then((res) => {
    dataTree.value = res.data.data;
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

let mode = ref("add");
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
  stopBtn.value = false;
  formData.value = {
    type: 1,
    valid: 1,
  };
  dialogProp.title = "新增";
  dialogProp.visible = true;
};
let form = ref();
const handleAddDo = () => {
  form.value.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        roleAdd(formData.value)
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
            tree();
          })
          .catch(() => {
            stopBtn.value = false;
          });
      } else if (mode.value === "edit") {
        roleUpdate(formData.value)
          .then((res) => {
            dialogProp.visible = false;
            if (res.data.code === 200) {
              ElMessage({
                message: "保存成功",
                type: "success",
                plain: true,
              });
            }
            load();
            tree();
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
    roleDelete(rows.map((item: any) => item.id))
      .then((res) => {
        if (res.data.code === 200) {
          ElMessage({
            message: "删除成功",
            type: "success",
            plain: true,
          });
        }
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

const handleView = (row: any) => {
  mode.value = "view";
  stopBtn.value = false;
  formData.value = row;
  dialogProp.title = "查看";
  dialogProp.visible = true;
  roleSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    dialogProp.visible = true;
  });
};

const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  formData.value = row;
  dialogProp.title = "编辑";
  dialogProp.visible = true;
  roleSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    dialogProp.visible = true;
  });
};

const handleAddChild = (row: any) => {
  mode.value = "add";
  stopBtn.value = false;
  formData.value = {
    type: 1,
    valid: 1,
  };
  formData.value.parentId = row.id;
  dialogProp.title = "新增子项";
  dialogProp.visible = true;
};

// 权限配置
const authDialogProp = reactive({
  visible: false,
  title: "",
  top: "5vh",
  width: "35%",
  modal: true,
  appendToBody: true,
  showClose: false,
  closeOnClickModal: false,
});
const handleAuthCnfig = (row: any) => {
  selectedRows.value = [row];
  stopBtn.value = false;
  authDialogProp.title = "权限配置";
  authDialogProp.visible = true;
};

let authConfigRef = ref();
const handleAuthConfigSave = () => {
  stopBtn.value = true;
  saveAuthConfig({
    roleId: selectedRows.value[0].id,
    menuIds: authConfigRef.value.checkedMenuIds,
    buttonIds: authConfigRef.value.checkedButtonIds,
    apiIds: authConfigRef.value.checkedApiIds,
  })
    .then(() => {
      authDialogProp.visible = false;
    })
    .finally(() => {
      stopBtn.value = false;
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
        <el-form-item label="上级角色" prop="parentId" class="form-item">
          <el-tree-select
            v-model="queryParams.parentId"
            :data="dataTree"
            :render-after-expand="true"
            :default-expand-all="false"
            :props="prop"
            class="input-search"
            placeholder="上级角色"
            filterable
            check-strictly
            clearable
          ></el-tree-select>
        </el-form-item>
        <el-form-item label="角色名称" prop="name" class="form-item">
          <el-input
            v-model="queryParams.name"
            placeholder="角色名称"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="角色编码" prop="code" class="form-item">
          <el-input
            v-model="queryParams.code"
            placeholder="角色编码"
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
        <el-button
          v-permission="'role_add'"
          type="primary"
          icon="CirclePlus"
          @click="handleAdd()"
          >新增</el-button
        >
        <el-button
          v-permission="'role_delete'"
          type="danger"
          icon="Delete"
          @click="handleDelete(selectedRows)"
        >
          删除
        </el-button>
      </el-col>
      <el-col :span="8" style="text-align: right">
        <el-button icon="RefreshRight" @click="handleSearch" title="刷新"
          >刷新</el-button
        >
      </el-col>
    </el-row>
    <!-- 列表区域 -->
    <div
      class="data-list"
      :style="[{ height: 'calc(100vh - 150px - ' + tHeight + 'px)' }]"
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
          label="角色名称"
          prop="name"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="角色编码"
          prop="code"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="是否有效"
          prop="valid"
          align="center"
          width="100"
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
          label="角色排序"
          prop="sort"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column label="操作" width="360" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="'role_view'"
              link
              type=""
              icon="View"
              @click="handleView(scope.row)"
              >查看</el-button
            >
            <el-button
              v-permission="'role_edit'"
              link
              type=""
              icon="Edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="'role_delete'"
              link
              type=""
              icon="Delete"
              style="color: #f63434"
              @click="handleDelete([scope.row])"
            >
              删除
            </el-button>
            <el-button
              v-permission="'role_add_child'"
              link
              type=""
              icon="CirclePlus"
              @click="handleAddChild(scope.row)"
            >
              新增子项
            </el-button>
            <el-button
              v-permission="'role_auth'"
              link
              type="primary"
              icon="Lock"
              @click="handleAuthCnfig(scope.row)"
            >
              权限配置
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
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
          <el-form-item label="上级角色" prop="parentId">
            <el-tree-select
              v-model="formData.parentId"
              :data="dataTree"
              :render-after-expand="true"
              :default-expand-all="false"
              :props="prop"
              :disabled="mode === 'view'"
              placeholder="请选择上级角色"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="角色名称" prop="name">
            <el-input
              placeholder="请填写角色名称"
              maxlength="40"
              :disabled="mode === 'view'"
              v-model="formData.name"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织编号" prop="code">
            <el-input
              placeholder="请填写组织编号"
              maxlength="40"
              :disabled="mode === 'view'"
              v-model="formData.code"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="角色排序" prop="sort">
            <el-input
              placeholder="请填写角色排序"
              v-model="formData.sort"
              :disabled="mode === 'view'"
              type="number"
              clearable
            ></el-input>
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
  <el-dialog
    v-bind="authDialogProp"
    v-model="authDialogProp.visible"
    class="role-auth-config"
  >
    <AuthConfig
      ref="authConfigRef"
      :role-id="selectedRows[0].id"
      v-if="authDialogProp.visible"
    />
    <template #footer>
      <el-button
        type="primary"
        icon="CircleCheck"
        :loading="stopBtn"
        @click="handleAuthConfigSave()"
        >保 存</el-button
      >
      <el-button @click="authDialogProp.visible = false" icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;

.search-box,
.button-box,
.data-list {
  margin-left: 10px;
}
</style>
<style>
.el-overlay .el-overlay-dialog .role-auth-config .el-dialog__body {
  padding-top: 0 !important;
}
</style>
