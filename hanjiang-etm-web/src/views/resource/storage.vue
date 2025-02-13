<script lang="ts" setup>
import { onMounted, reactive, ref, h, shallowReactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  storageAdd,
  storageUpdate,
  storageDelete,
  storagePage,
  storageSelectOne,
  storageUpdateProtocol,
  storageValidSuccess,
} from "@/api/storage";
import S3 from "@/components/storage/S3Form.vue";
import FTP from "@/components/storage/FtpForm.vue";
import LOCAL from "@/components/storage/LocalForm.vue";

const protocolConfigComponent = shallowReactive<any>({
  S3,
  FTP,
  LOCAL,
});

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
// 表单数据
let formData = ref<any>({
  valid: 0,
});
const rules = {
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  protocol: [
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
  storagePage({ ...queryParams, ...page })
    .then((res: any) => {
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

// 设置是否有效为true
const doValidSuccess = (row: any) => {
  if (row.valid === 1) {
    ElMessage({
      message: "当前配置已经是启用状态",
      type: "warning",
      plain: true,
    });
    return;
  }

  loading.value = true;
  storageValidSuccess(row.id)
    .then((res: any) => {
      load();
    })
    .catch(() => {
      loading.value = false;
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
  showClose: false,
  closeOnClickModal: false,
});
// 点击新增
const handleAdd = () => {
  mode.value = "add";
  stopBtn.value = false;
  formData.value = {
    valid: 0,
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
        storageAdd(formData.value)
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
        storageUpdate(formData.value)
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
    storageDelete(rows.map((item: any) => item.id))
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
  storageSelectOne(row.id).then((res: any) => {
    formData.value = res.data.data;
    formData.value.params = JSON.parse(res.data.data.params || "[]");
    dialogProp.visible = true;
  });
};

const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  dialogProp.title = "编辑";
  storageSelectOne(row.id).then((res: any) => {
    formData.value = res.data.data;
    formData.value.params = JSON.parse(res.data.data.params || "[]");
    dialogProp.visible = true;
  });
};

const dialogPropProtocol = reactive({
  id: "",
  protocol: "",
  config: {},
  visible: false,
  title: "协议配置",
  top: "20vh",
  width: "50%",
  modal: true,
  appendToBody: true,
  showClose: false,
  closeOnClickModal: false,
});
const handelOpenProtocol = (row: any) => {
  stopBtn.value = false;
  storageSelectOne(row.id).then((res: any) => {
    const data = res.data.data;
    dialogPropProtocol.id = data.id;
    dialogPropProtocol.protocol = data.protocol;
    dialogPropProtocol.config = JSON.parse(data.protocolConfig);
    dialogPropProtocol.visible = true;
  });
};
let protocolForm = ref();
const handelSaveProtocol = () => {
  protocolForm.value.form.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      storageUpdateProtocol({
        id: dialogPropProtocol.id,
        protocolConfig: JSON.stringify(protocolForm.value.getConfig()),
      })
        .then(() => {
          dialogPropProtocol.id = "";
          dialogPropProtocol.protocol = "";
          dialogPropProtocol.config = {};
          dialogPropProtocol.visible = false;
        })
        .finally(() => {
          stopBtn.value = false;
        });
    }
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
        <el-form-item label="存储名称" prop="name" class="form-item">
          <el-input
            v-model="queryParams.name"
            placeholder="存储名称"
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
            <el-option label="S3" value="S3" />
            <el-option label="FTP" value="FTP" />
            <el-option label="LOCAL" value="LOCAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用" prop="valid" class="form-item">
          <el-select
            v-model="queryParams.valid"
            class="input-search"
            placeholder="是否启用"
            clearable
          >
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
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
          v-permission="'storage_add'"
          type="primary"
          icon="CirclePlus"
          @click="handleAdd()"
          >新增</el-button
        >
        <el-button
          v-permission="'storage_delete'"
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
        <el-table-column label="存储名称" prop="name" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="存储协议" prop="protocol" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="是否启用" prop="valid" align="center">
          <template #default="scope">
            <el-switch
              @click="doValidSuccess(scope.row)"
              :modelValue="scope.row.valid === 1"
            />
          </template>
          <!-- <template #default="scope">
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
          </template> -->
        </el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="'storage_view'"
              link
              type=""
              icon="View"
              @click="handleView(scope.row)"
              >查看
            </el-button>
            <el-button
              v-permission="'storage_edit'"
              link
              type=""
              icon="Edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="'storage_delete'"
              link
              type=""
              icon="Delete"
              style="color: #f63434"
              @click="handleDelete([scope.row])"
            >
              删除
            </el-button>
            <el-button
              v-permission="'storage_config'"
              link
              type=""
              icon="Setting"
              @click="handelOpenProtocol(scope.row)"
            >
              协议配置
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
  <!-- 基本配置 -->
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <el-form
      v-if="dialogProp.visible"
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="80px"
    >
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="存储名称" prop="name">
            <el-input
              placeholder="请填写存储名称"
              maxlength="20"
              :disabled="mode === 'view'"
              v-model="formData.name"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="存储协议" prop="protocol">
            <el-select
              v-model="formData.protocol"
              :disabled="mode === 'view'"
              placeholder="请选择存储协议"
            >
              <el-option label="S3" value="S3" />
              <el-option label="FTP" value="FTP" />
              <el-option label="LOCAL" value="LOCAL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <el-input
              placeholder="请填写备注"
              maxlength="100"
              :disabled="mode === 'view'"
              v-model="formData.remark"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用" prop="valid">
            <el-select
              v-model="formData.valid"
              :disabled="mode === 'view'"
              placeholder="请选择是否启用"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
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
  <!-- 协议配置 -->
  <el-dialog v-bind="dialogPropProtocol" v-model="dialogPropProtocol.visible">
    <component
      v-if="dialogPropProtocol.visible"
      ref="protocolForm"
      :config="dialogPropProtocol.config"
      :is="protocolConfigComponent[dialogPropProtocol.protocol]"
    >
    </component>
    <template #footer>
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handelSaveProtocol"
        icon="CircleCheck"
        :loading="stopBtn"
        >保 存</el-button
      >
      <el-button @click="dialogPropProtocol.visible = false" icon="CircleClose"
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
