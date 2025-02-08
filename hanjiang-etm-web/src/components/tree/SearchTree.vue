<script lang="ts" setup>
import { ref, watch } from "vue";

defineProps({
  loading: {
    type: Boolean,
    default: () => false,
  },
  title: {
    type: String,
    default: () => "分类树",
  },
  data: {
    type: Array,
    default: () => [],
  },
  height: {
    type: String,
    default: () => "500px",
  },
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
});
let keyWord = ref(null);
let tree = ref();

watch(keyWord, (val) => {
  tree.value.filter(val);
});

const emits = defineEmits(["node-click"]);

const handleNodeClick = (node: any) => {
  emits("node-click", node);
};
const filterNode = (value: string, data: any) => {
  if (!value) return true;
  return data.name.indexOf(value) !== -1;
};
</script>

<template>
  <div>
    <div class="title">
      {{ title }}
    </div>
    <el-input
      v-model="keyWord"
      class="etm-search-input"
      placeholder="输入关键字进行过滤"
    >
      <template #suffix>
        <el-icon class="el-input__icon">
          <search />
        </el-icon>
      </template>
    </el-input>
    <el-scrollbar :height="height">
      <el-tree
        :data="data"
        :props="prop"
        style="padding: 0 10px"
        :expand-on-click-node="false"
        @node-click="handleNodeClick"
        :filter-node-method="filterNode"
        ref="tree"
      >
        <template #default="{ node, data }">
          <el-icon v-if="data[prop.children] && data[prop.children].length > 0">
            <component
              :is="node.expanded ? 'FolderOpened' : 'Folder'"
            ></component>
          </el-icon>
          <!-- <el-icon v-else>
            <CollectionTag />
          </el-icon> -->
          <span style="font-size: 12px">&nbsp;{{ node.label }}</span>
        </template>
      </el-tree>
    </el-scrollbar>
  </div>
</template>

<style lang="scss" scoped>
.title {
  height: 20px;
  font-size: 13px; 
  padding: 10px 10px 6px 18px;
  color: #686A6E;
  width: 100%;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    left: 10px;
    top: 11px;
    bottom: 0;
    width: 3px;
    height: 15px;
    background-color: #2d96ff;
  }
}
.etm-search-input {
  // height: 50px;
  padding: 0 10px 10px 10px;
  background-color: white;
}
</style>
