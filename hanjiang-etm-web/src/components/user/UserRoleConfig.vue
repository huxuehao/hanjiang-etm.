<script lang="ts" setup>
import { getUserRole, roleTree } from "@/api/role";
import { onMounted, ref, watch } from "vue";

const defaultProps = {
  value: "id",
  label: "name",
  children: "children",
};

const props = defineProps({
  userId: {
    type: String,
    default: () => null,
  },
});

let activeName = ref("first");

// 用户角色
let loading = ref(false);
let roleKeyWord = ref("");
let roleData = ref<any>([]);
let roleTreeRef = ref();
let checkedRoleIds = ref<any[]>([]);
onMounted(async () => {
  // 初始化已选择数据
  await getUserRole(props.userId).then((res) => {
    checkedRoleIds.value = res.data.data;
  });
  loading.value = true;
  roleTree({})
    .then((res) => {
      roleData.value = res.data.data;
    })
    .finally(() => {
      loading.value = false;
    });
});
watch(roleKeyWord, (val: any) => {
  roleTreeRef.value!.filter(val);
});
const handleRoleCheckChange = (
  data: any,
  checked: boolean,
  indeterminate: boolean
) => {
  if (checked && !checkedRoleIds.value.includes(data.id)) {
    checkedRoleIds.value.push(data.id);
  } else {
    checkedRoleIds.value = checkedRoleIds.value.filter(
      (id: any) => id !== data.id
    );
  }
};

// 节点过滤
const filterNode = (value: string, data: any) => {
  if (!value) return true;
  return data.name.includes(value);
};

defineExpose({
  checkedRoleIds,
});
</script>

<template>
  <!-- <el-tabs v-model="activeName">
    <el-tab-pane label="角色" name="first"> -->
      <el-input
        v-model="roleKeyWord"
        placeholder="请输入关键字进行过滤"
        clearable
        style="margin-bottom: 10px"
      />
      <el-scrollbar height="450px">
        <el-tree
          ref="roleTreeRef"
          class="filter-tree"
          :data="roleData"
          :props="defaultProps"
          :filter-node-method="filterNode"
          :default-checked-keys="checkedRoleIds"
          node-key="id"
          default-expand-all
          show-checkbox
          check-strictly
          v-loading="loading"
          @check-change="handleRoleCheckChange"
        />
      </el-scrollbar>
    <!-- </el-tab-pane>
  </el-tabs> -->
</template>

<style lang="scss" scoped></style>
