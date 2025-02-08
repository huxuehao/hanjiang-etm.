<script lang="ts" setup>
import { nextTick, onMounted, onUnmounted, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import { getUserTrainConfig } from "@/api/trainTemplate";
import TrainPreview from "./TrainPreview.vue";
import { ElMessage, ElMessageBox } from "element-plus";

const props = defineProps({
  trainId: {
    type: String,
    default: null,
  },
  userId: {
    type: String,
    default: null,
  },
});

const route = useRoute();
onMounted(() => {
  nextTick(() => {
    if (props.trainId && props.userId) {
      initConfig(props.trainId, props.userId);
    } else {
      if (!route.params.params) {
        ElMessage({
          message: "缺少必要参数，加载失败",
          type: "warning",
          plain: true,
        });
      } else {
        const params_: string = String(route.params.params);
        const param = JSON.parse(atob(decodeURIComponent(params_)));
        const { trainId, userId } = param;
        initConfig(trainId, userId);
      }
    }
  });
  // 监听窗口关闭
  window.addEventListener("beforeunload", handelBeforeunload);
});

let loading = ref(false);
let trainPreviewVisible = ref<boolean>(false)
// preview 标准模式，answer 培训模式, history 历史模式
let mode = ref("history");
const config = reactive<any>({});
// 初始化配置
const initConfig = (trainId: string, userId: string) => {
  loading.value = true;
  getUserTrainConfig(trainId, userId)
    .then((res) => {
      Object.assign(config, res.data.data);
      mode.value = config.user[0].paperDone === 1 ? "history" : "answer";
      trainPreviewVisible.value = true
    })
    .catch((err) => {
      if (window.opener) {
        ElMessageBox.alert(err.message, "关闭提示", {
          confirmButtonText: "确定",
          callback: () => {
            window.close();
          },
        });
      }
    })
    .finally(() => {
      loading.value = false;
    });
};

const emit = defineEmits(["close"]);
// 关闭浏览器sheet页（不弹出）
const handelClose = () => {
  if (props.trainId && props.userId) {
    emit("close");
  } else {
    if (window.opener) {
      ElMessageBox.confirm("确认关闭当前页面？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        window.close();
      });
    } else {
      ElMessage({
        message: "此页面不能通过脚本关闭,请手动关闭浏览器标签",
        type: "warning",
        plain: true,
      });
    }
  }
};

// 窗口关闭监听处理器
const handelBeforeunload = () => {
  localStorage.setItem("reloadUserShow", new Date().getTime() + "");
};

onUnmounted(() => {
  // 销毁监听
  window.removeEventListener("beforeunload", handelBeforeunload);
});
</script>

<template>
  <div style="width: 100vw; height: 100vh;position: relative" v-loading="loading">
    <TrainPreview
      ref="train"
      v-if="trainPreviewVisible"
      :mode="mode"
      :data="config"
      @close="handelClose" :active="true">
    </TrainPreview>
  </div>
</template>

<style lang="scss" scoped></style>
