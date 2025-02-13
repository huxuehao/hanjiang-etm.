<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  paramsPage,
  paramsAdd,
  paramsUpdate,
  paramsDelete,
  paramsSelectOne,
} from "@/api/params";

// 初始化
onMounted(() => {
  load();
});

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
  paramName: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  paramKey: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  paramValue: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
};
// 当前所选行
let selectedRows = ref<any>([]);

// 加载列表数据
const load = () => {
  loading.value = true;
  paramsPage(page)
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
const changePageNo = (pageNo: number) => {
  page.current = pageNo;
  load();
};
const changePageSize = (pageSize: number) => {
  page.size = pageSize;
  load();
};

let mode = ref("add");
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "20vh",
  width: "50%",
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
        paramsAdd(formData.value)
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
        paramsUpdate(formData.value)
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
    paramsDelete(rows.map((item: any) => item.id))
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
  paramsSelectOne(row.id).then((res: any) => {
    formData.value = res.data.data;
    dialogProp.visible = true;
  });
};
const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  dialogProp.title = "编辑";
  paramsSelectOne(row.id).then((res: any) => {
    formData.value = res.data.data;
    dialogProp.visible = true;
  });
};
</script>

<template>
  <div style="height: 100%">
    <!-- 按钮区域-->
    <el-row class="button-box">
      <el-col :span="16">
        <el-button
          v-permission="'params_add'"
          type="primary"
          icon="CirclePlus"
          @click="handleAdd()"
          >新增</el-button
        >
        <el-button
          v-permission="'params_delete'"
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
    <div class="data-list" :style="[{ height: 'calc(100vh - 180px)' }]">
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
        <el-table-column label="名称" prop="paramName"> </el-table-column>
        <el-table-column label="Key" prop="paramKey"> </el-table-column>
        <el-table-column label="Value" prop="paramValue" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="'params_view'"
              link
              type=""
              icon="View"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              v-permission="'params_edit'"
              link
              type=""
              icon="Edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="'params_delete'"
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
        <el-col :span="24">
          <el-form-item label="名称" prop="paramName">
            <el-input
              placeholder="请填写名称"
              maxlength="40"
              :disabled="mode === 'view'"
              v-model="formData.paramName"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="Key" prop="paramKey">
            <el-input
              placeholder="请填写Key"
              maxlength="40"
              :disabled="mode === 'view'"
              v-model="formData.paramKey"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="Value" prop="paramValue">
            <el-input
              placeholder="请填写Value"
              type="textarea"
              maxlength="200"
              :rows="2"
              :disabled="mode === 'view'"
              v-model="formData.paramValue"
              clearable
            ></el-input>
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

.search-box,
.button-box,
.data-list {
  margin-left: 10px;
}
.button-box {
  margin-top: 10px;
  margin-bottom: 5px;
}
</style>
