<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
// 拖动组件
import ResizeBox from "@/components/resize/ResizeBox.vue";
// 左侧分类树
import SearchTree from "@/components/tree/SearchTree.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { orgTree } from "@/api/organization";
import {
  userAdd,
  userUpdate,
  userDelete,
  userPage,
  userSelectOne,
  userValid,
  userResetPwd,
} from "@/api/user";
import UserRoleConfig from "@/components/user/UserRoleConfig.vue";
import { saveUserRole } from "@/api/role";

const prop = {
  value: "id",
  label: "name",
  children: "children",
};

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
// 当前所选行
let selectedRows = ref<any>([]);

//  点击分类树
const handleNodeClick = (node: any) => {
  currentOrgId.value = node.id;
  load();
};
// 加载列表数据
const load = () => {
  loading.value = true;
  userPage({ ...page, ...queryParams, orgId: currentOrgId.value })
    .then((res) => {
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
  currentOrgId.value = null;
  queryParams.share = 1;
  load();
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
  phone: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  account: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  orgId: [
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
    valid: 1,
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
        userAdd(formData.value)
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
        userUpdate(formData.value)
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
  userSelectOne(row.id).then((res) => {
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
    userDelete(rows.map((item: any) => item.id))
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

const handleValid = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  ElMessageBox.confirm("此操作将解封所选择的用户 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    userValid(rows.map((item: any) => item.id))
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

const handleResetPwd = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  ElMessageBox.confirm("此操作将重置所选择用户的登录密码 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    userResetPwd(rows.map((item: any) => item.id))
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
  userSelectOne(row.id).then((res) => {
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

// 权限配置
const userRoleDialogProp = reactive({
  visible: false,
  title: "",
  top: "5vh",
  width: "30%",
  modal: true,
  appendToBody: true,
  showClose: false,
  closeOnClickModal: false,
});

const handleAuthCnfig = (row: any) => {
  stopBtn.value = false;
  selectedRows.value = [row];
  userRoleDialogProp.title = "角色配置";
  userRoleDialogProp.visible = true;
};

let userRoleRef = ref();
const handleUserRoleSave = () => {
  stopBtn.value = true;
  saveUserRole({
    userId: selectedRows.value[0].id,
    roleIds: userRoleRef.value.checkedRoleIds,
  })
    .then(() => {
      userRoleDialogProp.visible = false;
    })
    .finally(() => {
      stopBtn.value = false;
    });
};
</script>

<template>
  <ResizeBox :initialWidth="250" :minWidth="250" :maxWidth="450">
    <template #left>
      <div class="resize-left-box">
        <SearchTree
          :data="orgTreeList"
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
              v-permission="'user_add'"
              type="primary"
              icon="CirclePlus"
              @click="handleAdd()"
              >新增</el-button
            >
            <el-button
              v-permission="'user_delete'"
              type="danger"
              icon="Delete"
              @click="handleDelete(selectedRows)"
            >
              删除
            </el-button>
            <el-button
              v-permission="'user_repassword'"
              type="warning"
              icon="RefreshLeft"
              @click="handleResetPwd(selectedRows)"
            >
              重置密码
            </el-button>
            <el-button
              v-permission="'user_valid'"
              type="success"
              icon="CircleCheck"
              @click="handleValid(selectedRows)"
            >
              解封账号
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
              width="120"
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
              width="280"
              align="center"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  v-permission="'user_view'"
                  link
                  type=""
                  icon="View"
                  @click="handleView(scope.row)"
                  >查看</el-button
                >
                <el-button
                  v-permission="'user_edit'"
                  link
                  type=""
                  icon="Edit"
                  @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  v-permission="'user_delete'"
                  link
                  type=""
                  icon="Delete"
                  style="color: #f63434"
                  @click="handleDelete([scope.row])"
                >
                  删除
                </el-button>
                <el-button
                  v-permission="'user_role'"
                  link
                  type="primary"
                  icon="User"
                  @click="handleAuthCnfig(scope.row)"
                >
                  角色配置
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
          <el-form-item label="用户姓名" prop="name">
            <el-input
              placeholder="请填写用户姓名"
              v-model="formData.name"
              maxlength="10"
              :disabled="mode === 'view'"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户编号" prop="code">
            <el-input
              placeholder="请填写用户编号"
              v-model="formData.code"
              maxlength="40"
              :disabled="mode === 'view'"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="手机号码" prop="phone">
            <el-input
              placeholder="请填写手机号码"
              :disabled="mode === 'view'"
              maxlength="11"
              v-model="formData.phone"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户昵称" prop="nickname">
            <el-input
              placeholder="请填写用户昵称"
              v-model="formData.nickname"
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
          <el-form-item label="所属组织" prop="orgId">
            <el-tree-select
              v-model="formData.orgId"
              :data="orgTreeList"
              :render-after-expand="true"
              :default-expand-all="false"
              :props="prop"
              :disabled="mode === 'view'"
              class="input-search"
              placeholder="请选择所属组织"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱号码" prop="email">
            <el-input
              placeholder="请填写邮箱号码"
              :disabled="mode === 'view'"
              maxlength="100"
              v-model="formData.email"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="登录账号" prop="account">
            <el-input
              placeholder="请填写登录账号"
              v-model="formData.account"
              :disabled="mode === 'view'"
              maxlength="40"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="登录密码" prop="password">
            <el-input
              placeholder="请填写登录密码"
              v-model="formData.password"
              :disabled="mode !== 'add'"
              maxlength="100"
              type="password"
              show-password
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="用户排序" prop="sort">
            <el-input
              placeholder="请填写用户排序"
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
  <el-dialog v-bind="userRoleDialogProp" v-model="userRoleDialogProp.visible">
    <UserRoleConfig
      ref="userRoleRef"
      :user-id="selectedRows[0].id"
      v-if="userRoleDialogProp.visible"
    />
    <template #footer>
      <el-button
        type="primary"
        icon="CircleCheck"
        :loading="stopBtn"
        @click="handleUserRoleSave()"
        >保 存</el-button
      >
      <el-button @click="userRoleDialogProp.visible = false" icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
</style>
