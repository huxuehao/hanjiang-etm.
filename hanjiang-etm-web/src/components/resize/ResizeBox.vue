<script lang="ts" setup>
import { ref, onBeforeUnmount } from "vue";

const splitLayout = ref();
const props = defineProps({
  // 初始宽度
  initialWidth: {
    type: Number,
    default: 250,
  },
  // 最小宽度
  minWidth: {
    type: Number,
    default: 250,
  },
  // 最大宽度
  maxWidth: {
    type: Number,
    default: 350,
  },
});

const leftWidth = ref(props.initialWidth); // 当前左侧菜单宽度
let isDragging = ref(false);
let lastX = ref(0);

// 开始拖拽
const onMouseDown = (event: any) => {
  isDragging.value = true;
  lastX.value = event.clientX;

  // 禁用文本选择
  document.body.style.userSelect = "none";
  document.addEventListener("mousemove", onMouseMove);
  document.addEventListener("mouseup", onMouseUp);
};

// 拖拽过程中
const onMouseMove = (event: any) => {
  if (!isDragging.value) return;
  splitLayout.value.style.cursor = "ew-resize";

  const X = event.clientX - lastX.value;
  lastX.value = event.clientX;

  // 计算新的宽度并限制最小/最大值
  let newWidth = leftWidth.value + X;
  newWidth = Math.max(props.minWidth, newWidth); // 最小宽度限制
  newWidth = Math.min(props.maxWidth, newWidth); // 最大宽度限制

  leftWidth.value = newWidth;
};

// 停止拖拽
const onMouseUp = () => {
  isDragging.value = false;
  document.body.style.userSelect = ""; // 恢复文本选择
  splitLayout.value.style.cursor = "default";
  document.removeEventListener("mousemove", onMouseMove);
  document.removeEventListener("mouseup", onMouseUp);
};

// 清理事件
onBeforeUnmount(() => {
  document.removeEventListener("mousemove", onMouseMove);
  document.removeEventListener("mouseup", onMouseUp);
});
</script>

<template>
  <div class="split-layout" ref="splitLayout">
    <div class="left-panel" :style="{ width: `${leftWidth}px` }">
      <slot name="left"></slot>
    </div>
    <div class="resize-bar">
      <div class="tool-Bar" @mousedown="onMouseDown"></div>
    </div>
    <div class="right-panel">
      <slot name="right"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.split-layout {
  display: flex;
  height: 100%;
}

.left-panel {
  transition: width 5ms ease;
  overflow: hidden;
}

.right-panel {
  flex-grow: 1;
  overflow: hidden;
}

.resize-bar {
  width: 12px;
  display: flex;
  justify-content: center;
  align-items: center;

  &:hover>.tool-Bar {
    border-left: 1px solid #a5a5a5;
    border-right: 1px solid #a5a5a5;
  }

  .tool-Bar {
    width: 2px;
    height: 40px;
    cursor: ew-resize;
    display: inline-block;
    border-left: 1px solid #d4d4d4;
    border-right: 1px solid #d4d4d4;
  }
}
</style>
