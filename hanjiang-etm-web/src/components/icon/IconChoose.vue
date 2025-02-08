<script lang="ts" setup>
import { onMounted, ref, watch } from "vue";
import icons from "./config";

let props = defineProps({
  value: {
    type: String,
    default: () => "",
  },
});

let choose = ref("");
let activeName = ref("系统");

onMounted(() => {
  choose.value = props.value;
});

const emits = defineEmits(["choose"]);
</script>

<template>
  <el-tabs v-model="activeName">
    <el-tab-pane v-for="icon in icons" :label="icon.label" :name="icon.label">
      <el-scrollbar height="calc(100vh - 280px)">
        <template v-for="(item, index) in icon.items" >
          <el-row v-if="index % 12 === 0">
            <template v-for="i in 12">
              <el-col 
                @click="() => emits('choose', icon.items[index + i -1])" 
                v-if="icon.items[index + i -1]" 
                :span="2" 
                :class="{'icon-item-full':icon.items[index + i -1] === choose}"
                class="icon-item">
                <el-icon size="25">
                  <component :is="icon.items[index + i -1]" />
                </el-icon>
              </el-col>
            </template>
          </el-row>
        </template>
      </el-scrollbar>
    </el-tab-pane>
  </el-tabs>
</template>

<style lang="scss" scoped>
.icon-item {
  height: 60px;
  line-height: 75px;
  text-align: center;
  border: 1px solid #f4f6fa;
  transition: 300ms;

  &-full {
    background-color: #F2F6FC;
    border: 1px solid #95C9FF;
  }

  &:hover {
    cursor: pointer;
    background-color: #F2F6FC;
  }
}
</style>
