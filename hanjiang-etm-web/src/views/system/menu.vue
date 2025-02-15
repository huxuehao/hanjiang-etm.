<script lang="ts" setup>
import { onMounted, reactive, ref, h } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import IconChoose from "@/components/icon/IconChoose.vue";

import {
  menuAdd,
  menuUpdate,
  menuDelete,
  menuTree,
  menuSelectOne,
  menuSetVliad,
} from "@/api/menu";

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
let formData = ref<any>({
  valid: 1,
  params: [],
});
const rules = {
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  icon: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  alias: [
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
  sort: [
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
  menuTree(queryParams)
    .then((res) => {
      dataList.value = res.data.data;
    })
    .finally(() => {
      loading.value = false;
    });
};
// 加载列表数据
const tree = () => {
  menuTree({}).then((res) => {
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

const handleSetVliad = (rows: any, valid: number) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }
  ElMessageBox.confirm(
    `此操作将设置所选择的数据状态为${
      valid === 1 ? "有效" : "无效"
    } , 是否确定?`,
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    loading.value = true;
    menuSetVliad(
      rows.map((item: any) => item.id),
      valid
    )
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

let mode = ref("add");
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "20vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: false,
});
// 点击新增
const handleAdd = () => {
  mode.value = "add";
  stopBtn.value = false;
  formData.value = {
    valid: 1,
    params: [],
  };
  dialogProp.title = "新增";
  dialogProp.visible = true;
};
let form = ref();
const handleAddDo = () => {
  form.value.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      let formData_: any = {};
      Object.assign(formData_, formData.value);
      formData_.params = JSON.stringify(formData_.params);
      if (mode.value === "add") {
        menuAdd(formData_)
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
          .finally(() => {
            stopBtn.value = false;
          });
      } else if (mode.value === "edit") {
        menuUpdate(formData_)
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
          .finally(() => {
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
    menuDelete(rows.map((item: any) => item.id))
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

const handleView = (row: any) => {
  mode.value = "view";
  stopBtn.value = false;
  dialogProp.title = "查看";
  menuSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    formData.value.params = JSON.parse(res.data.data.params || "[]");
    dialogProp.visible = true;
  });
};

const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  dialogProp.title = "编辑";
  menuSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    formData.value.params = JSON.parse(res.data.data.params || "[]");
    dialogProp.visible = true;
  });
};

const handleAddChild = (row: any) => {
  mode.value = "add";
  stopBtn.value = false;
  formData.value = {
    parentId: row.id,
    valid: 1,
  };
  dialogProp.title = "新增子项";
  dialogProp.visible = true;
};

const dialogIconProp = reactive({
  visible: false,
  title: "",
  top: "10vh",
  width: "70%",
  modal: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: true,
});
const openIconChoose = () => {
  dialogIconProp.title = "图标选择";
  dialogIconProp.visible = true;
};
const handelChoose = (icon: string) => {
  formData.value.icon = icon;
  dialogIconProp.visible = false;
};

const addReadStar = (data: { column: any; $index: number }) => {
  return [
    h(
      "span",
      {
        style: "color: #f67a7a",
      },
      "*"
    ),
    h("span", " " + data.column.label),
  ];
};

const clickAddParams = () => {
  if (!formData.value.params) {
    formData.value.params = [];
  }
  formData.value.params.push({
    key: null,
    value: null,
  });
};
const clickRemoveParams = (index: number) => {
  formData.value.params.splice(index, 1);
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
        <el-form-item label="上级菜单" prop="parentId" class="form-item">
          <el-tree-select
            v-model="queryParams.parentId"
            :data="dataTree"
            :render-after-expand="true"
            :default-expand-all="false"
            :props="prop"
            class="input-search"
            placeholder="上级菜单"
            filterable
            check-strictly
            clearable
          ></el-tree-select>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name" class="form-item">
          <el-input
            v-model="queryParams.name"
            placeholder="菜单名称"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="菜单别名" prop="alias" class="form-item">
          <el-input
            v-model="queryParams.alias"
            placeholder="菜单别名"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="菜单编码" prop="code" class="form-item">
          <el-input
            v-model="queryParams.code"
            placeholder="菜单编码"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="访问地址" prop="path" class="form-item">
          <el-input
            v-model="queryParams.path"
            placeholder="访问地址"
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
          v-permission="'menu_add'"
          type="primary"
          icon="CirclePlus"
          @click="handleAdd()"
          >新增</el-button
        >
        <el-button
          v-permission="'menu_delete'"
          type="danger"
          icon="Delete"
          @click="handleDelete(selectedRows)"
        >
          删除
        </el-button>
        <el-button
          v-permission="'menu_valid'"
          type="success"
          icon="CircleCheck"
          @click="handleSetVliad(selectedRows, 1)"
          >设置有效</el-button
        >
        <el-button
          v-permission="'menu_unvalid'"
          type="danger"
          icon="CircleClose"
          @click="handleSetVliad(selectedRows, 0)"
          >设置无效</el-button
        >
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
        <el-table-column label="菜单名称" prop="name" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          label="访问地址"
          prop="path"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="菜单编码"
          prop="code"
          align="center"
          width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="菜单图标"
          prop="icon"
          align="center"
          width="100"
        >
          <template #default="scope">
            <el-icon><component :is="scope.row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column
          label="是否有效"
          prop="valid"
          align="center"
          width="100"
        >
          <template #default="scope">
            <el-tag
              v-if="scope.row.valid === 1"
              disable-transitions
              type="success"
              >是</el-tag
            >
            <el-tag
              v-else-if="scope.row.valid === 0"
              disable-transitions
              type="danger"
              >否</el-tag
            >
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column
          label="组织排序"
          prop="sort"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="'menu_view'"
              link
              type=""
              icon="View"
              @click="handleView(scope.row)"
              >查看</el-button
            >
            <el-button
              v-permission="'menu_edit'"
              link
              type=""
              icon="Edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="'menu_delete'"
              link
              type=""
              icon="Delete"
              style="color: #f63434"
              @click="handleDelete([scope.row])"
            >
              删除
            </el-button>
            <el-button
              v-permission="'menu_add_child'"
              link
              type=""
              icon="CirclePlus"
              @click="handleAddChild(scope.row)"
            >
              新增子项
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
          <el-form-item label="上级菜单" prop="parentId">
            <el-tree-select
              v-model="formData.parentId"
              :data="dataTree"
              :render-after-expand="true"
              :default-expand-all="false"
              :props="prop"
              :disabled="mode === 'view'"
              class="input-search"
              placeholder="请选择上级菜单"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="菜单图标" prop="icon">
            <el-input
              placeholder="请选择菜单图标"
              v-model="formData.icon"
              :disabled="mode === 'view'"
              clearable
            >
              <template #prepend>
                <el-icon size="15"><component :is="formData.icon" /></el-icon>
              </template>
              <template #append v-if="mode !== 'view'">
                <el-button @click="openIconChoose" title="选项图标"
                  >选择</el-button
                >
              </template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="菜单名称" prop="name">
            <el-input
              placeholder="请填写菜单名称"
              maxlength="20"
              :disabled="mode === 'view'"
              v-model="formData.name"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="菜单别名" prop="alias">
            <el-input
              placeholder="请填写菜单别名"
              maxlength="20"
              :disabled="mode === 'view'"
              v-model="formData.alias"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="访问地址" prop="path">
            <el-input
              placeholder="请填写访问地址"
              maxlength="200"
              :disabled="mode === 'view'"
              v-model="formData.path"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="菜单编号" prop="code">
            <el-input
              placeholder="请填写菜单编号"
              maxlength="40"
              :disabled="['edit', 'view'].includes(mode)"
              v-model="formData.code"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="菜单排序" prop="sort">
            <el-input
              placeholder="请填写菜单排序"
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
      <el-row>
        <el-col :span="24">
          <el-form-item label="请求参数" required>
            <el-table
              :data="formData.params"
              :maxHeight="180"
              empty-text="暂无请求参数"
              ref="subTable"
              class="subTable"
              :header-cell-style="{
                backgroundColor: '#F5F7FA',
                color: '#666666',
              }"
              row-key="rowKey"
              border
            >
              <el-table-column
                type="index"
                label="#"
                width="40px"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="title"
                label="参数名"
                :render-header="addReadStar"
              >
                <template #default="scope">
                  <el-form-item
                    label-width="0"
                    :style="{ marginBottom: 0 }"
                    :prop="'params[' + scope.$index + '].key'"
                    :rules="[
                      { required: true, message: '必填项', trigger: 'blur' },
                    ]"
                  >
                    <el-input
                      v-model="scope.row.key"
                      :disabled="mode === 'view'"
                      placeholder="参数名"
                      clearable
                    ></el-input>
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                prop="value"
                label="参数值"
                :render-header="addReadStar"
              >
                <template #default="scope">
                  <el-form-item
                    label-width="0"
                    :style="{ marginBottom: 0 }"
                    :prop="'params[' + scope.$index + '].value'"
                    :rules="[
                      { required: true, message: '必填项', trigger: 'blur' },
                    ]"
                  >
                    <el-input
                      v-model="scope.row.value"
                      :disabled="mode === 'view'"
                      placeholder="参数值"
                      clearable
                    ></el-input>
                  </el-form-item>
                </template>
              </el-table-column>

              <el-table-column
                align="center"
                width="50px"
                v-if="mode !== 'view'"
              >
                <template #header>
                  <el-button
                    title="添加"
                    @click="clickAddParams()"
                    icon="Plus"
                    circle
                    type="primary"
                  ></el-button>
                </template>
                <template #default="scope">
                  <el-button
                    title="删除"
                    @click="clickRemoveParams(scope.$index)"
                    icon="Delete"
                    circle
                    type="danger"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
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
    v-bind="dialogIconProp"
    v-model="dialogIconProp.visible"
    @close="() => (dialogIconProp.visible = false)"
  >
    <IconChoose
      v-if="dialogIconProp.visible"
      :value="formData.icon"
      @choose="handelChoose"
    ></IconChoose>
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
