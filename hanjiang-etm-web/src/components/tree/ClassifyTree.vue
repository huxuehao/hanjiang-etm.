<script lang="ts" setup>
import { ElMessage, ElMessageBox } from "element-plus";
import { reactive, ref, watch } from "vue";

let props = defineProps({
  prop: {
    type: Object,
    default: () => {
      return {
        value: "id",
        label: "name",
        children: "children",
      };
    },
  },
  dict: {
    type: Array,
    default: () => {
      return [];
    },
  },
});

let mode = <"delete" | "add" | "edit">"add";
let stopBtn = ref(false);
let keyWord = ref("");
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "20vh",
  width: "35%",
  modal: true,
  appendToBody: true,
  showClose: true,
});
const config = ref<any>({
  id: null,
  parentId: null,
  code: null,
  name: null,
});
const rules = {
  code: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
};

let tree = ref();
watch(keyWord, (val) => {
  tree.value.filter(val);
});

const filterNode = (value: string, data: any) => {
  if (!value) return true;
  return data.name.indexOf(value) !== -1;
};

const handelAdd = () => {
  mode = "add";
  config.value = {};
  stopBtn.value = false;
  dialogProp.title = "新增-分类";
  dialogProp.visible = true;
};

const form = ref();
const emits = defineEmits(["add", "edit", "delete"]);
const handleSave = () => {
  form.value.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      emits(mode, config.value);
    }
  });
};
const handelAddChild = (node: any, data: any) => {
  mode = "add";
  config.value = {
    parentId: data.id,
  };
  stopBtn.value = false;
  dialogProp.title = "新增-分类";
  dialogProp.visible = true;
};
const handelEdit = (node: any, data: any) => {
  mode = "edit";
  config.value.id = data.id;
  config.value.parentId = data.parentId;
  config.value.code = data.code;
  config.value.name = data.name;
  stopBtn.value = false;
  dialogProp.title = "编辑-分类";
  dialogProp.visible = true;
};
const handelDelete = (node: any, data: any) => {
  if (data.children && data.children.length > 0) {
    ElMessage({
      message: "请先删除子节点",
      type: "warning",
      plain: true,
    });
    return;
  }
  ElMessageBox.confirm(
    `删除 [ ${node.label} ] 后,绑定该分类的数据也将不会显示，是否确定?`,
    "提示",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    emits("delete", node, data);
  });
};
const close = () => {
  dialogProp.visible = false;
  stopBtn.value = false;
};
const error = () => {
  stopBtn.value = false;
};

defineExpose({ close, error });
</script>

<template>
  <el-input
    style="padding: 0 10px"
    placeholder="输入关键字进行过滤"
    v-model="keyWord"
  >
    <template #append>
      <el-icon size="16px" title="添加" @click="handelAdd" class="pointer">
        <Plus />
      </el-icon>
    </template>
  </el-input>
  <div
    style="
      height: calc(100% - 50px);
      background-color: white;
      padding: 10px 5px 10px 10px;
    "
  >
    <el-tree
      :data="dict"
      :props="prop"
      :expand-on-click-node="false"
      :default-expand-all="true"
      :filter-node-method="filterNode"
      ref="tree"
    >
      <template #default="{ node, data }">
        <div class="slot-t-node" style="width: 100%; position: relative">
          <el-icon v-if="data[prop.children] && data[prop.children].length > 0">
            <component
              :is="node.expanded ? 'FolderOpened' : 'Folder'"
            ></component>
          </el-icon>
          <!-- <el-icon v-else>
            <CollectionTag />
          </el-icon> -->
          <span style="font-size: 13px;">&nbsp;{{ node.label }}</span>

          <div style="width: 105px; float: right;margin-top: 2px;">
            <el-icon size="15" class="plus-btn btn" title="新增子项"  @click="handelAddChild(node, data)"><CirclePlus/></el-icon>
            <el-icon size="15" class="edit-btn btn" title="编辑"  @click="handelEdit(node, data)"><Edit/></el-icon>
            <el-icon size="15" class="delete-btn btn" title="删除"  @click="handelDelete(node, data)"><Delete/></el-icon>
          </div>
        </div>
      </template>
    </el-tree>
  </div>
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <el-form
      label-width="50px"
      ref="form"
      v-if="dialogProp.visible"
      :model="config"
      :rules="rules"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item label="上级" prop="parentId">
            <el-tree-select
              v-model="config.parentId"
              :data="dict"
              :render-after-expand="true"
              :default-expand-all="true"
              :props="prop"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="名称" prop="name">
            <el-input v-model="config.name" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="编号" prop="code">
            <el-input v-model="config.code" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button
        type="primary"
        @click="handleSave"
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
.slot-t-node:hover .btn {
  display: inline-block;
}
.btn {
  display: none;
  width: 35px;
  text-align: center;
}
.plus-btn:hover {
  color: rgb(0, 0, 0);
}
.edit-btn:hover {
  color: rgb(0, 0, 0);
}
.delete-btn:hover {
  color: red;
}
</style>
