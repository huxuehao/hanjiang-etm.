<script lang="ts" setup>
import { onMounted, ref, watch } from "vue";
import {
  menuTree,
} from "@/api/menu";
import {
  getAuthConfig
} from "@/api/role";
import { menuButtonTree } from "@/api/menuButton";
import { apiButtonTree } from "@/api/menuApi";

const defaultProps = {
  value: "id",
  label: "name",
  children: "children",
}

const props = defineProps({
  roleId: {
    type: String,
    default: () => null
  }
})

let activeName = ref("first");

// 初始化初始化数据
onMounted(async () => {
  await getAuthConfig(props.roleId).then((res) => {
    checkedMenuIds.value = res.data.data.menuIds
    checkedButtonIds.value = res.data.data.buttonIds
    checkedApiIds.value = res.data.data.apiIds
  })

  // 菜单列表
  menuLoading.value = true
  menuTree({}).then((res) => {
    menuData.value = res.data.data
  }).finally(() => {
    menuLoading.value = false
  })

  // 按钮列表
  buttonLoading.value = true
  menuButtonTree().then((res) => {
    buttonData.value = res.data.data
  }).finally(() => {
    buttonLoading.value = false
  })

  // 接口列表
  apiLoading.value = true
  apiButtonTree().then((res) => {
    apiData.value = res.data.data
  }).finally(() => {
    apiLoading.value = false
  })
})

// 菜单权限树
let menuLoading = ref(false)
let menuKeyWord = ref("");
let menuData = ref<any>([])
let menuTreeRef = ref()
let checkedMenuIds = ref<any[]>([])
watch(menuKeyWord, (val: any) => {
  menuTreeRef.value!.filter(val)
})
const handleMenuCheckChange = (
  data: any,
  checked: boolean,
  indeterminate: boolean
) => {
  if(checked && !checkedMenuIds.value.includes(data.id)) {
    checkedMenuIds.value.push(data.id)
  } else {
    checkedMenuIds.value = checkedMenuIds.value.filter((id: any) => id !== data.id)
    
  }
};


// 按钮权限
let buttonLoading = ref(false)
let buttonKeyWord = ref(null);
let buttonData = ref<any>([])
let buttonTreeRef = ref()
let checkedButtonIds = ref<any[]>([])
watch(buttonKeyWord, (val: any) => {
  buttonTreeRef.value!.filter(val)
})
const handleButtonCheckChange = (
  data: any,
  checked: boolean,
  indeterminate: boolean
) => {
  if(checked && !checkedButtonIds.value.includes(data.id)) {
    checkedButtonIds.value.push(data.id)
  } else {
    checkedButtonIds.value = checkedButtonIds.value.filter((id: any) => id !== data.id)
  }
};


// 接口权限
let apiLoading = ref(false)
let apiKeyWord = ref(null);
let apiData = ref<any>([])
let apiTreeRef = ref()
let checkedApiIds = ref<any[]>([])
watch(apiKeyWord, (val: any) => {
  apiTreeRef.value!.filter(val)
})
const handleApiCheckChange = (
  data: any,
  checked: boolean,
  indeterminate: boolean
) => {
  if(checked && !checkedApiIds.value.includes(data.id)) {
    checkedApiIds.value.push(data.id)
  } else {
    checkedApiIds.value = checkedApiIds.value.filter((id: any) => id !== data.id)
  }
};


// 节点过滤
const filterNode = (value: string, data: any) => {
  if (!value) return true
  return data.name.includes(value)
}

defineExpose({
  checkedMenuIds,
  checkedButtonIds,
  checkedApiIds
})

</script>

<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="菜单权限" name="first">
      <el-input v-model="menuKeyWord" placeholder="请输入关键字进行过滤" clearable style="margin-bottom: 10px;" />
      <el-scrollbar height="440px">
        <el-tree
          ref="menuTreeRef"
          class="filter-tree"
          :data="menuData"
          :props="defaultProps"
          :filter-node-method="filterNode"
          :default-checked-keys="checkedMenuIds"
          node-key="id"
          default-expand-all
          show-checkbox
          check-strictly
          v-loading="menuLoading" 
          @check-change="handleMenuCheckChange"
        />
      </el-scrollbar>
    </el-tab-pane>
    <el-tab-pane label="按钮权限" name="second">
      <el-input v-model="buttonKeyWord" placeholder="请输入关键字进行过滤" clearable style="margin-bottom: 10px;" />
      <el-scrollbar height="440px">
        <el-tree
          ref="buttonTreeRef"
          class="filter-tree"
          :data="buttonData"
          :props="defaultProps"
          :filter-node-method="filterNode"
          :default-checked-keys="checkedButtonIds"
          node-key="id"
          default-expand-all
          show-checkbox
          check-strictly
          v-loading="buttonLoading"
          @check-change="handleButtonCheckChange"
        />
      </el-scrollbar>
    </el-tab-pane>
    <el-tab-pane label="接口权限" name="third">
      <el-input v-model="apiKeyWord" placeholder="请输入关键字进行过滤" clearable style="margin-bottom: 10px;" />
      <el-scrollbar height="440px">
        <el-tree
          ref="apiTreeRef"
          class="filter-tree"
          :data="apiData"
          :props="defaultProps"
          :filter-node-method="filterNode"
          :default-checked-keys="checkedApiIds"
          node-key="id"
          default-expand-all
          show-checkbox
          check-strictly
          v-loading="apiLoading"
          @check-change="handleApiCheckChange"
        />
      </el-scrollbar>
    </el-tab-pane>
  </el-tabs>
</template>

<style lang="scss" scoped></style>
