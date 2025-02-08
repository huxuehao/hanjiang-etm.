<script lang="ts" setup>
import { examConfigByCorrect, examCorrect } from '@/api/examTemplate';
import { ElMessage, ElMessageBox } from 'element-plus';
import { nextTick, onMounted, onUnmounted, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import PaperPreview from '../paper/PaperPreview.vue';

const props = defineProps({
  examId: {
    type: String,
    default: () => null,
  },
  userId: {
    type: String,
    default: () => null,
  },
})

let loading = ref(false)
let loadingText = ref("")
// preview 标准模式，answer 培训模式, 审批模式 history 历史模式
let mode = ref("correct")
const config = reactive<any>({})
let paperPreviewVisible = ref<any>(null)

onMounted(() => {
  nextTick(() => {
    init()
  })
  // 监听窗口关闭
  window.addEventListener('beforeunload', handelBeforeunload);
})
onUnmounted(() => {
  // 销毁监听
  window.removeEventListener('beforeunload', handelBeforeunload);
})

// 窗口关闭监听处理器
const handelBeforeunload = () => {
  localStorage.setItem('reloadUserShow', new Date().getTime() + "");
};

const route = useRoute()
// 初始化
const init = () => {
  if (props.examId && props.userId) {
    loadConfig(props.examId, props.userId)
  } else {
    if (!route.params.params) {
      ElMessage({
        message: "缺少必要参数，加载失败",
        type: "warning",
        plain: true
      })
    } else {
      const params_:string = String(route.params.params)
      const param = JSON.parse(atob(decodeURIComponent(params_)))
      const { exmaId, userId } = param
      loadConfig(exmaId, userId)
    }
  }
};

// 提交
const handelPaperCorrect = (config_: any) => {
  loadingText.value = '批改提交中'
  loading.value = true;
  examCorrect(config_).then(() => {
    paperPreviewVisible.value = false;
  }).finally(() => {
    loading.value = false;
    loadingText.value = ''
  });
};

// 加载配置
const loadConfig = (examId:string, userId:string) => {
  loading.value = true
  examConfigByCorrect(examId, userId).then((res) => {
   Object.assign(config, res.data.data)
    config.title = config.name
    mode.value = 'correct'
    paperPreviewVisible.value = true
  }).catch((err) => {
    if (window.opener) {
      ElMessageBox.alert(err.message, '关闭提示', {
        confirmButtonText: '确定',
        callback: () => {
          window.close();
        }
      })
    }
  }).finally(() => {
    loading.value = false
  })
};

const emit = defineEmits(["close"]);
// 关闭试卷
const handelPaperClose = () => {
  if (props.examId && props.userId) {
    emit("close")
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
</script>

<template>
  <div
    style="width: 100vw; height: 100vh; position: relative"
    v-loading="loading"
    :element-loading-text="loadingText"
  >
    <PaperPreview
      v-if="paperPreviewVisible === true"
      :data="config"
      :mode="mode"
      :key="mode"
      :showAlert="false"
      @correct="handelPaperCorrect"
      @close="handelPaperClose"
    ></PaperPreview>
    <el-row v-if="paperPreviewVisible === false" style="margin-top: 20vh">
      <el-col :span="24">
        <el-result
          icon="success"
          title="您已完成批改"
          subTitle="请点击下方按钮关闭页面"
        >
          <template #extra>
            <el-button type="primary" @click="handelPaperClose"
              >关闭</el-button
            >
          </template>
        </el-result>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped></style>
